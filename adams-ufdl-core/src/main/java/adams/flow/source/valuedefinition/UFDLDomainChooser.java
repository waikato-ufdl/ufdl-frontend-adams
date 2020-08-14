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
 * UFDLDomainChooser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.gui.chooser.UFDLDomainChooserPanel;
import adams.gui.core.PropertiesParameterPanel;

/**
 * For selecting a UFDL domains.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLDomainChooser
  extends AbstractUFDLSpreadSheetBasedValueDefinition {

  private static final long serialVersionUID = 4093023607556720026L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "For selecting UFDL domains.";
  }

  /**
   * Adds the value to the panel.
   *
   * @param panel	the panel to add to
   * @return		true if successfully added
   */
  public boolean addToPanel(PropertiesParameterPanel panel) {
    UFDLDomainChooserPanel	chooser;

    if (!check())
      return false;

    chooser  = new UFDLDomainChooserPanel();
    chooser.setConnection(m_Connection);
    chooser.setSorting(m_Sorting);
    chooser.setMultiSelection(m_MultiSelection);
    chooser.setSeparator(m_Separator);

    panel.addPropertyType(getName(), getType());
    panel.setComponent(getName(), chooser);
    if (!getDisplay().trim().isEmpty())
      panel.setLabel(getName(), getDisplay());
    if (!getHelp().isEmpty())
      panel.setHelp(getName(), getHelp());
    return true;
  }
}
