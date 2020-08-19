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
 * AbstractComparisonExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.field;

import com.github.waikatoufdl.ufdl4j.filter.field.AbstractCompareExpression.Comparison;

/**
 * Ancestor for expressions that operate on fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractCompareExpression
  extends AbstractFieldExpression {

  private static final long serialVersionUID = -8841643458918933591L;

  /** the comparison. */
  protected Comparison m_Comparison;

  /**
   * Default constructor.
   */
  protected AbstractCompareExpression() {
    super();
  }

  /**
   * Initializes the expression with the field name.
   *
   * @param field	the field to use
   */
  protected AbstractCompareExpression(String field, Comparison comparison) {
    super(field);
    setComparison(comparison);
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "comparison", "comparison",
      Comparison.LESS_THAN);
  }

  /**
   * Sets the type of comparison to use.
   *
   * @param value	the comparison
   */
  public void setComparison(Comparison value) {
    m_Comparison = value;
    reset();
  }

  /**
   * Returns the type of comparison to use.
   *
   * @return		the comparison
   */
  public Comparison getComparison() {
    return m_Comparison;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String fieldTipText() {
    return "The type of comparison to use.";
  }
}
