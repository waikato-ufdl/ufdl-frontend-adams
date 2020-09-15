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
 * ExportJobTemplate.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer.ufdl;

import adams.core.MessageCollection;
import adams.core.io.PrettyPrintingSupporter;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Exports the job template via PK or name as JSON string.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ExportJobTemplate
  extends AbstractJobTemplateTransformerAction
  implements PrettyPrintingSupporter {

  private static final long serialVersionUID = 2890424326502728143L;

  /** whether to use pretty-printing. */
  protected boolean m_PrettyPrinting;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Exports the job template via PK or name as JSON string.";
  }

  /**
   * Adds options to the internal list of options.
   */
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "pretty-printing", "prettyPrinting",
      false);
  }

  /**
   * Sets whether to use pretty-printing or not.
   *
   * @param value	true if to use pretty-printing
   */
  public void setPrettyPrinting(boolean value) {
    m_PrettyPrinting = value;
    reset();
  }

  /**
   * Returns whether pretty-printing is used or not.
   *
   * @return		true if to use pretty-printing
   */
  public boolean getPrettyPrinting() {
    return m_PrettyPrinting;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String prettyPrintingTipText() {
    return "If enabled, the output is printed in a 'pretty' format.";
  }

  /**
   * Returns the classes that the transformer generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{String.class};
  }

  /**
   * Transforms the job template.
   *
   * @param template	the input data
   * @param errors 	for collecting errors
   * @return 		the transformed data
   */
  @Override
  protected Object doTransform(JobTemplate template, MessageCollection errors) {
    JsonObject 		json;
    Gson 		gson;

    try {
      json = m_Client.jobTemplates().exportTemplate(template);
      if (m_PrettyPrinting) {
	gson = new GsonBuilder().setPrettyPrinting().create();
	return gson.toJson(json);
      }
      else {
	return json.toString();
      }
    }
    catch (Exception e) {
      errors.add("Failed to export job template " + template, e);
      return null;
    }
  }
}
