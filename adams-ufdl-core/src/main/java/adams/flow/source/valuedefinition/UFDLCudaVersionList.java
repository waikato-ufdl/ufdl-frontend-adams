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
 * UFDLCudaVersionList.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.ClassCrossReference;
import adams.flow.transformer.UFDLExtractAndTransfer;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.CudaVersions.CudaVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * For selecting a UFDL cuda version.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLCudaVersionList
  extends AbstractUFDLListValueDefinition
  implements ClassCrossReference {

  private static final long serialVersionUID = 4093023607556720026L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "For selecting a UFDL cuda version.";
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
      for (CudaVersion cuda : m_Connection.getClient().cuda().list())
        result.add(new Struct2<>(cuda.getPK(), cuda.getVersion()));
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to retrieve list of cuda versions!", e);
    }

    return result;
  }
}
