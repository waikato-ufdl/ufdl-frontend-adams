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
 * IsNull.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.field;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;

/**
 * Checks for null values in fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class IsNull
  extends AbstractFieldExpression {

  private static final long serialVersionUID = -7143892332723711826L;

  /**
   * Default constructor.
   */
  public IsNull() {
    super();
  }

  /**
   * Initializes the expression.
   *
   * @param field 		the field to operate on
   */
  public IsNull(String field) {
    super(field);
  }

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Checks for null values in fields.";
  }

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  @Override
  protected AbstractExpression doGenerate(MessageCollection errors) {
    return new com.github.waikatoufdl.ufdl4j.filter.field.IsNull(m_Field, m_Invert);
  }
}
