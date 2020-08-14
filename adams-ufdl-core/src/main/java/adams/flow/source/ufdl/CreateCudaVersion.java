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
 * CreateCudaVersion.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.CudaVersions.CudaVersion;

/**
 * Creates a cuda version and forwards the cuda version object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateCudaVersion
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the cuda version. */
  protected String m_Version;

  /** the cuda full version. */
  protected String m_FullVersion;

  /** the minimum driver version. */
  protected String m_MinDriverVersion;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a cuda version and forwards the cuda version object.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "version", "version",
      "");

    m_OptionManager.add(
      "full-version", "fullVersion",
      "");

    m_OptionManager.add(
      "min-driver-version", "minDriverVersion",
      "");
  }

  /**
   * Sets the cuda version.
   *
   * @param value	the version
   */
  public void setVersion(String value) {
    m_Version = value;
    reset();
  }

  /**
   * Returns the cuda version.
   *
   * @return		the version
   */
  public String getVersion() {
    return m_Version;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String versionTipText() {
    return "The cuda version.";
  }

  /**
   * Sets the cuda full version.
   *
   * @param value	the version
   */
  public void setFullVersion(String value) {
    m_FullVersion = value;
    reset();
  }

  /**
   * Returns the cuda full version.
   *
   * @return		the version
   */
  public String getFullVersion() {
    return m_FullVersion;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String fullVersionTipText() {
    return "The cuda version name.";
  }

  /**
   * Sets the minimum driver version.
   *
   * @param value	the version
   */
  public void setMinDriverVersion(String value) {
    m_MinDriverVersion = value;
    reset();
  }

  /**
   * Returns the minimum driver version.
   *
   * @return		the version
   */
  public String getMinDriverVersion() {
    return m_MinDriverVersion;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String minDriverVersionTipText() {
    return "The minimum driver version.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "version", m_Version, "version: ");
    result += QuickInfoHelper.toString(this, "fullVersion", m_FullVersion, ", full version: ");
    result += QuickInfoHelper.toString(this, "minDriverVersion", m_MinDriverVersion, ", min driver version: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{CudaVersion.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    CudaVersion    result;

    result = null;
    try {
      result = m_Client.cuda().create(m_Version, m_FullVersion, m_MinDriverVersion);
    }
    catch (Exception e) {
      errors.add("Failed to create cuda version!", e);
    }

    return result;
  }
}
