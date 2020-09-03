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
 * UFDLLogEntryToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Log.LogEntry;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts a log entry into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLLogEntryToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a log entry into a Spreadsheet.";
  }

  /**
   * Returns whether ID resolution is available.
   *
   * @return		true if available
   */
  @Override
  protected boolean allowIDResolution() {
    return false;
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return LogEntry.class;
  }

  /**
   * Generates the template.
   *
   * @return		the template
   */
  @Override
  public SpreadSheet getTemplate() {
    SpreadSheet result;
    Row 	row;

    result = new DefaultSpreadSheet();
    row    = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("t").setContentAsString("timestamp");
    row.addCell("l").setContentAsString("level");
    row.addCell("m").setContentAsString("conditions");
    row.addCell("i").setContentAsString("limitations");

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
    LogEntry 		entry;

    entry  = (LogEntry) m_Input;
    result = getTemplate();
    row    = result.addRow();
    row.addCell("pk").setContent(entry.getPK());
    row.addCell("t").setContent(new DateTimeMsec(Date.from(entry.getCreationTime().toInstant(ZoneOffset.UTC))));
    row.addCell("l").setContentAsString(entry.getLevel().toString());
    row.addCell("m").setContent(entry.getMessage());
    row.addCell("i").setContent(entry.isInternal());

    return result;
  }
}
