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
 * UFDLFilterHandler.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

import adams.data.ufdlfilter.AbstractUFDLFilter;

/**
 * Interface for classes that allow the user to specify a filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface UFDLFilterHandler {

  /**
   * Sets the filter to apply.
   *
   * @param value	the filter
   */
  public void setFilter(AbstractUFDLFilter value);

  /**
   * Returns the filter to apply.
   *
   * @return		the filter
   */
  public AbstractUFDLFilter getFilter();
}
