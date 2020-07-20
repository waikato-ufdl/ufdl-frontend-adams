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
 * AbstractUFDLSpreadSheetBasedValueDefinition.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.io.ConsoleHelper;
import adams.flow.core.UFDLListSorting;
import adams.gui.chooser.AbstractUFDLPKChooserPanel;
import adams.gui.core.PropertiesParameterPanel.PropertyType;
import com.github.fracpete.javautils.struct.Struct2;

/**
 * Ancestor for value definitions that allow selection of one or more objects
 * from a spreadsheet dialog.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLSpreadSheetBasedValueDefinition
  extends AbstractUFDLValueDefinition {

  private static final long serialVersionUID = -3323078223665162476L;

  public static final String NO_ENTRIES = "No entries";

  public static final int NO_PK = -1;

  /** how to sort. */
  protected UFDLListSorting m_Sorting;

  /** allow multiple objects to be selected. */
  protected boolean m_MultiSelection;

  /** the separator in use. */
  protected String m_Separator;

  /** the default value. */
  protected String m_DefaultValue;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "sorting", "sorting",
      UFDLListSorting.BY_DESCRIPTION_CASE_INSENSITIVE);

    m_OptionManager.add(
      "multi-selection", "multiSelection",
      false);

    m_OptionManager.add(
      "separator", "separator",
      AbstractUFDLPKChooserPanel.DEFAULT_SEPARATOR);

    m_OptionManager.add(
      "default-value", "defaultValue",
      "");
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
   * Sets whether multiple objects can be selected.
   *
   * @param value	true if multi-selection
   */
  public void setMultiSelection(boolean value) {
    m_MultiSelection = value;
    reset();
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
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String multiSelectionTipText() {
    return "If enabled, multiple objects can be selected.";
  }

  /**
   * Sets the separator for the string representation.
   *
   * @param value	the separator
   */
  public void setSeparator(String value) {
    if ((value != null) && (value.length() == 1)) {
      m_Separator = value;
      reset();
    }
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
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String separatorTipText() {
    return "The separator to use when selecting multiple objects.";
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
    return PropertyType.CUSTOM_COMPONENT;
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
   * Prompts the user to enter a value in headless mode and returns it.
   *
   * @return		the entered value, null if canceled
   */
  public String headlessInteraction() {
    String		msg;

    if (!check())
      return null;

    if (m_MultiSelection)
      msg = "Please enter PKs " + (getDisplay().trim().isEmpty() ? getName() : getDisplay()) + ": ";
    else
      msg = "Please enter PK " + (getDisplay().trim().isEmpty() ? getName() : getDisplay()) + ": ";

    return ConsoleHelper.enterValue(msg, getDefaultValueAsString());
  }
}
