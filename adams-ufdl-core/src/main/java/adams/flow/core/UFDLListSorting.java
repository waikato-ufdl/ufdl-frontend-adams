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
 * UFDLListSorting.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

import adams.core.Utils;
import com.github.fracpete.javautils.struct.Struct2;

/**
 * The type of sorting to use.
 */
public enum UFDLListSorting {
  BY_ID,
  BY_DESCRIPTION_CASE_SENSITIVE,
  BY_DESCRIPTION_CASE_INSENSITIVE;

  /**
   * Assembles the list item into string ('ID: DESC' or 'DESC [ID]').
   *
   * @param item	the item to assemble
   * @return		the generating string
   */
  public String toString(Struct2<Integer,String> item) {
    switch (this) {
      case BY_ID:
	return item.value1 + ": " + item.value2;

      case BY_DESCRIPTION_CASE_SENSITIVE:
      case BY_DESCRIPTION_CASE_INSENSITIVE:
	return item.value2 + " [" + item.value1 + "]";

      default:
        throw new IllegalStateException("Unhandled sorting: " + this);
    }
  }

  /**
   * Parses the list item from the string representation.
   *
   * @param s		the string to parse
   * @return		the generated item, null if failed to parse
   */
  public Struct2<Integer,String> fromString(String s) {
    String	idStr;
    int		id;
    String	desc;

    switch (this) {
      case BY_ID:
        if (!s.contains(":")) {
          System.err.println("Failed to parse item '" + s + "', expected format: 'PK: DESC'");
	  return null;
	}
        idStr = s.substring(0, s.indexOf(':'));
        if (!Utils.isInteger(idStr)) {
          System.err.println("Failed to parse item '" + s + "', PK is not an integer, expected format: 'PK: DESC'");
          return null;
	}
	id   = Integer.parseInt(idStr);
        desc = s.substring(s.indexOf(':')).trim();
	break;

      case BY_DESCRIPTION_CASE_SENSITIVE:
      case BY_DESCRIPTION_CASE_INSENSITIVE:
        if (!s.contains("[") && !s.contains("]")) {
	  System.err.println("Failed to parse item '" + s + "', expected format: 'DESC [PK]'");
	  return null;
	}
	idStr = s.substring(s.lastIndexOf('[') + 1, s.lastIndexOf(']'));
        if (!Utils.isInteger(idStr)) {
          System.err.println("Failed to parse item '" + s + "', PK is not an integer, expected format: 'DESC [PK]'");
          return null;
	}
	id   = Integer.parseInt(idStr);
        desc = s.substring(0, s.indexOf('[')).trim();
	break;

      default:
        throw new IllegalStateException("Unhandled sorting: " + this);
    }

    return new Struct2<>(id, desc);
  }
}
