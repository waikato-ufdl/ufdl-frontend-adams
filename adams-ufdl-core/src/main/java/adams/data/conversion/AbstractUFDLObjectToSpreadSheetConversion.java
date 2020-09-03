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

import java.util.HashMap;
import java.util.Map;

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

  /** the cuda version cache. */
  protected Map<Integer,String> m_CacheCudaVersion;

  /** the docker image cache. */
  protected Map<Integer,String> m_CacheDockerImage;

  /** the framework cache. */
  protected Map<Integer,String> m_CacheFramework;

  /** the hardware generation cache. */
  protected Map<Integer,String> m_CacheHardware;

  /** the job template cache. */
  protected Map<Integer,String> m_CacheJobTemplate;

  /** the license cache. */
  protected Map<Integer,String> m_CacheLicense;

  /** the project cache. */
  protected Map<Integer,String> m_CacheProject;

  /** the team cache. */
  protected Map<Integer,String> m_CacheTeam;

  /** the user cache. */
  protected Map<Integer,String> m_CacheUser;

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
    resetCaches();
  }

  /**
   * Resets the caches.
   */
  protected void resetCaches() {
    m_CacheCudaVersion = new HashMap<>();
    m_CacheDockerImage = new HashMap<>();
    m_CacheFramework   = new HashMap<>();
    m_CacheHardware    = new HashMap<>();
    m_CacheJobTemplate = new HashMap<>();
    m_CacheLicense     = new HashMap<>();
    m_CacheProject     = new HashMap<>();
    m_CacheTeam        = new HashMap<>();
    m_CacheUser        = new HashMap<>();
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheUser.containsKey(id)) {
        m_CacheUser.clear();
	try {
	  for (User user: m_Connection.getClient().users().list())
	    m_CacheUser.put(user.getPK(), user.getUserName());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list users for caching!");
	}
      }
      if (m_CacheUser.containsKey(id))
	result = m_CacheUser.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheLicense.containsKey(id)) {
	try {
	  m_CacheLicense.clear();
	  for (License license: m_Connection.getClient().licenses().list())
	    m_CacheLicense.put(license.getPK(), license.getName());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list licenses for caching!");
	}
      }
      if (m_CacheLicense.containsKey(id))
	result = m_CacheLicense.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheTeam.containsKey(id)) {
        m_CacheTeam.clear();
	try {
	  for (Team team: m_Connection.getClient().teams().list())
	    m_CacheTeam.put(team.getPK(), team.getName());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list teams for caching!");
	}
      }
      if (m_CacheTeam.containsKey(id))
	result = m_CacheTeam.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheProject.containsKey(id)) {
	try {
	  m_CacheProject.clear();
	  for (Project project: m_Connection.getClient().projects().list())
	    m_CacheProject.put(project.getPK(), project.getName());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list projects for caching!");
	}
      }
      if (m_CacheProject.containsKey(id))
	result = m_CacheProject.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheFramework.containsKey(id)) {
        m_CacheFramework.clear();
	try {
	  for (Framework framework: m_Connection.getClient().frameworks().list())
	    m_CacheFramework.put(framework.getPK(), framework.getName() + "/" + framework.getVersion());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list frameworks for caching!");
	}
      }
      if (m_CacheFramework.containsKey(id))
	result = m_CacheFramework.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheCudaVersion.containsKey(id)) {
        m_CacheCudaVersion.clear();
        try {
	  for (CudaVersion cuda : m_Connection.getClient().cuda().list())
	    m_CacheCudaVersion.put(cuda.getPK(), cuda.getVersion());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list cuda versions for caching!");
	}
      }
      if (m_CacheCudaVersion.containsKey(id))
	result = m_CacheCudaVersion.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheHardware.containsKey(id)) {
        m_CacheHardware.clear();
	try {
	  for (HardwareGeneration gen: m_Connection.getClient().hardware().list())
	    m_CacheHardware.put(gen.getPK(), gen.getGeneration());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list hardware generations for caching!");
	}
      }
      if (m_CacheHardware.containsKey(id))
	result = m_CacheHardware.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheDockerImage.containsKey(id)) {
        m_CacheDockerImage.clear();
	try {
	  for (DockerImage img: m_Connection.getClient().docker().list())
	    m_CacheDockerImage.put(img.getPK(), img.getName() + "/" + img.getVersion());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list docker images for caching!");
	}
      }
      if (m_CacheDockerImage.containsKey(id))
	result = m_CacheDockerImage.get(id);
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

    result = "" + id;

    if (m_ResolveIDs && (id > -1)) {
      if (!m_CacheJobTemplate.containsKey(id)) {
        m_CacheJobTemplate.clear();
	try {
	  for (JobTemplate template: m_Connection.getClient().jobTemplates().list())
	    m_CacheJobTemplate.put(template.getPK(), template.getName() + "/" + template.getVersion());
	}
	catch (Exception e) {
	  getLogger().severe("Failed to list job templates for caching!");
	}
      }
      if (m_CacheJobTemplate.containsKey(id))
	result = m_CacheJobTemplate.get(id);
    }

    return result;
  }
}
