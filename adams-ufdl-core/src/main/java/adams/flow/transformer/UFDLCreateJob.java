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
 * UFDLCreateJob.java
 * Copyright (C) 2020-2023 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer;

import adams.core.Properties;
import adams.core.QuickInfoHelper;
import adams.core.Utils;
import adams.core.base.BaseBoolean;
import adams.core.base.BaseDouble;
import adams.core.base.BaseInteger;
import adams.core.base.BaseObject;
import adams.core.base.BaseString;
import adams.core.net.HtmlUtils;
import adams.data.ufdlfilter.DomainFilter;
import adams.data.ufdlfilter.GenericFilter;
import adams.data.ufdlfilter.field.ExactInteger;
import adams.flow.core.ActorUtils;
import adams.flow.core.Token;
import adams.flow.core.UFDLContractType;
import adams.flow.core.UFDLListSorting;
import adams.flow.core.UFDLSoftDeleteObjectState;
import adams.flow.standalone.UFDLConnection;
import adams.gui.chooser.UFDLDatasetChooserPanel;
import adams.gui.chooser.UFDLDockerImageChooserPanel;
import adams.gui.chooser.UFDLJobOutputChooserPanel;
import adams.gui.core.BaseButton;
import adams.gui.core.BaseDialog;
import adams.gui.core.BasePanel;
import adams.gui.core.BaseScrollPane;
import adams.gui.core.BaseTextArea;
import adams.gui.core.GUIHelper;
import adams.gui.core.PropertiesParameterPanel;
import adams.gui.core.PropertiesParameterPanel.PropertyType;
import adams.gui.dialog.ApprovalDialog;
import adams.gui.goe.GenericArrayEditorPanel;
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Domains.Domain;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.core.TypeValuePair;
import com.github.waikatoufdl.ufdl4j.core.Types;
import com.github.waikatoufdl.ufdl4j.core.types.DomainType;
import com.github.waikatoufdl.ufdl4j.core.types.FrameworkType;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.github.waikatoufdl.ufdl4j.core.Types.dataset;
import static com.github.waikatoufdl.ufdl4j.core.Types.dockerImage;
import static com.github.waikatoufdl.ufdl4j.core.Types.domain;
import static com.github.waikatoufdl.ufdl4j.core.Types.model;
import static com.github.waikatoufdl.ufdl4j.core.Types.pk;

/**
 <!-- globalinfo-start -->
 * Generates a dialog for the user to fill based on the received job template. The provided values for inputs and parameters are used to generate and submit the job.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;java.lang.Integer<br>
 * &nbsp;&nbsp;&nbsp;com.github.waikatoufdl.ufdl4j.action.JobTemplates$JobTemplate<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;com.github.waikatoufdl.ufdl4j.action.Jobs$Job<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * &nbsp;&nbsp;&nbsp;min-user-mode: Expert
 * </pre>
 *
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: UFDLCreateJob
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
 * &nbsp;&nbsp;&nbsp;min-user-mode: Expert
 * </pre>
 *
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console; Note: the enclosing
 * &nbsp;&nbsp;&nbsp;actor handler must have this enabled as well.
 * &nbsp;&nbsp;&nbsp;default: false
 * &nbsp;&nbsp;&nbsp;min-user-mode: Expert
 * </pre>
 *
 * <pre>-short-title &lt;boolean&gt; (property: shortTitle)
 * &nbsp;&nbsp;&nbsp;If enabled uses just the name for the title instead of the actor's full
 * &nbsp;&nbsp;&nbsp;name.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-width &lt;int&gt; (property: width)
 * &nbsp;&nbsp;&nbsp;The width of the dialog.
 * &nbsp;&nbsp;&nbsp;default: 800
 * &nbsp;&nbsp;&nbsp;minimum: 1
 * </pre>
 *
 * <pre>-height &lt;int&gt; (property: height)
 * &nbsp;&nbsp;&nbsp;The height of the dialog.
 * &nbsp;&nbsp;&nbsp;default: 600
 * &nbsp;&nbsp;&nbsp;minimum: 1
 * </pre>
 *
 * <pre>-x &lt;int&gt; (property: x)
 * &nbsp;&nbsp;&nbsp;The X position of the dialog (&gt;=0: absolute, -1: left, -2: center, -3: right
 * &nbsp;&nbsp;&nbsp;).
 * &nbsp;&nbsp;&nbsp;default: -1
 * &nbsp;&nbsp;&nbsp;minimum: -3
 * </pre>
 *
 * <pre>-y &lt;int&gt; (property: y)
 * &nbsp;&nbsp;&nbsp;The Y position of the dialog (&gt;=0: absolute, -1: top, -2: center, -3: bottom
 * &nbsp;&nbsp;&nbsp;).
 * &nbsp;&nbsp;&nbsp;default: -1
 * &nbsp;&nbsp;&nbsp;minimum: -3
 * </pre>
 *
 * <pre>-stop-if-canceled &lt;boolean&gt; (property: stopFlowIfCanceled)
 * &nbsp;&nbsp;&nbsp;If enabled, the flow gets stopped in case the user cancels the dialog.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-custom-stop-message &lt;java.lang.String&gt; (property: customStopMessage)
 * &nbsp;&nbsp;&nbsp;The custom stop message to use in case a user cancelation stops the flow
 * &nbsp;&nbsp;&nbsp;(default is the full name of the actor)
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-stop-mode &lt;GLOBAL|STOP_RESTRICTOR&gt; (property: stopMode)
 * &nbsp;&nbsp;&nbsp;The stop mode to use.
 * &nbsp;&nbsp;&nbsp;default: GLOBAL
 * </pre>
 *
 * <pre>-contract-type &lt;TRAIN|PREDICT&gt; (property: contractType)
 * &nbsp;&nbsp;&nbsp;The contract type to use.
 * &nbsp;&nbsp;&nbsp;default: TRAIN
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLCreateJob
  extends AbstractInteractiveTransformerDialog {

  private static final long serialVersionUID = 7467922709474210365L;

  /**
   * Container for storing job template data.
   */
  public static class JobTemplateData
    implements Serializable {

    private static final long serialVersionUID = -8500730651973165146L;

    /** the job template. */
    public JobTemplate template;

    /** the domain. */
    public Domain domain;

    /** the framework. */
    public Framework framework;

    /** the license. */
    public License license;

    /** the contract type. */
    public UFDLContractType contractType;

    /** the types. */
    public Map<String,String> types;

    /** the inputs. */
    public Map<String, String> inputs;

    /** the chosen input types. */
    public Properties inputTypes;

    /** the chosen input values. */
    public Properties inputValues;

    /** the parameters. */
    public List<JobTemplates.Parameter> parameters;

    /** the chosen parameter types. */
    public Properties parameterTypes;

    /** the chosen parameter values. */
    public Properties parameterValues;

    /**
     * Initializes the container.
     *
     * @param client	the client to use for connecting to the API
     * @param template 	the template to use
     * @param contractType 	the contract type
     * @throws Exception	if initialization fails (eg due to API problems)
     */
    public JobTemplateData(Client client, JobTemplate template, UFDLContractType contractType) throws Exception {
      this.template = template;
      this.contractType = contractType;

      // license
      license = client.licenses().load(template.getLicense());
      if (license == null)
	throw new IllegalStateException("Failed to load license: " + template.getLicense());

      // types/domain/framework
      types = client.jobTemplates().getTypes(template);
      domain = null;
      framework = null;
      for (String key : types.keySet()) {
	if (key.equals(DomainType.TYPE))
	  domain = new DomainType().parse(client, types.get(key));
	if (key.equals(FrameworkType.TYPE))
	  framework = new FrameworkType().parse(client, types.get(key));
      }
      if (domain == null)
	throw new IllegalStateException("No domain determined!");
      if (framework == null)
	throw new IllegalStateException("No framework determined!");

      // parameters
      parameters = client.jobTemplates().getAllParameters(template);

      // inputs
      inputs = contractType.getContract().inputs(domain, framework);
    }
  }

  /** the contract type. */
  protected UFDLContractType m_ContractType;

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /** the template info. */
  protected StringBuilder m_TemplateInfo;

  /** the description for the job. */
  protected BaseTextArea m_TextDescription;

  /** the properties parameter panel (inputs). */
  protected PropertiesParameterPanel m_PropertiesPanelInputs;

  /** the properties parameter panel (parameters). */
  protected PropertiesParameterPanel m_PropertiesPanelParameters;

  /** whether the dialog got accepted. */
  protected boolean m_Accepted;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Generates a dialog for the user to fill based on the received job template. "
      + "The provided values for inputs and parameters are used to generate and submit the job.";
  }

  /**
   * Resets the scheme.
   */
  @Override
  protected void reset() {
    super.reset();

    m_Connection = null;
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "contract-type", "contractType",
      UFDLContractType.TRAIN);
  }

  /**
   * Sets the contract type to use.
   *
   * @param value	the type
   */
  public void setContractType(UFDLContractType value) {
    m_ContractType = value;
    reset();
  }

  /**
   * Returns the contract type to use.
   *
   * @return		the type
   */
  public UFDLContractType getContractType() {
    return m_ContractType;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String contractTypeTipText() {
    return "The contract type to use.";
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Integer.class, JobTemplate.class};
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		the Class of the generated tokens
   */
  @Override
  public Class[] generates() {
    return new Class[]{Job.class};
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = super.getQuickInfo();
    result += QuickInfoHelper.toString(this, "contractType", m_ContractType, ", type: ");

    return result;
  }

  /**
   * Initializes the item for flow execution.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  public String setUp() {
    String		result;

    result = super.setUp();

    if (result == null) {
      m_Connection = (UFDLConnection) ActorUtils.findClosestType(this, UFDLConnection.class, true);
      if (m_Connection == null)
	result = "Failed to locate an instance of " + Utils.classToString(UFDLConnection.class) + "!";
    }

    return result;
  }

  /**
   * Clears the content of the panel.
   */
  @Override
  public void clearPanel() {
    if (m_TemplateInfo != null)
      m_TemplateInfo.delete(0, m_TemplateInfo.length());
    if (m_TextDescription != null)
      m_TextDescription.setText("");
    if (m_PropertiesPanelInputs != null)
      m_PropertiesPanelInputs.clearPropertyTypes();
    if (m_PropertiesPanelParameters != null)
      m_PropertiesPanelParameters.clearPropertyTypes();
  }

  /**
   * Creates the panel to display in the dialog.
   *
   * @return		the panel
   */
  @Override
  protected BasePanel newPanel() {
    BasePanel 	result;
    BasePanel	panelDesc;
    BasePanel	panelInputsParams;

    result = new BasePanel(new BorderLayout());

    m_TemplateInfo = new StringBuilder();

    panelDesc = new BasePanel(new BorderLayout());
    panelDesc.setBorder(BorderFactory.createTitledBorder("Job description"));
    result.add(panelDesc, BorderLayout.NORTH);
    m_TextDescription = new BaseTextArea(4, 40);
    panelDesc.add(new BaseScrollPane(m_TextDescription), BorderLayout.CENTER);

    panelInputsParams = new BasePanel(new BorderLayout());
    result.add(panelInputsParams, BorderLayout.CENTER);

    m_PropertiesPanelInputs = new PropertiesParameterPanel();
    m_PropertiesPanelInputs.setBorder(BorderFactory.createTitledBorder("Inputs"));
    panelInputsParams.add(m_PropertiesPanelInputs, BorderLayout.NORTH);

    m_PropertiesPanelParameters = new PropertiesParameterPanel();
    m_PropertiesPanelParameters.setBorder(BorderFactory.createTitledBorder("Parameters"));
    panelInputsParams.add(m_PropertiesPanelParameters, BorderLayout.CENTER);

    return result;
  }

  /**
   * Adds the info to the info string.
   *
   * @param label	the label to use
   * @param text	the text to display
   */
  protected void addInfo(String label, String text) {
    if (text.isEmpty())
      text = "N/A";
    m_TemplateInfo.append("<tr>");
    m_TemplateInfo.append("<td>");
    m_TemplateInfo.append(HtmlUtils.toHTML(label));
    m_TemplateInfo.append("</td>");
    m_TemplateInfo.append("<td>");
    m_TemplateInfo.append(HtmlUtils.markUpURLs(text, true));
    m_TemplateInfo.append("</td>");
    m_TemplateInfo.append("</tr>\n");
  }

  /**
   * Adds the input to the input panel.
   *
   * @param panel		the panel to add to
   * @param key			the name of the input to add
   * @param templateData	the template data to use
   * @param values 		for the default values
   * @param types 		for storing the type for the key
   */
  protected void addInput(PropertiesParameterPanel panel, String key, JobTemplateData templateData, Properties values, Properties types) {
    String 	datasetPK;
    String	label;
    String	type;

    datasetPK = pk(dataset(domain(templateData.domain)));
    label     = key.replace(GUIHelper.MNEMONIC_INDICATOR, '-');
    type      = templateData.inputs.get(key);

    // dataset?
    if (type.equals(datasetPK)) {
      UFDLDatasetChooserPanel chooser = new UFDLDatasetChooserPanel();
      chooser.setConnection(m_Connection);
      chooser.setFilter(new DomainFilter(templateData.domain));
      chooser.setMultiSelection(false);
      chooser.setSorting(UFDLListSorting.BY_ID_ONLY);
      chooser.setState(UFDLSoftDeleteObjectState.ACTIVE);
      panel.addPropertyType(key, PropertyType.CUSTOM_COMPONENT);
      panel.setLabel(key, label);
      panel.setComponent(key, chooser);
      types.setProperty(key, type);
      values.setInteger(key, -1);
    }

    // job output?
    if (type.startsWith("JobOutput<")) {
      UFDLJobOutputChooserPanel chooser = new UFDLJobOutputChooserPanel();
      chooser.setConnection(m_Connection);
      chooser.setOutputType(model(templateData.domain, templateData.framework));
      chooser.setDisplayType(UFDLJobOutputChooserPanel.DisplayType.ID);
      panel.addPropertyType(key, PropertyType.CUSTOM_COMPONENT);
      panel.setLabel(key, label);
      panel.setComponent(key, chooser);
      types.setProperty(key, type);
      values.setProperty(key, "");
    }
  }

  /**
   * Adds the parameter to the parameter panel.
   *
   * @param panel		the panel to add to
   * @param parameter		the parameter to add
   * @param templateData	the template data to use
   * @param values 		for the default values
   * @param types 		for storing the type for the parameter
   * @throws Exception		if adding of parameter fails
   */
  protected void addParameter(PropertiesParameterPanel panel, JobTemplates.Parameter parameter, JobTemplateData templateData, Properties values, Properties types) throws Exception {
    String 			datasetPK;
    String 			dockerImage;
    String 			dockerImagePK;
    String 			dockerImageName;
    String			defValue;
    Object			defValues;
    GenericArrayEditorPanel	gae;
    boolean			added;
    boolean			ignored;
    String			key;
    String			label;

    key             = parameter.getName();
    label           = parameter.getName().replace(GUIHelper.MNEMONIC_INDICATOR, '-');
    datasetPK       = pk(dataset(domain(templateData.domain)));
    dockerImage     = dockerImage(templateData.domain, templateData.framework, null);
    dockerImagePK   = Types.PK + dockerImage;
    dockerImageName = Types.NAME + dockerImage;

    defValue = null;
    added    = false;
    ignored  = false;
    for (String type: parameter.getTypes()) {
      if (type.equals(datasetPK)) {
	added = true;
	UFDLDatasetChooserPanel chooser = new UFDLDatasetChooserPanel();
	chooser.setConnection(m_Connection);
	chooser.setFilter(new DomainFilter(templateData.domain));
	chooser.setMultiSelection(false);
	chooser.setSorting(UFDLListSorting.BY_ID_ONLY);
	chooser.setState(UFDLSoftDeleteObjectState.ACTIVE);
	panel.addPropertyType(key, PropertyType.CUSTOM_COMPONENT);
	panel.setLabel(key, label);
	panel.setComponent(key, chooser);
	types.setProperty(key, type);
      }
      else if (type.startsWith(dockerImagePK)) {
	added = true;
	UFDLDockerImageChooserPanel chooser = new UFDLDockerImageChooserPanel();
	chooser.setConnection(m_Connection);
	GenericFilter filter = new GenericFilter();
	filter.addExpression(new ExactInteger("domain", templateData.domain.getPK()));
	filter.addExpression(new ExactInteger("framework", templateData.framework.getPK()));
	chooser.setFilter(filter);
	chooser.setTask(m_ContractType.getName());
	chooser.setMultiSelection(false);
	chooser.setSorting(UFDLListSorting.BY_ID_ONLY);
	panel.addPropertyType(key, PropertyType.CUSTOM_COMPONENT);
	panel.setLabel(key, label);
	panel.setComponent(key, chooser);
	types.setProperty(key, type);
      }
      else if (type.startsWith(dockerImageName) || type.startsWith(dockerImage)) {
	// handled by dockerImagePK
	ignored = true;
      }
      else {
        // change size limited arrays to open ones:
        if (type.startsWith(Types.ARRAY_BOOL_LIMITED))
          type = Types.ARRAY_BOOL;
        else if (type.startsWith(Types.ARRAY_INT_LIMITED))
          type = Types.ARRAY_INT;
        else if (type.startsWith(Types.ARRAY_FLOAT_LIMITED))
          type = Types.ARRAY_FLOAT;
        else if (type.startsWith(Types.ARRAY_STR_LIMITED))
          type = Types.ARRAY_STR;

	switch (type) {
	  case Types.PRIMITIVE_BOOL:
	    added = true;
	    panel.addPropertyType(key, PropertyType.BOOLEAN);
	    panel.setLabel(key, label);
	    if (parameter.hasDefault())
	      defValue = "" + parameter.getDefault();
	    else
	      defValue = "false";
	    types.setProperty(key, type);
	    break;

	  case Types.PRIMITIVE_INT:
	    added = true;
	    panel.addPropertyType(key, PropertyType.INTEGER);
	    panel.setLabel(key, label);
	    if (parameter.hasDefault())
	      defValue = "" + parameter.getDefault();
	    else
	      defValue = "-1";
	    types.setProperty(key, type);
	    break;

	  case Types.PRIMITIVE_FLOAT:
	    added = true;
	    panel.addPropertyType(key, PropertyType.DOUBLE);
	    panel.setLabel(key, label);
	    if (parameter.hasDefault())
	      defValue = "" + parameter.getDefault();
	    else
	      defValue = "0.0";
	    types.setProperty(key, type);
	    break;

	  case Types.PRIMITIVE_STR:
	    added = true;
	    panel.addPropertyType(key, PropertyType.STRING);
	    panel.setLabel(key, label);
	    if (parameter.hasDefault())
	      defValue = "" + parameter.getDefault();
	    else
	      defValue = "";
	    types.setProperty(key, type);
	    break;

	  case Types.ARRAY_STR:
	    added = true;
	    if (parameter.hasDefault()) {
	      defValues = parameter.getDefault();
	      if (defValues instanceof List)
		defValues = BaseObject.toObjectArray((String[]) ((List) defValues).toArray(new String[0]), BaseString.class);
	      values.setProperty(key, Utils.flatten((BaseString[]) defValues, "\n"));
	    }
	    else {
	      defValues = new BaseString[0];
	    }
	    gae = new GenericArrayEditorPanel(defValues);
	    panel.addPropertyType(key, PropertyType.ARRAY_EDITOR);
	    panel.setLabel(key, label);
	    panel.setChooser(key, gae);
	    panel.setArrayClass(key, BaseString.class);
	    panel.setArraySeparator(key, "\n");
	    types.setProperty(key, type);
	    break;

          case Types.ARRAY_BOOL:
            added = true;
            if (parameter.hasDefault()) {
              defValues = parameter.getDefault();
              if (defValues instanceof List)
                defValues = BaseObject.toObjectArray((String[]) ((List) defValues).toArray(new String[0]), BaseBoolean.class);
              values.setProperty(key, Utils.flatten((BaseBoolean[]) defValues, "\n"));
            }
            else {
              defValues = new BaseBoolean[0];
            }
            gae = new GenericArrayEditorPanel(defValues);
            panel.addPropertyType(key, PropertyType.ARRAY_EDITOR);
            panel.setLabel(key, label);
            panel.setChooser(key, gae);
            panel.setArrayClass(key, BaseBoolean.class);
            panel.setArraySeparator(key, "\n");
            types.setProperty(key, type);
            break;

	  case Types.ARRAY_INT:
	    added = true;
	    if (parameter.hasDefault()) {
	      defValues = parameter.getDefault();
	      if (defValues instanceof List)
		defValues = BaseObject.toObjectArray((String[]) ((List) defValues).toArray(new String[0]), BaseInteger.class);
	      values.setProperty(key, Utils.flatten((BaseInteger[]) defValues, "\n"));
	    }
	    else {
	      defValues = new BaseInteger[0];
	    }
	    gae = new GenericArrayEditorPanel(defValues);
	    panel.addPropertyType(key, PropertyType.ARRAY_EDITOR);
	    panel.setLabel(key, label);
	    panel.setChooser(key, gae);
	    panel.setArrayClass(key, BaseInteger.class);
	    panel.setArraySeparator(key, "\n");
	    types.setProperty(key, type);
	    break;

          case Types.ARRAY_FLOAT:
            added = true;
            if (parameter.hasDefault()) {
              defValues = parameter.getDefault();
              if (defValues instanceof List)
                defValues = BaseObject.toObjectArray((String[]) ((List) defValues).toArray(new String[0]), BaseDouble.class);
              values.setProperty(key, Utils.flatten((BaseDouble[]) defValues, "\n"));
            }
            else {
              defValues = new BaseDouble[0];
            }
            gae = new GenericArrayEditorPanel(defValues);
            panel.addPropertyType(key, PropertyType.ARRAY_EDITOR);
            panel.setLabel(key, label);
            panel.setChooser(key, gae);
            panel.setArrayClass(key, BaseDouble.class);
            panel.setArraySeparator(key, "\n");
            types.setProperty(key, type);
            break;

	  default:
	    if (!ignored) {
	      if (type.contains(Types.DOCKER_IMAGE))
		throw new IllegalStateException("Unhandled parameter type: " + type + "\n" + parameter.toString());
	      else
		getLogger().warning("Unhandled parameter type: " + type + "\n" + parameter.toString());
	    }
	}
      }
      if (added)
	break;
    }

    // set default value
    if (defValue != null)
      values.setProperty(key, defValue);
  }

  /**
   * Populates the properties panel using the job template data.
   *
   * @param templateData	the template data to use
   * @throws Exception		if setting up of panel fails
   */
  protected void updatePanel(JobTemplateData templateData) throws Exception {
    List<String>	keys;
    Properties 		defValues;

    clearPanel();

    // info
    m_TemplateInfo.append("<html>\n");
    m_TemplateInfo.append("<h2>").append(templateData.template.getName()).append("</h2>\n");
    m_TemplateInfo.append("<table border=\"1\" cellspacing=\"0\">\n");
    addInfo("Description", templateData.template.getDescription());
    addInfo("Version", "" + templateData.template.getVersion());
    addInfo("Domain", templateData.domain.getDescription());
    addInfo("Framework", templateData.framework.getName() + "/" + templateData.framework.getVersion());
    addInfo("License", templateData.license.getName());
    addInfo("Executor class", templateData.template.getExecutorClass());
    if (!templateData.template.getRequiredPackages().isEmpty())
      addInfo("Required packages", templateData.template.getRequiredPackages());
    m_TemplateInfo.append("</table>\n");
    m_TemplateInfo.append("</html>\n");

    // inputs
    templateData.inputTypes = new Properties();
    defValues               = new Properties();
    keys                    = new ArrayList<>(templateData.inputs.keySet());
    Collections.sort(keys);
    for (String key: keys)
      addInput(m_PropertiesPanelInputs, key, templateData, defValues, templateData.inputTypes);
    m_PropertiesPanelInputs.setPropertyOrder(keys);
    m_PropertiesPanelInputs.setProperties(defValues);

    // parameters
    templateData.parameterTypes = new Properties();
    defValues                   = new Properties();
    keys                        = new ArrayList<>();
    for (JobTemplates.Parameter parameter: templateData.parameters) {
      if (parameter.isConstant())
	continue;
      keys.add(parameter.getName());
      addParameter(m_PropertiesPanelParameters, parameter, templateData, defValues, templateData.parameterTypes);
    }
    m_PropertiesPanelParameters.setPropertyOrder(keys);
    m_PropertiesPanelParameters.setProperties(defValues);
  }

  /**
   * Hook method after the dialog got created.
   *
   * @param dialog	the dialog that got just created
   * @param panel	the panel displayed in the frame
   */
  protected void postCreateDialog(final BaseDialog dialog, final BasePanel panel) {
    BaseButton 		buttonInfo;
    BaseButton 		buttonOK;
    BaseButton		buttonCancel;
    JPanel 		panelButtons;

    panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    dialog.getContentPane().add(panelButtons, BorderLayout.SOUTH);

    buttonInfo = new BaseButton("Info");
    buttonInfo.addActionListener((ActionEvent e) ->
      GUIHelper.showInformationMessage(panel, m_TemplateInfo.toString(), "Template info", true, GUIHelper.getDefaultDialogDimension()));
    panelButtons.add(buttonInfo);

    buttonOK = new BaseButton("OK");
    buttonOK.addActionListener((ActionEvent e) -> {
      m_Accepted = true;
      dialog.setVisible(false);
    });
    panelButtons.add(buttonOK);

    buttonCancel = new BaseButton("Cancel");
    buttonCancel.addActionListener((ActionEvent e) -> {
      m_Accepted = false;
      dialog.setVisible(false);
    });
    panelButtons.add(buttonCancel);
  }

  /**
   * Fixes the type of the value if necessary.
   *
   * @param type	the desired type
   * @param value	the current value
   * @return		the potentially fixed value
   */
  protected Object fixValue(String key, String type, Object value) {
    String[]	parts;
    Boolean[]	boolArray;
    Integer[]	intArray;
    Double[]	numArray;
    int		i;

    try {
      if (value instanceof String) {
	if (type.startsWith(Types.PK)) {
	  value = Integer.parseInt((String) value);
	}
	else if (type.startsWith(Types.JOB_OUTPUT)) {
	  value = Integer.parseInt((String) value);
	}
	else if (type.equals(Types.ARRAY_STR) || type.startsWith(Types.ARRAY_STR_LIMITED)) {
	  value = Utils.unbackQuoteChars((String) value).split("\n");
	  // TODO check size
	}
	else if (type.equals(Types.ARRAY_INT) || type.startsWith(Types.ARRAY_INT_LIMITED)) {
	  parts    = ((String) value).split("\n");
	  intArray = new Integer[parts.length];
	  for (i = 0; i < parts.length; i++)
	    intArray[i] = Integer.parseInt(parts[i]);
	  value = intArray;
	  // TODO check size
	}
	else if (type.equals(Types.ARRAY_BOOL) || type.startsWith(Types.ARRAY_BOOL_LIMITED)) {
	  parts     = ((String) value).split("\n");
	  boolArray = new Boolean[parts.length];
	  for (i = 0; i < parts.length; i++)
	    boolArray[i] = Boolean.parseBoolean(parts[i]);
	  value = boolArray;
	  // TODO check size
	}
	else if (type.equals(Types.ARRAY_FLOAT) || type.startsWith(Types.ARRAY_FLOAT_LIMITED)) {
	  parts    = ((String) value).split("\n");
	  numArray = new Double[parts.length];
	  for (i = 0; i < parts.length; i++)
	    numArray[i] = Double.parseDouble(parts[i]);
	  value = numArray;
	  // TODO check size
	}
	else {
	  switch (type) {
	    case Types.PRIMITIVE_BOOL:
	      value = Boolean.parseBoolean((String) value);
	      break;
	    case Types.PRIMITIVE_INT:
	      value = Integer.parseInt((String) value);
	      break;
	    case Types.PRIMITIVE_FLOAT:
	      value = Double.parseDouble((String) value);
	      break;
	  }
	}
      }

      return value;
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to parse key/type/value: " + key + "/" + type + "/" + value);
      return value;
    }
  }

  /**
   * Creates a job from the template and the provided inputs/parameters.
   *
   * @param templateData	the template data the job is based on
   * @param description 	the job description
   * @return			the job, null if failed to create
   * @throws Exception		if API calls fails
   */
  protected Job createJob(JobTemplateData templateData, String description) throws Exception {
    Job					result;
    Map<String,Map<String,Object>>	inputs;
    Map<String,Map<String,Object>>	params;
    String				value;
    String				type;

    if (isLoggingEnabled()) {
      getLogger().info("Inputs for job: " + templateData.inputValues);
      getLogger().info("Parameters for job: " + templateData.parameterValues);
    }

    inputs = new HashMap<>();
    params = new HashMap<>();
    for (String key: templateData.inputValues.keySetAll()) {
      type  = templateData.inputTypes.getProperty(key);
      value = templateData.inputValues.getProperty(key);
      inputs.put(key, TypeValuePair.typeValuePair(type, fixValue(key, type, value)));
    }
    for (String key: templateData.parameterValues.keySetAll()) {
      type  = templateData.parameterTypes.getProperty(key);
      value = templateData.parameterValues.getProperty(key);
      params.put(key, TypeValuePair.typeValuePair(type, fixValue(key, type, value)));
    }

    result = m_Connection.getClient().jobTemplates().newJob(templateData.template.getPK(), inputs, params, description);
    if (isLoggingEnabled())
      getLogger().info("Job: " + result);

    return result;
  }

  /**
   * Performs the interaction with the user.
   *
   * @return		null if successfully interacted, otherwise error message
   */
  @Override
  public String doInteract() {
    JobTemplate 	template;
    Job			job;
    JobTemplateData	templateData;
    boolean		cancelled;
    int			retVal;
    String		msg;

    m_Accepted   = false;
    cancelled    = false;
    templateData = null;

    while (!m_Accepted && !cancelled) {
      try {
	if (m_InputToken.hasPayload(Integer.class))
	  template = m_Connection.getClient().jobTemplates().load(m_InputToken.getPayload(Integer.class));
	else
	  template = (JobTemplate) m_InputToken.getPayload();
	if (isLoggingEnabled())
	  getLogger().info("Template: " + template);
	if (template != null) {
	  templateData = new JobTemplateData(m_Connection.getClient(), template, m_ContractType);
	  updatePanel(templateData);
	}
      }
      catch (Exception e) {
	msg = "Failed to configure 'create job' view!";
	getLogger().log(Level.SEVERE, msg, e);
	return msg;
      }

      if (templateData == null) {
	msg = "Failed to collect job-template data!";
	getLogger().severe(msg);
	return msg;
      }

      registerWindow(m_Dialog, m_Dialog.getTitle());
      m_Dialog.setVisible(true);
      deregisterWindow(m_Dialog);

      cancelled = !m_Accepted;
      if (m_Accepted) {
	try {
	  templateData.inputValues = m_PropertiesPanelInputs.getProperties();
	  templateData.parameterValues = m_PropertiesPanelParameters.getProperties();
	  job = createJob(templateData, m_TextDescription.getText());
	  if (job != null)
	    m_OutputToken = new Token(job);
	  else
	    getLogger().severe("Failed to create job!");
	}
	catch (Exception e) {
	  getLogger().log(Level.SEVERE, "Failed to create job!", e);
	  retVal = GUIHelper.showConfirmMessage(getParentComponent(), "Failed to create job - try again?\n" + e.getMessage());
	  if (retVal == ApprovalDialog.CANCEL_OPTION)
	    cancelled = true;
	  if (retVal == ApprovalDialog.APPROVE_OPTION)
	    m_Accepted = false;
	}
      }
    }

    if (m_Accepted)
      return null;
    else
      return INTERACTION_CANCELED;
  }
}
