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
 * UFDLTeamToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.core.Utils;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts a team into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLTeamToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a team into a Spreadsheet.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "resolve-ids", "resolveIDs",
      false);
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return Team.class;
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
    row.addCell("tn").setContentAsString("name");
    row.addCell("ci").setContentAsString("creator");
    row.addCell("ct").setContentAsString("creation_time");
    row.addCell("dt").setContentAsString("deletion_time");
    row.addCell("me").setContentAsString("members");

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
    Team		team;

    team   = (Team) m_Input;
    result = getTemplate();
    row    = result.addRow();
    row.addCell("pk").setContent(team.getPK());
    row.addCell("tn").setContent(team.getName());
    row.addCell("ci").setContent(getUser(team.getCreator()));
    if (team.getCreationTime() != null)
      row.addCell("ct").setContent(new DateTimeMsec(Date.from(team.getCreationTime().toInstant(ZoneOffset.UTC))));
    if (team.getDeletionTime() != null)
      row.addCell("dt").setContent(new DateTimeMsec(Date.from(team.getDeletionTime().toInstant(ZoneOffset.UTC))));
    if (getResolveIDs())
      row.addCell("me").setContent(Utils.flatten(team.members(), ","));
    else
      row.addCell("me").setContent(Utils.flatten(team.membersPK(), ","));

    return result;
  }
}
