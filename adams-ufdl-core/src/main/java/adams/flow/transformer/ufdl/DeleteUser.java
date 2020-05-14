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
 * DeleteUser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Deletes the user either via PK or username.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DeleteUser
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Deletes the user either via PK or user name.";
  }

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class};
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Boolean.class};
  }

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Object input, MessageCollection errors) {
    boolean	result;
    User	user;

    result = false;

    if (isLoggingEnabled())
      getLogger().info("Deleting user: " + input);

    // load user
    user = null;
    try {
      if (input instanceof Integer)
	user = m_Client.users().load((Integer) input);
      else
	user = m_Client.users().load("" + input);
    }
    catch (Exception e) {
      errors.add("Failed to load user: " + input, e);
    }

    if (user == null) {
      errors.add("Unknown user: " + input);
    }
    else {
      try {
	result = m_Client.users().delete(user);
      }
      catch (Exception e) {
        errors.add("Failed to delete user: " + user, e);
      }
    }

    return result;
  }
}
