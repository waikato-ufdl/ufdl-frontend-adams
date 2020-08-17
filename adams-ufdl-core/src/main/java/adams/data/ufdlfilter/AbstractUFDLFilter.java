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
 * AbstractUFDLFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import adams.core.option.AbstractOptionHandler;
import com.github.waikatoufdl.ufdl4j.filter.Filter;

/**
 * Ancestor for UFDL filters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLFilter
  extends AbstractOptionHandler {

  private static final long serialVersionUID = 2332835022954590242L;

  /**
   * Hook method for checks.
   *
   * @return		null if successfully passed checks
   */
  protected String check() {
    return null;
  }

  /**
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		the filter, null if failed to generate
   */
  protected abstract Filter doGenerate(MessageCollection errors);

  /**
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		the filter, null if failed to generate
   */
  public Filter generate(MessageCollection errors) {
    String	msg;

    msg = check();
    if (msg != null) {
      errors.add(msg);
      return null;
    }
    else {
      try {
	return doGenerate(errors);
      }
      catch (Exception e) {
        errors.add("Failed to generate filter!", e);
        return null;
      }
    }
  }
}
