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
import adams.core.base.JsonPathExpression;
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

  /** the paths for metadata to display. */
  protected JsonPathExpression[] m_MetaDataKeys;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Lists the images in the incoming image classification dataset.\n"
      + "Additional meta-data values can be displayed using the meta-data keys.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "meta-data-key", "metaDataKeys",
      new JsonPathExpression[0]);
  }

  /**
   * Sets the keys of the meta-data values to display as separate columns.
   *
   * @param value 	the keys
   */
  public void setMetaDataKeys(JsonPathExpression[] value) {
    m_MetaDataKeys = value;
    reset();
  }

  /**
   * Returns the keys of the meta-data values to display as separate columns.
   *
   * @return 		the keys
   */
  public JsonPathExpression[] getMetaDataKeys() {
    return m_MetaDataKeys;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String metaDataKeysTipText() {
    return "The keys of the meta-data values to display as separate columns (only simple keys are allowed).";
  }

  /**
   * Returns whether the action requires flow context.
   *
   * @return		true if required
   */
  @Override
  public boolean requiresFlowContext() {
    return true;
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
    SpreadSheet						result;
    UFDLImageClassificationDatasetFilesToSpreadSheet	conv;
    String						msg;

    result = null;

    try {
      conv = new UFDLImageClassificationDatasetFilesToSpreadSheet();
      conv.setFlowContext(getFlowContext());
      conv.setMetaDataKeys(m_MetaDataKeys);
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
