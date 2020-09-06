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
 * UFDLJobOutputChooser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.io.ConsoleHelper;
import adams.gui.chooser.UFDLJobOutputChooserPanel;
import adams.gui.core.PropertiesParameterPanel;
import adams.gui.core.PropertiesParameterPanel.PropertyType;

/**
 * Allows selecting a Job output (returning a {@link UFDLJobOutput}).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobOutputChooser
  extends AbstractUFDLValueDefinition {

  private static final long serialVersionUID = 1832359990293662049L;

  /** the default value. */
  protected String m_DefaultValue;

  /** the type to filter on. */
  protected String m_OutputType;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Allows selecting a Job output (returning a UFDLJobOutput).";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "default-value", "defaultValue",
      "");

    m_OptionManager.add(
      "output-type", "outputType",
      "");
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
   * Sets the type to filter on.
   *
   * @param value	the filter, null or empty to disable filtering
   */
  public void setOutputType(String value) {
    m_OutputType = value;
    reset();
  }

  /**
   * Returns the type to filter on.
   *
   * @return		the filter, empty string if no filtering
   */
  public String getOutputType() {
    return m_OutputType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String outputTypeTipText() {
    return "The output type to filter on, ignored if empty string.";
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
      return "-1||";
    else
      return m_DefaultValue;
  }

  /**
   * Adds the value to the panel.
   *
   * @param panel	the panel to add to
   * @return		true if successfully added
   */
  @Override
  public boolean addToPanel(PropertiesParameterPanel panel) {
    UFDLJobOutputChooserPanel chooser;

    if (!check())
      return false;

    chooser  = new UFDLJobOutputChooserPanel();
    chooser.setConnection(m_Connection);
    chooser.setOutputType(m_OutputType);

    panel.addPropertyType(getName(), getType());
    panel.setComponent(getName(), chooser);
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
  @Override
  public String headlessInteraction() {
    String		msg;

    if (!check())
      return null;

    msg = "Please enter job output (pk|name|type) " + (getDisplay().trim().isEmpty() ? getName() : getDisplay()) + ": ";

    return ConsoleHelper.enterValue(msg, getDefaultValueAsString());
  }
}
