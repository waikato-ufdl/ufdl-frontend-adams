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
 * UFDLExtractImageNameFromFile.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.io.PlaceholderFile;
import adams.flow.core.ufdl.ImageNameExtraction;

/**
 * Converts a file into a name used in datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLExtractImageNameFromFile
  extends AbstractStringConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /** how to extract the name of the file. */
  protected ImageNameExtraction m_ImageNameExtraction;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a file into a name used in datasets.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "image-name-extraction", "imageNameExtraction",
      ImageNameExtraction.NAME);
  }

  /**
   * Sets how to extract the image name from the file name.
   *
   * @param value	the extraction type
   */
  public void setImageNameExtraction(ImageNameExtraction value) {
    m_ImageNameExtraction = value;
    reset();
  }

  /**
   * Returns how to extract the image name from the file name.
   *
   * @return		the extraction type
   */
  public ImageNameExtraction getImageNameExtraction() {
    return m_ImageNameExtraction;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String imageNameExtractionTipText() {
    return "Determines how to generate the name of the image from its filename.";
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    return m_ImageNameExtraction.extract(new PlaceholderFile((String) m_Input));
  }
}
