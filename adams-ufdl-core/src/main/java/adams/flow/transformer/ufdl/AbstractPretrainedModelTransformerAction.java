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
 * AbstractPretrainedModelTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels;
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels.PretrainedModel;

/**
 * Ancestor of transformer actions on pretrained models.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of PretrainedModels action
 */
public abstract class AbstractPretrainedModelTransformerAction<T extends PretrainedModels>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, PretrainedModel.class};
  }

  /**
   * Returns the pretrained models action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getPretrainedModelsAction() throws Exception {
    return (T) m_Client.action(PretrainedModels.class);
  }

  /**
   * Transforms the pretrained model.
   *
   * @param model	the pretrained model
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(PretrainedModel model, MessageCollection errors);

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
    PretrainedModel 	model;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming pretrained model: " + input);

    // load pretrained model
    model = null;
    try {
      action = getPretrainedModelsAction();
      if (input instanceof Integer)
	model = action.load((Integer) input);
      else if (input instanceof String)
	model = action.load("" + input);
      else
	model = (PretrainedModel) input;
    }
    catch (Exception e) {
      errors.add("Failed to load pretrained model: " + input, e);
    }

    if (model == null)
      errors.add("Unknown pretrained model: " + input);
    else
      result = doTransform(model, errors);

    return result;
  }
}
