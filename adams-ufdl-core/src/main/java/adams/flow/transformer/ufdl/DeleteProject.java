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
 * DeleteProject.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;

/**
 * Deletes the project either via PK or projectname.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DeleteProject
  extends AbstractProjectTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** whether to perform a hard delete. */
  protected boolean m_Hard;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Deletes the project either via PK or project name.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "hard", "hard",
      false);
  }

  /**
   * Sets whether to remove or just flag as deleted.
   *
   * @param value	true if to remove
   */
  public void setHard(boolean value) {
    m_Hard = value;
    reset();
  }

  /**
   * Returns whether to remove or just flag as deleted.
   *
   * @return		true if to remove
   */
  public boolean getHard() {
    return m_Hard;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String hardTipText() {
    return "Whether to remove or just flag as deleted.";
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
   * Transforms the project.
   *
   * @param project	the project
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Project project, MessageCollection errors) {
    boolean	result;

    result = false;

    if (isLoggingEnabled())
      getLogger().info("Deleting project (hard=" + m_Hard + "): " + project);

    try {
      result = m_Client.projects().delete(project, m_Hard);
    }
    catch (Exception e) {
      errors.add("Failed to delete project (hard=" + m_Hard + "): " + project, e);
    }

    return result;
  }
}
