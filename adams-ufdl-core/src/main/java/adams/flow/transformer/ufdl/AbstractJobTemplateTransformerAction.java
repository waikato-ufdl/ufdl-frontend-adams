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
 * AbstractJobTemplateTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

/**
 * Ancestor of transformer actions on job templates.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractJobTemplateTransformerAction
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, JobTemplate.class};
  }

  /**
   * Returns the job templates action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected JobTemplates getJobTemplatesAction() throws Exception {
    return m_Client.action(JobTemplates.class);
  }

  /**
   * Transforms the job template.
   *
   * @param template	the job template
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(JobTemplate template, MessageCollection errors);

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Object input, MessageCollection errors) {
    Object		result;
    JobTemplate 	template;
    JobTemplates	action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming job template: " + input);

    // load job template
    template = null;
    try {
      action = getJobTemplatesAction();
      if (input instanceof Integer)
	template = action.load((Integer) input);
      else
	template = (JobTemplate) input;
    }
    catch (Exception e) {
      errors.add("Failed to load job template: " + input, e);
    }

    if (template == null)
      errors.add("Unknown job template: " + input);
    else
      result = doTransform(template, errors);

    return result;
  }
}
