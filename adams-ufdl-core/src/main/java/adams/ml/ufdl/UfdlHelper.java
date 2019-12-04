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
 * UfdlHelper.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package adams.ml.ufdl;

import adams.core.Properties;
import adams.core.base.BasePassword;
import adams.env.Environment;
import adams.env.UfdlDefinition;

/**
 * Helper class for LaTeX setup.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UfdlHelper {

  /** the name of the props file. */
  public final static String FILENAME = "UFDL.props";

  /** the host. */
  public final static String HOST = "Host";

  /** the user. */
  public final static String USER = "User";

  /** the password. */
  public final static String PASSWORD = "Password";

  /** the properties. */
  protected static Properties m_Properties;

  /**
   * Returns the underlying properties.
   *
   * @return		the properties
   */
  public synchronized static Properties getProperties() {
    if (m_Properties == null) {
      try {
	m_Properties = Environment.getInstance().read(UfdlDefinition.KEY);
      }
      catch (Exception e) {
	m_Properties = new Properties();
      }
    }

    return m_Properties;
  }

  /**
   * Writes the specified properties to disk.
   *
   * @return		true if successfully stored
   */
  public synchronized static boolean writeProperties() {
    return writeProperties(getProperties());
  }

  /**
   * Writes the specified properties to disk.
   *
   * @param props	the properties to write to disk
   * @return		true if successfully stored
   */
  public synchronized static boolean writeProperties(Properties props) {
    boolean	result;

    result = Environment.getInstance().write(UfdlDefinition.KEY, props);
    // require reload
    m_Properties = null;

    return result;
  }

  /**
   * Returns the host.
   *
   * @return		the host
   */
  public static String getHost() {
    return getProperties().getPath(HOST, "http://localhost:8000");
  }

  /**
   * Returns the user.
   *
   * @return		the user
   */
  public static String getUser() {
    return getProperties().getProperty(USER, "");
  }

  /**
   * Returns the password.
   *
   * @return		the password
   */
  public static BasePassword getPassword() {
    return getProperties().getPassword(PASSWORD, new BasePassword());
  }
}
