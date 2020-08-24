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
 * AbstractPipeSeparatedBaseString.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

import adams.core.base.AbstractBaseString;

/**
 * Ancestor for BaseObject classes that use the pipe to separate multiple values.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractPipeSeparatedBaseString
  extends AbstractBaseString {

  private static final long serialVersionUID = -681525829858038002L;

  /** the separator for the three values. */
  public final static String SEPARATOR = "|";

  /**
   * Initializes the parameter.
   */
  protected AbstractPipeSeparatedBaseString() {
    super();
  }

  /**
   * Initializes the parameter.
   *
   * @param s		the string to use
   */
  protected AbstractPipeSeparatedBaseString(String s) {
    super(s);
  }

  /**
   * Splits the string into its separate parts.
   *
   * @param value	the value to split
   * @return		the parts
   */
  protected String[] split(String value) {
    if (value.isEmpty())
      return new String[0];
    else
      return value.split("\\|");
  }

  /**
   * Returns the number of parts that are considered valid.
   *
   * @return		the valid number of parts
   */
  protected abstract int[] validNumParts();

  /**
   * Checks whether the string value is a valid presentation for this class.
   *
   * @param value	the string value to check
   * @return		true if non-null
   */
  @Override
  public boolean isValid(String value) {
    String[]  	parts;
    int[]	nums;

    if (value == null)
      return false;
    if (value.isEmpty())
      return true;
    parts = split(value);

    nums = validNumParts();
    for (int num: nums) {
      if (parts.length == num)
        return true;
    }

    return false;
  }

  /**
   * Returns the specified part.
   *
   * @param index	the index of the part to retrieve
   * @return		the part or empty string if not available
   */
  protected String getPart(int index) {
    String[]  	parts;

    parts = split(getValue());
    if (parts.length > index)
      return parts[index];
    else
      return "";
  }
}
