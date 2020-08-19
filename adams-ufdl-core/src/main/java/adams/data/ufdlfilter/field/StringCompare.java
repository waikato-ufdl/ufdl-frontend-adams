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
 * StringCompare.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.field;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Compares strings using the specified comparison operator.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StringCompare
  extends AbstractCompareExpression {

  private static final long serialVersionUID = -1516313370847400951L;

  /** the value to compare against. */
  protected String m_Value;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Compares strings using the specified comparison operator.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "value", "value",
      "");
  }

  /**
   * Sets the value to compare the field against.
   *
   * @param value	the value
   */
  public void setValue(String value) {
    m_Value = value;
    reset();
  }

  /**
   * Returns the value to compare the field against.
   *
   * @return		the value
   */
  public String getValue() {
    return m_Value;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String valueTipText() {
    return "The value to compare the field against.";
  }

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  @Override
  protected AbstractExpression doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.field.StringCompare(m_Field, m_Value, m_Comparison, m_Invert);
  }
}
