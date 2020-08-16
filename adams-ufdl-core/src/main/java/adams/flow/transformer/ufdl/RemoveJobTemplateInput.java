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
 * RemoveJobTemplateInput.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.base.BaseString;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

/**
 * Removes the specified input(s) from the job template passing through.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RemoveJobTemplateInput
  extends AbstractJobTemplateTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the inputs remove. */
  protected BaseString[] m_Inputs;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Removes the specified input(s) from the job template passing through.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "input", "inputs",
      new BaseString[0]);
  }

  /**
   * Sets the input names to remove.
   *
   * @param value	the input names
   */
  public void setInputs(BaseString[] value) {
    m_Inputs = value;
    reset();
  }

  /**
   * Returns the input names to remove.
   *
   * @return		the input names
   */
  public BaseString[] getInputs() {
    return m_Inputs;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String inputsTipText() {
    return "The PKs of the members to remove.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{JobTemplate.class};
  }

  /**
   * Transforms the template.
   *
   * @param template	the template
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(JobTemplate template, MessageCollection errors) {
    JobTemplate 	result;

    for (BaseString input : m_Inputs) {
      try {
        if (isLoggingEnabled())
          getLogger().info("Removing input " + input + " from job template '" + template + "'");
        if (!m_Client.jobTemplates().deleteInput(template, input.getValue()))
          errors.add("Failed to remove input " + input + " from job template '" + template + "': " + template);
      }
      catch (Exception e) {
        errors.add("Failed to remove input " + input + " from job template '" + template + "': " + template, e);
      }
    }

    // reload template
    result = template;
    try {
      result = m_Client.jobTemplates().load(template.getPK());
    }
    catch (Exception e) {
      errors.add("Failed to reload job template: " + template, e);
    }

    return result;
  }
}
