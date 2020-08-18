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
 * NameFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.Filter;

/**
 * Performs an exact match on the name.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NameFilter
  extends AbstractUFDLFilter {

  private static final long serialVersionUID = 2619705759928753024L;

  /** the name. */
  protected String m_Name;

  /**
   * Default constructor.
   */
  public NameFilter() {
    super();
  }

  /**
   * Initializes the filer with the name.
   *
   * @param name	the name
   */
  public NameFilter(String name) {
    this();
    setName(name);
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
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		the filter, null if failed to generate
   */
  @Override
  protected Filter doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.NameFilter(m_Name);
  }
}
