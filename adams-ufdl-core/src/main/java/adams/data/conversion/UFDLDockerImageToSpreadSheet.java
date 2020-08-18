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
 * UFDLDockerImageToSpreadSheet.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.Utils;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import com.github.waikatoufdl.ufdl4j.action.DockerImages.DockerImage;

/**
 * Converts a DockerImage object into a Spreadsheet.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLDockerImageToSpreadSheet
  extends AbstractUFDLObjectToSpreadSheetConversion {

  private static final long serialVersionUID = 2239463804853893127L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a DockerImage object into a Spreadsheet.";
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return DockerImage.class;
  }

  /**
   * Generates the template.
   *
   * @return		the template
   */
  @Override
  public SpreadSheet getTemplate() {
    SpreadSheet result;
    Row 	row;

    result = new DefaultSpreadSheet();

    row = result.getHeaderRow();
    row.addCell("pk").setContentAsString("pk");
    row.addCell("na").setContentAsString("name");
    row.addCell("ve").setContentAsString("version");
    row.addCell("li").setContentAsString("license");
    row.addCell("ru").setContentAsString("registry_url");
    row.addCell("run").setContentAsString("registry_username");
    row.addCell("rp").setContentAsString("registry_password");
    row.addCell("do").setContentAsString("domain");
    row.addCell("ta").setContentAsString("tasks");
    row.addCell("cv").setContentAsString("cuda_version");
    row.addCell("fw").setContentAsString("framework");
    row.addCell("mh").setContentAsString("min_hardware_generation");
    row.addCell("cp").setContentAsString("cpu");

    return result;
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    SpreadSheet		result;
    Row			row;
    DockerImage 	image;

    image = (DockerImage) m_Input;
    result  = getTemplate();
    row     = result.addRow();
    row.addCell("pk").setContent(image.getPK());
    row.addCell("na").setContentAsString(image.getName());
    row.addCell("ve").setContentAsString(image.getVersion());
    row.addCell("li").setContent(getLicense(image.getLicense()));
    row.addCell("do").setContentAsString(image.getDomain());
    row.addCell("ta").setContentAsString(Utils.arrayToString(image.getTasks()));
    row.addCell("ru").setContentAsString(image.getRegistryUrl());
    row.addCell("run").setContentAsString(image.getRegistryUserName());
    row.addCell("rp").setContentAsString(image.getRegistryPassword().isEmpty() ? "" : "***");
    row.addCell("cv").setContent(getCudaVersion(image.getCudaVersion()));
    row.addCell("fw").setContent(getFramework(image.getFramework()));
    row.addCell("mh").setContentAsString(image.getMinHardwareGeneration());
    row.addCell("cp").setContent(image.getCPU());

    return result;
  }
}
