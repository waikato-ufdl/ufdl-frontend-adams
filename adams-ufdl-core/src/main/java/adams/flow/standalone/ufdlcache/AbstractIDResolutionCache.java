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
 * AbstractIDResolutionCache.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.standalone.ufdlcache;

import adams.core.logging.LoggingObject;
import adams.flow.standalone.UFDLConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Ancestor for ID resolution caches.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the object this cache is for
 */
public abstract class AbstractIDResolutionCache<T>
  extends LoggingObject {

  private static final long serialVersionUID = -4608863864715020767L;

  /** the cache (ID -> string). */
  protected Map<Integer,String> m_Cache;

  /** the connection to use. */
  protected UFDLConnection m_Connection;

  /** the expiry in seconds. */
  protected int m_TimeToLive;

  /** when the last cache rebuild happened. */
  protected long m_LastRebuild;

  /**
   * Initializes the cache.
   *
   * @param timeToLive 	the expiry in seconds (< 1 = no expiry)
   */
  protected AbstractIDResolutionCache(UFDLConnection connection, int timeToLive) {
    super();
    m_Connection  = connection;
    m_Cache       = new HashMap<>();
    m_TimeToLive  = timeToLive;
    m_LastRebuild = 0;
  }

  /**
   * Returns the connection in use.
   *
   * @return		the connection
   */
  public UFDLConnection getConnection() {
    return m_Connection;
  }

  /**
   * Returns the time to live.
   *
   * @return		the expiry in seconds (< 1 = no expiry)
   */
  public int getTimeToLive() {
    return m_TimeToLive;
  }

  /**
   * Clears the cache.
   */
  public void clear() {
    m_Cache.clear();
  }

  /**
   * Returns the size of the cache.
   *
   * @return		the size
   */
  public int size() {
    return m_Cache.size();
  }

  /**
   * Initializes the cache.
   *
   * @throws Exception 	if initialization fails
   */
  protected abstract void doInitialize() throws Exception;

  /**
   * Initializes the cache.
   */
  protected void initialize() {
    try {
      doInitialize();
    }
    catch (Exception e) {
      m_Cache.clear();
      getLogger().log(Level.SEVERE, "Failed to initialized cache!", e);
    }
    m_LastRebuild = System.currentTimeMillis();
  }

  /**
   * Resolves the ID into a string.
   *
   * @param id		the ID to resolve
   * @return		the resolved string
   */
  public String resolve(int id) {
    String	result;

    if (id == -1)
      return "" + id;

    if (m_LastRebuild + m_TimeToLive*1000 <= System.currentTimeMillis())
      initialize();

    result = m_Cache.get(id);
    if (result == null) {
      result = "" + id;
      m_Cache.put(id, result);
    }

    return result;
  }
}
