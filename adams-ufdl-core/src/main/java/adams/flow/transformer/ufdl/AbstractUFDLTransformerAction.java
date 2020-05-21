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
 * AbstractUFDLSinkAction.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.flow.core.AbstractUFDLAction;

/**
 * Ancestor for UFDL sink actions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLTransformerAction
  extends AbstractUFDLAction {

  private static final long serialVersionUID = 8794267287172508581L;

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  public abstract Class[] accepts();

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  public abstract Class[] generates();

  /**
   * Check method before processing the data.
   *
   * @param input	the data to check
   * @return		null if checks successful, otherwise error message
   */
  protected String check(Object input) {
    if (m_Client == null)
      return "No client provided!";
    if (input == null)
      return "No input provided!";
    if (requiresFlowContext() && (m_FlowContext == null))
      return "No flow context set!";
    return null;
  }

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Object input, MessageCollection errors);

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  public Object transform(Object input, MessageCollection errors) {
    Object 	result;
    String 	msg;

    result = null;
    msg    = check(input);
    if (msg == null)
      result = doTransform(input, errors);
    else
      errors.add(msg);

    return result;
  }
}
