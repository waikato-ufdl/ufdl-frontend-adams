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
 * UFDLContractList.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.source.valuedefinition;

import adams.core.Utils;
import adams.core.io.ConsoleHelper;
import adams.gui.core.PropertiesParameterPanel;
import com.github.waikatoufdl.ufdl4j.contract.AbstractContract;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * For selecting a contract type.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class UFDLContractList
  extends AbstractValueDefinition {

  private static final long serialVersionUID = -3488518552142729321L;

  /** the default value. */
  protected String m_DefaultValue;

  /**
   * Returns a string describing the object.
   *
   * @return a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "The available job contract types.";
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_DefaultValue = "Train";
  }

  /**
   * Returns whether flow context is required.
   *
   * @return true if required
   */
  @Override
  protected boolean requiresFlowContext() {
    return false;
  }

  /**
   * Returns the type of the value.
   *
   * @return the type
   */
  @Override
  public PropertiesParameterPanel.PropertyType getType() {
    return PropertiesParameterPanel.PropertyType.LIST;
  }

  /**
   * Sets the default value as string.
   *
   * @param value the default value
   */
  @Override
  public void setDefaultValueAsString(String value) {
    m_DefaultValue = value;
  }

  /**
   * Returns the default of the value as string.
   *
   * @return the default
   */
  @Override
  public String getDefaultValueAsString() {
    return m_DefaultValue;
  }

  /**
   * Adds the value to the panel.
   *
   * @param panel the panel to add to
   * @return true if successfully added
   */
  @Override
  public boolean addToPanel(PropertiesParameterPanel panel) {
    boolean	found;
    String	defValue;

    if (!check())
      return false;

    // ensure that default is item of list
    found = false;
    for (String value: AbstractContract.getContractNames()) {
      if (value.equals(m_DefaultValue)) {
	found = true;
	break;
      }
    }

    if (!found) {
      if (!m_DefaultValue.isEmpty()) {
	getLogger().severe("Failed to located default value '" + m_DefaultValue + "' in list contract names '" + Utils.flatten(AbstractContract.getContractNames(), ",") + "'!");
	return false;
      }
      else {
	defValue = AbstractContract.getContractNames().get(0);
      }
    }
    else {
      defValue = m_DefaultValue;
    }

    panel.addPropertyType(getName(), getType());
    panel.setList(getName(), AbstractContract.getContractNames().toArray(new String[0]));
    panel.setListDefault(getName(), defValue);
    if (!getDisplay().trim().isEmpty())
      panel.setLabel(getName(), getDisplay());
    if (!getHelp().isEmpty())
      panel.setHelp(getName(), getHelp());
    return true;
  }

  /**
   * Prompts the user to enter a value in headless mode and returns it.
   *
   * @return the entered value, null if canceled
   */
  @Override
  public String headlessInteraction() {
    String	msg;
    String	choice;
    String 	result;
    Set<String> choiceSet;
    String[]	values;

    result = null;

    if (!check())
      return null;

    values = AbstractContract.getContractNames().toArray(new String[0]);
    do {
      msg = "Please select " + (getDisplay().trim().isEmpty() ? getName() : getDisplay())
	+ " from " + Utils.flatten(AbstractContract.getContractNames(), ",")
	+ " (type: " + getType() + "): ";

      choice = ConsoleHelper.enterValue(msg, getDefaultValueAsString());
      if (choice == null)
	break;

      // valid?
      choiceSet = new HashSet<>();
      choiceSet.add(choice);
      choiceSet.removeAll(Arrays.asList(values));
      if (choiceSet.size() == 0)
	result = choice;
    }
    while (result == null);

    return result;
  }
}
