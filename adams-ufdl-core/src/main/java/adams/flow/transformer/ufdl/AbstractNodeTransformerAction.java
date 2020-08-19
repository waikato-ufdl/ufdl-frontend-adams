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
 * AbstractNodeTransformerAction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import com.github.waikatoufdl.ufdl4j.action.Nodes;
import com.github.waikatoufdl.ufdl4j.action.Nodes.Node;

/**
 * Ancestor of transformer actions on nodes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @param <T> the type of Nodes action
 */
public abstract class AbstractNodeTransformerAction<T extends Nodes>
  extends AbstractUFDLTransformerAction {

  private static final long serialVersionUID = 1320770985737432995L;
  /**
   * Returns the classes that the transformer accepts.
   *
   * @return		the classes
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, String.class, Node.class};
  }

  /**
   * Returns the nodes action to use.
   *
   * @return		the action
   * @throws Exception	if instantiation of action fails
   */
  protected T getNodesAction() throws Exception {
    return (T) m_Client.action(Nodes.class);
  }

  /**
   * Transforms the node.
   *
   * @param node	the node
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  protected abstract Object doTransform(Node node, MessageCollection errors);

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
    Node 	node;
    T		action;

    result = null;

    if (isLoggingEnabled())
      getLogger().info("Transforming node: " + input);

    // load node
    node = null;
    try {
      action = getNodesAction();
      if (input instanceof Integer)
	node = action.load((Integer) input);
      else if (input instanceof String)
	node = action.load((String) input);
      else
	node = (Node) input;
    }
    catch (Exception e) {
      errors.add("Failed to load node: " + input, e);
    }

    if (node == null)
      errors.add("Unknown node: " + input);
    else
      result = doTransform(node, errors);

    return result;
  }
}
