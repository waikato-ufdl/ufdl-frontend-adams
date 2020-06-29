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
 * UFDLLicenseToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.Utils;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

/**
 * Converts a license into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLLicenseToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a license into a Spreadsheet.";
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
   * Generates the template.
   *
   * @return		the template
   */
  @Override
  protected SpreadSheet getTemplate() {
    SpreadSheet result;
    Row 	row;

    result = new DefaultSpreadSheet();

    row = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("n").setContentAsString("name");
    row.addCell("u").setContentAsString("url");
    row.addCell("p").setContentAsString("permissions");
    row.addCell("c").setContentAsString("conditions");
    row.addCell("l").setContentAsString("limitations");

    return result;
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    SpreadSheet		result;
    Row			row;
    License 		license;

    license = (License) m_Input;
    result  = getTemplate();
    row     = result.addRow();
    row.addCell("pk").setContent(license.getPK());
    row.addCell("n").setContent(license.getName());
    row.addCell("u").setContent(license.getURL());
    row.addCell("p").setContent(Utils.flatten(license.getPermissions().toArray(), ","));
    row.addCell("c").setContent(Utils.flatten(license.getConditions().toArray(), ","));
    row.addCell("l").setContent(Utils.flatten(license.getLimitations().toArray(), ","));

    return result;
  }
}
