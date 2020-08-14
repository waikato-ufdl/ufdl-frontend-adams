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
 * AbstractJobTypeTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.JobTypes;
import com.github.waikatoufdl.ufdl4j.action.JobTypes.JobType;

/**
 * Ancestor of transformer actions on job types.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of JobTypes action
 */
public abstract class AbstractJobTypeTransformerAction<T extends JobTypes>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, JobType.class};
  }

  /**
   * Returns the job types action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getJobTypesAction() throws Exception {
    return (T) m_Client.action(JobTypes.class);
  }

  /**
   * Transforms the job type.
   *
   * @param type	the job type
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(JobType type, MessageCollection errors);

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
    JobType 	type;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming job type: " + input);

    // load job type
    type = null;
    try {
      action = getJobTypesAction();
      if (input instanceof Integer)
	type = action.load((Integer) input);
      else if (input instanceof String)
	type = action.load("" + input);
      else
	type = (JobType) input;
    }
    catch (Exception e) {
      errors.add("Failed to load job type: " + input, e);
    }

    if (type == null)
      errors.add("Unknown job type: " + input);
    else
      result = doTransform(type, errors);

    return result;
  }
}
