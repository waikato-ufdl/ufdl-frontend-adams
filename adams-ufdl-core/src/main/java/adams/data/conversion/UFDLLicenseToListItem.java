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
 * UFDLLicenseToListItem.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

/**
 * Converts a UFDL License object into a list item string.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLLicenseToListItem
  extends AbstractUFDLObjectToListItemConversion<License> {

  private static final long serialVersionUID = -6864071327496473568L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a UFDL License object into a list item string.";
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return License.class;
  }

  /**
   * Converts the input object into a struct.
   *
   * @param input	the input object to convert
   * @return		the generated struct
   */
  @Override
  protected Struct2<Integer, String> objectToStruct(License input) {
    return new Struct2<>(input.getPK(), input.getName());
  }
}
