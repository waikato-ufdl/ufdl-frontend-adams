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
import adams.core.Utils;
import adams.core.io.PlaceholderFile;
import adams.flow.control.StorageName;
import adams.flow.control.StorageUser;
import adams.flow.core.ufdl.ImageNameExtraction;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotations;

/**
 * Adds the files to the dataset passing through (PK or dataset name).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddObjectDetectionFile
  extends AbstractObjectDetectionDatasetTransformerAction
  implements StorageUser {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the file to add. */
  protected PlaceholderFile m_File;

  /** how to extract the name of the file. */
  protected ImageNameExtraction m_ImageNameExtraction;

  /** the storage item with the annotations. */
  protected StorageName m_StorageName;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the file and its annotations to the dataset passing through (PK or dataset name).";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "file", "file",
      new PlaceholderFile());

    m_OptionManager.add(
      "image-name-extraction", "imageNameExtraction",
      ImageNameExtraction.NAME);

    m_OptionManager.add(
      "storage-name", "storageName",
      new StorageName("annotations"));
  }

  /**
   * Sets the file to add.
   *
   * @param value	the file
   */
  public void setFile(PlaceholderFile value) {
    m_File = value;
    reset();
  }

  /**
   * Returns the file to add.
   *
   * @return		the file
   */
  public PlaceholderFile getFile() {
    return m_File;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String fileTipText() {
    return "The file to add to the dataset.";
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
   * Sets the name for the annotations in the internal storage.
   *
   * @param value	the name
   */
  public void setStorageName(StorageName value) {
    m_StorageName = value;
    reset();
  }

  /**
   * Returns the name for the annotations in the internal storage.
   *
   * @return		the name
   */
  public StorageName getStorageName() {
    return m_StorageName;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String storageNameTipText() {
    return "The name for the annotations in the internal storage, ignored if not present.";
  }

  /**
   * Returns whether storage items are being used.
   *
   * @return		true if storage items are used
   */
  public boolean isUsingStorage() {
    return true;
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
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "file", m_File);
    result += QuickInfoHelper.toString(this, "imageNameExtraction", m_ImageNameExtraction, ", extract: ");
    result += QuickInfoHelper.toString(this, "storageName", m_StorageName, ", storage: ");

    return result;
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Dataset.class};
  }

  /**
   * Check method before processing the data.
   *
   * @param input	the data to check
   * @return		null if checks successful, otherwise error message
   */
  @Override
  protected String check(Object input) {
    String 	result;

    result = super.check(input);

    if (result == null) {
      if (m_FlowContext.getStorageHandler() == null)
	result = "No storage handler available!";
      else if (m_FlowContext.getStorageHandler().getStorage() == null)
	result = "No storage available!";
    }

    return result;
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
    ObjectDetectionDatasets 	action;
    String			name;
    Annotations			anns;

    try {
      action = getDatasetsAction();
    }
    catch (Exception e) {
      errors.add("Failed to instantiate " + Utils.classToString(ObjectDetectionDatasets.class) + " action!", e);
      return null;
    }

    name = m_ImageNameExtraction.extract(m_File);
    if (isLoggingEnabled())
      getLogger().info("Adding image '" + name + "' to dataset " + dataset + ": " + m_File);
    try {
      if (!action.addFile(dataset, m_File.getAbsoluteFile(), name))
	errors.add("Failed to add image '" + name + "' to " + dataset + ": " + m_File);
    }
    catch (Exception e) {
      errors.add("Failed to add image '" + name + "' to " + dataset + ": " + m_File, e);
    }

    if (errors.isEmpty()) {
      anns = null;
      if (m_FlowContext.getStorageHandler().getStorage().has(m_StorageName))
	anns = (Annotations) m_FlowContext.getStorageHandler().getStorage().get(m_StorageName);
      if (anns != null) {
	if (isLoggingEnabled())
	  getLogger().info("Adding annotations for '" + name + "' in dataset " + dataset);
	try {
	  if (!action.setAnnotations(dataset, name, anns))
	    errors.add("Failed to set annotations for '" + name + "' in " + dataset);
	}
	catch (Exception e) {
	  errors.add("Failed to set annotations for '" + name + "' in " + dataset, e);
	}
      }
    }

    return dataset;
  }
}
