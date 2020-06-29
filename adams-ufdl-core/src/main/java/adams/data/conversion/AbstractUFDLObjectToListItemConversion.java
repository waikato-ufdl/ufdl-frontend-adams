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
 * AbstractUFDLObjectToListItemConversion.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.QuickInfoHelper;
import adams.flow.core.UFDLListSorting;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.core.AbstractJsonObjectWrapper;

/**
 * Ancestor for conversions that turn UFDL objects into list item strings.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLObjectToListItemConversion<T extends AbstractJsonObjectWrapper>
  extends AbstractConversion {

  private static final long serialVersionUID = -4958030929508325441L;

  /** the type of sorting to use for generating the string. */
  protected UFDLListSorting m_Sorting;

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
   * Sets the sorting that will get applied to the list item.
   *
   * @param value	the sorting
   */
  public void setSorting(UFDLListSorting value) {
    m_Sorting = value;
    reset();
  }

  /**
   * Returns the sorting that will get applied to the list item.
   *
   * @return 		the sorting
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
    return "The sorting that will get applied to the list item.";
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
    return String.class;
  }

  /**
   * Converts the input object into a struct.
   *
   * @param input	the input object to convert
   * @return		the generated struct
   */
  protected abstract Struct2<Integer,String> objectToStruct(T input);

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() {
    Struct2<Integer,String> 	item;

    item = objectToStruct((T) m_Input);
    return m_Sorting.toString(item);
  }
}
