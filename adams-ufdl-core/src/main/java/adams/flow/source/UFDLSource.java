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

package adams.flow.source;

import adams.core.AdditionalInformationHandler;
import adams.core.DateUtils;
import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.Utils;
import adams.data.text.TextContainer;
import adams.flow.control.StorageName;
import adams.flow.control.StorageQueueHandler;
import adams.flow.control.StorageUpdater;
import adams.flow.core.ActorUtils;
import adams.flow.core.QueueHelper;
import adams.flow.core.Token;
import adams.flow.source.ufdl.AbstractUFDLSourceAction;
import adams.flow.source.ufdl.Null;
import adams.flow.standalone.UFDLConnection;

import java.util.Date;

/**
 <!-- globalinfo-start -->
 * Forwards the data generate by the specified action.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - generates:<br>
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
 * &nbsp;&nbsp;&nbsp;default: UFDLSource
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
 * <pre>-action &lt;adams.flow.source.ufdl.AbstractUFDLSourceAction&gt; (property: action)
 * &nbsp;&nbsp;&nbsp;The UFDL action to use.
 * &nbsp;&nbsp;&nbsp;default: adams.flow.source.ufdl.Null
 * </pre>
 *
 * <pre>-use-error-queue &lt;boolean&gt; (property: useErrorQueue)
 * &nbsp;&nbsp;&nbsp;If enabled, forwards the errors from the action to the specified queue.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-error-queue &lt;adams.flow.control.StorageName&gt; (property: errorQueue)
 * &nbsp;&nbsp;&nbsp;The name of the queue in internal storage to forward the errors to.
 * &nbsp;&nbsp;&nbsp;default: errors
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLSource
  extends AbstractSimpleSource
  implements AdditionalInformationHandler, StorageUpdater {

  private static final long serialVersionUID = -2779693911370733238L;

  /** the action to use. */
  protected AbstractUFDLSourceAction m_Action;

  /** whether to forward errors to the error queue. */
  protected boolean m_UseErrorQueue;

  /** the name of the error queue in the internal storage. */
  protected StorageName m_ErrorQueue;

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Forwards the data generate by the specified action.";
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

    m_OptionManager.add(
      "use-error-queue", "useErrorQueue",
      false);

    m_OptionManager.add(
      "error-queue", "errorQueue",
      new StorageName("errors"));
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
    String  	result;

    result = QuickInfoHelper.toString(this, "action", m_Action);

    if (m_UseErrorQueue)
      result += QuickInfoHelper.toString(this, "errorQueue", m_ErrorQueue, ", error queue: ");

    return result;
  }

  /**
   * Sets the action to use.
   *
   * @param value	the action
   */
  public void setAction(AbstractUFDLSourceAction value) {
    m_Action = value;
    reset();
  }

  /**
   * Returns the action in use.
   *
   * @return		the host
   */
  public AbstractUFDLSourceAction getAction() {
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
   * Sets whether to forward the errors from the action to the specified queue.
   *
   * @param value	true if to forward
   */
  public void setUseErrorQueue(boolean value) {
    m_UseErrorQueue = value;
    reset();
  }

  /**
   * Returns whether to forward the errors from the action to the specified queue.
   *
   * @return		true if to forward
   */
  public boolean getUseErrorQueue() {
    return m_UseErrorQueue;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String useErrorQueueTipText() {
    return "If enabled, forwards the errors from the action to the specified queue.";
  }

  /**
   * Sets the name for the queue in internal storage to forward the errors to.
   *
   * @param value	the name
   */
  public void setErrorQueue(StorageName value) {
    m_ErrorQueue = value;
    reset();
  }

  /**
   * Returns the name for the queue in internal storage to forward the errors to.
   *
   * @return		the name
   */
  public StorageName getErrorQueue() {
    return m_ErrorQueue;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String errorQueueTipText() {
    return "The name of the queue in internal storage to forward the errors to.";
  }

  /**
   * Returns whether storage items are being updated.
   *
   * @return		true if storage items are updated
   */
  public boolean isUpdatingStorage() {
    return m_UseErrorQueue;
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		the Class of the generated tokens
   */
  @Override
  public Class[] generates() {
    return m_Action.generates();
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

    result.append("\n\n");
    result.append("If enabled, errors get forwarded as ").append(Utils.classToString(TextContainer.class)).append(", with the error as the content.\n");
    result.append("The 'Actor' field in the report contains the actor that generated the error, the 'Action' field the full command-line of the action and 'Timestamp' the date/time of the error.");

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
    Object		output;
    MessageCollection	errors;
    StorageQueueHandler queue;
    TextContainer	cont;

    result = null;
    errors = new MessageCollection();
    try {
      m_Action.setClient(m_Connection.getClient());
      m_Action.setFlowContext(this);
      output = m_Action.generate(errors);
      if ((output != null) && errors.isEmpty())
        m_OutputToken = new Token(output);
    }
    catch (Exception e) {
      errors.add("Failed to generate output data!", e);
    }

    if (!errors.isEmpty()) {
      result = errors.toString();

      if (m_UseErrorQueue) {
        queue = QueueHelper.getQueue(this, m_ErrorQueue);
        if (queue != null) {
          cont = new TextContainer();
          cont.setContent(errors.toString());
          cont.setID(getFullName());
          cont.getReport().setStringValue("Actor", getFullName());
          cont.getReport().setStringValue("Action", m_Action.toCommandLine());
          cont.getReport().setStringValue("Timestamp", DateUtils.getTimestampFormatterMsecs().format(new Date()));
          queue.add(cont);
        }
        else {
          getLogger().warning("Error queue not found: " + m_ErrorQueue);
        }
      }
    }

    return result;
  }
}
