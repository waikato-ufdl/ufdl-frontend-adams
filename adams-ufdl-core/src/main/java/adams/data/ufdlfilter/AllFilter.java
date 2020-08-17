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
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.Filter;

/**
 * Dummy filter, performs no filtering.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AllFilter
  extends AbstractUFDLFilter {

  private static final long serialVersionUID = 2812847375649367286L;

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
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		always null
   */
  @Override
  protected Filter doGenerate(MessageCollection errors) {
    return null;
  }
}
