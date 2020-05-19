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
 * CreateImageClassificationDataset.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets;

/**
 * Creates an image classification dataset and forwards the dataset object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateImageClassificationDataset
  extends AbstractCreateDataset {

  private static final long serialVersionUID = 2444931814949354710L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates an image classification dataset and forwards the dataset object.";
  }

  /**
   * Creates the dataset and returns it.
   *
   * @return		the dataset
   */
  @Override
  protected Dataset createDataset() throws Exception {
      return m_Client.action(ImageClassificationDatasets.class).create(
        m_Name, m_Version, m_Project, m_Licence, m_IsPublic, m_Tags);
  }
}