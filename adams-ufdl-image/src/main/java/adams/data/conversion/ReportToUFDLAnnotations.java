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
 * ReportToUFDLAnnotations.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.data.objectfinder.AllFinder;
import adams.data.objectfinder.ObjectFinder;
import adams.data.report.Report;
import adams.flow.transformer.locateobjects.LocatedObject;
import adams.flow.transformer.locateobjects.LocatedObjects;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotation;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotations;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a Report with object annotations to UFDL annotations.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ReportToUFDLAnnotations
  extends AbstractConversion {

  private static final long serialVersionUID = -8013274283216304472L;

  /** the object finder to use. */
  protected ObjectFinder m_Finder;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Converts a Report with object annotations to UFDL annotations.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "finder", "finder",
      new AllFinder());
  }

  /**
   * Sets the object finder to use.
   *
   * @param value 	the finder
   */
  public void setFinder(ObjectFinder value) {
    m_Finder = value;
    reset();
  }

  /**
   * Returns the object finder in use.
   *
   * @return 		the finder
   */
  public ObjectFinder getFinder() {
    return m_Finder;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String finderTipText() {
    return "The object finder to use for locating objects in the report.";
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return		the class
   */
  @Override
  public Class accepts() {
    return Report.class;
  }

  /**
   * Returns the class that is generated as output.
   *
   * @return		the class
   */
  @Override
  public Class generates() {
    return Annotations.class;
  }

  /**
   * Performs the actual conversion.
   *
   * @return		the converted data
   * @throws Exception	if something goes wrong with the conversion
   */
  @Override
  protected Object doConvert() throws Exception {
    Annotations		result;
    Report		report;
    LocatedObjects	objs;
    Annotation		ann;
    String		label;
    int[]		x;
    int[]		y;
    int			i;
    List<int[]> 	coordinates;

    report = (Report) m_Input;
    objs   = m_Finder.findObjects(report);
    result = new Annotations();
    for (LocatedObject obj: objs) {
      label = "Object";
      if (obj.getMetaData().containsKey("type"))
        label = "" + obj.getMetaData().containsKey("type");
      if (obj.hasPolygon()) {
	x = obj.getPolygonX();
	y = obj.getPolygonY();
	coordinates = new ArrayList<>();
	for (i = 0; i < x.length; i++)
	  coordinates.add(new int[]{x[i], y[i]});
	if (coordinates.size() >= 3) {
	  ann = new Annotation(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight(), label, new Polygon(coordinates));
	}
	else {
	  getLogger().warning("Object's polygon has fewer than 3 points, skipping: " + obj);
	  ann = new Annotation(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight(), label);
	}
      }
      else {
	ann = new Annotation(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight(), label);
      }
      result.add(ann);
    }

    return result;
  }
}
