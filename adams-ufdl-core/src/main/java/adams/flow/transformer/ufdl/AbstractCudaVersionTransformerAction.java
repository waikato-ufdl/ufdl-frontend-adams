/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * AbstractCudaVersionTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.CudaVersions;
import com.github.waikatoufdl.ufdl4j.action.CudaVersions.CudaVersion;

/**
 * Ancestor of transformer actions on CUDA versions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of CudaVersions action
 */
public abstract class AbstractCudaVersionTransformerAction<T extends CudaVersions>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, CudaVersion.class};
  }

  /**
   * Returns the cuda versions action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getCudaVersionsAction() throws Exception {
    return (T) m_Client.action(CudaVersions.class);
  }

  /**
   * Transforms the CUDA version.
   *
   * @param cuda the cuda version
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(CudaVersion cuda, MessageCollection errors);

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Object input, MessageCollection errors) {
    Object	result;
    CudaVersion 	cuda;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming CUDA version: " + input);

    // load cuda version
    cuda = null;
    try {
      action = getCudaVersionsAction();
      if (input instanceof Integer)
	cuda = action.load((Integer) input);
      else
	cuda = (CudaVersion) input;
    }
    catch (Exception e) {
      errors.add("Failed to load CUDA version: " + input, e);
    }

    if (cuda == null)
      errors.add("Unknown CUDA version: " + input);
    else
      result = doTransform(cuda, errors);

    return result;
  }
}
