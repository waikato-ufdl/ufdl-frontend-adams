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
 * DeleteJobType.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.JobTypes.JobType;

/**
 * Deletes the job type either via PK or job type name.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DeleteJobType
  extends AbstractJobTypeTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Deletes the job type either via PK or job type name.";
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
   * Transforms the job type.
   *
   * @param type	the job type
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(JobType type, MessageCollection errors) {
    boolean	result;

    result = false;

    if (isLoggingEnabled())
      getLogger().info("Deleting job type: " + type);

    try {
      result = m_Client.jobTypes().delete(type);
    }
    catch (Exception e) {
      errors.add("Failed to delete job type: " + type, e);
    }

    return result;
  }
}
