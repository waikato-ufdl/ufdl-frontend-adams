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
 * CreateJobType.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.JobTypes.JobType;

/**
 * Creates a job type and forwards the job type object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateJobType
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the job type name. */
  protected String m_Name;

  /** the python pkg. */
  protected String m_Pkg;

  /** the python cls. */
  protected String m_Cls;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a job type and forwards the job type object.";
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
      "pkg", "pkg",
      "");

    m_OptionManager.add(
      "cls", "cls",
      "");
  }

  /**
   * Sets the job type name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the job type name.
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
    return "The job type name.";
  }

  /**
   * Sets the Python package name.
   *
   * @param value	the package
   */
  public void setPkg(String value) {
    m_Pkg = value;
    reset();
  }

  /**
   * Returns the Python package name.
   *
   * @return		the package
   */
  public String getPkg() {
    return m_Pkg;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String pkgTipText() {
    return "The Python package name.";
  }

  /**
   * Sets the Python class name.
   *
   * @param value	the class
   */
  public void setCls(String value) {
    m_Cls = value;
    reset();
  }

  /**
   * Returns the Python class name.
   *
   * @return		the class
   */
  public String getCls() {
    return m_Cls;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String clsTipText() {
    return "The Python class name.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", m_Name, "name: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{JobType.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    JobType    result;

    result = null;
    try {
      result = m_Client.jobTypes().create(m_Name, m_Pkg, m_Cls);
    }
    catch (Exception e) {
      errors.add("Failed to create job type!", e);
    }

    return result;
  }
}
