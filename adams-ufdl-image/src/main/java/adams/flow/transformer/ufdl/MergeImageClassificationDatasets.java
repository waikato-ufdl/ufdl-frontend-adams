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
 * MergeImageClassificationDatasets.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Merges the source dataset into the one passing through.
 * Source dataset can be deleted afterwards.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MergeImageClassificationDatasets
  extends AbstractImageClassificationDatasetTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the source dataset PK to merge with. */
  protected int m_Source;

  /** whether to delete the source dataset afterwards. */
  protected boolean m_Delete;

  /** whether to perform a hard delete. */
  protected boolean m_Hard;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Merges the source dataset into the one passing through.\n"
      + "Source dataset can be deleted afterwards.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "source", "source",
      -1, -1, null);

    m_OptionManager.add(
      "delete", "delete",
      false);

    m_OptionManager.add(
      "hard", "hard",
      false);
  }

  /**
   * Sets the PK of the source dataset to merge with.
   *
   * @param value	the PK
   */
  public void setSource(int value) {
    m_Source = value;
    reset();
  }

  /**
   * Returns the PK of the source dataset to merge with.
   *
   * @return		the PK
   */
  public int getSource() {
    return m_Source;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String sourceTipText() {
    return "The PK of the dataset to merge with.";
  }

  /**
   * Sets whether to remove the source dataset afterwards.
   *
   * @param value	true if to remove
   */
  public void setDelete(boolean value) {
    m_Delete = value;
    reset();
  }

  /**
   * Returns whether to remove or just flag as deleted.
   *
   * @return		true if to remove
   */
  public boolean getDelete() {
    return m_Delete;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String deleteTipText() {
    return "Whether to delete the source dataset after the merge.";
  }

  /**
   * Sets whether to remove or just flag as deleted.
   *
   * @param value	true if to remove
   */
  public void setHard(boolean value) {
    m_Hard = value;
    reset();
  }

  /**
   * Returns whether to remove or just flag as deleted.
   *
   * @return		true if to remove
   */
  public boolean getHard() {
    return m_Hard;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String hardTipText() {
    return "Whether to remove or just flag as deleted.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "source", m_Source, "source: ");
    result += QuickInfoHelper.toString(this, "delete", (m_Delete ? "delete source" : "keep source"));
    result += QuickInfoHelper.toString(this, "hard", (m_Hard ? "hard" : "soft"), ", ");

    return result;
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Dataset.class};
  }

  /**
   * Transforms the dataset.
   *
   * @param dataset	the dataset
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    boolean	result;

    result = false;

    if (isLoggingEnabled())
      getLogger().info("Merging dataset PK " + dataset.getPK() + " with source PK " + m_Source + " (delete=" + m_Delete + ")");

    try {
      result = getDatasetsAction().merge(dataset.getPK(), m_Source, m_Delete);  // TODO hard/soft delete
    }
    catch (Exception e) {
      errors.add("Failed to merge dataset PK " + dataset.getPK() + " with source PK " + m_Source + " (delete=" + m_Delete + ")", e);
    }

    return result;
  }
}
