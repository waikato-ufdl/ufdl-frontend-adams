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
 * UFDLDatasetList.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.ClassCrossReference;
import adams.flow.transformer.UFDLExtractAndTransferPK;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * For selecting a UFDL dataset.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLDatasetList
  extends AbstractUFDLSoftDeleteListValueDefinition
  implements ClassCrossReference {

  private static final long serialVersionUID = 4093023607556720026L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "For selecting a UFDL dataset.";
  }

  /**
   * Returns the cross-referenced classes.
   *
   * @return		the classes
   */
  @Override
  public Class[] getClassCrossReferences() {
    return new Class[]{UFDLExtractAndTransferPK.class};
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
      for (Dataset dataset : m_Connection.getClient().datasets().list()) {
        if (!m_State.accept(dataset))
          continue;
        result.add(new Struct2<>(dataset.getPK(), dataset.getName()));
      }
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to retrieve list of datasets!", e);
    }

    return result;
  }
}
