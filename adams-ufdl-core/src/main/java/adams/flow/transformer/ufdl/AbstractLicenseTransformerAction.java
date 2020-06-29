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
 * AbstractLicenseTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

/**
 * Ancestor for transformer actions on licenses.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractLicenseTransformerAction
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 2890424326502728143L;

  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, License.class};
  }

  /**
   * Transforms the license.
   *
   * @param license	the license
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(License license, MessageCollection errors);

  /**
   * Transforms the input data.
   *
   * @param input	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Object input, MessageCollection errors) {
    Object	result;
    License	license;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming license: " + input);

    // load license
    license = null;
    try {
      if (input instanceof Integer)
	license = m_Client.licenses().load((Integer) input);
      else if (input instanceof String)
	license = m_Client.licenses().load("" + input);
      else
        license = (License) input;
    }
    catch (Exception e) {
      errors.add("Failed to load license: " + input, e);
    }

    if (license == null)
      errors.add("Unknown license: " + input);
    else
      result = doTransform(license, errors);

    return result;
  }
}
