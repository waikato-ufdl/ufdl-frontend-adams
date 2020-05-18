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
 * AbstractCreateDataset.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Ancestor for actions that create datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractCreateDataset
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the dataset name. */
  protected String m_Name;

  /** the version. */
  protected int m_Version;

  /** the project PK. */
  protected int m_Project;

  /** the license. */
  protected String m_Licence;

  /** whether it is public. */
  protected boolean m_IsPublic;

  /** the tags to add. */
  protected String m_Tags;

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
      "project", "project",
      -1, -1, null);

    m_OptionManager.add(
      "licence", "licence",
      "");

    m_OptionManager.add(
      "is-public", "isPublic",
      false);

    m_OptionManager.add(
      "tags", "tags",
      "");
  }

  /**
   * Sets the dataset name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the dataset name.
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
    return "The dataset name.";
  }

  /**
   * Sets the dataset version.
   *
   * @param value	the version
   */
  public void setVersion(int value) {
    m_Version = value;
    reset();
  }

  /**
   * Returns the dataset version.
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
    return "The dataset version.";
  }

  /**
   * Sets the project PK this dataset belongs to.
   *
   * @param value	the project
   */
  public void setProject(int value) {
    m_Project = value;
    reset();
  }

  /**
   * Returns the project PK this dataset belongs to.
   *
   * @return		the project
   */
  public int getProject() {
    return m_Project;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String projectTipText() {
    return "The project PK this dataset belongs to.";
  }

  /**
   * Sets the licence.
   *
   * @param value	the licence
   */
  public void setLicence(String value) {
    m_Licence = value;
    reset();
  }

  /**
   * Returns the licence.
   *
   * @return		the licence
   */
  public String getLicence() {
    return m_Licence;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String licenceTipText() {
    return "The dataset licence.";
  }

  /**
   * Sets whether the dataset is a public one.
   *
   * @param value	true if public
   */
  public void setIsPublic(boolean value) {
    m_IsPublic = value;
    reset();
  }

  /**
   * Returns whether the dataset is a public one.
   *
   * @return		true if public
   */
  public boolean getIsPublic() {
    return m_IsPublic;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String isPublicTipText() {
    return "Whether the dataset is publicly available.";
  }

  /**
   * Sets the tags for the dataset.
   *
   * @param value	the tags
   */
  public void setTags(String value) {
    m_Tags = value;
    reset();
  }

  /**
   * Returns the tags for the dataset.
   *
   * @return		the tags
   */
  public String getTags() {
    return m_Tags;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String tagsTipText() {
    return "The tags for the dataset.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", m_Name);
    result += QuickInfoHelper.toString(this, "version", m_Version, "/");
    result += QuickInfoHelper.toString(this, "project", m_Project, ", project: ");
    result += QuickInfoHelper.toString(this, "licence", m_LoggingIsEnabled, ", licence: ");
    result += QuickInfoHelper.toString(this, "isPublic", m_IsPublic, "public", ", ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Dataset.class};
  }

  /**
   * Creates the dataset and returns it.
   *
   * @return		the dataset
   */
  protected abstract Dataset createDataset() throws Exception;

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    Dataset    	result;

    result = null;
    try {
      result = createDataset();
    }
    catch (Exception e) {
      errors.add("Failed to create dataset!", e);
    }

    return result;
  }
}
