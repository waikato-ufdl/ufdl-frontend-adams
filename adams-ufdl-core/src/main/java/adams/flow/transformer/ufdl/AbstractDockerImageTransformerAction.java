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
 * AbstractDockerImageTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;

/**
 * Ancestor of transformer actions on docker images.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractDockerImageTransformerAction
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, DockerImage.class};
  }

  /**
   * Transforms the docker image.
   *
   * @param image	the docker image
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(DockerImage image, MessageCollection errors);

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Object input, MessageCollection errors) {
    Object		result;
    DockerImage 	image;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming docker image: " + input);

    // load docker image
    image = null;
    try {
      if (input instanceof Integer)
	image = m_Client.docker().load((Integer) input);
      else
	image = (DockerImage) input;
    }
    catch (Exception e) {
      errors.add("Failed to load docker image: " + input, e);
    }

    if (image == null)
      errors.add("Unknown docker image: " + input);
    else
      result = doTransform(image, errors);

    return result;
  }
}
