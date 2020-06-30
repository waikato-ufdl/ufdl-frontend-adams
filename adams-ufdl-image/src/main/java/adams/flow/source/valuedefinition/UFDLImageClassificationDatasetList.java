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
 * UFDLImageClassificationDatasetList.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.valuedefinition;

import adams.core.ClassCrossReference;
import adams.flow.transformer.UFDLExtractAndTransfer;
import com.github.fracpete.javautils.struct.Struct2;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * For selecting an image classification dataset.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLImageClassificationDatasetList
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
    return "For selecting an image classification dataset.";
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
    ImageClassificationDatasets  	action;

    result = new ArrayList<>();

    try {
      action = m_Connection.getClient().action(ImageClassificationDatasets.class);
      for (Dataset dataset : action.list()) {
        if (!m_State.accept(dataset))
          continue;
        result.add(new Struct2<>(dataset.getPK(), dataset.getName()));
      }
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to retrieve list of image classification datasets!", e);
    }

    return result;
  }
}
