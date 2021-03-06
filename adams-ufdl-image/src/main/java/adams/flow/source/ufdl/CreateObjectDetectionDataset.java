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
 * CreateObjectDetectionDataset.java
 * Copyright (C) 2020-2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.ObjectDetectionDataset;

/**
 * Creates an object detection dataset and forwards the dataset object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateObjectDetectionDataset
  extends AbstractCreateDataset {

  private static final long serialVersionUID = 2444931814949354710L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates an object detection dataset and forwards the dataset object.";
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{ObjectDetectionDataset.class};
  }

  /**
   * Creates the dataset and returns it.
   *
   * @return		the dataset
   */
  @Override
  protected Dataset createDataset() throws Exception {
      return m_Client.action(ObjectDetectionDatasets.class).create(
        m_Name, m_Description, m_Project, m_License, m_IsPublic, m_Tags);
  }
}
