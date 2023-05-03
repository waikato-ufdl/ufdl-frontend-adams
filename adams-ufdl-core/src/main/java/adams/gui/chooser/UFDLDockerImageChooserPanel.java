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
 * UFDLDockerImageChooserPanel.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.gui.chooser;

import adams.core.MessageCollection;
import adams.data.conversion.AbstractUFDLObjectToSpreadSheetConversion;
import adams.data.conversion.UFDLDockerImageToSpreadSheet;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;

import java.util.List;

/**
 * Allows the user to select docker images.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLDockerImageChooserPanel
  extends AbstractUFDLSpreadSheetBasedChooserPanel<DockerImage> {

  private static final long serialVersionUID = -5162524212611793388L;

  /** the task to require (ignored if null). */
  protected String m_Task;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Task = null;
  }

  /**
   * Sets the task to filter by.
   *
   * @param value	the task, ignored if null
   */
  public void setTask(String value) {
    m_Task = value;
  }

  /**
   * Returns the task to filter by.
   *
   * @return		the task, null if none set
   */
  public String getTask() {
    return m_Task;
  }

  /**
   * Turns the object into a struct (ID and string).
   *
   * @param value	the object to convert
   * @return		the generated struct
   */
  @Override
  protected Struct2<Integer, String> toStruct(DockerImage value) {
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
  protected DockerImage loadObject(int pk) throws Exception {
    return m_Connection.getClient().docker().load(pk);
  }

  /**
   * Creates a new array of the specified object.
   *
   * @param len		the length of the array
   * @return		the array
   */
  @Override
  protected DockerImage[] newArray(int len) {
    return new DockerImage[len];
  }

  /**
   * Returns the conversion to use for generating a spreadsheet from an object.
   *
   * @return		the conversion
   */
  @Override
  protected AbstractUFDLObjectToSpreadSheetConversion getConversion() {
    return new UFDLDockerImageToSpreadSheet();
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
  protected int getPK(DockerImage object) {
    return object.getPK();
  }

  /**
   * Returns all available objects.
   *
   * @return		the objects
   * @throws Exception	if API call fails
   */
  @Override
  protected DockerImage[] getAvailableObjects() throws Exception {
    List<DockerImage> 	result;
    int			i;
    boolean		keep;

    result = m_Connection.getClient().docker().list(m_Filter.generate(new MessageCollection()));

    if (m_Task != null) {
      i = 0;
      while (i < result.size()) {
        keep = false;
        for (String task: result.get(i).getTasks()) {
          if (task.equals(m_Task)) {
            keep = true;
            break;
	  }
	}
        if (keep)
          i++;
        else
          result.remove(i);
      }
    }

    return result.toArray(new DockerImage[0]);
  }

  /**
   * Returns the title for the dialog.
   *
   * @return		the title
   */
  @Override
  protected String getDialogTitle() {
    if (m_MultiSelection)
      return "Select docker images";
    else
      return "Select docker image";
  }
}
