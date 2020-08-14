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
 * CreateHardwareGeneration.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.source.ufdl;

import adams.core.MessageCollection;
import adams.core.QuickInfoHelper;
import com.github.waikatoufdl.ufdl4j.action.HardwareGenerations.HardwareGeneration;

/**
 * Creates a hardware generation and forwards the hardware generation object.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CreateHardwareGeneration
  extends AbstractUFDLSourceAction {

  private static final long serialVersionUID = 2444931814949354710L;

  /** the hardware generation name. */
  protected String m_Generation;

  /** the minimum compute capability (incl). */
  protected double m_MinComputeCapability;

  /** the maximum compute capability (excl). */
  protected double m_MaxComputeCapability;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Creates a hardware generation and forwards the hardware generation object.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "generation", "generation",
      "");

    m_OptionManager.add(
      "min-compute-capability", "minComputeCapability",
      1.0, 0.0, null);

    m_OptionManager.add(
      "max-compute-capability", "maxComputeCapability",
      2.0, 0.0, null);
  }

  /**
   * Sets the hardware generation name.
   *
   * @param value	the name
   */
  public void setGeneration(String value) {
    m_Generation = value;
    reset();
  }

  /**
   * Returns the hardware generation name.
   *
   * @return		the name
   */
  public String getGeneration() {
    return m_Generation;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String generationTipText() {
    return "The hardware generation name.";
  }

  /**
   * Sets the minimum compute capability.
   *
   * @param value	the minimum
   */
  public void setMinComputeCapability(double value) {
    m_MinComputeCapability = value;
    reset();
  }

  /**
   * Returns the minimum compute capability.
   *
   * @return		the minimum
   */
  public double getMinComputeCapability() {
    return m_MinComputeCapability;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String minComputeCapabilityTipText() {
    return "The minimum compute capability (included).";
  }

  /**
   * Sets the maximum compute capability.
   *
   * @param value	the maximum
   */
  public void setMaxComputeCapability(double value) {
    m_MaxComputeCapability = value;
    reset();
  }

  /**
   * Returns the maximum compute capability.
   *
   * @return		the maximum
   */
  public double getMaxComputeCapability() {
    return m_MaxComputeCapability;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String maxComputeCapabilityTipText() {
    return "The maximum compute capability (excluded).";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "generation", m_Generation, "generation: ");
    result += QuickInfoHelper.toString(this, "minComputeCapability", m_MinComputeCapability, ", min: ");
    result += QuickInfoHelper.toString(this, "maxComputeCapability", m_MaxComputeCapability, ", max: ");

    return result;
  }

  /**
   * Returns the classes that the source generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] generates() {
    return new Class[]{HardwareGeneration.class};
  }

  /**
   * Generates the data.
   *
   * @param errors 	for collecting errors
   * @return		the generated data, null if none generated
   */
  @Override
  protected Object doGenerate(MessageCollection errors) {
    HardwareGeneration    result;

    result = null;
    try {
      result = m_Client.hardware().create(m_Generation, m_MinComputeCapability, m_MaxComputeCapability);
    }
    catch (Exception e) {
      errors.add("Failed to create hardware generation!", e);
    }

    return result;
  }
}
