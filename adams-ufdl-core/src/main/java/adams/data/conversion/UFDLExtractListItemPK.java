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
 * UFDLExtractListItemID.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.QuickInfoHelper;
import adams.flow.core.UFDLListSorting;
import com.github.fracpete.javautils.struct.Struct2;

/**
 * Extracts the PK from the string representation of the list item.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLExtractListItemPK
  extends AbstractConversionFromString {

  private static final long serialVersionUID = 3902623383598030005L;

  /** how to the list items were sorted. */
  protected UFDLListSorting m_Sorting;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Extracts the PK from the string representation of the list item.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "sorting", "sorting",
      UFDLListSorting.BY_DESCRIPTION_CASE_INSENSITIVE);
  }

  /**
   * Sets how to list items were sorted.
   *
   * @param value	true if to sort by ID
   */
  public void setSorting(UFDLListSorting value) {
    m_Sorting = value;
    reset();
  }

  /**
   * Returns how to list items were sorted.
   *
   * @return 		true if to sort by ID
   */
  public UFDLListSorting getSorting() {
    return m_Sorting;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String sortingTipText() {
    return "The sorting that was applied to the list items.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "sorting", m_Sorting);
  }

  /**
   * Returns the class that is generated as output.
   *
   * @return		the class
   */
  @Override
  public Class generates() {
    return Integer.class;
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    Integer			result;
    Struct2<Integer,String> 	item;

    item = m_Sorting.fromString((String) m_Input);
    if (item == null)
      result = -1;
    else
      result = item.value1;

    return result;
  }
}
