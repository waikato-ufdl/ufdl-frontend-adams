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
 * UFDLExtractAndTransferPK.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer;

import adams.core.QuickInfoHelper;
import adams.core.VariableName;
import adams.flow.core.UFDLListSorting;
import adams.flow.core.Unknown;
import com.github.fracpete.javautils.struct.Struct2;

/**
 <!-- globalinfo-start -->
 * Extracts the primary key (PK) of a list item from the source variable and transfer it into the target variable.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;adams.flow.core.Unknown<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;adams.flow.core.Unknown<br>
 * <br><br>
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
 * &nbsp;&nbsp;&nbsp;default: UFDLExtractAndTransferPK
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
 * <pre>-source &lt;adams.core.VariableName&gt; (property: source)
 * &nbsp;&nbsp;&nbsp;The source variable to obtain the list item string from.
 * &nbsp;&nbsp;&nbsp;default: variable
 * </pre>
 *
 * <pre>-sorting &lt;BY_ID|BY_DESCRIPTION_CASE_SENSITIVE|BY_DESCRIPTION_CASE_INSENSITIVE&gt; (property: sorting)
 * &nbsp;&nbsp;&nbsp;The sorting that was applied to the list items.
 * &nbsp;&nbsp;&nbsp;default: BY_DESCRIPTION_CASE_INSENSITIVE
 * </pre>
 *
 * <pre>-target &lt;adams.core.VariableName&gt; (property: target)
 * &nbsp;&nbsp;&nbsp;The target variable to obtain the list item string from.
 * &nbsp;&nbsp;&nbsp;default: variable
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLExtractAndTransferPK
  extends AbstractTransformer {

  private static final long serialVersionUID = -4112175976999577551L;

  /** the source variable. */
  protected VariableName m_Source;

  /** how to the list items were sorted. */
  protected UFDLListSorting m_Sorting;

  /** the target variable. */
  protected VariableName m_Target;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Extracts the primary key (PK) of a list item from the source variable and transfer it into the target variable.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "source", "source",
      new VariableName());

    m_OptionManager.add(
      "sorting", "sorting",
      UFDLListSorting.BY_DESCRIPTION_CASE_INSENSITIVE);

    m_OptionManager.add(
      "target", "target",
      new VariableName());
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "source", m_Source);
    result += QuickInfoHelper.toString(this, "sorting", m_Sorting, " -> ");
    result += QuickInfoHelper.toString(this, "target", m_Target, " -> ");

    return result;
  }

  /**
   * Sets the source variable.
   *
   * @param value	the variable
   */
  public void setSource(VariableName value) {
    m_Source = value;
    reset();
  }

  /**
   * Returns the source variable.
   *
   * @return		the host
   */
  public VariableName getSource() {
    return m_Source;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String sourceTipText() {
    return "The source variable to obtain the list item string from.";
  }

  /**
   * Sets how to list items were sorted.
   *
   * @param value	true if to sort by ID
   */
  public void setSorting(UFDLListSorting value) {
    m_Sorting = value;
    reset();
  }

  /**
   * Returns how to list items were sorted.
   *
   * @return 		true if to sort by ID
   */
  public UFDLListSorting getSorting() {
    return m_Sorting;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String sortingTipText() {
    return "The sorting that was applied to the list items.";
  }

  /**
   * Sets the target variable.
   *
   * @param value	the variable
   */
  public void setTarget(VariableName value) {
    m_Target = value;
    reset();
  }

  /**
   * Returns the target variable.
   *
   * @return		the host
   */
  public VariableName getTarget() {
    return m_Target;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String targetTipText() {
    return "The target variable to obtain the list item string from.";
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Unknown.class};
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		the Class of the generated tokens
   */
  @Override
  public Class[] generates() {
    return new Class[]{Unknown.class};
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String			result;
    String 			itemStr;
    Struct2<Integer,String> 	item;

    result = null;

    if (!getVariables().has(m_Source.getValue()))
      result = "Variable '" + m_Source + "' not present!";

    if (result == null) {
      itemStr = getVariables().get(m_Source.getValue());
      try {
	item = m_Sorting.fromString(itemStr);
	if (item == null)
	  result = "Failed to extract PK from: " + itemStr;
	else
	  getVariables().set(m_Target.getValue(), "" + item.value1);
      }
      catch (Exception e) {
        result = handleException("Failed to extract PK from: " + itemStr, e);
      }
    }

    if (result == null)
      m_OutputToken = m_InputToken;

    return result;
  }
}
