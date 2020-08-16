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
 * AddJobTemplateInput.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

/**
 * Adds the specified input to a job template passing through.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddJobTemplateInput
  extends AbstractJobTemplateTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the input name. */
  protected String m_Name;

  /** the input type. */
  protected String m_Type;

  /** the (optional) options. */
  protected String m_Options;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the specified input to a job template passing through.";
  }

  /**
   * Adds the specified input to a job template passing through.
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
      "options", "options",
      "");
  }

  /**
   * Sets the input name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the input name.
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
    return "The input name.";
  }

  /**
   * Sets the input type, e.g., bool/int/float/str/model/dataset.
   *
   * @param value	the type
   */
  public void setType(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the input type, e.g., bool/int/float/str/model/dataset.
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
    return "The input type, e.g., bool/int/float/str/model/dataset.";
  }

  /**
   * Sets the (optional) input options, e.g., for conversion of datasets.
   *
   * @param value	the options
   */
  public void setOptions(String value) {
    m_Options = value;
    reset();
  }

  /**
   * Returns the (optional) input options, e.g., for conversion of datasets.
   *
   * @return		the options
   */
  public String getOptions() {
    return m_Options;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String optionsTipText() {
    return "The (optional) input options, e.g., for conversion of datasets.";
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
	getLogger().info("Adding input " + m_Name + " to job template '" + template + "'");
      if (!m_Client.jobTemplates().addInput(template, m_Name, m_Type, m_Options))
	errors.add("Failed to add input " + m_Name + " to job template '" + template + "': " + template);
    }
    catch (Exception e) {
      errors.add("Failed to add input " + m_Name + " to job template '" + template + "': " + template, e);
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
