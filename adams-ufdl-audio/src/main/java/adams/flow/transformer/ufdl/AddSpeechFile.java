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
 * AddSpeechFile.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.Utils;
import adams.core.base.BaseText;
import adams.core.io.PlaceholderFile;
import adams.flow.core.UFDLFileNameExtraction;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets;

/**
 * Adds the file to the dataset passing through (PK or dataset name).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddSpeechFile
  extends AbstractSpeechDatasetTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the file to add. */
  protected PlaceholderFile m_File;

  /** how to extract the name of the file. */
  protected UFDLFileNameExtraction m_FileNameExtraction;

  /** the transcr to assign to each file. */
  protected BaseText m_Transcript;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Adds the file to the dataset passing through (PK or dataset name).";
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
      "file-name-extraction", "fileNameExtraction",
      UFDLFileNameExtraction.NAME);

    m_OptionManager.add(
      "transcript", "transcript",
      new BaseText());
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
   * Sets how to extract the file name from the file name.
   *
   * @param value	the extraction type
   */
  public void setFileNameExtraction(UFDLFileNameExtraction value) {
    m_FileNameExtraction = value;
    reset();
  }

  /**
   * Returns how to extract the file name from the file name.
   *
   * @return		the extraction type
   */
  public UFDLFileNameExtraction getFileNameExtraction() {
    return m_FileNameExtraction;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String fileNameExtractionTipText() {
    return "Determines how to generate the name of the file from its filename.";
  }

  /**
   * Sets the transcript for the file.
   *
   * @param value	the transcript
   */
  public void setTranscript(BaseText value) {
    m_Transcript = value;
    reset();
  }

  /**
   * Returns the transcript to set for the file.
   *
   * @return		the transcript
   */
  public BaseText getTranscript() {
    return m_Transcript;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String transcriptTipText() {
    return "The transcript to set for the file.";
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
    result += QuickInfoHelper.toString(this, "fileNameExtraction", m_FileNameExtraction, ", extract: ");
    result += QuickInfoHelper.toString(this, "transcript", m_Transcript, ", transcript: ");

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
   * Transforms the dataset.
   *
   * @param dataset	the dataset
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    SpeechDatasets	action;
    String		name;

    try {
      action = getDatasetsAction();
    }
    catch (Exception e) {
      errors.add("Failed to instantiate " + Utils.classToString(SpeechDatasets.class) + " action!", e);
      return null;
    }

    name = m_FileNameExtraction.extract(m_File);
    if (isLoggingEnabled())
      getLogger().info("Adding file '" + name + "' to dataset " + dataset + ": " + m_File);
    try {
      if (!action.addFile(dataset, m_File.getAbsoluteFile(), name))
        errors.add("Failed to add file '" + name + "' to " + dataset + ": " + m_File);
    }
    catch (Exception e) {
      errors.add("Failed to add file '" + name + "' to " + dataset + ": " + m_File, e);
    }

    if (errors.isEmpty()) {
      if (isLoggingEnabled())
        getLogger().info("Set transcript for '" + name + "' to dataset " + dataset + ": " + m_Transcript);
      try {
        if (!action.setTranscript(dataset, name, m_Transcript.getValue()))
	  errors.add("Failed to set transcript for '" + name + "' to " + dataset + ": " + m_Transcript);
      }
      catch (Exception e) {
	errors.add("Failed to set transcript for '" + name + "' to " + dataset + ": " + m_Transcript, e);
      }
    }

    return dataset;
  }
}
