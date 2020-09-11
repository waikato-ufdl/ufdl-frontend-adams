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
 * CopyDataset.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Copies the dataset either via PK or datasetname.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CopyDataset
  extends AbstractDatasetTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /** the new name. */
  protected String m_NewName;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Copies the dataset either via PK or dataset name.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "new-name", "newName",
      "");
  }

  /**
   * Sets the new name of the dataset.
   * If left empty, simply increments the version number
   *
   * @param value	the new name
   */
  public void setNewName(String value) {
    m_NewName = value;
    reset();
  }

  /**
   * Returns the new name of the dataset.
   * If left empty, simply increments the version number
   *
   * @return		the new name
   */
  public String getNewName() {
    return m_NewName;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String newNameTipText() {
    return "The new name of the dataset; if left empty, simply increments the version number.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Boolean.class};
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
      getLogger().info("Copying dataset: " + dataset);

    try {
      result = m_Client.datasets().copy(dataset, m_NewName);
    }
    catch (Exception e) {
      errors.add("Failed to copy dataset: " + dataset, e);
    }

    return result;
  }
}
