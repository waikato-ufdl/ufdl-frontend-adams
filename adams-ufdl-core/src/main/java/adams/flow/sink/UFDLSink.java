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
 * UFDLSink.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.sink;

import adams.core.AdditionalInformationHandler;
import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.Utils;
import adams.flow.core.ActorUtils;
import adams.flow.sink.ufdl.AbstractUFDLSinkAction;
import adams.flow.sink.ufdl.Null;
import adams.flow.standalone.UFDLConnection;

/**
 <!-- globalinfo-start -->
 * Forwards the data to the specified action.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;adams.flow.core.Unknown<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: UFDLSink
 * </pre>
 *
 * <pre>-annotation &lt;adams.core.base.BaseAnnotation&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-skip &lt;boolean&gt; (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded
 * &nbsp;&nbsp;&nbsp;as it is.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-stop-flow-on-error &lt;boolean&gt; (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow execution at this level gets stopped in case this
 * &nbsp;&nbsp;&nbsp;actor encounters an error; the error gets propagated; useful for critical
 * &nbsp;&nbsp;&nbsp;actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console; Note: the enclosing
 * &nbsp;&nbsp;&nbsp;actor handler must have this enabled as well.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-action &lt;adams.flow.sink.ufdl.AbstractUFDLSinkAction&gt; (property: action)
 * &nbsp;&nbsp;&nbsp;The UFDL action to use.
 * &nbsp;&nbsp;&nbsp;default: adams.flow.sink.ufdl.Null
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLSink
  extends AbstractSink
  implements AdditionalInformationHandler {

  private static final long serialVersionUID = -2779693911370733238L;

  /** the action to use. */
  protected AbstractUFDLSinkAction m_Action;

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Forwards the data to the specified action.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "action", "action",
      new Null());
  }

  /**
   * Resets the scheme.
   */
  @Override
  protected void reset() {
    super.reset();

    m_Connection = null;
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "action", m_Action);
  }

  /**
   * Sets the action to use.
   *
   * @param value	the action
   */
  public void setAction(AbstractUFDLSinkAction value) {
    m_Action = value;
    reset();
  }

  /**
   * Returns the action in use.
   *
   * @return		the host
   */
  public AbstractUFDLSinkAction getAction() {
    return m_Action;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String actionTipText() {
    return "The UFDL action to use.";
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  @Override
  public Class[] accepts() {
    return m_Action.accepts();
  }

  /**
   * Returns the additional information.
   *
   * @return		the additional information
   */
  @Override
  public String getAdditionalInformation() {
    StringBuilder	result;

    result = new StringBuilder(super.getAdditionalInformation());

    if (m_Action instanceof AdditionalInformationHandler) {
      result.append("\n\n");
      result.append(((AdditionalInformationHandler) m_Action).getAdditionalInformation());
    }

    return result.toString();
  }

  /**
   * Initializes the item for flow execution.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  public String setUp() {
    String		result;

    result = super.setUp();

    if (result == null) {
      m_Connection = (UFDLConnection) ActorUtils.findClosestType(this, UFDLConnection.class, true);
      if (m_Connection == null)
        result = "Failed to locate an instance of " + Utils.classToString(UFDLConnection.class) + "!";
    }

    return result;
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String		result;
    MessageCollection	errors;

    result = null;
    errors = new MessageCollection();
    try {
      m_Action.setClient(m_Connection.getClient());
      m_Action.setFlowContext(this);
      m_Action.consume(m_InputToken.getPayload(), errors);
    }
    catch (Exception e) {
      errors.add("Failed to consume input data!", e);
    }

    if (!errors.isEmpty())
      result = errors.toString();

    return result;
  }
}
