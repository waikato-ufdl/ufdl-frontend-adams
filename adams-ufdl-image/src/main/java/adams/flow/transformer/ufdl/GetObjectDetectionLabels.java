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
 * GetObjectDetectionLabels.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

import java.util.List;

/**
 * Obtains the labels for the dataset passing through and forwards them.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class GetObjectDetectionLabels
  extends AbstractObjectDetectionDatasetTransformerAction {

  private static final long serialVersionUID = -1421130988687306299L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Obtains the labels for the dataset passing through and forwards them.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{String[].class};
  }

  /**
   * Transforms the input data.
   *
   * @param dataset	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    String[]		result;
    List<String> 	labels;

    result = null;
    try {
      labels = getDatasetsAction().getLabels(dataset);
      result = labels.toArray(new String[0]);
    }
    catch (Exception e) {
      errors.add("Failed to retrieve labels for dataset: " + dataset, e);
    }

    return result;
  }
}
