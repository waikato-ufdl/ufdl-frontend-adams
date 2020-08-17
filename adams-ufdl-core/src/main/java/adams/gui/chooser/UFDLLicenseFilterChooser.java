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

import adams.core.MessageCollection;
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
  extends AbstractUFDLPKChooserPanel<License> {

  private static final long serialVersionUID = 2236959758363695310L;

  /** the filter panel. */
  protected transient LicenseFilterPanel m_PanelFilter;

  /** the dialog. */
  protected transient ApprovalDialog m_Dialog;

  /**
   * Turns the object into a struct (ID and string).
   *
   * @param value	the object to convert
   * @return		the generated struct
   */
  @Override
  protected Struct2<Integer, String> toStruct(License value) {
    return new Struct2<>(value.getPK(), value.getName());
  }

  /**
   * Loads the object based on the ID.
   *
   * @param pk		the ID to load
   * @return		the object, null if failed to load
   * @throws Exception	if loading failed
   */
  @Override
  protected License loadObject(int pk) throws Exception {
    return m_Connection.getClient().licenses().load(pk);
  }

  /**
   * Creates a new array of the specified object.
   *
   * @param len		the length of the array
   * @return		the array
   */
  @Override
  protected License[] newArray(int len) {
    return new License[len];
  }

  /**
   * Performs the actual choosing of an object.
   *
   * @return		the chosen object or null if none chosen
   */
  @Override
  protected License[] doChoose() {
    License[] 	result;
    License[]	selected;

    result = getCurrent();

    if (m_Dialog == null) {
      m_PanelFilter = new LicenseFilterPanel();
      if (m_MultiSelection)
        m_PanelFilter.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      else
        m_PanelFilter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      if (getParentDialog() != null)
	m_Dialog = new ApprovalDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
      else
	m_Dialog = new ApprovalDialog(getParentFrame(), true);
      m_Dialog.setDefaultCloseOperation(ApprovalDialog.HIDE_ON_CLOSE);
      if (m_MultiSelection)
	m_Dialog.setTitle("Select licenses");
      else
	m_Dialog.setTitle("Select license");
      m_Dialog.getContentPane().add(m_PanelFilter, BorderLayout.CENTER);
      m_Dialog.setDiscardVisible(false);
      m_Dialog.setCancelVisible(true);
      m_Dialog.setApproveVisible(true);
      m_Dialog.setSize(GUIHelper.getDefaultDialogDimension());
      m_Dialog.setLocationRelativeTo(getParent());
    }

    try {
      m_PanelFilter.setLicenses(m_Connection.getClient().licenses().list(m_Filter.generate(new MessageCollection())).toArray(new License[0]));
    }
    catch (Exception e) {
      ConsolePanel.getSingleton().append("Failed to list licenses!", e);
    }
    m_PanelFilter.clearFilters();
    if (result != null)
      m_PanelFilter.setSelectedLicenses(result);
    m_Dialog.setVisible(true);
    if (m_Dialog.getOption() == ApprovalDialog.APPROVE_OPTION) {
      selected = m_PanelFilter.getSelectedLicenses();
      if (!m_MultiSelection) {
	if (selected.length == 1)
	  result = selected;
      }
      else {
        result = selected;
      }
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
