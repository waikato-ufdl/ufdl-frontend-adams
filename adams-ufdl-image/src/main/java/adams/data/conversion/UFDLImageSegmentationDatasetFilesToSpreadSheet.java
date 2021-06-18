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
 * UFDLImageSegmentationDatasetFilesToSpreadSheet.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.Utils;
import adams.core.base.JsonPathExpression;
import adams.data.json.JsonHelper;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageSegmentationDatasets;
import com.github.waikatoufdl.ufdl4j.action.ImageSegmentationDatasets.ImageSegmentationDataset;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Converts the files of an image segmentation dataset into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLImageSegmentationDatasetFilesToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /** the paths for metadata to display. */
  protected JsonPathExpression[] m_MetaDataKeys;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts the files of an image segmentation dataset into a Spreadsheet.";
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
   * Returns whether ID resolution is available.
   *
   * @return		true if available
   */
  @Override
  protected boolean allowIDResolution() {
    return false;
  }

  /**
   * Returns whether an UFDL connection is required.
   *
   * @return		true if connection required
   */
  @Override
  protected boolean requiresConnection() {
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
    SpreadSheet		result;
    Row			row;
    int			i;
    String		key;

    result = new DefaultSpreadSheet();

    row = result.getHeaderRow();
    row.addCell("f").setContentAsString("image");
    row.addCell("l").setContentAsString("labels");
    for (i = 0; i < m_MetaDataKeys.length; i++) {
      key = m_MetaDataKeys[i].getValue();
      if (key.startsWith("$."))
        key = key.substring(2);
      row.addCell("md" + i).setContentAsString(key);
    }

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
    SpreadSheet			result;
    Row				row;
    Dataset			dataset;
    ImageSegmentationDataset 	isdataset;
    int				i;
    Map<String,String> 		metadata;
    String			metadataStr;
    List<String> 		labels;
    JSONObject 			json;
    JsonPath[]			paths;

    dataset   = (Dataset) m_Input;
    isdataset = dataset.as(ImageSegmentationDataset.class);

    metadata  = null;
    paths     = new JsonPath[m_MetaDataKeys.length];
    if (m_MetaDataKeys.length > 0) {
      for (i = 0; i < m_MetaDataKeys.length; i++)
	paths[i] = m_MetaDataKeys[i].toJsonPath();
      try {
	metadata = m_Connection.getClient().action(ImageSegmentationDatasets.class).getMetadata(isdataset);
      }
      catch (Exception e) {
	throw new IllegalStateException("Failed to retrieve meta-data for dataset " + dataset.getPK(), e);
      }
    }

    labels = null;
    try {
      labels = m_Connection.getClient().action(ImageSegmentationDatasets.class).getLabels(isdataset);
    }
    catch (Exception e) {
      throw new IllegalStateException("Failed to retrieve labels for dataset " + dataset.getPK(), e);
    }

    result = getTemplate();
    for (String file: isdataset.getFiles()) {
      row = result.addRow();
      row.getCell("f").setContentAsString(file);
      if (labels != null)
	row.getCell("l").setContentAsString(Utils.flatten(labels, ","));
      if (metadata != null) {
	for (i = 0; i < m_MetaDataKeys.length; i++) {
	  metadataStr = metadata.get(file);
	  if ((metadataStr != null) && !metadataStr.isEmpty() && (paths[i] != null)) {
	    try {
	      json = (JSONObject) JsonHelper.parse(metadataStr, this);
	      row.getCell("md" + i).setNative(paths[i].read(json));
	    }
	    catch (Exception e) {
	      throw new IllegalStateException("Failed to parse meta-data '" + metadataStr + "' as JSON object!", e);
	    }
	  }
	}
      }
    }

    return result;
  }
}
