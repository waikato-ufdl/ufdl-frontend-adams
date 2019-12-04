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
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.flow.core.AbstractUFDLAction;

/**
 * Ancestor for UFDL source actions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLSourceAction
  extends AbstractUFDLAction {

  private static final long serialVersionUID = 8794267287172508581L;

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  public abstract Class[] generates();

  /**
   * Check method before generating the data.
   *
   * @return		null if checks successful, otherwise error message
   */
  protected String check() {
    if (m_Client == null)
      return "No client provided!";
    return null;
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  protected abstract Object doGenerate(MessageCollection errors);

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  public Object generate(MessageCollection errors) {
    Object	result;
    String 	msg;

    result = null;
    msg    = check();
    if (msg == null)
      result = doGenerate(errors);
    else
      errors.add(msg);

    return result;
  }
}
