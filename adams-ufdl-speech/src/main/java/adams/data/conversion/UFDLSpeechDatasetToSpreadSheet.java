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
 * UFDLSpeechDatasetToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.core.Utils;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets.SpeechDataset;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts a speech dataset into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLSpeechDatasetToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a speech dataset into a Spreadsheet.";
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
    return Dataset.class;
  }

  /**
   * Generates the template.
   *
   * @return		the template
   */
  @Override
  public SpreadSheet getTemplate() {
    SpreadSheet			result;
    Row				row;

    result = new DefaultSpreadSheet();

    row = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("tn").setContentAsString("name");
    row.addCell("de").setContentAsString("description");
    row.addCell("ci").setContentAsString("creator");
    row.addCell("ct").setContentAsString("creation_time");
    row.addCell("dt").setContentAsString("deletion_time");
    row.addCell("li").setContentAsString("license");
    row.addCell("pi").setContentAsString("project");
    row.addCell("ve").setContentAsString("version");
    row.addCell("ta").setContentAsString("tags");
    row.addCell("pu").setContentAsString("public");
    row.addCell("fs").setContentAsString("files");
    row.addCell("ts").setContentAsString("transcripts");

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
    Dataset		dataset;
    SpeechDataset 	spdataset;

    dataset   = (Dataset) m_Input;
    spdataset = dataset.as(SpeechDataset.class);
    result    = getTemplate();
    row       = result.addRow();
    row.addCell("pk").setContent(dataset.getPK());
    row.addCell("tn").setContent(dataset.getName());
    row.addCell("de").setContent(dataset.getDescription());
    row.addCell("ci").setContent(getUser(dataset.getCreator()));
    if (dataset.getCreationTime() != null)
      row.addCell("ct").setContent(new DateTimeMsec(Date.from(dataset.getCreationTime().toInstant(ZoneOffset.UTC))));
    if (dataset.getDeletionTime() != null)
      row.addCell("dt").setContent(new DateTimeMsec(Date.from(dataset.getDeletionTime().toInstant(ZoneOffset.UTC))));
    row.addCell("li").setContent(getLicense(dataset.getLicense()));
    row.addCell("pi").setContent(getProject(dataset.getProject()));
    row.addCell("ve").setContent(dataset.getVersion());
    row.addCell("ta").setContent(dataset.getTags());
    row.addCell("pu").setContent(dataset.isPublic());
    if (spdataset.files().size() > 0)
      row.addCell("fs").setContent(Utils.flatten(spdataset.files(), "|"));
    if (spdataset.transcripts().size() > 0)
      row.addCell("ts").setContent(spdataset.transcripts().toString());

    return result;
  }
}
