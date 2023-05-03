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
 * UFDLJobTemplateChooserByContract.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.data.ufdlfilter.AllFilter;
import adams.flow.core.UFDLContractType;
import adams.gui.chooser.UFDLJobTemplateChooserByContractPanel;
import adams.gui.core.PropertiesParameterPanel;

import java.util.logging.Level;

/**
 * For selecting a UFDL job templates (by contract).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobTemplateChooserByContract
  extends AbstractUFDLSpreadSheetBasedSoftDeleteValueDefinition {

  private static final long serialVersionUID = 4093023607556720026L;

  /** the contract type. */
  protected UFDLContractType m_ContractType;

  /** the domain to filter by. */
  protected int m_Domain;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "For selecting UFDL job templates (by contract).";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.removeByProperty("filter");

    m_OptionManager.add(
      "contract-type", "contractType",
      UFDLContractType.TRAIN);

    m_OptionManager.add(
      "domain", "domain",
      -1);
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Filter = new AllFilter();
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  @Override
  public String stateTipText() {
    return "The state that the job templates must have in order to be listed.";
  }

  /**
   * Sets the contract type to look for.
   *
   * @param value	the type
   */
  public void setContractType(UFDLContractType value) {
    m_ContractType = value;
    reset();
  }

  /**
   * Returns the contract type to look for.
   *
   * @return		the type
   */
  public UFDLContractType getContractType() {
    return m_ContractType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String contractTypeTipText() {
    return "The contract type to look for.";
  }

  /**
   * Sets the domain PK to look for.
   *
   * @param value	the domain
   */
  public void setDomain(int value) {
    m_Domain = value;
    reset();
  }

  /**
   * Returns the domain PK to look for.
   *
   * @return		the domain
   */
  public int getDomain() {
    return m_Domain;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String domainTipText() {
    return "The domain PK to look for, ignored if -1.";
  }

  /**
   * Adds the value to the panel.
   *
   * @param panel	the panel to add to
   * @return		true if successfully added
   */
  public boolean addToPanel(PropertiesParameterPanel panel) {
    UFDLJobTemplateChooserByContractPanel chooser;

    if (!check())
      return false;

    chooser  = new UFDLJobTemplateChooserByContractPanel();
    chooser.setConnection(m_Connection);
    chooser.setContractType(m_ContractType);
    if (m_Domain == -1) {
      chooser.setDomain(null);
    }
    else {
      try {
        chooser.setDomain(m_Connection.getClient().domains().load(m_Domain));
      }
      catch (Exception e) {
        getLogger().log(Level.SEVERE, "Failed to load domain: " + m_Domain, e);
        chooser.setDomain(null);
      }
    }
    chooser.setSorting(m_Sorting);
    chooser.setMultiSelection(m_MultiSelection);
    chooser.setSeparator(m_Separator);
    chooser.setState(m_State);

    panel.addPropertyType(getName(), getType());
    panel.setComponent(getName(), chooser);
    if (!getDisplay().trim().isEmpty())
      panel.setLabel(getName(), getDisplay());
    if (!getHelp().isEmpty())
      panel.setHelp(getName(), getHelp());
    return true;
  }
}
