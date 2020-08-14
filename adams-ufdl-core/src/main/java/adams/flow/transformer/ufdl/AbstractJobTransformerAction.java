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
 * AbstractJobTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Jobs;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;

/**
 * Ancestor of transformer actions on jobs.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of Jobs action
 */
public abstract class AbstractJobTransformerAction<T extends Jobs>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, Job.class};
  }

  /**
   * Returns the jobs action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getJobsAction() throws Exception {
    return (T) m_Client.action(Jobs.class);
  }

  /**
   * Transforms the job.
   *
   * @param job	the job
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Job job, MessageCollection errors);

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
    Job 	job;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming job: " + input);

    // load job
    job = null;
    try {
      action = getJobsAction();
      if (input instanceof Integer)
	job = action.load((Integer) input);
      else
	job = (Job) input;
    }
    catch (Exception e) {
      errors.add("Failed to load job: " + input, e);
    }

    if (job == null)
      errors.add("Unknown job: " + input);
    else
      result = doTransform(job, errors);

    return result;
  }
}
