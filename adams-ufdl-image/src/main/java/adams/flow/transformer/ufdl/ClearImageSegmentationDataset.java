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
 * ClearImageSegmentationDataset.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageSegmentationDatasets.ImageSegmentationDataset;

/**
 * Clears the image segmentation dataset either via PK or datasetname.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ClearImageSegmentationDataset
  extends AbstractImageSegmentationDatasetTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Clears the image segmentation dataset either via PK or dataset name.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{ImageSegmentationDataset.class};
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
    Dataset result;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Clearing dataset: " + dataset);

    try {
      result = getDatasetsAction().clear(dataset);
    }
    catch (Exception e) {
      errors.add("Failed to clear dataset: " + dataset, e);
    }

    return result;
  }
}
