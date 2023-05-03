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
 * UFDLContractType.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.core;

import com.github.waikatoufdl.ufdl4j.contract.AbstractContract;

/**
 * The types of available job contracts.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 * @see AbstractContract#getContractNames()
 */
public enum UFDLContractType {
  TRAIN("Train"),
  PREDICT("Predict");

  /** the underlying name. */
  private String m_Name;

  /**
   * Initializes the enum item.
   *
   * @param name	the underlying name
   */
  private UFDLContractType(String name) {
    m_Name = name;
  }

  /**
   * Returns the underlying contract name.
   *
   * @return		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Returns the contract associated with the name.
   * 
   * @return		the contact
   * @see		AbstractContract#forName(String)
   */
  public AbstractContract getContract() {
    return AbstractContract.forName(m_Name);
  }
}
