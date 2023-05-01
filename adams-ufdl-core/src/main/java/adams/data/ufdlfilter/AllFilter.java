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
 * AllFilter.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.OrderBy;

/**
 * Dummy filter, performs no filtering.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AllFilter
  extends AbstractUFDLFilter {

  private static final long serialVersionUID = 2812847375649367286L;

  /** whether to include inactive elements. */
  protected boolean m_IncludeInactive;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Dummy filter, performs no filtering.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "include-inactive", "includeInactive",
      true);
  }

  /**
   * Sets whether to include inactive items in the result.
   *
   * @param value	true if to include
   */
  public void setIncludeInactive(boolean value) {
    m_IncludeInactive = value;
    reset();
  }

  /**
   * Returns whether to include inactive items in the result.
   *
   * @return		true if to include
   */
  public boolean getIncludeInactive() {
    return m_IncludeInactive;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String includeInactiveTipText() {
    return "If enabled, inactive results will be included in the results.";
  }

  /**
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		the filter
   */
  @Override
  protected Filter doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.GenericFilter(
      new AbstractExpression[0], new OrderBy[0], m_IncludeInactive);
  }
}
