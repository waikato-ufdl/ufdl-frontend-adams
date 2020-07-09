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
 * UpdateLicenseSubDescriptors.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Condition;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Domain;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Limitation;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Updates the permissions, conditions and limitations of a license and forwards the license object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UpdateLicenseSubDescriptors
  extends AbstractLicenseTransformerAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the domains. */
  protected Domain[] m_Domains;
  
  /** the permissions. */
  protected Permission[] m_Permissions;

  /** the conditions. */
  protected Condition[] m_Conditions;

  /** the limitations. */
  protected Limitation[] m_Limitations;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Updates the domains, permissions, conditions and limitations of a license and forwards the license object.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "domain", "domains",
      new Domain[0]);

    m_OptionManager.add(
      "permission", "permissions",
      new Permission[0]);

    m_OptionManager.add(
      "condition", "conditions",
      new Condition[0]);

    m_OptionManager.add(
      "limitation", "limitations",
      new Limitation[0]);
  }

  /**
   * Sets the new domains.
   *
   * @param value	the domains
   */
  public void setDomains(Domain[] value) {
    m_Domains = value;
    reset();
  }

  /**
   * Returns the new domains.
   *
   * @return		the domains
   */
  public Domain[] getDomains() {
    return m_Domains;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String domainsTipText() {
    return "The new domains.";
  }

  /**
   * Sets the new permissions.
   *
   * @param value	the permissions
   */
  public void setPermissions(Permission[] value) {
    m_Permissions = value;
    reset();
  }

  /**
   * Returns the new permissions.
   *
   * @return		the permissions
   */
  public Permission[] getPermissions() {
    return m_Permissions;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String permissionsTipText() {
    return "The new permissions.";
  }

  /**
   * Sets the new conditions.
   *
   * @param value	the conditions
   */
  public void setConditions(Condition[] value) {
    m_Conditions = value;
    reset();
  }

  /**
   * Returns the new conditions.
   *
   * @return		the conditions
   */
  public Condition[] getConditions() {
    return m_Conditions;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String conditionsTipText() {
    return "The new conditions.";
  }

  /**
   * Sets the new limitations.
   *
   * @param value	the limitations
   */
  public void setLimitations(Limitation[] value) {
    m_Limitations = value;
    reset();
  }

  /**
   * Returns the new limitations.
   *
   * @return		the limitations
   */
  public Limitation[] getLimitations() {
    return m_Limitations;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String limitationsTipText() {
    return "The new limitations.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "domains", m_Domains, "domains: ");
    result += QuickInfoHelper.toString(this, "permissions", m_Permissions, ", permissions: ");
    result += QuickInfoHelper.toString(this, "conditions", m_Conditions, ", conditions: ");
    result += QuickInfoHelper.toString(this, "limitations", m_Limitations, ", limitations: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{License.class};
  }

  /**
   * Transforms the input data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doTransform(License license, MessageCollection errors) {
    License    		result;
    Set<Domain>		domAdd;
    Set<Domain>		domRem;
    Set<Permission>	permAdd;
    Set<Permission>	permRem;
    Set<Condition>	condAdd;
    Set<Condition>	condRem;
    Set<Limitation>	limAdd;
    Set<Limitation>	limRem;
    boolean		success;

    result = null;
    
    // domains
    domAdd = new HashSet<>(Arrays.asList(m_Domains));
    domAdd.removeAll(license.getDomains());
    domRem = new HashSet<>(license.getDomains());
    domRem.removeAll(Arrays.asList(m_Domains));
    
    // permissions
    permAdd = new HashSet<>(Arrays.asList(m_Permissions));
    permAdd.removeAll(license.getPermissions());
    permRem = new HashSet<>(license.getPermissions());
    permRem.removeAll(Arrays.asList(m_Permissions));
    
    // conditions
    condAdd = new HashSet<>(Arrays.asList(m_Conditions));
    condAdd.removeAll(license.getConditions());
    condRem = new HashSet<>(license.getConditions());
    condRem.removeAll(Arrays.asList(m_Conditions));
    
    // limitations
    limAdd = new HashSet<>(Arrays.asList(m_Limitations));
    limAdd.removeAll(license.getLimitations());
    limRem = new HashSet<>(license.getLimitations());
    limRem.removeAll(Arrays.asList(m_Limitations));

    if (isLoggingEnabled()) {
      getLogger().info("Remove: dom=" + domRem + ", perm=" + permRem + ", cond=" + condRem + ", lim=" + limRem);
      getLogger().info("Add: dom=" + domAdd + ", perm=" + permAdd + ", cond=" + condAdd + ", lim=" + limAdd);
    }
    
    success = true;
    if (!domRem.isEmpty() || !permRem.isEmpty() || !condRem.isEmpty() || !limRem.isEmpty()) {
      try {
	success = m_Client.licenses().removeSubDescriptors(
	  license,
	  domRem.toArray(new Domain[0]),
	  permRem.toArray(new Permission[0]),
	  condRem.toArray(new Condition[0]),
	  limRem.toArray(new Limitation[0]));
	if (!success)
	  errors.add("Failed to remove sub-descriptors from license (dom=" + domRem + ", perm=" + permRem + ", cond=" + condRem + ", lim=" + limRem + "): " + license);
      }
      catch (Exception e) {
	errors.add("Failed to remove sub-descriptors from license (dom=" + domRem + ", perm=" + permRem + ", cond=" + condRem + ", lim=" + limRem + "): " + license, e);
      }
    }
    
    if (success) {
      if (!domAdd.isEmpty() || !permAdd.isEmpty() || !condAdd.isEmpty() || !limAdd.isEmpty()) {
	try {
	  success = m_Client.licenses().addSubDescriptors(
	    license,
	    domAdd.toArray(new Domain[0]),
	    permAdd.toArray(new Permission[0]),
	    condAdd.toArray(new Condition[0]),
	    limAdd.toArray(new Limitation[0]));
	  if (!success)
	    errors.add("Failed to add sub-descriptors from license (dom=" + domAdd + ", perm=" + permAdd + ", cond=" + condAdd + ", lim=" + limAdd + "): " + license);
	}
	catch (Exception e) {
	  errors.add("Failed to add sub-descriptors from license (dom=" + domAdd + ", perm=" + permAdd + ", cond=" + condAdd + ", lim=" + limAdd + "): " + license, e);
	}
      }
    }

    // load license again
    if (success) {
      try {
	result = m_Client.licenses().load(license.getPK());
      }
      catch (Exception e) {
	errors.add("Failed to reload updated license: " + license, e);
      }
    }

    return result;
  }
}
