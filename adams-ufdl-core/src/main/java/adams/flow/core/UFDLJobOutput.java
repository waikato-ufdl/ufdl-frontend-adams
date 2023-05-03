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
 * UFDLJobTemplateInput.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

/**
 * Defines a job output (job-pk|name|type).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobOutput
  extends AbstractPipeSeparatedBaseString {

  private static final long serialVersionUID = -681525829858038002L;

  /**
   * Initializes the job output.
   */
  public UFDLJobOutput() {
    super();
  }

  /**
   * Initializes the job output.
   *
   * @param s		the string to use
   */
  public UFDLJobOutput(String s) {
    super(s);
  }

  /**
   * Initializes the job output.
   *
   * @param pk 		the primary key of the job
   */
  public UFDLJobOutput(int pk) {
    this("" + pk);
  }

  /**
   * Initializes the job output.
   *
   * @param pk 		the primary key of the job
   * @param name	the name
   * @param type 	the type, can be empty
   */
  public UFDLJobOutput(int pk, String name, String type) {
    this(pk + SEPARATOR + name + SEPARATOR + type);
  }

  /**
   * Returns the number of parts that are considered valid.
   *
   * @return		the valid number of parts
   */
  @Override
  protected int[] validNumParts() {
    return new int[]{1, 3};
  }

  /**
   * Returns the primary key part, if possible.
   *
   * @return		the pk, -1 if not available
   */
  public int pkValue() {
    String	part;

    part = getPart(0);
    if (part.isEmpty())
      return -1;
    else
      return Integer.parseInt(part);
  }

  /**
   * Returns the name part, if possible.
   *
   * @return		the name, empty if not available
   */
  public String nameValue() {
    return getPart(1);
  }

  /**
   * Returns the type part, if possible.
   *
   * @return		the type, empty if not available
   */
  public String typeValue() {
    return getPart(2);
  }

  /**
   * Returns a tool tip for the GUI editor (ignored if null is returned).
   *
   * @return		the tool tip
   */
  @Override
  public String getTipText() {
    return "The job output definition";
  }
}
