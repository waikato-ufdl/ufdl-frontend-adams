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
 * UFDLSpeechDatasetFilesToSpreadSheet.java
 * Copyright (C) 2020-2021 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets.SpeechDataset;

/**
 * Converts the files (and transcripts) of a speech dataset into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLSpeechDatasetFilesToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts the files (and transcripts) of a speech dataset into a Spreadsheet.";
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
    row.addCell("f").setContentAsString("file");
    row.addCell("t").setContentAsString("transcript");

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
    SpeechDatasets	spdatasets;

    dataset    = (Dataset) m_Input;
    spdataset  = dataset.as(SpeechDataset.class);
    spdatasets = m_Connection.getClient().action(SpeechDatasets.class);
    result     = getTemplate();
      for (String file: spdataset.files()) {
        row = result.addRow();
        row.getCell("f").setContentAsString(file);
        row.getCell("t").setContentAsString(spdatasets.getTranscript(spdataset, file));
      }

    return result;
  }
}
