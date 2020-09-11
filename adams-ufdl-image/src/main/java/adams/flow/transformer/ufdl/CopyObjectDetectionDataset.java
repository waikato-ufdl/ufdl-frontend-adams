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
 * CopyObjectDetectionDataset.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;

/**
 * Copies the object detection dataset either via PK or datasetname.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CopyObjectDetectionDataset
  extends CopyDataset {

  private static final long serialVersionUID = 2890424326502728143L;

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
      getLogger().info("Copying object detection dataset: " + dataset);

    try {
      result = m_Client.action(ObjectDetectionDatasets.class).copy(dataset, m_NewName);
    }
    catch (Exception e) {
      errors.add("Failed to copy object detection dataset: " + dataset, e);
    }

    return result;
  }
}
