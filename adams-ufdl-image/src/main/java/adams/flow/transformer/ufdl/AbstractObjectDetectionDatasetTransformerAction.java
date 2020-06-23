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
 * AbstractObjectDetectionDatasetTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;

/**
 * Ancestor for transformer actions that work on image classification datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractObjectDetectionDatasetTransformerAction
  extends AbstractDatasetTransformerAction<ObjectDetectionDatasets> {

  private static final long serialVersionUID = 4566670026767066812L;

  /**
   * Returns the datasets action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  @Override
  protected ObjectDetectionDatasets getDatasetsAction() throws Exception {
    return m_Client.action(ObjectDetectionDatasets.class);
  }
}
