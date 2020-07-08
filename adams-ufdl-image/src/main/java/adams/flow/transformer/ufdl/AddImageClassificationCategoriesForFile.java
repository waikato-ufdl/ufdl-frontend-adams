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
 * AddImageClassificationCategoriesForFile.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseObject;
import adams.core.base.BaseString;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

import java.util.Arrays;

/**
 * Assigns the specified categories to the named images for the dataset passing through.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AddImageClassificationCategoriesForFile
  extends AbstractImageClassificationDatasetTransformerAction {

  private static final long serialVersionUID = -1421130988687306299L;

  /** the names of the images to assign the categories to. */
  protected BaseString[] m_Names;

  /** the categories to assign to each image. */
  protected BaseString[] m_Categories;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Assigns the specified categories to the named images for the dataset passing through.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "name", "names",
      new BaseString[0]);

    m_OptionManager.add(
      "category", "categories",
      new BaseString[0]);
  }

  /**
   * Sets the names of the images to update.
   *
   * @param value	the names
   */
  public void setNames(BaseString[] value) {
    m_Names = value;
    reset();
  }

  /**
   * Returns the names of the images to update.
   *
   * @return		the names
   */
  public BaseString[] getNames() {
    return m_Names;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String namesTipText() {
    return "The names of the images to update.";
  }

  /**
   * Sets the categories to add to each image.
   *
   * @param value	the categories
   */
  public void setCategories(BaseString[] value) {
    m_Categories = value;
    reset();
  }

  /**
   * Returns the categories to add to each image.
   *
   * @return		the categories
   */
  public BaseString[] getCategories() {
    return m_Categories;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String categoriesTipText() {
    return "The categories to assign to each image.";
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
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "names", m_Names, "names: ");
    result += QuickInfoHelper.toString(this, "categories", m_Categories, ", categories: ");

    return result;
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
    try {
      if (!getDatasetsAction().addCategories(
        dataset,
	Arrays.asList(BaseObject.toStringArray(m_Names)),
	Arrays.asList(BaseObject.toStringArray(m_Categories))))
	errors.add("Failed to add categories for dataset: " + dataset);
    }
    catch (Exception e) {
      errors.add("Failed to add categories for dataset: " + dataset, e);
    }

    return dataset;
  }
}
