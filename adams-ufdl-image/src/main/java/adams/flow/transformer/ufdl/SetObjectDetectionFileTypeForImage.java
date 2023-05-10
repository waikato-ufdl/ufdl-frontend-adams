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
 * SetObjectDetectionFileTypeForImage.java
 * Copyright (C) 2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;

/**
 * Sets the file type (file extension, dimensions and video length) of the specified image/video in the dataset coming in.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SetObjectDetectionFileTypeForImage
  extends AbstractObjectDetectionDatasetTransformerAction {

  private static final long serialVersionUID = -1421130988687306299L;

  /** the name of the image/video to get the annotations for. */
  protected String m_Name;

  /** the file format (file extension). */
  protected String m_Format;
  
  /** the image/video width. */
  protected int m_Width;
  
  /** the image/video height. */
  protected int m_Height;
  
  /** the video length in seconds (use <= 0 for images). */
  protected int m_Length;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Sets the file type (extension) and dimensions of the specified image/video in the dataset coming in.\n"
      + "Providing a video length (in seconds) larger than 0 implies that the file is a video.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "name", "name",
      "");

    m_OptionManager.add(
      "format", "format",
      "");

    m_OptionManager.add(
      "width", "width",
      -1, -1, null);

    m_OptionManager.add(
      "height", "height",
      -1, -1, null);

    m_OptionManager.add(
      "length", "length",
      -1, -1, null);
  }

  /**
   * Sets the name of the image/video to set the annotations for.
   *
   * @param value 	the name
   */
  public void setName(String value) {
    m_Name = value;
    reset();
  }

  /**
   * Returns the name of the image/video to set the annotations for.
   *
   * @return 		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String nameTipText() {
    return "The name of the image/video to set the annotations for.";
  }

  /**
   * Sets the format (= file extension).
   *
   * @param value	the format, ignored if empty string
   */
  public void setFormat(String value) {
    m_Format = value;
    reset();
  }

  /**
   * Returns the format (= file extension).
   *
   * @return		the format
   */
  public String getFormat() {
    return m_Format;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String formatTipText() {
    return "The file extension of the image/video, ignored if empty.";
  }

  /**
   * Sets the width of the image/video.
   *
   * @param value	the width
   */
  public void setWidth(int value) {
    m_Width = value;
    reset();
  }

  /**
   * Returns the width of the image/video.
   *
   * @return		the width
   */
  public int getWidth() {
    return m_Width;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String widthTipText() {
    return "The width of the image/video.";
  }

  /**
   * Sets the height of the image/video.
   *
   * @param value	the height
   */
  public void setHeight(int value) {
    m_Height = value;
    reset();
  }

  /**
   * Returns the height of the image/video.
   *
   * @return		the height
   */
  public int getHeight() {
    return m_Height;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String heightTipText() {
    return "The height of the image/video.";
  }

  /**
   * Sets the length of the video in seconds (use <= 0 for images).
   *
   * @param value	the length in sec
   */
  public void setLength(int value) {
    m_Length = value;
    reset();
  }

  /**
   * Returns the length of the video in seconds (use <= 0 for images).
   *
   * @return		the length
   */
  public int getLength() {
    return m_Length;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String lengthTipText() {
    return "The video length in seconds (use <= 0 for images).";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "name", (m_Name.isEmpty() ? "-none-" : m_Name));
    result += QuickInfoHelper.toString(this, "format", m_Format, ", format: ");
    result += QuickInfoHelper.toString(this, "width", m_Width, ", width: ");
    result += QuickInfoHelper.toString(this, "height", m_Height, ", height: ");
    result += QuickInfoHelper.toString(this, "length", m_Length, ", length: ");

    return result;
  }

  /**
   * Returns whether the action requires flow context.
   *
   * @return		true if required
   */
  @Override
  public boolean requiresFlowContext() {
    return true;
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{Boolean.class};
  }

  /**
   * Transforms the input data.
   *
   * @param dataset	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(Dataset dataset, MessageCollection errors) {
    boolean			result;

    result = false;
    try {
      result = getDatasetsAction().setFileType(
        dataset,
        m_Name,
        m_Format.isEmpty() ? null : m_Format,
        m_Width,
        m_Height,
        m_Length <= 0 ? null : m_Length);
    }
    catch (Exception e) {
      errors.add("Failed to set file type etc for image/video '" + m_Name + "' in dataset: " + dataset, e);
    }

    return result;
  }
}
