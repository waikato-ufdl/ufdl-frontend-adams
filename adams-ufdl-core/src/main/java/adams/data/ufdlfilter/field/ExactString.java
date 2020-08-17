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
 * ExactString.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.field;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Performs an exact match.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ExactString
  extends AbstractFieldExpression {

  private static final long serialVersionUID = -7143892332723711826L;

  /** the value the field must have. */
  protected String m_Value;

  /** whether the comparison is case-sensitive. */
  protected boolean m_CaseInsensitive;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Performs an exact match.";
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

    m_OptionManager.add(
      "case-insensitive", "caseInsensitive",
      false);
  }

  /**
   * Sets the string to compare the field against.
   *
   * @param value	the value
   */
  public void setValue(String value) {
    m_Value = value;
    reset();
  }

  /**
   * Returns the string to compare the field against.
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
   * Sets whether to perform a case-insensitive comparison.
   *
   * @param value	true if case-insensitive
   */
  public void setCaseInsensitive(boolean value) {
    m_CaseInsensitive = value;
    reset();
  }

  /**
   * Returns whether to perform a case-insensitive comparison.
   *
   * @return		true if case-insensitive
   */
  public boolean getCaseInsensitive() {
    return m_CaseInsensitive;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String caseInsensitiveTipText() {
    return "If enabled, the comparison will be case-insensitive.";
  }

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  @Override
  protected AbstractExpression doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.field.ExactString(m_Field, m_Value, m_CaseInsensitive, m_Invert);
  }
}
