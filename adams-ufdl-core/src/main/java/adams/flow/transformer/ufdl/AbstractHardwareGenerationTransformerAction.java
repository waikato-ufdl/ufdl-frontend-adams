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
 * AbstractHardwareGenerationTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations.HardwareGeneration;

/**
 * Ancestor of transformer actions on hardware generations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of HardwareGenerations action
 */
public abstract class AbstractHardwareGenerationTransformerAction<T extends HardwareGenerations>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, HardwareGeneration.class};
  }

  /**
   * Returns the hardware generations action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getHardwareGenerationsAction() throws Exception {
    return (T) m_Client.action(HardwareGenerations.class);
  }

  /**
   * Transforms the hardware generation.
   *
   * @param generation	the hardware generation
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(HardwareGeneration generation, MessageCollection errors);

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
    HardwareGeneration generation;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming hardware generation: " + input);

    // load hardware generation
    generation = null;
    try {
      action = getHardwareGenerationsAction();
      if (input instanceof Integer)
	generation = action.load((Integer) input);
      else if (input instanceof String)
	generation = action.load("" + input);
      else
	generation = (HardwareGeneration) input;
    }
    catch (Exception e) {
      errors.add("Failed to load hardware generation: " + input, e);
    }

    if (generation == null)
      errors.add("Unknown hardware generation: " + input);
    else
      result = doTransform(generation, errors);

    return result;
  }
}
