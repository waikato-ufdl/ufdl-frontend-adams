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
 * AbstractLogicalExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.logical;

import adams.data.ufdlfilter.AbstractExpression;

/**
 * Ancestor for logical expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractLogicalExpression
  extends AbstractExpression {

  private static final long serialVersionUID = 5333438427635336532L;
  
  /** the sub expressions. */
  protected AbstractExpression[] m_SubExpressions;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "sub-expression", "subExpressions",
      new AbstractExpression[0]);
  }

  /**
   * Sets the sub-expressions that make up the filter.
   *
   * @param value	the expressions
   */
  public void setSubExpressions(AbstractExpression[] value) {
    m_SubExpressions = value;
    reset();
  }

  /**
   * Returns the sub-expressions that make up the filter.
   *
   * @return		the expressions
   */
  public AbstractExpression[] getSubExpressions() {
    return m_SubExpressions;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String subExpressionsTipText() {
    return "The sub-expressions that make up the filter.";
  }

  /**
   * Adds an expression to the array of expressions.
   *
   * @param expression	the expression to add
   * @return		itself
   */
  public AbstractLogicalExpression addSubExpression(AbstractExpression expression) {
    AbstractExpression[] 	subExpressions;
    int				i;

    subExpressions = new AbstractExpression[m_SubExpressions.length + 1];
    for (i = 0; i < m_SubExpressions.length; i++)
      subExpressions[i] = m_SubExpressions[i];
    subExpressions[subExpressions.length - 1] = expression;
    m_SubExpressions = subExpressions;
    reset();

    return this;
  }
}
