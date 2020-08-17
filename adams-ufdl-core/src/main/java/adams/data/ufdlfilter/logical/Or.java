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
 * Or.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.logical;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Represents a logical OR combination of the sub-expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Or
  extends AbstractLogicalExpression {

  private static final long serialVersionUID = 3441662393846705762L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Represents a logical OR combination of the sub-expressions.";
  }

  /**
   * Generates the expression.
   *
   * @param errors 	for collecting errors
   * @return		the expression, null if failed to generate
   */
  @Override
  protected AbstractExpression doGenerate(MessageCollection errors) {
    AbstractExpression[]	expressions;
    int				i;

    expressions = new AbstractExpression[m_SubExpressions.length];
    for (i = 0; i < m_SubExpressions.length; i++)
      expressions[i] = m_SubExpressions[i].generate(errors);

    if (!errors.isEmpty())
      return null;

    return new com.github.waikatoufdl.ufdl4j.filter.logical.Or(expressions);
  }
}
