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
 * UFDLJobTemplateChooserPanel.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.core.MessageCollection;
import adams.data.conversion.AbstractUFDLObjectToSpreadSheetConversion;
import adams.data.conversion.UFDLJobTemplateToSpreadSheet;
import adams.data.ufdlfilter.AbstractUFDLFilter;
import adams.data.ufdlfilter.GenericFilter;
import adams.data.ufdlfilter.OrderBy;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;

/**
 * Allows the user to select job templates.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobTemplateChooserPanel
  extends AbstractUFDLSpreadSheetBasedSoftDeleteChooserPanel<JobTemplate> {

  private static final long serialVersionUID = -5162524212611793388L;

  /**
   * Returns the default filter.
   *
   * @return		the default
   */
  protected AbstractUFDLFilter getDefaultFilter() {
    GenericFilter 	result;

    result = new GenericFilter();
    result.setOrder(new OrderBy[]{new OrderBy("name")});

    return result;
  }

  /**
   * Turns the object into a struct (ID and string).
   *
   * @param value	the object to convert
   * @return		the generated struct
   */
  @Override
  protected Struct2<Integer, String> toStruct(JobTemplate value) {
    return new Struct2<>(value.getPK(), value.getShortDescription());
  }

  /**
   * Loads the object based on the ID.
   *
   * @param pk		the ID to load
   * @return		the object, null if failed to load
   * @throws Exception	if loading failed
   */
  @Override
  protected JobTemplate loadObject(int pk) throws Exception {
    return m_Connection.getClient().jobTemplates().load(pk);
  }

  /**
   * Creates a new array of the specified object.
   *
   * @param len		the length of the array
   * @return		the array
   */
  @Override
  protected JobTemplate[] newArray(int len) {
    return new JobTemplate[len];
  }

  /**
   * Returns the conversion to use for generating a spreadsheet from an object.
   *
   * @return		the conversion
   */
  @Override
  protected AbstractUFDLObjectToSpreadSheetConversion getConversion() {
    return new UFDLJobTemplateToSpreadSheet();
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
  protected int getPK(JobTemplate object) {
    return object.getPK();
  }

  /**
   * Returns all available objects.
   *
   * @return		the objects
   * @throws Exception	if API call fails
   */
  @Override
  protected JobTemplate[] getAvailableObjects() throws Exception {
    return m_Connection.getClient().jobTemplates().list(m_Filter.generate(new MessageCollection())).toArray(new JobTemplate[0]);
  }

  /**
   * Returns the title for the dialog.
   *
   * @return		the title
   */
  @Override
  protected String getDialogTitle() {
    if (m_MultiSelection)
      return "Select job templates";
    else
      return "Select job template";
  }
}
