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
 * GenericFilter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.Filter;

/**
 * Generic filter, requires knowledge about the API field names.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class GenericFilter
  extends AbstractUFDLFilter {

  private static final long serialVersionUID = 2308026390090971511L;

  /** the expressions. */
  protected AbstractExpression[] m_Expressions;

  /** the ordering. */
  protected OrderBy[] m_Order;

  /** whether to include inactive elements. */
  protected boolean m_IncludeInactive;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Generic filter, requires knowledge about the API field names.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "expression", "expressions",
      new AbstractExpression[0]);

    m_OptionManager.add(
      "order", "order",
      new OrderBy[0]);

    m_OptionManager.add(
      "include-inactive", "includeInactive",
      false);
  }

  /**
   * Sets the expressions to use.
   *
   * @param value	the expressions
   */
  public void setExpressions(AbstractExpression[] value) {
    m_Expressions = value;
    reset();
  }

  /**
   * Returns the expressions to use.
   *
   * @return		the expressions
   */
  public AbstractExpression[] getExpressions() {
    return m_Expressions;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String expressionsTipText() {
    return "The expressions to use.";
  }

  /**
   * Sets the order to impose.
   *
   * @param value	the order
   */
  public void setOrder(OrderBy[] value) {
    m_Order = value;
    reset();
  }

  /**
   * Returns the order to impose.
   *
   * @return		the order
   */
  public OrderBy[] getOrder() {
    return m_Order;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String orderTipText() {
    return "The order to impose on the result.";
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
   * @return		the filter, null if failed to generate
   */
  @Override
  protected Filter doGenerate(MessageCollection errors) {
    com.github.waikatoufdl.ufdl4j.filter.AbstractExpression[]	expressions;
    com.github.waikatoufdl.ufdl4j.filter.OrderBy[]		order;
    int								i;

    expressions = new com.github.waikatoufdl.ufdl4j.filter.AbstractExpression[m_Expressions.length];
    for (i = 0; i < m_Expressions.length; i++)
      expressions[i] = m_Expressions[i].generate(errors);

    order = new com.github.waikatoufdl.ufdl4j.filter.OrderBy[m_Order.length];
    for (i = 0; i < m_Order.length; i++)
      order[i] = m_Order[i].generate(errors);

    if (!errors.isEmpty())
      return null;

    return new com.github.waikatoufdl.ufdl4j.filter.GenericFilter(expressions, order, m_IncludeInactive);
  }
}
