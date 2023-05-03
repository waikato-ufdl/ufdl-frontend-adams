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
 * UFDLJobOutputChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.core.ObjectCopyHelper;
import adams.core.logging.LoggingLevel;
import adams.data.conversion.UFDLJobOutputToSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.ufdlfilter.AbstractUFDLFilter;
import adams.data.ufdlfilter.AllFilter;
import adams.data.ufdlfilter.GenericFilter;
import adams.data.ufdlfilter.field.ExactString;
import adams.flow.core.UFDLFilterHandler;
import adams.flow.core.UFDLJobOutput;
import adams.flow.core.UFDLListSorting;
import adams.flow.core.UFDLSoftDeleteObjectState;
import adams.gui.core.ConsolePanel;
import adams.gui.core.GUIHelper;
import adams.gui.core.MouseUtils;
import adams.gui.dialog.ApprovalDialog;
import adams.gui.dialog.SpreadSheetPanel;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.action.Jobs.JobOutput;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Chooser for panel for selecting a job output.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobOutputChooserPanel
  extends AbstractUFDLChooserPanel<UFDLJobOutput>
  implements UFDLFilterHandler {

  private static final long serialVersionUID = -5265818418046987726L;

  /**
   * How to display the information.
   */
  public enum DisplayType {
    ID,
    FULL,
  }

  /** the type to filter on. */
  protected String m_OutputType;

  /** the filter to apply (if no output type set). */
  protected AbstractUFDLFilter m_Filter;

  /** whether to only use the ID in the display. */
  protected DisplayType m_DisplayType;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_DisplayType    = DisplayType.FULL;
    m_OutputType     = "";
    m_Filter         = new AllFilter();
  }

  /**
   * Sets the display type.
   *
   * @param value	the type
   */
  public void setDisplayType(DisplayType value) {
    m_DisplayType = value;
  }

  /**
   * Returns the display type.
   *
   * @return		the type
   */
  public DisplayType getDisplayType() {
    return m_DisplayType;
  }

  /**
   * Sets the type to filter on.
   *
   * @param value	the filter, null or empty to disable filtering
   */
  public void setOutputType(String value) {
    if (value == null)
      value = "";
    m_OutputType = value;
  }

  /**
   * Returns the type to filter on.
   *
   * @return		the filter, empty string if no filtering
   */
  public String getOutputType() {
    return m_OutputType;
  }

  /**
   * Sets the filter to apply to the result (if no output type set).
   *
   * @param value	the filter
   */
  @Override
  public void setFilter(AbstractUFDLFilter value) {
    m_Filter = value;
  }

  /**
   * Returns the filter to apply to the result (if no output type set).
   *
   * @return		the filter
   */
  @Override
  public AbstractUFDLFilter getFilter() {
    return m_Filter;
  }

  /**
   * Converts the value into its string representation.
   *
   * @param value	the value to convert
   * @return		the generated string
   */
  @Override
  protected String toString(UFDLJobOutput value) {
    switch (m_DisplayType) {
      case FULL:
        return value.getValue();
      case ID:
        return "" + value.pkValue();
      default:
        throw new IllegalStateException("Unhandled display type: " + m_DisplayType);
    }
  }

  /**
   * Converts the string representation into its object representation.
   *
   * @param value	the string value to convert
   * @return		the generated object
   */
  @Override
  protected UFDLJobOutput fromString(String value) {
    return new UFDLJobOutput(value);
  }

  /**
   * Retrieves the outputs from the jobs and displays them in the table.
   *
   * @param job		the job to display the outputs for
   * @param panel	the panel to display the outputs in
   */
  protected void displayOutputs(Job job, SpreadSheetPanel panel) {
    SpreadSheet 		outputs;
    SpreadSheet 		sheet;
    UFDLJobOutputToSpreadSheet 	conv;
    String			msg;

    outputs = null;
    conv = new UFDLJobOutputToSpreadSheet();
    conv.setResolveIDs(true);
    conv.setFlowContext(getConnection());
    for (JobOutput output: job.getOutputs()) {
      if (!m_OutputType.isEmpty()) {
	if (!output.getType().equals(m_OutputType))
	  continue;
      }
      try {
	conv.setInput(output);
	msg = conv.convert();
	if (msg == null) {
	  sheet = (SpreadSheet) conv.getOutput();
	  if (outputs == null) {
	    outputs = sheet;
	  }
	  else {
	    for (Row row : sheet.rows())
	      outputs.addRow().assign(row);
	  }
	}
	else {
	  ConsolePanel.getSingleton().append(LoggingLevel.WARNING, "Failed to convert job output: " + output + "\n" + msg);
	}
      }
      catch (Exception e) {
	ConsolePanel.getSingleton().append("Failed to convert job output: " + output, e);
      }
    }

    if (outputs != null)
      panel.setSpreadSheet(outputs);
  }

  /**
   * Performs the actual choosing of an object.
   *
   * @return		the chosen object or null if none chosen
   */
  @Override
  protected UFDLJobOutput doChoose() {
    UFDLJobOutput		result;
    final ApprovalDialog	dialog;
    JPanel			panelAll;
    final UFDLJobChooserPanel	panelJob;
    final SpreadSheetPanel	panelOutputs;
    Job[]			jobs;
    SpreadSheet			sheet;
    int				row;
    int				pk;
    String			type;
    String			name;
    GenericFilter		filter;

    panelAll = new JPanel(new BorderLayout(5, 5));
    panelAll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    panelOutputs = new SpreadSheetPanel();
    panelOutputs.setShowSearch(false);
    panelOutputs.setShowColumnComboBox(false);
    panelOutputs.setShowFormulas(false);
    panelAll.add(panelOutputs, BorderLayout.CENTER);

    panelJob = new UFDLJobChooserPanel();
    if (m_OutputType.isEmpty()) {
      panelJob.setFilter(ObjectCopyHelper.copyObject(m_Filter));
    }
    else {
      filter = new GenericFilter();
      filter.addExpression(new ExactString("outputs.type", m_OutputType, false));
      panelJob.setFilter(filter);
    }
    panelJob.setConnection(m_Connection);
    panelJob.setSorting(UFDLListSorting.BY_ID_ONLY);
    panelJob.setMultiSelection(false);
    panelJob.setState(UFDLSoftDeleteObjectState.ACTIVE);
    panelJob.addChangeListener((ChangeEvent e) -> {
      Job[] current = panelJob.getCurrent();
      if ((current != null) && (current.length > 0))
	displayOutputs(current[0], panelOutputs);
    });
    panelAll.add(panelJob, BorderLayout.NORTH);

    if (getParentDialog() != null)
      dialog = new ApprovalDialog(getParentDialog(), ModalityType.DOCUMENT_MODAL);
    else
      dialog = new ApprovalDialog(getParentFrame(), true);
    panelOutputs.getTable().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
	if (MouseUtils.isDoubleClick(e)) {
	  if (panelOutputs.getTable().getSelectedRowCount() == 1)
	    dialog.getApproveButton().doClick();
	}
	if (!e.isConsumed())
	  super.mouseClicked(e);
      }
    });
    dialog.setTitle("Select job output");
    dialog.setDefaultCloseOperation(ApprovalDialog.DISPOSE_ON_CLOSE);
    dialog.setApproveVisible(true);
    dialog.setCancelVisible(true);
    dialog.setDiscardVisible(false);
    dialog.getContentPane().add(panelAll, BorderLayout.CENTER);
    dialog.setSize(GUIHelper.getDefaultSmallDialogDimension());
    dialog.setLocationRelativeTo(getParent());
    dialog.setVisible(true);
    if (dialog.getOption() != ApprovalDialog.APPROVE_OPTION)
      return null;
    jobs = panelJob.getCurrent();
    if ((jobs == null) || (jobs.length == 0))
      return null;
    if (panelOutputs.getTable().getSelectedRow() == -1)
      return null;
    sheet = panelOutputs.getSpreadSheet();
    row   = panelOutputs.getTable().getActualRow(panelOutputs.getTable().getSelectedRow());
    pk    = sheet.getRow(row).getCell(0).toLong().intValue();
    name  = sheet.getRow(row).getCell(1).getContent();
    type  = sheet.getRow(row).getCell(2).getContent();

    result = new UFDLJobOutput(pk, name, type);
    return result;
  }
}
