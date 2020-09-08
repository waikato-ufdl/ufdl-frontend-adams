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
 * UserCache.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.standalone.ufdlcache;

import adams.flow.standalone.UFDLConnection;
import com.github.waikatoufdl.ufdl4j.action.Users.User;

/**
 * Cache for users.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UserCache
  extends AbstractIDResolutionCache<User> {

  private static final long serialVersionUID = 3509541588705592939L;

  /**
   * Initializes the cache.
   *
   * @param connection	the connection to use
   * @param timeToLive the expiry in seconds (< 1 = no expiry)
   */
  public UserCache(UFDLConnection connection, int timeToLive) {
    super(connection, timeToLive);
  }

  /**
   * Initializes the cache.
   *
   * @throws Exception 	if initialization fails
   */
  @Override
  protected void doInitialize() throws Exception {
    for (User obj : m_Connection.getClient().users().list())
      m_Cache.put(obj.getPK(), obj.getShortDescription());
  }
}
