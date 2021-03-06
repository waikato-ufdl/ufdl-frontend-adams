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
 * UFDLNodeToSpreadSheet.java
 * Copyright (C) 2020-2021 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Nodes.Node;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts a Node object into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLNodeToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a Node object into a Spreadsheet.";
  }

  /**
   * Returns whether ID resolution is available.
   *
   * @return		true if available
   */
  @Override
  protected boolean allowIDResolution() {
    return true;
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return Node.class;
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

    row = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("ip").setContentAsString("ip");
    row.addCell("dv").setContentAsString("driver_version");
    row.addCell("hg").setContentAsString("hardware_generation");
    row.addCell("gi").setContentAsString("index");
    row.addCell("gm").setContentAsString("gpu_memory");
    row.addCell("cm").setContentAsString("cpu_memory");
    row.addCell("ls").setContentAsString("last_seen");

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
    Node 		node;

    node = (Node) m_Input;
    result  = getTemplate();
    row     = result.addRow();
    row.addCell("pk").setContent(node.getPK());
    row.addCell("ip").setContentAsString(node.getIP());
    row.addCell("dv").setContentAsString(node.getDriverVersion());
    row.addCell("hg").setContent(getHardwareGeneration(node.getHardwareGeneration()));
    row.addCell("gi").setContent(node.getIndex());
    row.addCell("gm").setContent(node.getGPUMemory());
    row.addCell("cm").setContent(node.getCPUMemory());
    if (node.getLastSeen() != null)
      row.addCell("ls").setContent(new DateTimeMsec(Date.from(node.getLastSeen().toInstant(ZoneOffset.UTC))));

    return result;
  }
}
