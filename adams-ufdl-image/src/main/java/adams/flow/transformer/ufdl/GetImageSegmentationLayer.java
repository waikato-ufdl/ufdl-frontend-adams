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
 * GetImageSegmentationLayer.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.io.FileWriter;
import adams.core.io.PlaceholderFile;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Obtains the specified mask layer from the dataset passing through and
 * forwards the downloaded filename. Suppress forwarding the name if a 0-length
 * image was downloaded.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class GetImageSegmentationLayer
  extends AbstractImageSegmentationDatasetTransformerAction
  implements FileWriter {

  private static final long serialVersionUID = -1421130988687306299L;

  /** the name of the image to get the mask for. */
  protected String m_Name;

  /** the layer to retrieve. */
  protected String m_Label;

  /** the output file to use. */
  protected PlaceholderFile m_OutputFile;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Obtains the specified mask layer from the dataset passing through "
      + "and forwards the downloaded filename. Suppress forwarding the name "
      + "if a 0-length image was downloaded.";
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
      "output-file", "outputFile",
      new PlaceholderFile());
  }

  /**
   * Sets the name of the image to get the layer for.
   *
   * @param value 	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name of the image to get the layer for.
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
    return "The name of the image to get the layer for.";
  }

  /**
   * Sets the name of the label to retrieve.
   *
   * @param value 	the label
   */
  public void setLabel(String value) {
    m_Label = value;
    reset();
  }

  /**
   * Returns the name of the label to retrieve.
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
    return "The name of layer mask to retrieve.";
  }

  /**
   * Sets the output file.
   *
   * @param value 	the file
   */
  public void setOutputFile(PlaceholderFile value) {
    m_OutputFile = value;
    reset();
  }

  /**
   * Returns the output file.
   *
   * @return 		the file
   */
  public PlaceholderFile getOutputFile() {
    return m_OutputFile;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String outputFileTipText() {
    return "The output file; the extension determines the file format.";
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
    result += QuickInfoHelper.toString(this, "outputFile", m_OutputFile, ", output: ");

    return result;
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{String.class};
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
    String	result;

    result = null;
    try {
      if (!getDatasetsAction().getLayer(dataset, m_Name, m_Label, m_OutputFile.getAbsoluteFile()))
        getLogger().warning("Failed to obtain layer '" + m_Label + "' for image '" + m_Name + "'!");
      else
	result = m_OutputFile.getAbsolutePath();
    }
    catch (Exception e) {
      errors.add("Failed to retrieve layer '" + m_Label + "' for image '" + m_Name + "' from dataset: " + dataset, e);
    }

    return result;
  }
}
