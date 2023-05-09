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
 * UFDLExtractFileNameFromFile.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.QuickInfoHelper;
import adams.core.io.PlaceholderFile;
import adams.flow.core.UFDLFileNameExtraction;

/**
 * Converts a file into a name used in datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLExtractFileNameFromFile
  extends AbstractStringConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /** how to extract the name of the file. */
  protected UFDLFileNameExtraction m_FileNameExtraction;

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
      "file-name-extraction", "fileNameExtraction",
      UFDLFileNameExtraction.NAME);
  }

  /**
   * Sets how to extract the name from the file name.
   *
   * @param value	the extraction type
   */
  public void setFileNameExtraction(UFDLFileNameExtraction value) {
    m_FileNameExtraction = value;
    reset();
  }

  /**
   * Returns how to extract the name from the file name.
   *
   * @return		the extraction type
   */
  public UFDLFileNameExtraction getFileNameExtraction() {
    return m_FileNameExtraction;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String imageNameExtractionTipText() {
    return "Determines how to generate the name from the filename.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "fileNameExtraction", m_FileNameExtraction, "extraction: ");
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    return m_FileNameExtraction.extract(new PlaceholderFile((String) m_Input));
  }
}
