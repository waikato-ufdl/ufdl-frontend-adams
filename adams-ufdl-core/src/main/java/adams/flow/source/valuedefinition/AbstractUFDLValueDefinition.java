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
 * AbstractUFDLValueDefinition.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.Utils;
import adams.flow.core.ActorUtils;
import adams.flow.standalone.UFDLConnection;

/**
 * Ancestor for UFDL value definitions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLValueDefinition
  extends AbstractValueDefinition {

  private static final long serialVersionUID = -5648467062589713523L;

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /**
   * Returns whether flow context is required.
   *
   * @return		true if required
   */
  @Override
  protected boolean requiresFlowContext() {
    return true;
  }

  /**
   * Method for checking setup state.
   *
   * @return		true if checks successful
   */
  @Override
  protected boolean check() {
    boolean	result;

    result = super.check();

    if (result) {
      m_Connection = (UFDLConnection) ActorUtils.findClosestType(m_FlowContext, UFDLConnection.class, true);
      if (m_Connection == null) {
        getLogger().severe("No " + Utils.classToString(UFDLConnection.class) + " instance found!");
	result = false;
      }
    }

    return result;
  }
}
