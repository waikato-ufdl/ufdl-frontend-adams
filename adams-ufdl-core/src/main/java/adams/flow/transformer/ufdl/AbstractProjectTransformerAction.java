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
 * AbstractProjectTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;

/**
 * Ancestor for transformer actions on projects.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractProjectTransformerAction
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, Project.class};
  }

  /**
   * Transforms the project.
   *
   * @param project	the project
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Project project, MessageCollection errors);

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
    Project	project;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming project: " + input);

    // load project
    project = null;
    try {
      if (input instanceof Integer)
	project = m_Client.projects().load((Integer) input);
      else if (input instanceof String)
	project = m_Client.projects().load("" + input);
      else
        project = (Project) input;
    }
    catch (Exception e) {
      errors.add("Failed to load project: " + input, e);
    }

    if (project == null)
      errors.add("Unknown project: " + input);
    else
      result = doTransform(project, errors);

    return result;
  }
}
