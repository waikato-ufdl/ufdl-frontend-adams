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
 * UpdateObjectDetectionDataset.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;

/**
 * Updates an object detection dataset and forwards the dataset object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdateObjectDetectionDataset
  extends AbstractUpdateDataset {

  private static final long serialVersionUID = 2444931814949354710L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates an object detection dataset and forwards the dataset object.";
  }

  /**
   * Updates the dataset and returns it.
   *
   * @param dataset	the dataset to update
   * @return		the updated dataset
   */
  protected Dataset updateDataset(Dataset dataset) throws Exception {
      return m_Client.action(ObjectDetectionDatasets.class).update(
        dataset, m_Name, m_Description, m_Project, m_License, m_IsPublic, m_Tags);
  }
}
