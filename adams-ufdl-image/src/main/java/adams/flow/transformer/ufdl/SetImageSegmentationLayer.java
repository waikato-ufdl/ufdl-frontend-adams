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
 * SetImageSegmentationLayer.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.io.PlaceholderFile;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Sets the layer in the incoming dataset, using the specified mask file.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SetImageSegmentationLayer
  extends AbstractImageSegmentationDatasetTransformerAction {

  private static final long serialVersionUID = -1421130988687306299L;

  /** the name of the image to get the mask for. */
  protected String m_Name;

  /** the layer to retrieve. */
  protected String m_Label;

  /** the mask file to upload. */
  protected PlaceholderFile m_MaskFile;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Sets the layer in the incoming dataset, using the specified mask file.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "name", "name",
      "");

    m_OptionManager.add(
      "label", "label",
      "");

    m_OptionManager.add(
      "mask-file", "maskFile",
      new PlaceholderFile());
  }

  /**
   * Sets the name of the image to set the layer for.
   *
   * @param value 	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name of the image to set the layer for.
   *
   * @return 		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String nameTipText() {
    return "The name of the image to set the layer for.";
  }

  /**
   * Sets the name of the label to set.
   *
   * @param value 	the label
   */
  public void setLabel(String value) {
    m_Label = value;
    reset();
  }

  /**
   * Returns the name of the label to set.
   *
   * @return 		the label
   */
  public String getLabel() {
    return m_Label;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String labelTipText() {
    return "The name of layer mask to set.";
  }

  /**
   * Sets the mask file.
   *
   * @param value 	the file
   */
  public void setMaskFile(PlaceholderFile value) {
    m_MaskFile = value;
    reset();
  }

  /**
   * Returns the mask file.
   *
   * @return 		the file
   */
  public PlaceholderFile getMaskFile() {
    return m_MaskFile;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String maskFileTipText() {
    return "The mask file to upload.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", (m_Name.isEmpty() ? "-none-" : m_Name), "name: ");
    result += QuickInfoHelper.toString(this, "label", (m_Label.isEmpty() ? "-none-" : m_Label), ", label: ");
    result += QuickInfoHelper.toString(this, "maskFile", m_MaskFile, ", mask: ");

    return result;
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
    return new Class[]{Boolean.class};
  }

  /**
   * Transforms the input data.
   *
   * @param dataset	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    boolean		result;

    result = false;
    try {
      result = getDatasetsAction().setLayer(dataset.getPK(), m_Name, m_Label, m_MaskFile.getAbsoluteFile());
    }
    catch (Exception e) {
      errors.add("Failed to set layer '" + m_Label + "' for image '" + m_Name + "' in dataset: " + dataset, e);
    }

    return result;
  }
}
