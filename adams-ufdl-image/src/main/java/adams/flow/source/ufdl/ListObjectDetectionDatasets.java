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
 * ListObjectDetectionDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.data.conversion.UFDLImageClassificationDatasetToSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.flow.core.UFDLSoftDeleteObjectState;
import adams.flow.core.UFDLSoftDeleteObjectStateHandler;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

import java.util.List;

/**
 * Outputs a spreadsheet with all the object detection datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ListObjectDetectionDatasets
  extends AbstractUFDLSourceAction
  implements UFDLSoftDeleteObjectStateHandler {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the state of the datasets to list. */
  protected UFDLSoftDeleteObjectState m_State;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Outputs a spreadsheet with all the object detection datasets.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "state", "state",
      UFDLSoftDeleteObjectState.ACTIVE);
  }

  /**
   * Sets the state of the objects to retrieve.
   *
   * @param value	the state
   */
  @Override
  public void setState(UFDLSoftDeleteObjectState value) {
    m_State = value;
    reset();
  }

  /**
   * Returns the state of the objects to retriev.
   *
   * @return		the state
   */
  @Override
  public UFDLSoftDeleteObjectState getState() {
    return m_State;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  @Override
  public String stateTipText() {
    return "The state of the datasets to retrieve.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "state", m_State, "state: ");
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
    List<Dataset> 		datasets;
    UFDLImageClassificationDatasetToSpreadSheet conv;
    String			msg;

    result = null;

    try {
      datasets = m_Client.datasets().list();
      conv  = new UFDLImageClassificationDatasetToSpreadSheet();
      for (Dataset dataset : datasets) {
        if (!m_State.accept(dataset))
          continue;
        conv.setInput(dataset);
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
          errors.add("Failed to convert dataset: " + dataset + "\n" + msg);
	}
      }
      conv.cleanUp();
    }
    catch (Exception e) {
      errors.add("Failed to list datasets!", e);
    }

    return result;
  }
}