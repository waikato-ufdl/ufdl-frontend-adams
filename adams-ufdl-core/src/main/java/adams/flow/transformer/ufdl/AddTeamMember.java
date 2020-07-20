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
 * AddTeamMember.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.base.BaseInteger;
import com.github.waikatoufdl.ufdl4j.action.Permissions;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Adds the specified user as team member to the team passing through.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddTeamMember
  extends AbstractTeamTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the team members to add. */
  protected BaseInteger[] m_Members;

  /** the permissions to use. */
  protected Permissions m_Permissions;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the specified user(s) as team member(s) to the team passing through.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "member", "members",
      new BaseInteger[0]);

    m_OptionManager.add(
      "permissions", "permissions",
      Permissions.R);
  }

  /**
   * Sets the members to add.
   *
   * @param value	the member PKs
   */
  public void setMembers(BaseInteger[] value) {
    m_Members = value;
    reset();
  }

  /**
   * Returns the members to add.
   *
   * @return		the member PKs
   */
  public BaseInteger[] getMembers() {
    return m_Members;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String membersTipText() {
    return "The PKs of the members to add.";
  }

  /**
   * Sets the permissions to apply.
   *
   * @param value	the permissions
   */
  public void setPermissions(Permissions value) {
    m_Permissions = value;
    reset();
  }

  /**
   * Returns the permissions to apply.
   *
   * @return		the permissions
   */
  public Permissions getPermissions() {
    return m_Permissions;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String permissionsTipText() {
    return "The permissions to apply.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Team.class};
  }

  /**
   * Transforms the team.
   *
   * @param team	the team
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Team team, MessageCollection errors) {
    Team 	result;
    User 	user;

    for (BaseInteger member: m_Members) {
      try {
        if (isLoggingEnabled())
          getLogger().info("Adding member " + member + " to team '" + team + "'");
        user = m_Client.users().load(member.intValue());
        if (!m_Client.teams().addMembership(team, user, m_Permissions))
          errors.add("Failed to add member " + member + " to team '" + team + "': " + team);
      }
      catch (Exception e) {
        errors.add("Failed to add member " + member + " to team '" + team + "': " + team, e);
      }
    }

    // reload team
    result = team;
    try {
      result = m_Client.teams().load(team.getPK());
    }
    catch (Exception e) {
      errors.add("Failed to reload team: " + team, e);
    }

    return result;
  }
}
