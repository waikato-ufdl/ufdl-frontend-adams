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
 * UFDLObjectDetectionDatasetToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.DateTimeMsec;
import adams.core.Utils;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets.ImageClassificationDataset;

import java.sql.Date;
import java.time.ZoneOffset;

/**
 * Converts an object detection dataset into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLObjectDetectionDatasetToSpreadSheet
  extends AbstractConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a object detection into a Spreadsheet.";
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
   * Returns the class that is generated as output.
   *
   * @return		the class
   */
  @Override
  public Class generates() {
    return SpreadSheet.class;
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    SpreadSheet			result;
    Row				row;
    Dataset			dataset;
    ImageClassificationDataset 	icdataset;

    dataset   = (Dataset) m_Input;
    icdataset = dataset.as(ImageClassificationDataset.class);
    result    = new DefaultSpreadSheet();

    // header
    row = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("tn").setContentAsString("name");
    row.addCell("ci").setContentAsString("creator_id");
    row.addCell("ct").setContentAsString("creation_time");
    row.addCell("dt").setContentAsString("deletion_time");
    row.addCell("li").setContentAsString("licence");
    row.addCell("pi").setContentAsString("project_id");
    row.addCell("ve").setContentAsString("version");
    row.addCell("ta").setContentAsString("tags");
    row.addCell("pu").setContentAsString("public");
    row.addCell("fs").setContentAsString("files");

    // data
    row = result.addRow();
    row.addCell("pk").setContent(dataset.getPK());
    row.addCell("tn").setContent(dataset.getName());
    row.addCell("ci").setContent(dataset.getCreatorID());
    if (dataset.getCreationTime() != null)
      row.addCell("ct").setContent(new DateTimeMsec(Date.from(dataset.getCreationTime().toInstant(ZoneOffset.UTC))));
    if (dataset.getDeletionTime() != null)
      row.addCell("dt").setContent(new DateTimeMsec(Date.from(dataset.getDeletionTime().toInstant(ZoneOffset.UTC))));
    row.addCell("li").setContent(dataset.getLicence());
    row.addCell("pi").setContent(dataset.getProjectID());
    row.addCell("ve").setContent(dataset.getVersion());
    row.addCell("ta").setContent(dataset.getTags());
    row.addCell("pu").setContent(dataset.isPublic());
    if (icdataset.files().size() > 0)
      row.addCell("fs").setContent(Utils.flatten(icdataset.files(), ","));

    return result;
  }
}
