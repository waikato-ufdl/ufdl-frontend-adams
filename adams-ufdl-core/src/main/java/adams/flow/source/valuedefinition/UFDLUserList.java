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
 * UserList.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.ClassCrossReference;
import adams.core.TriState;
import adams.flow.transformer.UFDLExtractAndTransfer;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * For selecting a UFDL user.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLUserList
  extends AbstractUFDLListValueDefinition
  implements ClassCrossReference {

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
    return "For selecting a UFDL user.";
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
   * Returns the state of the users to retriev.
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
   * Returns the cross-referenced classes.
   *
   * @return		the classes
   */
  @Override
  public Class[] getClassCrossReferences() {
    return new Class[]{UFDLExtractAndTransfer.class};
  }

  /**
   * Returns the list of items to display.
   *
   * @return		the items for the list (PK and description)
   */
  @Override
  protected List<Struct2<Integer, String>> listItems() {
    List<Struct2<Integer, String>>	result;

    result = new ArrayList<>();

    try {
      for (User user : m_Connection.getClient().users().list()) {
        switch (m_Active) {
	  case FALSE:
	    if (user.isActive())
	      continue;
	    break;
	  case TRUE:
	    if (!user.isActive())
	      continue;
	    break;
	}
        result.add(new Struct2<>(user.getPK(), user.getUserName()));
      }
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to retrieve list of users!", e);
    }

    return result;
  }
}
