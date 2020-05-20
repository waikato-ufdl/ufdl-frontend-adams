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
 * UFDLSoftDeleteObjectState.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

import com.github.waikatoufdl.ufdl4j.core.SoftDeleteObject;

/**
 * Enumeration for states of a {@link SoftDeleteObject}.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public enum UFDLSoftDeleteObjectState {
  /** any state. */
  ANY,
  /** only active. */
  ACTIVE,
  /** only deleted. */
  DELETED;

  /**
   * Whether to accept the soft delete object based on the enum.
   *
   * @param object	the object to check
   * @return		true if to accept
   */
  public boolean accept(SoftDeleteObject object) {
    switch (this) {
      case ANY:
        // nothing to do
        break;
      case ACTIVE:
        if (object.getDeletionTime() != null)
          return false;
        break;
      case DELETED:
        if (object.getDeletionTime() == null)
          return false;
        break;
    }
    return true;
  }
}
