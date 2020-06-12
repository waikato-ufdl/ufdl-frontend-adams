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
 * SetImageClassificationMetadataForImage.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.Utils;
import adams.flow.control.StorageName;
import adams.flow.control.StorageUser;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets;

/**
 * Sets the metadata of the specified image in the dataset coming in.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SetImageClassificationMetadataForImage
  extends AbstractDatasetTransformerAction
  implements StorageUser {

  private static final long serialVersionUID = -1421130988687306299L;

  /** the name of the image to get the annotations for. */
  protected String m_Name;

  /** the storage item with the annotations. */
  protected StorageName m_StorageName;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Sets the metadata of the specified image in the dataset coming in.";
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
      "storage-name", "storageName",
      new StorageName("metadata"));
  }

  /**
   * Sets the name of the image to set the metadata for.
   *
   * @param value 	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name of the image to set the metadata for.
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
    return "The name of the image to set the metadata for.";
  }

  /**
   * Sets the name for the metadata in the internal storage.
   *
   * @param value	the name
   */
  public void setStorageName(StorageName value) {
    m_StorageName = value;
    reset();
  }

  /**
   * Returns the name for the metadata in the internal storage.
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
    return "The name for the metadata in the internal storage.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", m_Name);
    result += QuickInfoHelper.toString(this, "storageName", m_StorageName, ", storage: ");

    return result;
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
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Boolean.class};
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
      else if (!m_FlowContext.getStorageHandler().getStorage().has(m_StorageName))
        result = "Metadata are not available from storage: " + m_StorageName;
      else if (!(m_FlowContext.getStorageHandler().getStorage().get(m_StorageName) instanceof String))
        result = "Didn't find String object in storage ('" + m_StorageName + "'), but: "
	  + Utils.classToString(m_FlowContext.getStorageHandler().getStorage().get(m_StorageName));
    }

    return result;
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
    boolean			result;
    ImageClassificationDatasets action;
    String  			metadata;

    result = false;
    try {
      metadata = (String) m_FlowContext.getStorageHandler().getStorage().get(m_StorageName);
      action = m_Client.action(ImageClassificationDatasets.class);
      result = action.setMetadata(dataset, m_Name, metadata);
    }
    catch (Exception e) {
      errors.add("Failed to set metadata for image '" + m_Name + "' in dataset: " + dataset, e);
    }

    return result;
  }
}
