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
 * AbstractExpression.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.ufdlfilter;

import adams.core.MessageCollection;
import adams.core.option.AbstractOptionHandler;

/**
 * Ancestor for expressions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractExpression
  extends AbstractOptionHandler {

  private static final long serialVersionUID = -5030840098202546421L;

  /** whether to invert the matching sense. */
  protected boolean m_Invert;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "invert", "invert",
      false);
  }

  /**
   * Sets whether to invert the matching sense.
   *
   * @param value	true if inverting matching sense
   */
  public void setInvert(boolean value) {
    m_Invert = value;
    reset();
  }

  /**
   * Returns whether to invert the matching sense.
   *
   * @return		true if matching sense is inverted
   */
  public boolean getInvert() {
    return m_Invert;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String invertTipText() {
    return "If set to true, then the matching sense is inverted.";
  }

  /**
   * Hook method for checks.
   *
   * @return		null if successfully passed checks
   */
  protected String check() {
    return null;
  }

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  protected abstract com.github.waikatoufdl.ufdl4j.filter.AbstractExpression doGenerate(MessageCollection errors);

  /**
   * Generates the expression.
   *
   * @param errors	for collecting errors
   * @return		the expression, null if failed to generate
   */
  public com.github.waikatoufdl.ufdl4j.filter.AbstractExpression generate(MessageCollection errors) {
    String	msg;

    msg = check();
    if (msg != null) {
      errors.add(msg);
      return null;
    }
    else {
      try {
	return doGenerate(errors);
      }
      catch (Exception e) {
        errors.add("Failed to generate expression!", e);
        return null;
      }
    }
  }
}
