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
 * AddJobOutput.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.io.PlaceholderFile;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;

/**
 * Adds the file as job output via name/type.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddJobOutput
  extends AbstractJobTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the name of the output to add. */
  protected String m_Name;

  /** the type of the output to add. */
  protected String m_Type;

  /** the file to upload. */
  protected PlaceholderFile m_Output;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the job output via name/type.";
  }

  /**
   * Adds options to the internal list of options.
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
      "output", "output",
      new PlaceholderFile());
  }

  /**
   * Sets the name of the output to add.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name of the output to add.
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
    return "The name of the output to add.";
  }

  /**
   * Sets the type of the output to add.
   *
   * @param value 	the type
   */
  public void setType(String value) {
    m_Type = value;
    reset();
  }

  /**
   * Returns the type of the output to add.
   *
   * @return 		the type
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
    return "The type of the output to add.";
  }

  /**
   * Sets the output file to add.
   *
   * @param value 	the file
   */
  public void setOutput(PlaceholderFile value) {
    m_Output = value;
    reset();
  }

  /**
   * Returns the output file to add.
   *
   * @return 		the file
   */
  public PlaceholderFile getOutput() {
    return m_Output;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String outputTipText() {
    return "The job output file to add.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", (m_Name.isEmpty() ? "-none-" : m_Name), "name: ");
    result += QuickInfoHelper.toString(this, "type", (m_Type.isEmpty() ? "-none-" : m_Type), ", type: ");
    result += QuickInfoHelper.toString(this, "output", m_Output, ", output: ");

    return result;
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
   * Transforms the job.
   *
   * @param job	the job
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Job job, MessageCollection errors) {
    boolean	result;

    result = false;

    if (isLoggingEnabled())
      getLogger().info("Adding job output (" + m_Name + "/" + m_Type + "/" + m_Output + "): " + job);

    try {
      result = m_Client.jobs().addOutput(job, m_Name, m_Type, m_Output);
    }
    catch (Exception e) {
      errors.add("Failed to add job output (" + m_Name + "/" + m_Type + "/" + m_Output + "): " + job, e);
    }

    return result;
  }
}
