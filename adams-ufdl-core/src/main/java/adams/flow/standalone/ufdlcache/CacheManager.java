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
 * CacheManager.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.standalone.ufdlcache;

import adams.core.logging.LoggingObject;
import adams.flow.standalone.UFDLConnection;

/**
 * Manages all ID caches.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CacheManager
  extends LoggingObject {

  private static final long serialVersionUID = 5541190793124408610L;

  /** the underlying connection. */
  protected UFDLConnection m_Connection;

  /** the expiry in seconds. */
  protected int m_TimeToLive;

  /** cuda versions. */
  protected CudaVersionCache m_Cuda;

  /** docker images. */
  protected DockerImageCache m_Docker;

  /** frameworks. */
  protected FrameworkCache m_Frameworks;

  /** hardware generations. */
  protected HardwareGenerationCache m_Hardware;

  /** job templates. */
  protected JobTemplateCache m_JobTemplates;

  /** licenses. */
  protected LicenseCache m_Licenses;

  /** projects. */
  protected ProjectCache m_Projects;

  /** teams. */
  protected TeamCache m_Teams;

  /** users. */
  protected UserCache m_Users;

  /**
   * Initializes the cache manager.
   *
   * @param connection	the connection to use
   * @param timeToLive	the expiry in seconds (< 1 no expiry)
   */
  public CacheManager(UFDLConnection connection, int timeToLive) {
    m_Connection = connection;
    m_TimeToLive = timeToLive;

    m_Cuda         = new CudaVersionCache(getConnection(), getTimeToLive());
    m_Docker       = new DockerImageCache(getConnection(), getTimeToLive());
    m_Frameworks   = new FrameworkCache(getConnection(), getTimeToLive());
    m_Hardware     = new HardwareGenerationCache(getConnection(), getTimeToLive());
    m_JobTemplates = new JobTemplateCache(getConnection(), getTimeToLive());
    m_Licenses     = new LicenseCache(getConnection(), getTimeToLive());
    m_Projects     = new ProjectCache(getConnection(), getTimeToLive());
    m_Teams        = new TeamCache(getConnection(), getTimeToLive());
    m_Users        = new UserCache(getConnection(), getTimeToLive());
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
   * Returns the cache for CUDA versions.
   *
   * @return		the cache
   */
  public CudaVersionCache cuda() {
    return m_Cuda;
  }

  /**
   * Returns the cache for docker images.
   *
   * @return		the cache
   */
  public DockerImageCache docker() {
    return m_Docker;
  }

  /**
   * Returns the cache for frameworks.
   *
   * @return		the cache
   */
  public FrameworkCache frameworks() {
    return m_Frameworks;
  }

  /**
   * Returns the cache for hardware generations.
   *
   * @return		the cache
   */
  public HardwareGenerationCache hardware() {
    return m_Hardware;
  }

  /**
   * Returns the cache for job templates.
   *
   * @return		the cache
   */
  public JobTemplateCache jobTemplates() {
    return m_JobTemplates;
  }

  /**
   * Returns the cache for licenses.
   *
   * @return		the cache
   */
  public LicenseCache licenses() {
    return m_Licenses;
  }

  /**
   * Returns the cache for projects.
   *
   * @return		the cache
   */
  public ProjectCache projects() {
    return m_Projects;
  }

  /**
   * Returns the cache for teams.
   *
   * @return		the cache
   */
  public TeamCache teams() {
    return m_Teams;
  }

  /**
   * Returns the cache for users.
   *
   * @return		the cache
   */
  public UserCache users() {
    return m_Users;
  }
}
