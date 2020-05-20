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
 * AddImageClassificationFile.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.io.PlaceholderFile;
import adams.flow.core.ufdl.ImageNameExtraction;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets;

/**
 * Adds the files to the dataset passing through (PK or dataset name).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddImageClassificationFile
  extends AbstractDatasetTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the file(s) to add. */
  protected PlaceholderFile[] m_Files;

  /** how to extract the name of the file. */
  protected ImageNameExtraction m_ImageNameExtraction;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the files to the dataset passing through (PK or dataset name).";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "file", "files",
      new PlaceholderFile[0]);

    m_OptionManager.add(
      "image-name-extraction", "imageNameExtraction",
      ImageNameExtraction.NAME);
  }

  /**
   * Sets the files to add.
   *
   * @param value	the files
   */
  public void setFiles(PlaceholderFile[] value) {
    m_Files = value;
    reset();
  }

  /**
   * Returns the files to add.
   *
   * @return		the files
   */
  public PlaceholderFile[] getFiles() {
    return m_Files;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String filesTipText() {
    return "The files to add to the dataset.";
  }

  /**
   * Sets how to extract the image name from the file name.
   *
   * @param value	the extraction type
   */
  public void setImageNameExtraction(ImageNameExtraction value) {
    m_ImageNameExtraction = value;
    reset();
  }

  /**
   * Returns how to extract the image name from the file name.
   *
   * @return		the extraction type
   */
  public ImageNameExtraction getImageNameExtraction() {
    return m_ImageNameExtraction;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String imageNameExtractionTipText() {
    return "Determines how to generate the name of the image from its filename.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "files", m_Files);
    result += QuickInfoHelper.toString(this, "imageNameExtraction", m_ImageNameExtraction, ", extract: ");

    return result;
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Integer.class, String.class, Dataset.class};
  }

  /**
   * Transforms the dataset.
   *
   * @param dataset	the dataset
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    ImageClassificationDatasets	action;
    String			name;

    for (PlaceholderFile file: m_Files) {
      name = m_ImageNameExtraction.extract(file);
      if (isLoggingEnabled())
	getLogger().info("Adding image '" + name + "' to dataset " + dataset + ": " + file);
      try {
	action = m_Client.action(ImageClassificationDatasets.class);
	if (!action.addFile(dataset, file.getAbsoluteFile(), name))
	  errors.add("Failed to add image '" + name + "' to " + dataset + ": " + file);
      }
      catch (Exception e) {
	errors.add("Failed to add image '" + name + "' to " + dataset + ": " + file, e);
      }
    }

    return dataset;
  }
}
