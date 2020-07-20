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
 * UFDLUserChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.data.conversion.AbstractUFDLObjectToSpreadSheetConversion;
import adams.data.conversion.UFDLUserToSpreadSheet;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Allows the user to select users.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLUserChooserPanel
  extends AbstractUFDLSpreadSheetBasedChooserPanel<User> {

  private static final long serialVersionUID = -5162524212611793388L;

  /**
   * Turns the object into a struct (ID and string).
   *
   * @param value	the object to convert
   * @return		the generated struct
   */
  @Override
  protected Struct2<Integer, String> toStruct(User value) {
    return new Struct2<>(value.getPK(), value.getUserName());
  }

  /**
   * Loads the object based on the ID.
   *
   * @param pk		the ID to load
   * @return		the object, null if failed to load
   * @throws Exception	if loading failed
   */
  @Override
  protected User loadObject(int pk) throws Exception {
    return m_Connection.getClient().users().load(pk);
  }

  /**
   * Creates a new array of the specified object.
   *
   * @param len		the length of the array
   * @return		the array
   */
  @Override
  protected User[] newArray(int len) {
    return new User[len];
  }

  /**
   * Returns the conversion to use for generating a spreadsheet from an object.
   *
   * @return		the conversion
   */
  @Override
  protected AbstractUFDLObjectToSpreadSheetConversion getConversion() {
    return new UFDLUserToSpreadSheet();
  }

  /**
   * Returns the column index (0-based) with the PK in the spreadsheet.
   *
   * @return		the column
   */
  @Override
  protected int getPKColumn() {
    return 0;
  }

  /**
   * Returns the PK of the object.
   *
   * @param object	the object to get the PK from
   * @return		the PK
   */
  @Override
  protected int getPK(User object) {
    return object.getPK();
  }

  /**
   * Returns all available objects.
   *
   * @return		the objects
   * @throws Exception	if API call fails
   */
  @Override
  protected User[] getAvailableObjects() throws Exception {
    return m_Connection.getClient().users().list().toArray(new User[0]);
  }

  /**
   * Returns the title for the dialog.
   *
   * @return		the title
   */
  @Override
  protected String getDialogTitle() {
    if (m_MultiSelection)
      return "Select users";
    else
      return "Select user";
  }
}
