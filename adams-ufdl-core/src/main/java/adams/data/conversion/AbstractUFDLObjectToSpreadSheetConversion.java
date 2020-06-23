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
 * AbstractUFDLObjectToSpreadSheetConversion.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.AdditionalInformationHandler;
import adams.data.spreadsheet.SpreadSheet;

/**
 * Ancestor for conversions that convert UFDL objects into spreadsheets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLObjectToSpreadSheetConversion
  extends AbstractConversion
  implements AdditionalInformationHandler {

  private static final long serialVersionUID = -4489686955674646994L;

  /**
   * Returns the class that is generated as output.
   *
   * @return		the class
   */
  @Override
  public Class generates() {
    return SpreadSheet.class;
  }

  /**
   * Generates the template.
   *
   * @return		the template
   */
  protected abstract SpreadSheet getTemplate();

  /**
   * Returns the additional information.
   *
   * @return		the additional information, null or 0-length string for no information
   */
  @Override
  public String getAdditionalInformation() {
    StringBuilder	result;
    SpreadSheet		template;
    int			i;

    result   = new StringBuilder();
    template = getTemplate();
    result.append("Spreadsheet columns:\n");
    for (i = 0; i < template.getColumnCount(); i++)
      result.append((i+1)).append(". ").append(template.getColumnName(i)).append("\n");

    return result.toString();
  }
}
