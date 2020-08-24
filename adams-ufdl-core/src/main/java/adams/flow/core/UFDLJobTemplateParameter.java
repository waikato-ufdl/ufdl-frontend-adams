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
 * UFDLDockerImageParameter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

/**
 * Defines a job template parameter (name|type|default).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobTemplateParameter
  extends AbstractPipeSeparatedBaseString {

  private static final long serialVersionUID = -681525829858038002L;

  /**
   * Initializes the parameter.
   */
  public UFDLJobTemplateParameter() {
    super();
  }

  /**
   * Initializes the parameter.
   *
   * @param s		the string to use
   */
  public UFDLJobTemplateParameter(String s) {
    super(s);
  }

  /**
   * Initializes the parameter.
   *
   * @param name	the name
   * @param type 	the type
   * @param defValue 	the default value
   */
  public UFDLJobTemplateParameter(String name, String type, String defValue) {
    this(name, type, defValue, "");
  }

  /**
   * Initializes the parameter.
   *
   * @param name	the name
   * @param type 	the type
   * @param defValue 	the default value
   * @param help 	the help string
   */
  public UFDLJobTemplateParameter(String name, String type, String defValue, String help) {
    this(name + SEPARATOR + type + SEPARATOR + defValue + SEPARATOR + help);
  }

  /**
   * Returns the number of parts that are considered valid.
   *
   * @return		the valid number of parts
   */
  @Override
  protected int[] validNumParts() {
    return new int[]{2, 3, 4};
  }

  /**
   * Returns the name part, if possible.
   *
   * @return		the name, empty if not available
   */
  public String nameValue() {
    return getPart(0);
  }

  /**
   * Returns the type part, if possible.
   *
   * @return		the type, empty if not available
   */
  public String typeValue() {
    return getPart(1);
  }

  /**
   * Returns the default value part, if possible.
   *
   * @return		the default value, empty if not available
   */
  public String defaultValue() {
    return getPart(2);
  }

  /**
   * Returns the help part, if possible.
   *
   * @return		the help, empty if not available
   */
  public String helpValue() {
    return getPart(3);
  }

  /**
   * Returns a tool tip for the GUI editor (ignored if null is returned).
   *
   * @return		the tool tip
   */
  @Override
  public String getTipText() {
    return "The job template parameter definition";
  }
}
