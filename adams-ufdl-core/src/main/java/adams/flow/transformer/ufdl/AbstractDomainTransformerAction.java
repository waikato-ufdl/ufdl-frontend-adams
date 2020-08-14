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
 * AbstractDomainTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Domains;
import com.github.waikatoufdl.ufdl4j.action.Domains.Domain;

/**
 * Ancestor of transformer actions on domains.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of Domains action
 */
public abstract class AbstractDomainTransformerAction<T extends Domains>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, Domain.class};
  }

  /**
   * Returns the domains action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getDomainsAction() throws Exception {
    return (T) m_Client.action(Domains.class);
  }

  /**
   * Transforms the domain.
   *
   * @param domain	the domain
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Domain domain, MessageCollection errors);

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
    Domain 	domain;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming domain: " + input);

    // load domain
    domain = null;
    try {
      action = getDomainsAction();
      if (input instanceof Integer)
	domain = action.load((Integer) input);
      else
	domain = (Domain) input;
    }
    catch (Exception e) {
      errors.add("Failed to load domain: " + input, e);
    }

    if (domain == null)
      errors.add("Unknown domain: " + input);
    else
      result = doTransform(domain, errors);

    return result;
  }
}
