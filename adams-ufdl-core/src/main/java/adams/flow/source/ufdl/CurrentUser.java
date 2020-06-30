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
 * CurrentUser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Forwards the user object of the user logged in via the current context.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CurrentUser
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Forwards the user object of the user logged in via the current context.";
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{User.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    User    result;
    String  name;

    result = null;
    try {
      name   = m_Client.connection().authentication().getUser();
      result = m_Client.users().load(name);
    }
    catch (Exception e) {
      errors.add("Failed to load current user!", e);
    }

    return result;
  }
}
