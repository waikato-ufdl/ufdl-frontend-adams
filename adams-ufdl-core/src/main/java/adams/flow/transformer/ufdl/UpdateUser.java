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
 * UpdateUser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.TriState;
import adams.core.net.EmailAddress;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Updates a user and forwards the user object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdateUser
  extends AbstractUserTransformerAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the user name. */
  protected String m_UserName;

  /** the first name. */
  protected String m_FirstName;

  /** the last name. */
  protected String m_LastName;

  /** the email. */
  protected EmailAddress m_Email;

  /** the active state. */
  protected TriState m_Active;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates a user and forwards the user object.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "user-name", "userName",
      "");

    m_OptionManager.add(
      "first-name", "firstName",
      "");

    m_OptionManager.add(
      "last-name", "lastName",
      "");

    m_OptionManager.add(
      "email", "email",
      new EmailAddress());

    m_OptionManager.add(
      "active", "active",
      TriState.TRUE);
  }

  /**
   * Sets the user name.
   *
   * @param value	the name
   */
  public void setUserName(String value) {
    m_UserName = value;
    reset();
  }

  /**
   * Returns the user name.
   *
   * @return		the name
   */
  public String getUserName() {
    return m_UserName;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String userNameTipText() {
    return "The new user name.";
  }

  /**
   * Sets the first name.
   *
   * @param value	the name
   */
  public void setFirstName(String value) {
    m_FirstName = value;
    reset();
  }

  /**
   * Returns the first name.
   *
   * @return		the name
   */
  public String getFirstName() {
    return m_FirstName;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String firstNameTipText() {
    return "The new first name.";
  }

  /**
   * Sets the last name.
   *
   * @param value	the name
   */
  public void setLastName(String value) {
    m_LastName = value;
    reset();
  }

  /**
   * Returns the last name.
   *
   * @return		the name
   */
  public String getLastName() {
    return m_LastName;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String lastNameTipText() {
    return "The new last name.";
  }

  /**
   * Sets the email.
   *
   * @param value	the email
   */
  public void setEmail(EmailAddress value) {
    m_Email = value;
    reset();
  }

  /**
   * Returns the email.
   *
   * @return		the email
   */
  public EmailAddress getEmail() {
    return m_Email;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String emailTipText() {
    return "The new email.";
  }

  /**
   * Sets the active state.
   *
   * @param value	the state
   */
  public void setActive(TriState value) {
    m_Active = value;
    reset();
  }

  /**
   * Returns the active state.
   *
   * @return		the state
   */
  public TriState getActive() {
    return m_Active;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String activeTipText() {
    return "The 'active' state of the user.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "userName", m_UserName);
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
   * Transforms the input data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doTransform(User user, MessageCollection errors) {
    User    result;

    result = null;
    try {
      result = m_Client.users().partialUpdate(
        user, m_UserName, null, m_FirstName, m_LastName, m_Email.strippedValue(), m_Active.toBoolean());
    }
    catch (Exception e) {
      errors.add("Failed to update user: " + user, e);
    }

    return result;
  }
}
