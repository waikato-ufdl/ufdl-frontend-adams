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
 * AbstractUserTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Ancestor for transformer actions on users.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUserTransformerAction
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, User.class};
  }

  /**
   * Transforms the user.
   *
   * @param user	the user
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(User user, MessageCollection errors);

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
    User	user;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming user: " + input);

    // load user
    user = null;
    try {
      if (input instanceof Integer)
	user = m_Client.users().load((Integer) input);
      else if (input instanceof String)
	user = m_Client.users().load("" + input);
      else
        user = (User) input;
    }
    catch (Exception e) {
      errors.add("Failed to load user: " + input, e);
    }

    if (user == null)
      errors.add("Unknown user: " + input);
    else
      result = doTransform(user, errors);

    return result;
  }
}
