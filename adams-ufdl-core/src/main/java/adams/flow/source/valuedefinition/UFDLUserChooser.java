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
 * UFDLUserChooser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.TriState;
import adams.gui.chooser.UFDLUserChooserPanel;
import adams.gui.core.PropertiesParameterPanel;

/**
 * For selecting a UFDL users.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLUserChooser
  extends AbstractUFDLSpreadSheetBasedValueDefinition {

  private static final long serialVersionUID = 4093023607556720026L;

  /** the active state of the users. */
  protected TriState m_Active;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "For selecting UFDL users.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "active", "active",
      TriState.TRUE);
  }

  /**
   * Sets the state of the users to retrieve.
   *
   * @param value	the state
   */
  public void setActive(TriState value) {
    m_Active = value;
    reset();
  }

  /**
   * Returns the state of the users to retrieve.
   *
   * @return		the state
   */
  public TriState getActive() {
    return m_Active;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String activeTipText() {
    return "The active state of the users to retrieve.";
  }

  /**
   * Adds the value to the panel.
   *
   * @param panel	the panel to add to
   * @return		true if successfully added
   */
  public boolean addToPanel(PropertiesParameterPanel panel) {
    UFDLUserChooserPanel	chooser;

    if (!check())
      return false;

    chooser  = new UFDLUserChooserPanel();
    chooser.setConnection(m_Connection);
    chooser.setSorting(m_Sorting);
    chooser.setMultiSelection(m_MultiSelection);
    chooser.setSeparator(m_Separator);
    chooser.setActive(m_Active);

    panel.addPropertyType(getName(), getType());
    panel.setComponent(getName(), chooser);
    if (!getDisplay().trim().isEmpty())
      panel.setLabel(getName(), getDisplay());
    if (!getHelp().isEmpty())
      panel.setHelp(getName(), getHelp());
    return true;
  }
}
