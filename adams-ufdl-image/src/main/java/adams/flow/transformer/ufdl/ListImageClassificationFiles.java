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
 * ListImageClassificationFiles.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.AdditionalInformationHandler;
import adams.core.MessageCollection;
import adams.data.conversion.UFDLImageClassificationDatasetFilesToSpreadSheet;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Lists the images in the incoming image classification dataset.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ListImageClassificationFiles
  extends AbstractImageClassificationDatasetTransformerAction
  implements AdditionalInformationHandler {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Lists the images in the incoming image classification dataset.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Returns the additional information.
   *
   * @return		the additional information, null or 0-length string for no information
   */
  public String getAdditionalInformation() {
    return new UFDLImageClassificationDatasetFilesToSpreadSheet().getAdditionalInformation();
  }

  /**
   * Transforms the dataset.
   *
   * @param dataset	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    SpreadSheet			result;
    UFDLImageClassificationDatasetFilesToSpreadSheet	 conv;
    String			msg;

    result = null;

    try {
      conv = new UFDLImageClassificationDatasetFilesToSpreadSheet();
      conv.setInput(dataset);
      msg = conv.convert();
      if (msg != null)
	errors.add("Failed to list image classification files for dataset: " + dataset + "\n" + msg);
      else
        result = (SpreadSheet) conv.getOutput();
    }
    catch (Exception e) {
      errors.add("Failed to list image classification files for dataset: " + dataset, e);
    }

    return result;
  }
}
