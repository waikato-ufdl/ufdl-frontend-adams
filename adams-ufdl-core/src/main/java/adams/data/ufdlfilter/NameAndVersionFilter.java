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
 * NameAndVersionFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.Filter;

/**
 * Performs an exact match on the name/version.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NameAndVersionFilter
  extends AbstractUFDLFilter {

  private static final long serialVersionUID = 2619705759928753024L;

  /** the name. */
  protected String m_Name;

  /** the version. */
  protected String m_Version;

  /**
   * Default constructor.
   */
  public NameAndVersionFilter() {
    super();
  }

  /**
   * Initializes the filter with name and version.
   *
   * @param name	the name to use
   * @param version	the version to use
   */
  public NameAndVersionFilter(String name, String version) {
    this();
    setName(name);
    setVersion(version);
  }

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Performs an exact match on the name.";
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
      "version", "version",
      "");
  }

  /**
   * Sets the name to look for.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name to look for.
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
    return "The exact name to look for.";
  }

  /**
   * Sets the version to look for.
   *
   * @param value	the version
   */
  public void setVersion(String value) {
    m_Version = value;
    reset();
  }

  /**
   * Returns the version to look for.
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
    return "The exact version to look for.";
  }

  /**
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		the filter, null if failed to generate
   */
  @Override
  protected Filter doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.NameAndVersionFilter(m_Name, m_Version);
  }
}
