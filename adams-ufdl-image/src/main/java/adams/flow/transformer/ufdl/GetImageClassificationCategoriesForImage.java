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
 * GetImageClassificationCategoriesForImage.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

import java.util.List;

/**
 * Obtains the categories for the dataset passing through and forwards them (image -> list of categories).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class GetImageClassificationCategoriesForImage
  extends AbstractImageClassificationDatasetTransformerAction {

  private static final long serialVersionUID = -1421130988687306299L;

  /** the name of the image to get the metadata for. */
  protected String m_Name;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Obtains the categories for the specified image of the dataset passing through and forwards them (image -> list of categories).";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "name", "name",
      "");
  }

  /**
   * Sets the name of the image to get the metadata for.
   *
   * @param value 	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name of the image to get the metadata for.
   *
   * @return 		the name
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
    return "The name of the image to get the metadata for.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "name", (m_Name.isEmpty() ? "-none-" : m_Name));
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{List.class};
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
    List<String>		result;

    result = null;
    try {
      result = getDatasetsAction().getCategories(dataset, m_Name);
    }
    catch (Exception e) {
      errors.add("Failed to retrieve categories of image '" + m_Name + "' from dataset: " + dataset, e);
    }

    return result;
  }
}
