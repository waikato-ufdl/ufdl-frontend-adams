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
 * AbstractFieldExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter.field;

import adams.data.ufdlfilter.AbstractExpression;

/**
 * Ancestor for expressions that operate on fields.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractFieldExpression
  extends AbstractExpression {

  private static final long serialVersionUID = -8841643458918933591L;

  /** the field name. */
  protected String m_Field;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "field", "field",
      "");
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
}
