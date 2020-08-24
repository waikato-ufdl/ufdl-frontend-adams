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
 * AbstractUFDLListValueDefinition.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.Utils;
import adams.core.io.ConsoleHelper;
import adams.data.ufdlfilter.AbstractUFDLFilter;
import adams.data.ufdlfilter.AllFilter;
import adams.flow.core.UFDLFilterHandler;
import adams.flow.core.UFDLListSorting;
import adams.gui.core.PropertiesParameterPanel;
import adams.gui.core.PropertiesParameterPanel.PropertyType;
import com.github.fracpete.javautils.struct.Struct2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Ancestor for list-based UFDL value definitions that return int (the PK).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLListValueDefinition
  extends AbstractUFDLValueDefinition
  implements UFDLFilterHandler {

  private static final long serialVersionUID = 3824312130176924215L;

  public static final String NO_ENTRIES = "No entries";

  public static final int NO_PK = -1;

  /** the filter to apply. */
  protected AbstractUFDLFilter m_Filter;

  /** how to sort. */
  protected UFDLListSorting m_Sorting;

  /** the default value. */
  protected String m_DefaultValue;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "filter", "filter",
      new AllFilter());

    m_OptionManager.add(
      "sorting", "sorting",
      getDefaultSorting());

    m_OptionManager.add(
      "default-value", "defaultValue",
      "");
  }

  /**
   * Sets the filter to apply to the result.
   *
   * @param value	the filter
   */
  @Override
  public void setFilter(AbstractUFDLFilter value) {
    m_Filter = value;
    reset();
  }

  /**
   * Returns the filter to apply to the result.
   *
   * @return		the filter
   */
  @Override
  public AbstractUFDLFilter getFilter() {
    return m_Filter;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String filterTipText() {
    return "The filter to apply.";
  }

  /**
   * Returns the default sorting.
   *
   * @return		the default
   */
  protected UFDLListSorting getDefaultSorting() {
    return UFDLListSorting.BY_DESCRIPTION_CASE_INSENSITIVE;
  }

  /**
   * Sets how to sort the list items.
   *
   * @param value	the sorting
   */
  public void setSorting(UFDLListSorting value) {
    m_Sorting = value;
    reset();
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
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String sortingTipText() {
    return "The sorting to apply to the list items.";
  }

  /**
   * Sets how to sort the list items.
   *
   * @param value	the default value
   */
  public void setDefaultValue(String value) {
    m_DefaultValue = value;
    reset();
  }

  /**
   * Returns the default value.
   *
   * @return 		the default value
   */
  public String getDefaultValue() {
    return m_DefaultValue;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String defaultValueTipText() {
    return "The default value to use.";
  }

  /**
   * Returns the type of the value.
   *
   * @return 		the type
   */
  @Override
  public PropertyType getType() {
    return PropertyType.LIST;
  }

  /**
   * Returns the list of items to display.
   *
   * @return		the items for the list (PK and description)
   */
  protected abstract List<Struct2<Integer,String>> listItems();

  /**
   * Sorts the list items.
   *
   * @param items	the items to sort
   * @return		the sorted list
   */
  protected List<Struct2<Integer,String>> sortListItems(List<Struct2<Integer,String>> items) {
    Struct2<Integer,String>[]		array;
    Comparator<Struct2<Integer,String>>	comp;

    array = (Struct2<Integer,String>[]) items.toArray(new Struct2[0]);
    switch (m_Sorting) {
      case BY_ID:
	comp = new Comparator<Struct2<Integer, String>>() {
	  @Override
	  public int compare(Struct2<Integer, String> o1, Struct2<Integer, String> o2) {
	    return o1.value1.compareTo(o2.value1);
	  }
	};
	break;
      case BY_DESCRIPTION_CASE_SENSITIVE:
      case BY_DESCRIPTION_CASE_SENSITIVE_NO_ID:
	comp = new Comparator<Struct2<Integer, String>>() {
	  @Override
	  public int compare(Struct2<Integer, String> o1, Struct2<Integer, String> o2) {
	    return o1.value2.compareTo(o2.value2);
	  }
	};
	break;
      case BY_DESCRIPTION_CASE_INSENSITIVE:
      case BY_DESCRIPTION_CASE_INSENSITIVE_NO_ID:
	comp = new Comparator<Struct2<Integer, String>>() {
	  @Override
	  public int compare(Struct2<Integer, String> o1, Struct2<Integer, String> o2) {
	    return o1.value2.toLowerCase().compareTo(o2.value2.toLowerCase());
	  }
	};
	break;
      default:
	throw new IllegalStateException("Unhandled sorting: " + m_Sorting);
    }

    Arrays.sort(array, comp);
    return Arrays.asList(array);
  }

  /**
   * Sorts the items and turns them into a string list. If list is empty,
   * adds a dummy entry (-1, "No entries") to it.
   *
   * @param items	the items to process
   * @return		the generated list
   */
  protected List<String> toStringList(List<Struct2<Integer,String>> items) {
    List<String> result;

    items = sortListItems(items);
    result = new ArrayList<>();
    for (Struct2<Integer, String> item : items)
      result.add(m_Sorting.toString(item));

    if (result.size() == 0)
      result.add(m_Sorting.toString(new Struct2<>(NO_PK, NO_ENTRIES)));

    return result;
  }

  /**
   * Sets the default value as string.
   *
   * @param value	the default value
   */
  @Override
  public void setDefaultValueAsString(String value) {
    m_DefaultValue = value;
  }

  /**
   * Returns the default of the value as string.
   *
   * @return 		the default
   */
  @Override
  public String getDefaultValueAsString() {
    if (m_DefaultValue.isEmpty())
      return m_Sorting.toString(new Struct2<>(NO_PK, NO_ENTRIES));
    else
      return m_DefaultValue;
  }

  /**
   * Adds the value to the panel.
   *
   * @param panel	the panel to add to
   * @return		true if successfully added
   */
  public boolean addToPanel(PropertiesParameterPanel panel) {
    List<String>	itemsStr;

    if (!check())
      return false;

    itemsStr = toStringList(listItems());

    panel.addPropertyType(getName(), getType());
    panel.setList(getName(), itemsStr.toArray(new String[0]));
    panel.setListDefault(getName(), itemsStr.get(0));
    if (!getDisplay().trim().isEmpty())
      panel.setLabel(getName(), getDisplay());
    if (!getHelp().isEmpty())
      panel.setHelp(getName(), getHelp());
    return true;
  }

  /**
   * Prompts the user to enter a value in headless mode and returns it.
   *
   * @return		the entered value, null if canceled
   */
  public String headlessInteraction() {
    List<String>	itemsStr;
    String		msg;

    if (!check())
      return null;

    itemsStr = toStringList(listItems());

    msg = "Available options:\n"
      + Utils.flatten(itemsStr, "\n")
      + "\nPlease enter PK " + (getDisplay().trim().isEmpty() ? getName() : getDisplay()) + ": ";

    return ConsoleHelper.enterValue(msg, getDefaultValueAsString());
  }
}
