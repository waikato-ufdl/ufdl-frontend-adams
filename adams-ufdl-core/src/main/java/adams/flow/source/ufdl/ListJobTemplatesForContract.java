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
 * ListJobTemplatesForContract.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.AdditionalInformationHandler;
import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.data.conversion.UFDLJobTemplateToSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.flow.core.UFDLContractType;
import adams.flow.core.UFDLSoftDeleteObjectState;
import adams.flow.core.UFDLSoftDeleteObjectStateHandler;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

import java.util.List;

/**
 * Outputs a spreadsheet with all the job templates.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ListJobTemplatesForContract
  extends AbstractUFDLSourceAction
  implements UFDLSoftDeleteObjectStateHandler, AdditionalInformationHandler {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the contract to apply. */
  protected UFDLContractType m_ContractType;

  /** the state of the templates to list. */
  protected UFDLSoftDeleteObjectState m_State;

  /** whether to resolve IDs to names. */
  protected boolean m_ResolveIDs;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Outputs a spreadsheet with the job templates that match the contract type.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "contract-type", "contractType",
      UFDLContractType.TRAIN);

    m_OptionManager.add(
      "state", "state",
      UFDLSoftDeleteObjectState.ACTIVE);

    m_OptionManager.add(
      "resolve-ids", "resolveIDs",
      false);
  }

  /**
   * Sets the contract type to look for.
   *
   * @param value	the type
   */
  public void setContractType(UFDLContractType value) {
    m_ContractType = value;
    reset();
  }

  /**
   * Returns the contract type to look for.
   *
   * @return		the type
   */
  public UFDLContractType getContractType() {
    return m_ContractType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String contractTypeTipText() {
    return "The contract type to look for.";
  }

  /**
   * Sets the state of the objects to retrieve.
   *
   * @param value	the state
   */
  @Override
  public void setState(UFDLSoftDeleteObjectState value) {
    m_State = value;
    reset();
  }

  /**
   * Returns the state of the objects to retrieve.
   *
   * @return		the state
   */
  @Override
  public UFDLSoftDeleteObjectState getState() {
    return m_State;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String stateTipText() {
    return "The state of the projects to retrieve.";
  }

  /**
   * Sets whether to resolve any IDs.
   *
   * @param value	true if to resolve
   */
  public void setResolveIDs(boolean value) {
    m_ResolveIDs = value;
    reset();
  }

  /**
   * Returns whether to resolve any IDs.
   *
   * @return 		true if to resolve
   */
  public boolean getResolveIDs() {
    return m_ResolveIDs;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String resolveIDsTipText() {
    return "If enabled, any IDs get resolved (e.g., outputting license name instead of license ID).";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "filter", m_ContractType, "filter: ");
    result += QuickInfoHelper.toString(this, "state", m_State, ", state: ");
    result += QuickInfoHelper.toString(this, "resolveIDs", m_ResolveIDs, "resolve IDs", ", ");

    return result;
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
    return new UFDLJobTemplateToSpreadSheet().getAdditionalInformation();
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    SpreadSheet				result;
    SpreadSheet				sheet;
    List<JobTemplate> 			templates;
    UFDLJobTemplateToSpreadSheet 	conv;
    String				msg;

    result = null;

    try {
      templates = m_Client.jobTemplates().getAllMatchingTemplates(m_ContractType.getName());
      conv      = new UFDLJobTemplateToSpreadSheet();
      conv.setFlowContext(m_FlowContext);
      conv.setResolveIDs(m_ResolveIDs);
      for (JobTemplate template : templates) {
	if (!m_State.accept(template))
	  continue;
	conv.setInput(template);
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
	  errors.add("Failed to convert job template: " + template + "\n" + msg);
	}
      }
      conv.cleanUp();
    }
    catch (Exception e) {
      errors.add("Failed to list job templates!", e);
    }

    return result;
  }
}
