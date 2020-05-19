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
 * DeleteTeam.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;

/**
 * Deletes the team either via PK or teamname.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DeleteTeam
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Deletes the team either via PK or team name.";
  }

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, Team.class};
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
    Team	team;

    result = false;

    if (isLoggingEnabled())
      getLogger().info("Deleting team: " + input);

    // load team
    team = null;
    try {
      if (input instanceof Integer)
	team = m_Client.teams().load((Integer) input);
      else if (input instanceof String)
	team = m_Client.teams().load("" + input);
      else
        team = (Team) input;
    }
    catch (Exception e) {
      errors.add("Failed to load team: " + input, e);
    }

    if (team == null) {
      errors.add("Unknown team: " + input);
    }
    else {
      try {
	result = m_Client.teams().delete(team);
      }
      catch (Exception e) {
        errors.add("Failed to delete team: " + team, e);
      }
    }

    return result;
  }
}
