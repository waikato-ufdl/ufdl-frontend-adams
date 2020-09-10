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
 * UFDLJobToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts a Job object into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a Job object into a Spreadsheet.";
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
    return Job.class;
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
    row.addCell("de").setContentAsString("description");
    row.addCell("cr").setContentAsString("creator");
    row.addCell("ct").setContentAsString("creation_time");
    row.addCell("dt").setContentAsString("deletion_time");
    row.addCell("jt").setContentAsString("job_template");
    row.addCell("di").setContentAsString("docker_image");
    row.addCell("no").setContentAsString("node");
    row.addCell("er").setContentAsString("error");
    row.addCell("st").setContentAsString("start_time");
    row.addCell("et").setContentAsString("end_time");
    row.addCell("iv").setContentAsString("input_values");
    row.addCell("pv").setContentAsString("parameter_values");
    row.addCell("ou").setContentAsString("outputs");

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
    Job 		job;

    job     = (Job) m_Input;
    result  = getTemplate();
    row     = result.addRow();
    row.addCell("pk").setContent(job.getPK());
    row.addCell("de").setContentAsString(job.getDescription());
    row.addCell("cr").setContent(getUser(job.getCreator()));
    if (job.getCreationTime() != null)
      row.addCell("ct").setContent(new DateTimeMsec(Date.from(job.getCreationTime().toInstant(ZoneOffset.UTC))));
    if (job.getDeletionTime() != null)
      row.addCell("dt").setContent(new DateTimeMsec(Date.from(job.getDeletionTime().toInstant(ZoneOffset.UTC))));
    row.addCell("jt").setContent(getJobTemplate(job.getJobTemplate()));
    row.addCell("di").setContent(getDockerImage(job.getDockerImage()));
    row.addCell("no").setContent(job.getNode());
    row.addCell("er").setContentAsString(job.getError());
    if (job.getStartTime() != null)
      row.addCell("st").setContent(new DateTimeMsec(Date.from(job.getStartTime().toInstant(ZoneOffset.UTC))));
    if (job.getEndTime() != null)
      row.addCell("et").setContent(new DateTimeMsec(Date.from(job.getEndTime().toInstant(ZoneOffset.UTC))));
    row.addCell("iv").setContentAsString(job.getInputValues().toString());
    row.addCell("pv").setContentAsString(job.getParameterValues().toString());
    row.addCell("ou").setContentAsString(job.getOutputs().toString());

    return result;
  }
}
