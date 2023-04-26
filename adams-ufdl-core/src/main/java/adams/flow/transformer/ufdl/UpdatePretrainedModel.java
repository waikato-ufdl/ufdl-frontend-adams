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
 * UpdatePretrainedModel.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseText;
import com.github.waikatoufdl.ufdl4j.action.PretrainedModels.PretrainedModel;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;

/**
 * Updates a team and forwards the team object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdatePretrainedModel
  extends AbstractPretrainedModelTransformerAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the docker image name. */
  protected String m_Name;

  /** the framework. */
  protected int m_Framework;

  /** the domain. */
  protected String m_Domain;

  /** the license for the image. */
  protected String m_License;

  /** the URL. */
  protected String m_URL;

  /** the description. */
  protected BaseText m_Description;

  /** the metadata. */
  protected BaseText m_MetaData;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates a team and forwards the team object.";
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
      "framework", "framework",
      -1, -1, null);

    m_OptionManager.add(
      "domain", "domain",
      "");

    m_OptionManager.add(
      "license", "license",
      "");

    m_OptionManager.add(
      "url", "URL",
      "");

    m_OptionManager.add(
      "description", "description",
      new BaseText());

    m_OptionManager.add(
      "metadata", "metaData",
      new BaseText());
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
   * Sets the URL where to obtain the model from.
   *
   * @param value	the URL
   */
  public void setURL(String value) {
    m_URL = value;
    reset();
  }

  /**
   * Returns the URL where to obtain the model from.
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
    return "The URL to obtain the model from.";
  }

  /**
   * Sets the description.
   *
   * @param value	the description
   */
  public void setDescription(BaseText value) {
    m_Description = value;
    reset();
  }

  /**
   * Returns the description.
   *
   * @return		the description
   */
  public BaseText getDescription() {
    return m_Description;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String descriptionTipText() {
    return "The description for the model.";
  }

  /**
   * Sets the metadata.
   *
   * @param value	the metadata
   */
  public void setMetaData(BaseText value) {
    m_MetaData = value;
    reset();
  }

  /**
   * Returns the metadata.
   *
   * @return		the metadata
   */
  public BaseText getMetaData() {
    return m_MetaData;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String metaDataTipText() {
    return "The meta-data for the model, ignored if empty.";
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
    result += QuickInfoHelper.toString(this, "domain", m_Domain, ", domain: ");
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
    return new Class[]{Team.class};
  }

  /**
   * Transforms the input data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doTransform(PretrainedModel model, MessageCollection errors) {
    PretrainedModel    result;

    result = null;
    try {
      result = m_Client.pretrainedModels().update(model, m_Name, m_Framework, m_Domain, m_License, m_URL, m_Description.getValue(), m_MetaData.getValue());
    }
    catch (Exception e) {
      errors.add("Failed to update team: " + model, e);
    }

    return result;
  }
}
