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
 * AbstractUFDLAction.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

import adams.core.QuickInfoSupporter;
import adams.core.option.AbstractOptionHandler;
import com.github.waikatoufdl.ufdl4j.Client;

/**
 * Ancestor for all UFDL actions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLAction
  extends AbstractOptionHandler
  implements QuickInfoSupporter, FlowContextHandler {

  private static final long serialVersionUID = -7067718770232842692L;

  /** the client to use for interacting with the backend. */
  protected transient Client m_Client;

  /** the flow context. */
  protected Actor m_FlowContext;

  /**
   * Sets the flow context.
   *
   * @param value	the actor
   */
  public void setFlowContext(Actor value) {
    m_FlowContext = value;
  }

  /**
   * Returns the flow context, if any.
   *
   * @return		the actor, null if none available
   */
  public Actor getFlowContext() {
    return m_FlowContext;
  }

  /**
   * Returns whether the action requires flow context.
   *
   * @return		true if required
   */
  public boolean requiresFlowContext() {
    return false;
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   * <br>
   * Default implementation returns null.
   *
   * @return		null if no info available, otherwise short string
   */
  public String getQuickInfo() {
    return null;
  }

  /**
   * Sets the client to use.
   *
   * @param value	the client
   */
  public void setClient(Client value) {
    m_Client = value;
  }

  /**
   * Returns the client in use.
   *
   * @return		the client
   */
  public Client getClient() {
    return m_Client;
  }
}
