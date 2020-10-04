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
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels.PretrainedModel;

/**
 * Uploads the model file to the pretrained model passing through (PK).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UploadPretrainedModelFile
  extends AbstractPretrainedModelTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the file to add. */
  protected PlaceholderFile m_File;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Uploads the model file to the pretrained model passing through (PK).";
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
  }

  /**
   * Sets the model file to upload.
   *
   * @param value	the file
   */
  public void setFile(PlaceholderFile value) {
    m_File = value;
    reset();
  }

  /**
   * Returns the model file to upload.
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
    return "The model file to upload.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "file", m_File);
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{PretrainedModel.class};
  }

  /**
   * Transforms the dataset.
   *
   * @param model	the dataset
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(PretrainedModel model, MessageCollection errors) {
    if (isLoggingEnabled())
      getLogger().info("Uploading model file to pretrained model " + model + ": " + m_File);
    try {
      model = m_Client.pretrainedModels().upload(model, m_File.getAbsoluteFile());
    }
    catch (Exception e) {
      errors.add("Failed to upload model file to pretrained model " + model + ": " + m_File, e);
    }

    return model;
  }
}
