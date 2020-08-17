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
 * OrderBy.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import adams.core.TriState;
import adams.core.option.AbstractOptionHandler;

/**
 * For imposing order on the filtering.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class OrderBy
  extends AbstractOptionHandler {

  private static final long serialVersionUID = -6357892892671155727L;

  /** the field in question. */
  protected String m_Field;

  /** whether to sort ascending or descending. */
  protected boolean m_Ascending;

  /** whether to list nulls first. */
  protected TriState m_NullsFirst;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "For imposing order on the filtering.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "field", "field",
      "");

    m_OptionManager.add(
      "ascending", "ascending",
      true);

    m_OptionManager.add(
      "nulls-first", "nullsFirst",
      TriState.NOT_SET);
  }

  /**
   * Sets the name of the field to operate on.
   *
   * @param value	the name
   */
  public void setField(String value) {
    m_Field = value;
    reset();
  }

  /**
   * Returns the name of the field to operate on.
   *
   * @return		the name
   */
  public String getField() {
    return m_Field;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String fieldTipText() {
    return "The name of the field to operate on.";
  }

  /**
   * Sets whether the sorting is in ascending order.
   *
   * @param value	true if ascending
   */
  public void setAscending(boolean value) {
    m_Ascending = value;
    reset();
  }

  /**
   * Returns whether the sorting is in ascending order.
   *
   * @return		true if ascending
   */
  public boolean getAscending() {
    return m_Ascending;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String ascendingTipText() {
    return "If enabled, the sorting is in ascending order.";
  }

  /**
   * Sets whether to list nulls first. Uses database-default sorting it not set.
   *
   * @param value	whether to list nulls first
   */
  public void setNullsFirst(TriState value) {
    m_NullsFirst = value;
    reset();
  }

  /**
   * Returns whether to list nulls first. Uses database-default sorting it not set.
   *
   * @return		whether to list nulls first
   */
  public TriState getNullsFirst() {
    return m_NullsFirst;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String nullsFirstTipText() {
    return "If enabled, nulls are listed first; if not set, the database-default sorting is used.";
  }

  /**
   * Generates the OrderBy instance.
   *
   * @return		the instance
   */
  protected com.github.waikatoufdl.ufdl4j.filter.OrderBy doGenerate() {
    if (m_NullsFirst.toBoolean() == null)
      return new com.github.waikatoufdl.ufdl4j.filter.OrderBy(m_Field, m_Ascending);
    else
      return new com.github.waikatoufdl.ufdl4j.filter.OrderBy(m_Field, m_Ascending, m_NullsFirst.toBoolean());
  }

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  public com.github.waikatoufdl.ufdl4j.filter.OrderBy generate(MessageCollection errors) {
    try {
      return doGenerate();
    }
    catch (Exception e) {
      errors.add("Failed to generate OrderBy: " + toCommandLine(), e);
      return null;
    }
  }
}
