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
 * AbstractUFDLSpreadSheetBasedChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.core.logging.LoggingLevel;
import adams.data.conversion.AbstractUFDLObjectToSpreadSheetConversion;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.spreadsheet.SpreadSheetHelper;
import adams.flow.core.UFDLFilterHandler;
import adams.gui.core.ConsolePanel;
import adams.gui.core.GUIHelper;
import adams.gui.dialog.SpreadSheetDialog;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import javax.swing.ListSelectionModel;
import java.awt.Dialog.ModalityType;

/**
 * Ancestor for chooser panels which allow the user to select one or more
 * objects from a spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractUFDLSpreadSheetBasedChooserPanel<T>
  extends AbstractUFDLPKChooserPanel<T>
  implements UFDLFilterHandler {

  private static final long serialVersionUID = -5162524212611793388L;

  /** the dialog for selecting the projects. */
  protected SpreadSheetDialog m_Dialog;

  /**
   * Returns the conversion to use for generating a spreadsheet from an object.
   *
   * @return		the conversion
   */
  protected abstract AbstractUFDLObjectToSpreadSheetConversion getConversion();

  /**
   * Returns the column index (0-based) with the PK in the spreadsheet.
   *
   * @return		the column
   */
  protected abstract int getPKColumn();

  /**
   * Returns the PK of the object.
   *
   * @param object	the object to get the PK from
   * @return		the PK
   */
  protected abstract int getPK(T object);

  /**
   * Hook method for filtering objects.
   * <br>
   * The default implementation performs no filtering.
   *
   * @param objects	the objects to filter
   * @return		the filtered objects
   */
  protected T[] filterObjects(T[] objects) {
    return objects;
  }

  /**
   * Returns all available objects.
   *
   * @return		the objects
   * @throws Exception	if API call fails
   */
  protected abstract T[] getAvailableObjects() throws Exception;

  /**
   * Converts the available objects into a single spreadsheet to select from.
   *
   * @param objects	the object to convert
   * @return		the spreadsheet, null if no objects converted
   */
  protected SpreadSheet objectsToSpreadSheet(T[] objects) {
    SpreadSheet					result;
    AbstractUFDLObjectToSpreadSheetConversion	conv;
    int						i;
    SpreadSheet					sheet;
    String					msg;

    conv   = getConversion();
    result = conv.getTemplate();
    for (i = 0; i < objects.length; i++) {
      try {
	conv.setInput(objects[i]);
	msg = conv.convert();
	if (msg != null) {
	  ConsolePanel.getSingleton().append(LoggingLevel.SEVERE, "Failed to convert UFDL object to spreadsheet: " + msg);
	  return conv.getTemplate();
	}
	sheet = (SpreadSheet) conv.getOutput();
	if (i == 0)
	  result = sheet;
	else
	  result = SpreadSheetHelper.append(result, sheet, true);
      }
      catch (Exception e) {
	ConsolePanel.getSingleton().append("Failed to convert UFDL object to spreadsheet!", e);
	return conv.getTemplate();
      }
    }

    return result;
  }

  /**
   * Returns the title for the dialog.
   *
   * @return		the title
   */
  protected abstract String getDialogTitle();

  /**
   * Performs the actual choosing of an object.
   *
   * @return		the chosen object or null if none chosen
   */
  @Override
  protected T[] doChoose() {
    T[]		result;
    T[]		available;
    TIntList 	selected;
    int		i;
    int		n;
    String 	pkStr;
    int		pkCol;
    int[]	selRows;

    result = getCurrent();

    if (m_Dialog == null) {
      if (getParentDialog() != null)
	m_Dialog = new SpreadSheetDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
      else
	m_Dialog = new SpreadSheetDialog(getParentFrame(), true);
      m_Dialog.setTitle(getDialogTitle());
      if (m_MultiSelection)
	m_Dialog.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      else
	m_Dialog.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      m_Dialog.getTable().setShowRowColumn(false);
      m_Dialog.setShowColumnComboBox(false);
      m_Dialog.setShowSearch(true);
      m_Dialog.setSize(GUIHelper.getDefaultDialogDimension());
      m_Dialog.setLocationRelativeTo(getParent());
    }

    try {
      available = filterObjects(getAvailableObjects());
    }
    catch (Exception e) {
      ConsolePanel.getSingleton().append("Failed to determine available objects!", e);
      return null;
    }

    // clear search
    m_Dialog.getTable().search(null, false);

    // pre-select
    m_Dialog.setSpreadSheet(objectsToSpreadSheet(available));
    selected = new TIntArrayList();
    pkCol    = getPKColumn();
    for (i = 0; i < result.length; i++) {
      pkStr = "" + getPK(result[i]);
      for (n = 0; n < m_Dialog.getTable().getRowCount(); n++) {
        if (pkStr.equals("" + m_Dialog.getTable().getValueAt(n, pkCol))) {
          selected.add(n);
          break;
	}
      }
    }
    m_Dialog.getTable().setSelectedRows(selected.toArray());

    // display
    m_Dialog.setVisible(true);
    if (m_Dialog.getOption() != SpreadSheetDialog.APPROVE_OPTION)
      return result;

    // determine selected objects
    selRows = m_Dialog.getTable().getSelectedRows();
    if (!m_MultiSelection && (selRows.length != 1))
      return result;

    result = newArray(selRows.length);
    for (i = 0; i < selRows.length; i++) {
      pkStr = "" + m_Dialog.getTable().getValueAt(selRows[i], pkCol);
      for (n = 0; n < available.length; n++) {
        if (pkStr.equals("" + getPK(available[n]))) {
          result[i] = available[n];
          break;
	}
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
