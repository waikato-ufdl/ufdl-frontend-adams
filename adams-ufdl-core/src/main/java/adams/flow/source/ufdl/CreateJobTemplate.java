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
 * CreateJobTemplate.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseText;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

/**
 * Creates a job template and forwards the job template object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateJobTemplate
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the pretrained model name. */
  protected String m_Name;

  /** the pretrained model version. */
  protected int m_Version;

  /** the scope. */
  protected String m_Scope;

  /** the framework. */
  protected int m_Framework;

  /** the domain. */
  protected String m_Domain;

  /** the type. */
  protected String m_Type;

  /** the executor class. */
  protected String m_ExecutorClass;

  /** the required packages. */
  protected String m_RequiredPackages;

  /** the template. */
  protected BaseText m_Template;

  /** the license for the image. */
  protected int m_License;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a job template and forwards the job template object.";
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
      1);

    m_OptionManager.add(
      "scope", "scope",
      "");

    m_OptionManager.add(
      "framework", "framework",
      -1, -1, null);

    m_OptionManager.add(
      "domain", "domain",
      "");

    m_OptionManager.add(
      "type", "type",
      "");

    m_OptionManager.add(
      "executor-class", "executorClass",
      "");

    m_OptionManager.add(
      "required-packages", "requiredPackages",
      "");

    m_OptionManager.add(
      "template", "template",
      new BaseText());

    m_OptionManager.add(
      "license", "license",
      -1, -1, null);
  }

  /**
   * Sets the pretrained model name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the pretrained model name.
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
    return "The pretrained model name.";
  }

  /**
   * Sets the pretrained model version.
   *
   * @param value	the version
   */
  public void setVersion(int value) {
    m_Version = value;
    reset();
  }

  /**
   * Returns the pretrained model version.
   *
   * @return		the version
   */
  public int getVersion() {
    return m_Version;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String versionTipText() {
    return "The pretrained model version.";
  }

  /**
   * Sets the scope.
   *
   * @param value	the scope
   */
  public void setScope(String value) {
    m_Scope = value;
    reset();
  }

  /**
   * Returns the scope.
   *
   * @return		the scope
   */
  public String getScope() {
    return m_Scope;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String scopeTipText() {
    return "The scope of the template.";
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
    return "The domain this template is for.";
  }

  /**
   * Sets the type.
   *
   * @param value	the type
   */
  public void setType(String value) {
    m_Type = value;
    reset();
  }

  /**
   * Returns the type.
   *
   * @return		the URL
   */
  public String getType() {
    return m_Type;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String typeTipText() {
    return "The type.";
  }

  /**
   * Sets the Python executor class.
   *
   * @param value	the class
   */
  public void setExecutorClass(String value) {
    m_ExecutorClass = value;
    reset();
  }

  /**
   * Returns the Python executor class.
   *
   * @return		the class
   */
  public String getExecutorClass() {
    return m_ExecutorClass;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String executorClassTipText() {
    return "The Python executor class.";
  }

  /**
   * Sets the required Python packages (for pip).
   *
   * @param value	the packages
   */
  public void setRequiredPackages(String value) {
    m_RequiredPackages = value;
    reset();
  }

  /**
   * Returns the required Python packages (for pip).
   *
   * @return		the packages
   */
  public String getRequiredPackages() {
    return m_RequiredPackages;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String requiredPackagesTipText() {
    return "The required Python packages (for pip).";
  }

  /**
   * Sets the template instructions for the executor class.
   *
   * @param value	the template
   */
  public void setTemplate(BaseText value) {
    m_Template = value;
    reset();
  }

  /**
   * Returns the template instructions for the executor class.
   *
   * @return		the template
   */
  public BaseText getTemplate() {
    return m_Template;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String templateTipText() {
    return "The template instructions for the executor class.";
  }

  /**
   * Sets the PK of the license.
   *
   * @param value	the license
   */
  public void setLicense(int value) {
    m_License = value;
    reset();
  }

  /**
   * Returns the PK of the license.
   *
   * @return		the license
   */
  public int getLicense() {
    return m_License;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String licenseTipText() {
    return "The template license.";
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
    result += QuickInfoHelper.toString(this, "scope", m_Scope, ", scope: ");
    result += QuickInfoHelper.toString(this, "domain", m_Domain, ", domain: ");
    result += QuickInfoHelper.toString(this, "type", m_Type, ", type: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{JobTemplate.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    JobTemplate 	result;

    result = null;
    try {
      result = m_Client.jobTemplates().create(
        m_Name, m_Version, m_Scope, m_Framework, m_Domain, m_Type,
        m_ExecutorClass, m_RequiredPackages, m_Template.getValue());  // TODO license
    }
    catch (Exception e) {
      errors.add("Failed to create pretrained model!", e);
    }

    return result;
  }
}
