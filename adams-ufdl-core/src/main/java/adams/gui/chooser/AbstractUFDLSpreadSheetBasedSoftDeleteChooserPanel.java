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
 * AbstractUFDLSpreadSheetBasedSoftDeleteChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.flow.core.UFDLSoftDeleteObjectState;
import adams.flow.core.UFDLSoftDeleteObjectStateHandler;
import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Ancestor for chooser panels which allow the user to select one or more
 * soft-delete objects from a spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLSpreadSheetBasedSoftDeleteChooserPanel<T extends SoftDeleteObject>
  extends AbstractUFDLSpreadSheetBasedChooserPanel<T>
  implements UFDLSoftDeleteObjectStateHandler {

  private static final long serialVersionUID = -8773065516752491368L;

  /** the state of the objects to list. */
  protected UFDLSoftDeleteObjectState m_State;

  /**
   * Sets the state of the objects to retrieve.
   *
   * @param value	the state
   */
  @Override
  public void setState(UFDLSoftDeleteObjectState value) {
    m_State = value;
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
   * Hook method for filtering objects.
   *
   * @param objects	the objects to filter
   * @return		the filtered objects
   */
  protected T[] filterObjects(T[] objects) {
    Object	result;
    List    	filtered;
    int		i;

    if (m_State == UFDLSoftDeleteObjectState.ANY)
      return objects;

    filtered = new ArrayList();
    for (T object: objects) {
      if (m_State.accept(object))
        filtered.add(object);
    }

    result = newArray(filtered.size());
    for (i = 0; i < filtered.size(); i++)
      Array.set(result, i, filtered.get(i));

    return (T[]) result;
  }
}
