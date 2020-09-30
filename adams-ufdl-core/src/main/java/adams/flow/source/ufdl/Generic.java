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
 * Generic.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.base.BaseText;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.request.Method;
import okhttp3.MediaType;

/**
 * Generic API call.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Generic
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 3382503754835867361L;

  /** the method to use. */
  protected Method m_Method;

  /** the URL path to use. */
  protected String m_Path;

  /** whether a body is present. */
  protected boolean m_HasBody;

  /** the body (if method allows). */
  protected BaseText m_Body;

  /** the mediatype of the body. */
  protected String m_MediaType;

  /** whether to text or binary data is generated. */
  protected boolean m_BinaryOutput;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Generic API call.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "method", "method",
      Method.GET);

    m_OptionManager.add(
      "path", "path",
      "/v1/core/");

    m_OptionManager.add(
      "has-body", "hasBody",
      false);

    m_OptionManager.add(
      "body", "body",
      new BaseText());

    m_OptionManager.add(
      "media-type", "mediaType",
      MediaTypeHelper.APPLICATION_JSON_UTF8.toString());

    m_OptionManager.add(
      "binary-output", "binaryOutput",
      false);
  }

  /**
   * Sets the method.
   *
   * @param value	the method
   */
  public void setMethod(Method value) {
    m_Method = value;
    reset();
  }

  /**
   * Returns the method.
   *
   * @return		the method
   */
  public Method getMethod() {
    return m_Method;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String methodTipText() {
    return "The request method to use.";
  }

  /**
   * Sets the URL path to use.
   *
   * @param value	the path
   */
  public void setPath(String value) {
    m_Path = value;
    reset();
  }

  /**
   * Returns the URL path to use.
   *
   * @return		the path
   */
  public String getPath() {
    return m_Path;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String pathTipText() {
    return "The URL path to use in the request.";
  }

  /**
   * Sets whether to send the body.
   *
   * @param value	true if body
   */
  public void setHasBody(boolean value) {
    m_HasBody = value;
    reset();
  }

  /**
   * Returns whether to send the body.
   *
   * @return		true if body
   */
  public boolean getHasBody() {
    return m_HasBody;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String hasBodyTipText() {
    return "If enabled (and the method supports it) the body gets sent.";
  }

  /**
   * Sets the body to send.
   *
   * @param value	the body
   */
  public void setBody(BaseText value) {
    m_Body = value;
    reset();
  }

  /**
   * Returns the body to use.
   *
   * @return		the body
   */
  public BaseText getBody() {
    return m_Body;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String bodyTipText() {
    return "The body to send.";
  }

  /**
   * Sets the media-type to send.
   *
   * @param value	the media-type
   */
  public void setMediaType(String value) {
    if ((value != null) && !value.isEmpty()) {
      if (MediaType.parse(value) != null) {
	m_MediaType = value;
	reset();
      }
    }
  }

  /**
   * Returns the media-type to use.
   *
   * @return		the media-type
   */
  public String getMediaType() {
    return m_MediaType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String mediaTypeTipText() {
    return "The media-type of the body.";
  }

  /**
   * Sets whether binary output is produced.
   *
   * @param value	true if binary
   */
  public void setBinaryOutput(boolean value) {
    m_BinaryOutput = value;
    reset();
  }

  /**
   * Returns whether binary output is produced.
   *
   * @return		true if binary
   */
  public boolean getBinaryOutput() {
    return m_BinaryOutput;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String binaryOutputTipText() {
    return "If enabled, a byte array is forwarded instead of a string.";
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    if (m_BinaryOutput)
      return new Class[]{byte[].class};
    else
      return new Class[]{String.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    String	body;
    MediaType	mediaType;

    body = null;
    if (m_HasBody)
      body = m_Body.getValue();
    mediaType = null;
    if (body != null)
      mediaType = MediaType.parse(m_MediaType);

    try {
      switch (m_Method) {
	case GET:
	  return m_BinaryOutput ? m_Client.generic().getBinary(m_Path) : m_Client.generic().get(m_Path);
	case POST:
	  return m_BinaryOutput ? m_Client.generic().postBinary(m_Path, body, mediaType) : m_Client.generic().post(m_Path, body, mediaType);
	case PUT:
	  return m_BinaryOutput ? m_Client.generic().putBinary(m_Path, body, mediaType) : m_Client.generic().put(m_Path, body, mediaType);
	case PATCH:
	  return m_BinaryOutput ? m_Client.generic().patchBinary(m_Path, body, mediaType) : m_Client.generic().patch(m_Path, body, mediaType);
	case DELETE:
	  return m_Client.generic().delete(m_Path);
	case HEAD:
	  return m_Client.generic().head(m_Path);
	case OPTIONS:
	  return m_Client.generic().options(m_Path);
	default:
	  throw new IllegalStateException("Unhandled method: " + m_Method);
      }
    }
    catch (Exception e) {
      errors.add("Failed to " + m_Method + " '" + m_Path + "'!", e);
    }

    return null;
  }
}
