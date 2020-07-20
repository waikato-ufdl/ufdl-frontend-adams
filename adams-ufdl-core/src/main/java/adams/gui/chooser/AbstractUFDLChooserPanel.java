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
 * AbstractUFDLChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.flow.standalone.UFDLConnection;

/**
 * Ancestor for UFDL-based chooser panels.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the object type
 */
public abstract class AbstractUFDLChooserPanel<T>
  extends AbstractChooserPanel<T> {

  private static final long serialVersionUID = 2236959758363695310L;

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Connection = null;
  }

  /**
   * Sets the connection to use.
   *
   * @param value	the connection
   */
  public void setConnection(UFDLConnection value) {
    m_Connection = value;
  }

  /**
   * Returns the connection in use.
   *
   * @return		the connection, null if none set
   */
  public UFDLConnection getConnection() {
    return m_Connection;
  }
}
