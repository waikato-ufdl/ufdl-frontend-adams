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
 * AbstractUFDLObjectToSpreadSheetConversion.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.AdditionalInformationHandler;
import adams.core.Utils;
import adams.data.spreadsheet.SpreadSheet;
import adams.flow.core.Actor;
import adams.flow.core.ActorUtils;
import adams.flow.core.FlowContextHandler;
import adams.flow.standalone.UFDLConnection;

/**
 * Ancestor for conversions that convert UFDL objects into spreadsheets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLObjectToSpreadSheetConversion
  extends AbstractConversion
  implements AdditionalInformationHandler, FlowContextHandler {

  private static final long serialVersionUID = -4489686955674646994L;

  /** the flow context. */
  protected Actor m_FlowContext;

  /** whether to resolve IDs to names. */
  protected boolean m_ResolveIDs;

  /** the connection. */
  protected transient UFDLConnection m_Connection;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    if (allowIDResolution()) {
      m_OptionManager.add(
	"resolve-ids", "resolveIDs",
	false);
    }
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();
  }

  /**
   * Resets the members.
   */
  @Override
  protected void reset() {
    super.reset();

    m_Connection = null;
  }

  /**
   * Returns whether ID resolution is available.
   *
   * @return		true if available
   */
  protected abstract boolean allowIDResolution();

  /**
   * Sets the flow context.
   *
   * @param value	the actor
   */
  public void setFlowContext(Actor value) {
    m_FlowContext = value;
  }

  /**
   * Returns the flow context, if any.
   *
   * @return		the actor, null if none available
   */
  public Actor getFlowContext() {
    return m_FlowContext;
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
   * Returns whether an UFDL connection is required.
   *
   * @return		true if connection required
   */
  protected boolean requiresConnection() {
    return false;
  }

  /**
   * Returns the class that is generated as output.
   *
   * @return		the class
   */
  @Override
  public Class generates() {
    return SpreadSheet.class;
  }

  /**
   * Generates the template.
   *
   * @return		the template
   */
  public abstract SpreadSheet getTemplate();

  /**
   * Returns the additional information.
   *
   * @return		the additional information, null or 0-length string for no information
   */
  @Override
  public String getAdditionalInformation() {
    StringBuilder	result;
    SpreadSheet		template;
    int			i;

    result   = new StringBuilder();
    template = getTemplate();
    result.append("Spreadsheet columns:\n");
    for (i = 0; i < template.getColumnCount(); i++)
      result.append((i+1)).append(". ").append(template.getColumnName(i)).append("\n");

    return result.toString();
  }

  /**
   * Checks whether the data can be processed.
   *
   * @return		null if checks passed, otherwise error message
   */
  @Override
  protected String checkData() {
    String	result;

    result = super.checkData();

    if (result == null) {
      if (m_ResolveIDs || requiresConnection()) {
	m_Connection = (UFDLConnection) ActorUtils.findClosestType(m_FlowContext, UFDLConnection.class, true);
	if (m_Connection == null)
	  result = "Failed to locate an instance of " + Utils.classToString(UFDLConnection.class) + "!";
      }
    }

    return result;
  }

  /**
   * Returns the user ID or name, depending on whether IDs get resolved.
   *
   * @param id		the user ID
   * @return		the generated string
   */
  protected String getUser(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().users().resolve(id);

    return result;
  }

  /**
   * Returns the license ID or name, depending on whether IDs get resolved.
   *
   * @param id		the license ID
   * @return		the generated string
   */
  protected String getLicense(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().licenses().resolve(id);

    return result;
  }

  /**
   * Returns the team ID or name, depending on whether IDs get resolved.
   *
   * @param id		the team ID
   * @return		the generated string
   */
  protected String getTeam(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().teams().resolve(id);

    return result;
  }

  /**
   * Returns the project ID or name, depending on whether IDs get resolved.
   *
   * @param id		the project ID
   * @return		the generated string
   */
  protected String getProject(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().projects().resolve(id);

    return result;
  }

  /**
   * Returns the framework ID or name/version, depending on whether IDs get resolved.
   *
   * @param id		the framework ID
   * @return		the generated string
   */
  protected String getFramework(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().frameworks().resolve(id);

    return result;
  }

  /**
   * Returns the cuda version ID or version, depending on whether IDs get resolved.
   *
   * @param id		the cuda version ID
   * @return		the generated string
   */
  protected String getCudaVersion(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().cuda().resolve(id);

    return result;
  }

  /**
   * Returns the hardware generation ID or version, depending on whether IDs get resolved.
   *
   * @param id		the hardware generation ID
   * @return		the generated string
   */
  protected String getHardwareGeneration(int id) {
    String		result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().hardware().resolve(id);

    return result;
  }

  /**
   * Returns the docker image ID or name/version, depending on whether IDs get resolved.
   *
   * @param id		the docker image ID
   * @return		the generated string
   */
  protected String getDockerImage(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().docker().resolve(id);

    return result;
  }

  /**
   * Returns the job template ID or name/version, depending on whether IDs get resolved.
   *
   * @param id		the job template ID
   * @return		the generated string
   */
  protected String getJobTemplate(int id) {
    String	result;

    result = "" + id;

    if (m_ResolveIDs && (id > -1))
      result = m_Connection.getCacheManager().jobTemplates().resolve(id);

    return result;
  }
}
