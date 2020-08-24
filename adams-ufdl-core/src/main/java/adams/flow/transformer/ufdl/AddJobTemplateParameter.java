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
 * AddJobTemplateParameter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

/**
 * Adds the specified parameter to a job template passing through.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddJobTemplateParameter
  extends AbstractJobTemplateTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the parameter name. */
  protected String m_Name;

  /** the parameter type. */
  protected String m_Type;

  /** the default value. */
  protected String m_DefaultValue;

  /** the help string. */
  protected String m_Help;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the specified parameter to a job template passing through.";
  }

  /**
   * Adds the specified parameter to a job template passing through.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "name", "name",
      "");

    m_OptionManager.add(
      "type", "type",
      "");

    m_OptionManager.add(
      "default", "defaultValue",
      "");

    m_OptionManager.add(
      "help", "help",
      "");
  }

  /**
   * Sets the parameter name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the parameter name.
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
    return "The parameter name.";
  }

  /**
   * Sets the parameter type, e.g., bool/int/float/str/model/dataset.
   *
   * @param value	the type
   */
  public void setType(String value) {
    m_Type = value;
    reset();
  }

  /**
   * Returns the parameter type, e.g., bool/int/float/str/model/dataset.
   *
   * @return		the type
   */
  public String getType() {
    return m_Type;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String typeTipText() {
    return "The parameter type, e.g., bool/int/float/str/model/dataset.";
  }

  /**
   * Sets the default value of the parameter.
   *
   * @param value	the options
   */
  public void setDefaultValue(String value) {
    m_DefaultValue = value;
    reset();
  }

  /**
   * Returns the default value of the parameter.
   *
   * @return		the options
   */
  public String getDefaultValue() {
    return m_DefaultValue;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String defaultValueTipText() {
    return "The default value of the parameter.";
  }

  /**
   * Sets the (optional) help string.
   *
   * @param value	the help string
   */
  public void setHelp(String value) {
    m_Help = value;
    reset();
  }

  /**
   * Returns the (optional) help string.
   *
   * @return		the help string
   */
  public String getHelp() {
    return m_Help;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String helpTipText() {
    return "The (optional) help string.";
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
   * Transforms the job template.
   *
   * @param template	the job template
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(JobTemplate template, MessageCollection errors) {
    JobTemplate 	result;

    try {
      if (isLoggingEnabled())
	getLogger().info("Adding parameter " + m_Name + " to job template '" + template + "'");
      if (!m_Client.jobTemplates().addParameter(template, m_Name, m_Type, m_DefaultValue, m_Help))
	errors.add("Failed to add parameter " + m_Name + " to job template '" + template + "': " + template);
    }
    catch (Exception e) {
      errors.add("Failed to add parameter " + m_Name + " to job template '" + template + "': " + template, e);
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
