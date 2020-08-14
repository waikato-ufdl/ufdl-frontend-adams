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
 * AbstractFrameworkTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Frameworks;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;

/**
 * Ancestor of transformer actions on frameworks.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of Frameworks action
 */
public abstract class AbstractFrameworkTransformerAction<T extends Frameworks>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, Framework.class};
  }

  /**
   * Returns the frameworks action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getFrameworksAction() throws Exception {
    return (T) m_Client.action(Frameworks.class);
  }

  /**
   * Transforms the framework.
   *
   * @param framework	the framework
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Framework framework, MessageCollection errors);

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
    Framework 	framework;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming framework: " + input);

    // load framework
    framework = null;
    try {
      action = getFrameworksAction();
      if (input instanceof Integer)
	framework = action.load((Integer) input);
      else
	framework = (Framework) input;
    }
    catch (Exception e) {
      errors.add("Failed to load framework: " + input, e);
    }

    if (framework == null)
      errors.add("Unknown framework: " + input);
    else
      result = doTransform(framework, errors);

    return result;
  }
}
