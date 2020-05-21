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
 * GetImageClassificationCategories.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets;

import java.util.List;
import java.util.Map;

/**
 * Obtains the categories for the dataset passing through and forwards them (image -> list of categories).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class GetImageClassificationCategories
  extends AbstractDatasetTransformerAction {

  private static final long serialVersionUID = -1421130988687306299L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Obtains the categories for the dataset passing through and forwards them (image -> list of categories).";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Map.class};
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
    Map<String,List<String>>		result;
    ImageClassificationDatasets 	action;

    result = null;
    try {
      action = m_Client.action(ImageClassificationDatasets.class);
      result = action.getCategories(dataset);
    }
    catch (Exception e) {
      errors.add("Failed to retrieve categories for dataset: " + dataset, e);
    }

    return result;
  }
}
