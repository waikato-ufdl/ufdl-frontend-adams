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
 * DownloadSpeechDataset.java
 * Copyright (C) 2020-2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.sink.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.io.FileWriter;
import adams.core.io.PlaceholderFile;
import adams.core.logging.LoggingHelper;
import adams.core.option.OptionUtils;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.SpeechDatasets;

/**
 * Downloads the speech dataset either via PK or datasetname.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DownloadSpeechDataset
  extends AbstractUFDLSinkAction
  implements FileWriter {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the parameters for the dataset conversion. */
  protected String m_Parameters;

  /** the output file to use. */
  protected PlaceholderFile m_OutputFile;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Downloads the speech dataset either via PK or dataset name.\n"
      + "Output file must have zip or tar.gz extension.\n"
      + "The data format within the archive can be influenced by setting the "
      + "'parameters' option which represents wai.annotations output parameters "
      + "(https://github.com/waikato-ufdl/wai-annotations), e.g.:\n"
      + "'to-common-voice-sp -o train.csv'";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "parameters", "parameters",
      "");

    m_OptionManager.add(
      "output-file", "outputFile",
      new PlaceholderFile());
  }

  /**
   * Sets the wai.annotations output format parameters.
   *
   * @param value 	the file
   */
  public void setParameters(String value) {
    m_Parameters = value;
    reset();
  }

  /**
   * Returns the wai.annotations output format parameters.
   *
   * @return 		the parameters
   */
  public String getParameters() {
    return m_Parameters;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String parametersTipText() {
    return "The wai.annotations output format parameters, see https://github.com/waikato-ufdl/wai-annotations";
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
    return "The output file; the extension determines the file format (zip or tar.gz).";
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
    return new Class[]{Integer.class, String.class, Dataset.class};
  }

  /**
   * Consumes the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   */
  @Override
  protected void doConsume(Object input, MessageCollection errors) {
    boolean		result;
    String[]		params;
    SpeechDatasets 	action;

    result = true;

    if (isLoggingEnabled())
      getLogger().info("Downloading dataset: " + input);

    params = new String[0];
    if (!m_Parameters.trim().isEmpty()) {
      try {
	params = OptionUtils.splitOptions(m_Parameters);
      }
      catch (Exception e) {
        LoggingHelper.handleException(this, "Failed to split options into array: " + m_Parameters, e);
        result = false;
      }
    }

    // load dataset
    if (result) {
      try {
        action = m_Client.action(SpeechDatasets.class);
	if (input instanceof Integer)
	  result = action.download((Integer) input, params, m_OutputFile.getAbsoluteFile());
	else if (input instanceof String)
	  result = action.download(m_Client.datasets().load("" + input), params, m_OutputFile.getAbsoluteFile());
	else
	  result = action.download(((Dataset) input).getPK(), params, m_OutputFile.getAbsoluteFile());
	if (!result)
	  errors.add("Failed to download speech dataset: " + input);
      }
      catch (Exception e) {
	errors.add("Failed to download speech dataset: " + input, e);
      }
    }
  }
}
