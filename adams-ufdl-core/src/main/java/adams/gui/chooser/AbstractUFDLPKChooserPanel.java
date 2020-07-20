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
 * AbstractUFDLPKChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.core.Utils;
import adams.flow.core.UFDLListSorting;
import adams.gui.core.ConsolePanel;
import com.github.fracpete.javautils.struct.Struct2;

/**
 * Ancestor for UFDL chooser panels that use PKs in their strings.
 * Supports multi-selection.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLPKChooserPanel<T>
  extends AbstractUFDLChooserPanel<T[]> {

  private static final long serialVersionUID = -3378435434299216372L;

  /** the default separator. */
  public final static String DEFAULT_SEPARATOR = "|";

  /** how to sort. */
  protected UFDLListSorting m_Sorting;

  /** allow multiple objects to be selected. */
  protected boolean m_MultiSelection;

  /** the separator in use. */
  protected String m_Separator;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Sorting        = UFDLListSorting.BY_DESCRIPTION_CASE_INSENSITIVE;
    m_MultiSelection = false;
    m_Separator      = DEFAULT_SEPARATOR;
  }

  /**
   * Sets how to sort the list items.
   *
   * @param value	the sorting
   */
  public void setSorting(UFDLListSorting value) {
    m_Sorting = value;
  }

  /**
   * Returns how to sort the list items.
   *
   * @return 		the sorting
   */
  public UFDLListSorting getSorting() {
    return m_Sorting;
  }

  /**
   * Sets whether multiple objects can be selected.
   *
   * @param value	true if multi-selection
   */
  public void setMultiSelection(boolean value) {
    m_MultiSelection = value;
  }

  /**
   * Returns whether multiple objects can be selected.
   *
   * @return 		true if multi-selection
   */
  public boolean getMultiSelection() {
    return m_MultiSelection;
  }

  /**
   * Sets the separator for the string representation.
   *
   * @param value	the separator
   */
  public void setSeparator(String value) {
    if ((value == null) || (value.length() != 1))
      throw new IllegalArgumentException("Separator has to be one character, supplied: " + value);
    m_Separator = value;
  }

  /**
   * Returns the separator for the string representation.
   *
   * @return 		the separator
   */
  public String getSeparator() {
    return m_Separator;
  }

  /**
   * Turns the object into a struct (ID and string).
   *
   * @param value	the object to convert
   * @return		the generated struct
   */
  protected abstract Struct2<Integer,String> toStruct(T value);

  /**
   * Converts the value into its string representation.
   *
   * @param value	the value to convert
   * @return		the generated string
   */
  protected String toString(T[] value) {
    StringBuilder	result;
    int			i;

    result = new StringBuilder();
    for (i = 0; i < value.length; i++) {
      if (i > 0)
        result.append(m_Separator);
      result.append(m_Sorting.toString(toStruct(value[i])));
    }

    return result.toString();
  }

  /**
   * Loads the object based on the ID.
   *
   * @param pk		the ID to load
   * @return		the object, null if failed to load
   * @throws Exception	if loading failed
   */
  protected abstract T loadObject(int pk) throws Exception;

  /**
   * Creates a new array of the specified object.
   *
   * @param len		the length of the array
   * @return		the array
   */
  protected abstract T[] newArray(int len);

  /**
   * Converts the string representation into its object representation.
   *
   * @param value	the string value to convert
   * @return		the generated object, null if failed to convert
   */
  @Override
  protected T[] fromString(String value) {
    T[]		result;
    String[]	parts;
    int		i;

    if (value.trim().isEmpty())
      return newArray(0);

    parts = Utils.split(value, m_Separator);
    try {
      result = newArray(parts.length);
      for (i = 0; i < parts.length; i++)
        result[i] = loadObject(m_Sorting.fromString(parts[i]).value1);
      return result;
    }
    catch (Exception e) {
      ConsolePanel.getSingleton().append("Failed to parse object string: " + value, e);
      return null;
    }
  }
}
