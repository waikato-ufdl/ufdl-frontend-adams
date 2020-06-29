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
 * UpdateLicense.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseURL;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

/**
 * Updates a license and forwards the license object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdateLicense
  extends AbstractLicenseTransformerAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the license name. */
  protected String m_Name;

  /** the url. */
  protected BaseURL m_URL;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates a license and forwards the license object.";
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
      "url", "URL",
      new BaseURL());
  }

  /**
   * Sets the new license name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the new license name.
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
    return "The new license name.";
  }

  /**
   * Sets the new team URL.
   *
   * @param value	the URL
   */
  public void setURL(BaseURL value) {
    m_URL = value;
    reset();
  }

  /**
   * Returns the new URL.
   *
   * @return		the URL
   */
  public BaseURL getURL() {
    return m_URL;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String URLTipText() {
    return "The new license URL.";
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
    result += QuickInfoHelper.toString(this, "URL", m_URL, ", URL: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{License.class};
  }

  /**
   * Transforms the input data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doTransform(License license, MessageCollection errors) {
    License    result;

    result = null;
    try {
      result = m_Client.licenses().update(license, m_Name, m_URL.getValue());
    }
    catch (Exception e) {
      errors.add("Failed to update license!", e);
    }

    return result;
  }
}
