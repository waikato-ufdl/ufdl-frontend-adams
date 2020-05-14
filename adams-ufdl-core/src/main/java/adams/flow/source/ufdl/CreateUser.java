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
 * CreateUser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BasePassword;
import adams.core.net.EmailAddress;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Creates a user and forwards the user object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateUser
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the user name. */
  protected String m_UserName;

  /** the password. */
  protected BasePassword m_Password;

  /** the first name. */
  protected String m_FirstName;

  /** the last name. */
  protected String m_LastName;

  /** the email. */
  protected EmailAddress m_Email;

  /** whether the user is staff. */
  protected boolean m_Staff;

  /** whether the user is superuser. */
  protected boolean m_Superuser;

  /** whether the user is active. */
  protected boolean m_Active;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a user and forwards the user object.";
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
      "password", "password",
      new BasePassword(), false);

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
      "staff", "staff",
      false);

    m_OptionManager.add(
      "superuser", "superuser",
      false);

    m_OptionManager.add(
      "active", "active",
      false);
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
    return "The user name.";
  }

  /**
   * Sets the user's password.
   *
   * @param value	the password
   */
  public void setPassword(BasePassword value) {
    m_Password = value;
    reset();
  }

  /**
   * Returns the user's password.
   *
   * @return		the password
   */
  public BasePassword getPassword() {
    return m_Password;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String passwordTipText() {
    return "The user's password.";
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
    return "The first name.";
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
    return "The last name.";
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
    return "The user's email.";
  }

  /**
   * Sets whether the user is to be flagged as staff.
   *
   * @param value	true if staff
   */
  public void setStaff(boolean value) {
    m_Staff = value;
    reset();
  }

  /**
   * Returns whether the user is to be flagged as staff.
   *
   * @return		true if staff
   */
  public boolean getStaff() {
    return m_Staff;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String staffTipText() {
    return "Whether the user is to be flagged as staff.";
  }

  /**
   * Sets whether the user is to be flagged as superuser.
   *
   * @param value	true if superuser
   */
  public void setSuperuser(boolean value) {
    m_Superuser = value;
    reset();
  }

  /**
   * Returns whether the user is to be flagged as superuser.
   *
   * @return		true if superuser
   */
  public boolean getSuperuser() {
    return m_Superuser;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String superuserTipText() {
    return "Whether the user is to be flagged as superuser.";
  }

  /**
   * Sets whether the user is to be flagged as active.
   *
   * @param value	true if active
   */
  public void setActive(boolean value) {
    m_Active = value;
    reset();
  }

  /**
   * Returns whether the user is to be flagged as active.
   *
   * @return		true if active
   */
  public boolean getActive() {
    return m_Active;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String activeTipText() {
    return "Whether the user is to be flagged as active.";
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
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    User    result;

    result = null;
    try {
      result = m_Client.users().create(m_UserName, m_Password.getValue(), m_FirstName, m_LastName, m_Email.strippedValue(), m_Staff, m_Superuser, m_Active);
    }
    catch (Exception e) {
      errors.add("Failed to create user!", e);
    }

    return result;
  }
}
