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
 * UpdateProject.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;

/**
 * Updates a projects and forwards the project object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdateProject
  extends AbstractProjectTransformerAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the project name. */
  protected String m_Name;

  /** the team PK. */
  protected int m_Team;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates a project and forwards the project object.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "name", "name",
      "");

    m_OptionManager.add(
      "team", "team",
      -1, -1, null);
  }

  /**
   * Sets the new project name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the new project name.
   *
   * @return		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String nameTipText() {
    return "The new project name.";
  }

  /**
   * Sets the new team PK.
   *
   * @param value	the PK
   */
  public void setTeam(int value) {
    m_Team = value;
    reset();
  }

  /**
   * Returns the new team PK.
   *
   * @return		the PK
   */
  public int getTeam() {
    return m_Team;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String teamTipText() {
    return "The new team PK.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", m_Name, "name: ");
    result += QuickInfoHelper.toString(this, "team", m_Team, ", team: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Project.class};
  }

  /**
   * Transforms the input data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doTransform(Project project, MessageCollection errors) {
    Project    result;

    result = null;
    try {
      result = m_Client.projects().update(project, m_Name, m_Team);
    }
    catch (Exception e) {
      errors.add("Failed to update project!", e);
    }

    return result;
  }
}
