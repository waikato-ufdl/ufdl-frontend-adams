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
 * ListLogEntries.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.data.conversion.UFDLLogEntryToSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Log.LogEntry;

import java.util.List;

/**
 * Outputs a spreadsheet with all the log entries.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ListLogEntries
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Outputs a spreadsheet with all the log entries.";
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    SpreadSheet			result;
    SpreadSheet			sheet;
    List<LogEntry> 		entries;
    UFDLLogEntryToSpreadSheet	conv;
    String			msg;

    result = null;

    try {
      entries = m_Client.log().list();
      conv    = new UFDLLogEntryToSpreadSheet();
      for (LogEntry entry : entries) {
        conv.setInput(entry);
        msg = conv.convert();
        if (msg == null) {
          if (result == null) {
	    result = (SpreadSheet) conv.getOutput();
	  }
          else {
            sheet = (SpreadSheet) conv.getOutput();
            for (Row row: sheet.rows())
	      result.addRow().assign(row);
	  }
	}
	else {
          errors.add("Failed to convert log entry: " + entry + "\n" + msg);
	}
      }
      conv.cleanUp();
    }
    catch (Exception e) {
      errors.add("Failed to list log entries!", e);
    }

    return result;
  }
}
