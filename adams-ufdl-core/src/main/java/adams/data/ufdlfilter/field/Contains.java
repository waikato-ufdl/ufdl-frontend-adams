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
 * Contains.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.field;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Checks for sub-strings in string fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Contains
  extends AbstractFieldExpression {

  private static final long serialVersionUID = -7143892332723711826L;

  /** the substring to look for. */
  protected String m_SubString;

  /** whether the search is case-insensitive. */
  protected boolean m_CaseInsensitive;

  /**
   * Default constructor.
   */
  public Contains() {
    super();
  }

  /**
   * Initializes the expression.
   *
   * @param field 		the field to operate on
   * @param subString		the substring to search for
   * @param caseInsensitive	whether the search case insensitive or not
   */
  public Contains(String field, String subString, boolean caseInsensitive) {
    super(field);
    setSubString(subString);
    setCaseInsensitive(caseInsensitive);
  }

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Checks for sub-strings in string fields.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "sub-string", "subString",
      "");

    m_OptionManager.add(
      "case-insensitive", "caseInsensitive",
      false);
  }

  /**
   * Sets the string to search for.
   *
   * @param value	the string
   */
  public void setSubString(String value) {
    m_SubString = value;
    reset();
  }

  /**
   * Returns the string to search for.
   *
   * @return		the string
   */
  public String getSubString() {
    return m_SubString;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String subStringTipText() {
    return "The sub-string to locate in the field value.";
  }

  /**
   * Sets whether to perform a case-insensitive search.
   *
   * @param value	true if case-insensitive
   */
  public void setCaseInsensitive(boolean value) {
    m_CaseInsensitive = value;
    reset();
  }

  /**
   * Returns whether to perform a case-insensitive search.
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
    return "If enabled, the search will be case-insensitive.";
  }

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  @Override
  protected AbstractExpression doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.field.Contains(m_Field, m_SubString, m_CaseInsensitive, m_Invert);
  }
}
