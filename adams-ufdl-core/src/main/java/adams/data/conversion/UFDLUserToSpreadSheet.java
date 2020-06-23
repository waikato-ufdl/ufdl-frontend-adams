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
 * UFDLUserToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts a user into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLUserToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a user into a Spreadsheet.";
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return User.class;
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
    row    = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("un").setContentAsString("username");
    row.addCell("fn").setContentAsString("first_name");
    row.addCell("ln").setContentAsString("last_name");
    row.addCell("em").setContentAsString("email");
    row.addCell("dj").setContentAsString("joined");
    row.addCell("ll").setContentAsString("last_login");
    row.addCell("st").setContentAsString("staff");
    row.addCell("su").setContentAsString("superuser");
    row.addCell("ac").setContentAsString("active");

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
    User		user;

    user   = (User) m_Input;
    result = new DefaultSpreadSheet();
    row    = result.addRow();
    row.addCell("pk").setContent(user.getPK());
    row.addCell("un").setContent(user.getUserName());
    row.addCell("fn").setContent(user.getFirstName());
    row.addCell("ln").setContent(user.getLastName());
    row.addCell("em").setContent(user.getEmail());
    if (user.getJoined() != null)
      row.addCell("dj").setContent(new DateTimeMsec(Date.from(user.getJoined().toInstant(ZoneOffset.UTC))));
    if (user.getLastLogin() != null)
      row.addCell("ll").setContent(new DateTimeMsec(Date.from(user.getLastLogin().toInstant(ZoneOffset.UTC))));
    row.addCell("st").setContent(user.isStaff());
    row.addCell("su").setContent(user.isSuperuser());
    row.addCell("ac").setContent(user.isActive());

    return result;
  }
}
