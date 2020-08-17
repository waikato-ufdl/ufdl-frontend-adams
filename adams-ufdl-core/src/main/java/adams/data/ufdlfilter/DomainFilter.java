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
 * DomainFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.Filter;

/**
 * Performs an exact match on the domain.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DomainFilter
  extends AbstractUFDLFilter {

  private static final long serialVersionUID = 2619705759928753024L;

  /** the domain. */
  protected String m_Domain;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Performs an exact match on the domain.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "domain", "domain",
      "");
  }

  /**
   * Sets the domain to look for.
   *
   * @param value	the domain
   */
  public void setDomain(String value) {
    m_Domain = value;
    reset();
  }

  /**
   * Returns the domain to look for.
   *
   * @return		the domain
   */
  public String getDomain() {
    return m_Domain;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String domainTipText() {
    return "The exact domain to look for.";
  }

  /**
   * Generates the filter.
   *
   * @param errors	for collecting errors
   * @return		the filter, null if failed to generate
   */
  @Override
  protected Filter doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.DomainFilter(m_Domain);
  }
}
