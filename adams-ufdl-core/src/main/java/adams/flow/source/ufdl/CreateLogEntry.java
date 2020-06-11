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
 * CreateLogEntry.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.core.base.BaseText;
import com.github.waikatoufdl.ufdl4j.action.Log.LogEntry;
import com.github.waikatoufdl.ufdl4j.action.LogLevel;

/**
 * Creates a user and forwards the user object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateLogEntry
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the level. */
  protected LogLevel m_Level;

  /** the message. */
  protected BaseText m_Message;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a log entry and forwards the object.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "level", "level",
      LogLevel.INFO);

    m_OptionManager.add(
      "message", "message",
      new BaseText());
  }

  /**
   * Sets the level.
   *
   * @param value	the level
   */
  public void setLevel(LogLevel value) {
    m_Level = value;
    reset();
  }

  /**
   * Returns the level.
   *
   * @return		the level
   */
  public LogLevel getLevel() {
    return m_Level;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String levelTipText() {
    return "The level to use for the entry.";
  }

  /**
   * Sets the log message.
   *
   * @param value	the message
   */
  public void setMessage(BaseText value) {
    m_Message = value;
    reset();
  }

  /**
   * Returns the log message.
   *
   * @return		the message
   */
  public BaseText getMessage() {
    return m_Message;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String messageTipText() {
    return "The log message.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "level", m_Level);
    result += QuickInfoHelper.toString(this, "message", m_Message, ": ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{LogEntry.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    LogEntry    result;

    result = null;
    try {
      result = m_Client.log().create(m_Level, m_Message.getValue());
    }
    catch (Exception e) {
      errors.add("Failed to create user!", e);
    }

    return result;
  }
}
