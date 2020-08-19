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
 * ListJobOutputs.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.AdditionalInformationHandler;
import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.data.conversion.UFDLJobOutputToSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.action.Jobs.JobOutput;

/**
 * Outputs a spreadsheet with all the job outputs of the specified job.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ListJobOutputs
  extends AbstractUFDLSourceAction
  implements AdditionalInformationHandler {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the PK of the job to list the job outputs for. */
  protected int m_Job;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Outputs a spreadsheet with all the job outputs of the specified job.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "job", "job",
      -1, -1, null);
  }

  /**
   * Sets the PK of the job to get the outputs for.
   *
   * @param value	the PK
   */
  public void setJob(int value) {
    m_Job = value;
    reset();
  }

  /**
   * Returns the PK of the job to get the outputs for.
   *
   * @return		the PK
   */
  public int getJob() {
    return m_Job;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String jobTipText() {
    return "The PK of the job to list the outputs for.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "job", m_Job, "job: ");
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Returns the additional information.
   *
   * @return		the additional information, null or 0-length string for no information
   */
  public String getAdditionalInformation() {
    return new UFDLJobOutputToSpreadSheet().getAdditionalInformation();
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    SpreadSheet			result;
    SpreadSheet			sheet;
    Job				job;
    UFDLJobOutputToSpreadSheet 	conv;
    String			msg;

    result = null;

    try {
      job     = m_Client.jobs().load(m_Job);
      conv    = new UFDLJobOutputToSpreadSheet();
      conv.setFlowContext(m_FlowContext);
      for (JobOutput output : job.getOutputs()) {
        conv.setInput(output);
        msg = conv.convert();
        if (msg == null) {
          if (result == null) {
	    result = (SpreadSheet) conv.getOutput();
	  }
          else {
            sheet = (SpreadSheet) conv.getOutput();
            for (Row row: sheet.rows())
	      result.addRow().assign(row);
	  }
	}
	else {
          errors.add("Failed to convert job output: " + output + "\n" + msg);
	}
      }
      conv.cleanUp();
    }
    catch (Exception e) {
      errors.add("Failed to list job outputs!", e);
    }

    return result;
  }
}
