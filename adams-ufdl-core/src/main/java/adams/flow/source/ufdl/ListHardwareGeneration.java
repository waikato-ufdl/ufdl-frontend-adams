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
 * ListHardwareGenerations.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.AdditionalInformationHandler;
import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import adams.data.conversion.UFDLHardwareGenerationToSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.ufdlfilter.AbstractUFDLFilter;
import adams.data.ufdlfilter.AllFilter;
import adams.flow.core.UFDLFilterHandler;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations.HardwareGeneration;

import java.util.List;

/**
 * Outputs a spreadsheet with all the hardware generations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ListHardwareGeneration
  extends AbstractUFDLSourceAction
  implements UFDLFilterHandler, AdditionalInformationHandler {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the filter to apply. */
  protected AbstractUFDLFilter m_Filter;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Outputs a spreadsheet with the hardware generations that match the filter.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "filter", "filter",
      new AllFilter());
  }

  /**
   * Sets the filter to apply to the result.
   *
   * @param value	the filter
   */
  @Override
  public void setFilter(AbstractUFDLFilter value) {
    m_Filter = value;
    reset();
  }

  /**
   * Returns the filter to apply to the result.
   *
   * @return		the filter
   */
  @Override
  public AbstractUFDLFilter getFilter() {
    return m_Filter;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String filterTipText() {
    return "The filter to apply.";
  }

  /**
   * Returns a quick info about the object, which can be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "filter", m_Filter, "filter: ");
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Returns the additional information.
   *
   * @return		the additional information, null or 0-length string for no information
   */
  public String getAdditionalInformation() {
    return new UFDLHardwareGenerationToSpreadSheet().getAdditionalInformation();
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    SpreadSheet			result;
    SpreadSheet			sheet;
    List<HardwareGeneration> 		hardwareGenerations;
    UFDLHardwareGenerationToSpreadSheet 	conv;
    String			msg;

    result = null;

    try {
      hardwareGenerations = m_Client.hardware().list(m_Filter.generate(errors));
      conv     = new UFDLHardwareGenerationToSpreadSheet();
      conv.setFlowContext(m_FlowContext);
      for (HardwareGeneration hardwareGeneration : hardwareGenerations) {
        conv.setInput(hardwareGeneration);
        msg = conv.convert();
        if (msg == null) {
          if (result == null) {
	    result = (SpreadSheet) conv.getOutput();
	  }
          else {
            sheet = (SpreadSheet) conv.getOutput();
            for (Row row: sheet.rows())
	      result.addRow().assign(row);
	  }
	}
	else {
          errors.add("Failed to convert hardware generation: " + hardwareGeneration + "\n" + msg);
	}
      }
      conv.cleanUp();
    }
    catch (Exception e) {
      errors.add("Failed to list hardware generations!", e);
    }

    return result;
  }
}
