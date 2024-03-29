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
 * UFDLConnection.java
 * Copyright (C) 2019-2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.standalone;

import adams.core.PasswordPrompter;
import adams.core.QuickInfoHelper;
import adams.core.base.BasePassword;
import adams.core.base.BaseURL;
import adams.core.io.ConsoleHelper;
import adams.flow.control.Flow;
import adams.flow.core.OptionalPasswordPrompt;
import adams.flow.core.StopHelper;
import adams.flow.core.StopMode;
import adams.flow.standalone.ufdlcache.CacheManager;
import adams.gui.dialog.PasswordDialog;
import adams.ml.ufdl.UfdlHelper;
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.auth.LocalStorage;

import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Defines the connection to the UFDL backend API.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
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
 * &nbsp;&nbsp;&nbsp;default: UFDLConnection
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
 * <pre>-host &lt;adams.core.base.BaseURL&gt; (property: host)
 * &nbsp;&nbsp;&nbsp;The UFDL backend API host to use.
 * &nbsp;&nbsp;&nbsp;default: http:&#47;&#47;localhost:8000
 * </pre>
 *
 * <pre>-user &lt;java.lang.String&gt; (property: user)
 * &nbsp;&nbsp;&nbsp;The SMTP user to use.
 * </pre>
 *
 * <pre>-password &lt;adams.core.base.BasePassword&gt; (property: password)
 * &nbsp;&nbsp;&nbsp;The password of the SMTP user.
 * </pre>
 *
 * <pre>-prompt-for-password &lt;boolean&gt; (property: promptForPassword)
 * &nbsp;&nbsp;&nbsp;If enabled and authentication is required, the user gets prompted for enter
 * &nbsp;&nbsp;&nbsp;a password if none has been provided in the setup.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-stop-if-canceled &lt;boolean&gt; (property: stopFlowIfCanceled)
 * &nbsp;&nbsp;&nbsp;If enabled, the flow gets stopped in case the user cancels the dialog.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-custom-stop-message &lt;java.lang.String&gt; (property: customStopMessage)
 * &nbsp;&nbsp;&nbsp;The custom stop message to use in case a user cancelation stops the flow
 * &nbsp;&nbsp;&nbsp;(default is the full name of the actor)
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-stop-mode &lt;GLOBAL|STOP_RESTRICTOR&gt; (property: stopMode)
 * &nbsp;&nbsp;&nbsp;The stop mode to use.
 * &nbsp;&nbsp;&nbsp;default: GLOBAL
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLConnection
  extends AbstractStandalone
  implements OptionalPasswordPrompt, PasswordPrompter {

  private static final long serialVersionUID = -4863486410407933666L;

  /** the host. */
  protected BaseURL m_Host;

  /** the SMTP user to use. */
  protected String m_User;

  /** the SMTP password to use. */
  protected BasePassword m_Password;

  /** the actual SMTP password to use. */
  protected BasePassword m_ActualPassword;

  /** whether to prompt the user for a password if none provided. */
  protected boolean m_PromptForPassword;

  /** the connect timeout. */
  protected int m_ConnectTimeout;

  /** the read timeout. */
  protected int m_ReadTimeout;

  /** the write timeout. */
  protected int m_WriteTimeout;

  /** whether to stop the flow if canceled. */
  protected boolean m_StopFlowIfCanceled;

  /** the custom stop message to use if flow gets stopped due to cancelation. */
  protected String m_CustomStopMessage;

  /** how to perform the stop. */
  protected StopMode m_StopMode;

  /** the expiry in seconds for the ID cache. */
  protected int m_TimeToLive;

  /** the client initialized with the details. */
  protected transient Client m_Client;

  /** the cache manager. */
  protected transient CacheManager m_CacheManager;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Defines the connection to the UFDL backend API.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "host", "host",
      new BaseURL(UfdlHelper.getHost()));

    m_OptionManager.add(
      "user", "user",
      UfdlHelper.getUser(), false);

    m_OptionManager.add(
      "password", "password",
      UfdlHelper.getPassword(), false);

    m_OptionManager.add(
      "prompt-for-password", "promptForPassword",
      false);

    m_OptionManager.add(
      "stop-if-canceled", "stopFlowIfCanceled",
      false);

    m_OptionManager.add(
      "custom-stop-message", "customStopMessage",
      "");

    m_OptionManager.add(
      "stop-mode", "stopMode",
      StopMode.GLOBAL);

    m_OptionManager.add(
      "connect-timeout", "connectTimeout",
      30, -1, null);

    m_OptionManager.add(
      "read-timeout", "readTimeout",
      120, -1, null);

    m_OptionManager.add(
      "write-timeout", "writeTimeout",
      30, -1, null);

    m_OptionManager.add(
      "time-to-live", "timeToLive",
      60, -1, null);
  }

  /**
   * Resets the scheme.
   */
  @Override
  protected void reset() {
    super.reset();

    m_Client       = null;
    m_CacheManager = null;
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String		result;
    List<String> 	options;
    String		value;

    result = QuickInfoHelper.toString(this, "user", m_User);
    value = QuickInfoHelper.toString(this, "password", m_Password.getValue().replaceAll(".", "*"));
    if (value != null)
      result += ":" + value;
    result += "@";

    result += QuickInfoHelper.toString(this, "host", m_Host);

    options = new ArrayList<>();
    if ( (QuickInfoHelper.hasVariable(this, "promptForPassword") || m_PromptForPassword) ) {
      QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "promptForPassword", m_PromptForPassword, "prompt for password"));
      QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "stopFlowIfCanceled", m_StopFlowIfCanceled, "stop flow"));
    }
    result += QuickInfoHelper.flatten(options);

    return result;
  }

  /**
   * Sets the host to use.
   *
   * @param value	the host
   */
  public void setHost(BaseURL value) {
    m_Host = value;
    reset();
  }

  /**
   * Returns the host in use.
   *
   * @return		the host
   */
  public BaseURL getHost() {
    return m_Host;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String hostTipText() {
    return "The UFDL backend API host to use.";
  }

  /**
   * Sets the UFDL user to use.
   *
   * @param value	the user name
   */
  public void setUser(String value) {
    m_User = value;
    reset();
  }

  /**
   * Returns the UFDL user name to use.
   *
   * @return		the user name
   */
  public String getUser() {
    return m_User;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String userTipText() {
    return "The UFDL user to use.";
  }

  /**
   * Sets the SMTP password to use.
   *
   * @param value	the password
   */
  public void setPassword(BasePassword value) {
    m_Password = value;
    reset();
  }

  /**
   * Returns the SMTP password to use.
   *
   * @return		the password
   */
  public BasePassword getPassword() {
    return m_Password;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String passwordTipText() {
    return "The password of the SMTP user.";
  }

  /**
   * Sets whether to prompt for a password if none currently provided.
   *
   * @param value	true if to prompt for a password
   */
  public void setPromptForPassword(boolean value) {
    m_PromptForPassword = value;
    reset();
  }

  /**
   * Returns whether to prompt for a password if none currently provided.
   *
   * @return		true if to prompt for a password
   */
  public boolean getPromptForPassword() {
    return m_PromptForPassword;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String promptForPasswordTipText() {
    return
      "If enabled and authentication is required, the user gets prompted "
	+ "for enter a password if none has been provided in the setup.";
  }

  /**
   * Sets whether to stop the flow if dialog canceled.
   *
   * @param value	if true flow gets stopped if dialog canceled
   */
  public void setStopFlowIfCanceled(boolean value) {
    m_StopFlowIfCanceled = value;
    reset();
  }

  /**
   * Returns whether to stop the flow if dialog canceled.
   *
   * @return 		true if the flow gets stopped if dialog canceled
   */
  public boolean getStopFlowIfCanceled() {
    return m_StopFlowIfCanceled;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String stopFlowIfCanceledTipText() {
    return "If enabled, the flow gets stopped in case the user cancels the dialog.";
  }

  /**
   * Sets the custom message to use when stopping the flow.
   *
   * @param value	the stop message
   */
  public void setCustomStopMessage(String value) {
    m_CustomStopMessage = value;
    reset();
  }

  /**
   * Returns the custom message to use when stopping the flow.
   *
   * @return		the stop message
   */
  public String getCustomStopMessage() {
    return m_CustomStopMessage;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String customStopMessageTipText() {
    return
      "The custom stop message to use in case a user cancelation stops the "
	+ "flow (default is the full name of the actor)";
  }

  /**
   * Sets the stop mode.
   *
   * @param value	the mode
   */
  @Override
  public void setStopMode(StopMode value) {
    m_StopMode = value;
    reset();
  }

  /**
   * Returns the stop mode.
   *
   * @return		the mode
   */
  @Override
  public StopMode getStopMode() {
    return m_StopMode;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  @Override
  public String stopModeTipText() {
    return "The stop mode to use.";
  }

  /**
   * Sets the timeout in seconds for connecting.
   *
   * @param value	the timeout
   */
  public void setConnectTimeout(int value) {
    if (getOptionManager().isValid("connectTimeout", value)) {
      m_ConnectTimeout = value;
      reset();
    }
  }

  /**
   * Returns the timeout in seconds for connecting.
   *
   * @return		the timeout
   */
  public int getConnectTimeout() {
    return m_ConnectTimeout;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String connectTimeoutTipText() {
    return "The timeout in seconds for connecting.";
  }

  /**
   * Sets the timeout in seconds for reading.
   *
   * @param value	the timeout
   */
  public void setReadTimeout(int value) {
    if (getOptionManager().isValid("readTimeout", value)) {
      m_ReadTimeout = value;
      reset();
    }
  }

  /**
   * Returns the timeout in seconds for reading.
   *
   * @return		the timeout
   */
  public int getReadTimeout() {
    return m_ReadTimeout;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String readTimeoutTipText() {
    return "The timeout in seconds for reading.";
  }

  /**
   * Sets the timeout in seconds for writing.
   *
   * @param value	the timeout
   */
  public void setWriteTimeout(int value) {
    if (getOptionManager().isValid("writeTimeout", value)) {
      m_WriteTimeout = value;
      reset();
    }
  }

  /**
   * Returns the timeout in seconds for writing.
   *
   * @return		the timeout
   */
  public int getWriteTimeout() {
    return m_WriteTimeout;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String writeTimeoutTipText() {
    return "The timeout in seconds for writing.";
  }

  /**
   * Sets the expiry of the ID resolution caches.
   *
   * @param value	the expiry in seconds
   */
  public void setTimeToLive(int value) {
    m_TimeToLive = value;
    reset();
  }

  /**
   * Returns the expiry of the ID resolution caches.
   *
   * @return		the expiry in seconds
   */
  public int getTimeToLive() {
    return m_TimeToLive;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String timeToLiveTipText() {
    return "The expiry in seconds for the ID resolution caches (for displaying strings rather than numeric IDs).";
  }

  /**
   * Performs the interaction with the user.
   *
   * @return		null if successfully interacted, otherwise error message
   */
  @Override
  public String doInteract() {
    String		result;
    PasswordDialog dlg;

    dlg = new PasswordDialog((Dialog) null, ModalityType.DOCUMENT_MODAL);
    dlg.setLocationRelativeTo(getParentComponent());
    ((Flow) getRoot()).registerWindow(dlg, dlg.getTitle());
    dlg.setVisible(true);
    ((Flow) getRoot()).deregisterWindow(dlg);
    if (dlg.getOption() == PasswordDialog.APPROVE_OPTION)
      result = null;
    else
      result = INTERACTION_CANCELED;

    if (result == null)
      m_ActualPassword = dlg.getPassword();

    return result;
  }

  /**
   * Returns whether headless interaction is supported.
   *
   * @return		true if interaction in headless environment is possible
   */
  public boolean supportsHeadlessInteraction() {
    return true;
  }

  /**
   * Performs the interaction with the user in a headless environment.
   *
   * @return		null if successfully interacted, otherwise error message
   */
  @Override
  public String doInteractHeadless() {
    String		result;
    BasePassword	password;

    result   = INTERACTION_CANCELED;
    password = ConsoleHelper.enterPassword("Please enter password (" + getName() + "):");
    if (password != null) {
      result           = null;
      m_ActualPassword = password;
    }

    return result;
  }

  /**
   * Executes the flow item.
   *
   * @return		null if ok, otherwise error message
   */
  @Override
  protected String doExecute() {
    String	result;
    String	msg;

    result = null;

    m_ActualPassword = m_Password;

    if (m_PromptForPassword && (m_Password.getValue().length() == 0)) {
      if (!isHeadless()) {
        msg = doInteract();
	if (msg != null) {
	  if (m_StopFlowIfCanceled) {
	    if ((m_CustomStopMessage == null) || (m_CustomStopMessage.trim().length() == 0))
	      StopHelper.stop(this, m_StopMode, "Flow canceled: " + getFullName());
	    else
	      StopHelper.stop(this, m_StopMode, m_CustomStopMessage);
	    result = getStopMessage();
	  }
	}
      }
      else if (supportsHeadlessInteraction()) {
        msg = doInteractHeadless();
	if (msg != null) {
	  if (m_StopFlowIfCanceled) {
	    if ((m_CustomStopMessage == null) || (m_CustomStopMessage.trim().length() == 0))
	      StopHelper.stop(this, m_StopMode, "Flow canceled: " + getFullName());
	    else
	      StopHelper.stop(this, m_StopMode, m_CustomStopMessage);
	    result = getStopMessage();
	  }
	}
      }
    }

    if (result == null) {
      if (m_Client == null)
	m_Client = new Client(m_Host.getValue(), m_User, m_Password.getValue(), new LocalStorage(), m_ConnectTimeout, m_ReadTimeout, m_WriteTimeout);
      if (!m_Client.connection().authentication().getTokens().isValid())
	result = "No valid API tokens available!";
    }

    if (result == null)
      m_CacheManager = new CacheManager(this, m_TimeToLive);

    return result;
  }

  /**
   * Returns the client in use.
   *
   * @return		the client, null if not yet instantiated
   */
  public Client getClient() {
    return m_Client;
  }

  /**
   * Returns the cache manager in use.
   *
   * @return      the manager, null if not yet instantiated
   */
  public CacheManager getCacheManager() {
    return m_CacheManager;
  }
}
