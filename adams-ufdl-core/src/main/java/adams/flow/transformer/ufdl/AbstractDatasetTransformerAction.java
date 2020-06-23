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
 * AbstractDatasetTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Datasets;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Ancestor of transformer actions on datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of Datasets action
 */
public abstract class AbstractDatasetTransformerAction<T extends Datasets>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, Dataset.class};
  }

  /**
   * Returns the datasets action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getDatasetsAction() throws Exception {
    return (T) m_Client.action(Datasets.class);
  }

  /**
   * Transforms the dataset.
   *
   * @param dataset	the dataset
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Dataset dataset, MessageCollection errors);

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
    Dataset 	dataset;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming dataset: " + input);

    // load dataset
    dataset = null;
    try {
      action = getDatasetsAction();
      if (input instanceof Integer)
	dataset = action.load((Integer) input);
      else if (input instanceof String)
	dataset = action.load("" + input);
      else
	dataset = (Dataset) input;
    }
    catch (Exception e) {
      errors.add("Failed to load dataset: " + input, e);
    }

    if (dataset == null)
      errors.add("Unknown dataset: " + input);
    else
      result = doTransform(dataset, errors);

    return result;
  }
}
