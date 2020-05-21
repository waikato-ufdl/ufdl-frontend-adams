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
 * UFDLAnnotationsToReport.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.QuickInfoHelper;
import adams.data.report.Report;
import adams.flow.transformer.locateobjects.LocatedObject;
import adams.flow.transformer.locateobjects.LocatedObjects;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotation;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotations;

import java.awt.Polygon;
import java.util.List;

/**
 * Converts the UFDL annotations into an ADAMS Report.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLAnnotationsToReport
  extends AbstractConversion {

  private static final long serialVersionUID = 6711838162542057646L;

  /** the prefix to use. */
  protected String m_Prefix;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts the UFDL annotations into an ADAMS Report.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "prefix", "prefix",
      "Object.");
  }

  /**
   * Sets the prefix for the objects in the report.
   *
   * @param value 	the prefix
   */
  public void setPrefix(String value) {
    m_Prefix = value;
    reset();
  }

  /**
   * Returns the prefix for the objects in the report.
   *
   * @return 		the prefix
   */
  public String getPrefix() {
    return m_Prefix;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String prefixTipText() {
    return "The prefix for the objects in the report.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "prefix", m_Prefix);
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return Annotations.class;
  }

  /**
   * Returns the class that is generated as output.
   *
   * @return		the class
   */
  @Override
  public Class generates() {
    return Report.class;
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    Report		result;
    Annotations		annotations;
    LocatedObjects	objs;
    LocatedObject	obj;
    int[]		x;
    int[]		y;
    int			i;
    List<int[]> 	coordinates;
    
    annotations = (Annotations) m_Input;
    objs        = new LocatedObjects();
    for (Annotation ann: annotations) {
      obj = new LocatedObject(ann.getX(), ann.getY(), ann.getWidth(), ann.getHeight());
      obj.getMetaData().put("type", ann.getLabel());
      if (ann.hasPolygon()) {
        coordinates = ann.getPolygon().getCoordinates();
        x           = new int[coordinates.size()];
        y           = new int[coordinates.size()];
        for (i = 0; i < coordinates.size(); i++) {
          x[i] = coordinates.get(i)[0];
          y[i] = coordinates.get(i)[1];
	}
	obj.setPolygon(new Polygon(x, y, x.length));
      }
      objs.add(obj);
    }
    result = objs.toReport(m_Prefix);
    
    return result;
  }
}
