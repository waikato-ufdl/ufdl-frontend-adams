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
import com.github.waikatoufdl.ufdl4j.action.CudaVersions.CudaVersion;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations.HardwareGeneration;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

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
   * Resets the members.
   */
  @Override
  protected void reset() {
    super.reset();

    m_Connection = null;
  }

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
      if (m_ResolveIDs) {
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
    User 	user;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        user = m_Connection.getClient().users().load(id);
        if (!user.getUserName().isEmpty())
          result = user.getUserName();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve user ID: " + id);
      }
    }

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
    License 	license;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        license = m_Connection.getClient().licenses().load(id);
        if (!license.getName().isEmpty())
          result = license.getName();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve license ID: " + id);
      }
    }

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
    Team 	team;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        team = m_Connection.getClient().teams().load(id);
        if (!team.getName().isEmpty())
          result = team.getName();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve team ID: " + id);
      }
    }

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
    Project 	project;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        project = m_Connection.getClient().projects().load(id);
        if (!project.getName().isEmpty())
          result = project.getName();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve project ID: " + id);
      }
    }

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
    Framework 	framework;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        framework = m_Connection.getClient().frameworks().load(id);
	result = framework.getName() + "/" + framework.getVersion();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve framework ID: " + id);
      }
    }

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
    CudaVersion cuda;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        cuda = m_Connection.getClient().cuda().load(id);
	result = cuda.getVersion();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve cuda version ID: " + id);
      }
    }

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
    HardwareGeneration 	gen;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        gen = m_Connection.getClient().hardware().load(id);
	result = gen.getGeneration();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve hardware generation ID: " + id);
      }
    }

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
    DockerImage img;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        img = m_Connection.getClient().docker().load(id);
	result = img.getName() + "/" + img.getVersion();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve docker image ID: " + id);
      }
    }

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
    JobTemplate template;

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      try {
        template = m_Connection.getClient().jobTemplates().load(id);
	result = template.getName() + "/" + template.getVersion();
      }
      catch (Exception e) {
        getLogger().severe("Failed to resolve job template ID: " + id);
      }
    }

    return result;
  }
}
