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
 * AbortJob.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;

/**
 * Aborts the job via PK.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AbortJob
  extends AbstractJobTransformerAction {

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
    return "Aborts the job via PK.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Job.class};
  }

  /**
   * Transforms the job.
   *
   * @param job	the job
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Job job, MessageCollection errors) {
    Job	result;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Aborting job: " + job);

    try {
      result = m_Client.jobs().abort(job);
    }
    catch (Exception e) {
      errors.add("Failed to abort job: " + job, e);
    }

    return result;
  }
}
