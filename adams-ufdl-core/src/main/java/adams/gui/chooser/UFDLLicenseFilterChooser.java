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
 * UFDLLicenseFilterChooser.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.flow.core.UFDLListSorting;
import adams.flow.standalone.UFDLConnection;
import adams.gui.core.ConsolePanel;
import adams.gui.core.GUIHelper;
import adams.gui.dialog.ApprovalDialog;
import adams.gui.visualization.licenses.LicenseFilterPanel;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;

/**
 * For selecting a single license with the help of a filter based on
 * permissions, limitations and conditions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLLicenseFilterChooser
  extends AbstractChooserPanel<License> {

  private static final long serialVersionUID = 2236959758363695310L;

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /** how to sort. */
  protected UFDLListSorting m_Sorting;

  /** the filter panel. */
  protected transient LicenseFilterPanel m_PanelFilter;

  /** the dialog. */
  protected transient ApprovalDialog m_Dialog;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Connection = null;
    m_Sorting    = UFDLListSorting.BY_DESCRIPTION_CASE_INSENSITIVE;
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

  /**
   * Sets how to sort the list items.
   *
   * @param value	the sorting
   */
  public void setSorting(UFDLListSorting value) {
    m_Sorting = value;
  }

  /**
   * Returns how to sort the list items.
   *
   * @return 		the sorting
   */
  public UFDLListSorting getSorting() {
    return m_Sorting;
  }

  /**
   * Converts the value into its string representation.
   *
   * @param value	the value to convert
   * @return		the generated string
   */
  @Override
  protected String toString(License value) {
    return m_Sorting.toString(new Struct2<>(value.getPK(), value.getName()));
  }

  /**
   * Converts the string representation into its object representation.
   *
   * @param value	the string value to convert
   * @return		the generated object, null if failed to convert
   */
  @Override
  protected License fromString(String value) {
    try {
      return m_Connection.getClient().licenses().load(m_Sorting.fromString(value).value1);
    }
    catch (Exception e) {
      ConsolePanel.getSingleton().append("Failed to parse license string: " + value, e);
      return null;
    }
  }

  /**
   * Performs the actual choosing of an object.
   *
   * @return		the chosen object or null if none chosen
   */
  @Override
  protected License doChoose() {
    License 	result;
    License[]	selected;

    result = getCurrent();

    if (m_Dialog == null) {
      m_PanelFilter = new LicenseFilterPanel();
      m_PanelFilter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      if (getParentDialog() != null)
	m_Dialog = new ApprovalDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
      else
	m_Dialog = new ApprovalDialog(getParentFrame(), true);
      m_Dialog.setDefaultCloseOperation(ApprovalDialog.HIDE_ON_CLOSE);
      m_Dialog.setTitle("Select license");
      m_Dialog.getContentPane().add(m_PanelFilter, BorderLayout.CENTER);
      m_Dialog.setDiscardVisible(false);
      m_Dialog.setCancelVisible(true);
      m_Dialog.setApproveVisible(true);
      m_Dialog.setSize(GUIHelper.getDefaultDialogDimension());
      m_Dialog.setLocationRelativeTo(getParent());
    }

    try {
      m_PanelFilter.setLicenses(m_Connection.getClient().licenses().list().toArray(new License[0]));
    }
    catch (Exception e) {
      ConsolePanel.getSingleton().append("Failed to list licenses!", e);
    }
    if (result != null)
      m_PanelFilter.setSelectedLicenses(new License[]{result});
    m_Dialog.setVisible(true);
    if (m_Dialog.getOption() == ApprovalDialog.APPROVE_OPTION) {
      selected = m_PanelFilter.getSelectedLicenses();
      if (selected.length == 1)
        result = selected[0];
    }

    return result;
  }

  /**
   * Cleans up data structures, frees up memory.
   */
  @Override
  public void cleanUp() {
    if (m_Dialog != null) {
      m_Dialog.dispose();
      m_Dialog = null;
    }

    super.cleanUp();
  }
}
