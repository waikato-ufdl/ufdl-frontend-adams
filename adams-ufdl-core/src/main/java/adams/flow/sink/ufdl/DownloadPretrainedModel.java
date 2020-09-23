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
 * DownloadPretrainedModel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.sink.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.io.FileWriter;
import adams.core.io.PlaceholderFile;

/**
 * Downloads the pretrained model either via PK.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DownloadPretrainedModel
  extends AbstractUFDLSinkAction
  implements FileWriter {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the output file to use. */
  protected PlaceholderFile m_OutputFile;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Downloads the pretrained model either via PK.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "output-file", "outputFile",
      new PlaceholderFile());
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
    return "The output file.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "outputFile", m_OutputFile);
  }

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class};
  }

  /**
   * Consumes the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   */
  @Override
  protected void doConsume(Object input, MessageCollection errors) {
    boolean	result;

    if (isLoggingEnabled())
      getLogger().info("Downloading pretrained model: " + input);

    // load dataset
    try {
      result = m_Client.pretrainedModels().download((Integer) input, m_OutputFile);
      if (!result)
	errors.add("Failed to download pretrained model: " + input);
    }
    catch (Exception e) {
      errors.add("Failed to download pretrained model: " + input, e);
    }
  }
}
