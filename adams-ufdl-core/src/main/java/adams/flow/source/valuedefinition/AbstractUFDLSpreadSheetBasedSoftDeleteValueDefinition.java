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
 * AbstractUFDLSpreadSheetBasedSoftDeleteValueDefinition.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.flow.core.UFDLSoftDeleteObjectState;
import adams.flow.core.UFDLSoftDeleteObjectStateHandler;

/**
 * Ancestor for value definitions that allow selection of one or more soft-delete
 * objects from a spreadsheet dialog.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLSpreadSheetBasedSoftDeleteValueDefinition
  extends AbstractUFDLSpreadSheetBasedValueDefinition
  implements UFDLSoftDeleteObjectStateHandler {

  private static final long serialVersionUID = 6062420176229244118L;

  /** the state of the objects to list. */
  protected UFDLSoftDeleteObjectState m_State;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "state", "state",
      UFDLSoftDeleteObjectState.ACTIVE);
  }

  /**
   * Sets the state of the objects to retrieve.
   *
   * @param value	the state
   */
  @Override
  public void setState(UFDLSoftDeleteObjectState value) {
    m_State = value;
    reset();
  }

  /**
   * Returns the state of the objects to retrieve.
   *
   * @return		the state
   */
  @Override
  public UFDLSoftDeleteObjectState getState() {
    return m_State;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public abstract String stateTipText();
}
