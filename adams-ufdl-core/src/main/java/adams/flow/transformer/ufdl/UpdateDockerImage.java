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
 * UpdateDockerImage.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseObject;
import adams.core.base.BasePassword;
import adams.core.base.BaseString;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

/**
 * Updates a docker image and forwards the docker image object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdateDockerImage
  extends AbstractDockerImageTransformerAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the docker image name. */
  protected String m_Name;

  /** the docker image version. */
  protected String m_Version;

  /** the docker image URL. */
  protected String m_URL;

  /** the registry URL. */
  protected String m_RegistryURL;

  /** the registry user. */
  protected String m_RegistryUser;

  /** the registry password. */
  protected BasePassword m_RegistryPassword;

  /** the cuda version. */
  protected String m_CudaVersion;

  /** the framework. */
  protected int m_Framework;

  /** the domain. */
  protected String m_Domain;

  /** the tasks. */
  protected BaseString[] m_Tasks;

  /** the name of the minimum hardware generation. */
  protected String m_MinHardwareGeneration;

  /** whether it works on a CPU as well. */
  protected boolean m_CPU;

  /** the name of the license for the image. */
  protected String m_License;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates a docker image and forwards the docker image object.";
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
      "version", "version",
      "");

    m_OptionManager.add(
      "url", "URL",
      "");

    m_OptionManager.add(
      "registry-url", "registryURL",
      "");

    m_OptionManager.add(
      "registry-user", "registryUser",
      "");

    m_OptionManager.add(
      "registry-password", "registryPassword",
      new BasePassword(), false);

    m_OptionManager.add(
      "cuda-version", "cudaVersion",
      "");

    m_OptionManager.add(
      "framework", "framework",
      -1, -1, null);

    m_OptionManager.add(
      "domain", "domain",
      "");

    m_OptionManager.add(
      "task", "tasks",
      new BaseString[0]);

    m_OptionManager.add(
      "min-hardware-generation", "minHardwareGeneration",
      "");

    m_OptionManager.add(
      "cpu", "CPU",
      false);

    m_OptionManager.add(
      "license", "license",
      "");
  }

  /**
   * Sets the docker image name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the docker image name.
   *
   * @return		the name
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
    return "The docker image name.";
  }

  /**
   * Sets the docker image version.
   *
   * @param value	the version
   */
  public void setVersion(String value) {
    m_Version = value;
    reset();
  }

  /**
   * Returns the docker image version.
   *
   * @return		the version
   */
  public String getVersion() {
    return m_Version;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String versionTipText() {
    return "The docker image version.";
  }

  /**
   * Sets the URL.
   *
   * @param value	the URL
   */
  public void setURL(String value) {
    m_URL = value;
    reset();
  }

  /**
   * Returns the URL.
   *
   * @return		the URL
   */
  public String getURL() {
    return m_URL;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String URLTipText() {
    return "The image URL.";
  }

  /**
   * Sets the registry URL.
   *
   * @param value	the URL
   */
  public void setRegistryURL(String value) {
    m_RegistryURL = value;
    reset();
  }

  /**
   * Returns the registry URL.
   *
   * @return		the URL
   */
  public String getRegistryURL() {
    return m_RegistryURL;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String registryURLTipText() {
    return "The registry URL.";
  }

  /**
   * Sets the registry User.
   *
   * @param value	the User
   */
  public void setRegistryUser(String value) {
    m_RegistryUser = value;
    reset();
  }

  /**
   * Returns the registry User.
   *
   * @return		the User
   */
  public String getRegistryUser() {
    return m_RegistryUser;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String registryUserTipText() {
    return "The registry user.";
  }

  /**
   * Sets the registry password.
   *
   * @param value	the password
   */
  public void setRegistryPassword(BasePassword value) {
    m_RegistryPassword = value;
    reset();
  }

  /**
   * Returns the registry password.
   *
   * @return		the password
   */
  public BasePassword getRegistryPassword() {
    return m_RegistryPassword;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String registryPasswordTipText() {
    return "The registry password.";
  }

  /**
   * Sets the cuda version.
   *
   * @param value	the cuda version
   */
  public void setCudaVersion(String value) {
    m_CudaVersion = value;
    reset();
  }

  /**
   * Returns the cuda version.
   *
   * @return		the cuda version
   */
  public String getCudaVersion() {
    return m_CudaVersion;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String cudaVersionTipText() {
    return "The CUDA version, eg 10.0.";
  }

  /**
   * Sets the PK of the framework.
   *
   * @param value	the framework
   */
  public void setFramework(int value) {
    m_Framework = value;
    reset();
  }

  /**
   * Returns the PK of the framework.
   *
   * @return		the framework
   */
  public int getFramework() {
    return m_Framework;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String frameworkTipText() {
    return "The framework PK.";
  }

  /**
   * Sets the domain.
   *
   * @param value	the domain
   */
  public void setDomain(String value) {
    m_Domain = value;
    reset();
  }

  /**
   * Returns the domain.
   *
   * @return		the domain
   */
  public String getDomain() {
    return m_Domain;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String domainTipText() {
    return "The domain this image is for.";
  }

  /**
   * Sets the tasksk.
   *
   * @param value	the tasks
   */
  public void setTasks(BaseString[] value) {
    m_Tasks = value;
    reset();
  }

  /**
   * Returns the tasksk.
   *
   * @return		the tasksk
   */
  public BaseString[] getTasks() {
    return m_Tasks;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String tasksTipText() {
    return "The tasks this image can be used for (e.g., train or predict).";
  }

  /**
   * Sets the name of the minimum hardware generation.
   *
   * @param value	the generation
   */
  public void setMinHardwareGeneration(String value) {
    m_MinHardwareGeneration = value;
    reset();
  }

  /**
   * Returns the name of the minimum hardware generation.
   *
   * @return		the generation
   */
  public String getMinHardwareGeneration() {
    return m_MinHardwareGeneration;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String minHardwareGenerationTipText() {
    return "The name of the minimum hardware generation.";
  }

  /**
   * Sets whether the image runs a CPU.
   *
   * @param value	true if runs on CPU
   */
  public void setCPU(boolean value) {
    m_CPU = value;
    reset();
  }

  /**
   * Returns whether the image runs a CPU.
   *
   * @return		true if runs on CPU
   */
  public boolean getCPU() {
    return m_CPU;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String CPUTipText() {
    return "Whether the image can be run with just a CPU.";
  }

  /**
   * Sets the name of the license.
   *
   * @param value	the license
   */
  public void setLicense(String value) {
    m_License = value;
    reset();
  }

  /**
   * Returns the name of the license.
   *
   * @return		the license
   */
  public String getLicense() {
    return m_License;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String licenseTipText() {
    return "The name of the license.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", m_Name, "name: ");
    result += QuickInfoHelper.toString(this, "version", m_Version, ", version: ");
    result += QuickInfoHelper.toString(this, "URL", m_URL, ", URL: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{License.class};
  }

  /**
   * Transforms the input data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doTransform(DockerImage image, MessageCollection errors) {
    DockerImage    result;

    result = null;
    try {
      result = m_Client.docker().update(
        image, m_Name, m_Version, m_URL,
        m_RegistryURL, m_RegistryUser, m_RegistryPassword.getValue(),
        m_CudaVersion, m_Framework, m_Domain, BaseObject.toStringArray(m_Tasks),
        m_MinHardwareGeneration, m_CPU, m_License);
    }
    catch (Exception e) {
      errors.add("Failed to update docker image!", e);
    }

    return result;
  }
}
