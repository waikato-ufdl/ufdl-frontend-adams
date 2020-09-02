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
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.transformer;

import adams.core.ObjectCopyHelper;
import adams.core.Properties;
import adams.core.Utils;
import adams.data.ufdlfilter.DomainFilter;
import adams.data.ufdlfilter.GenericFilter;
import adams.data.ufdlfilter.OrderBy;
import adams.data.ufdlfilter.field.ExactInteger;
import adams.data.ufdlfilter.logical.And;
import adams.flow.core.ActorUtils;
import adams.flow.core.Token;
import adams.flow.core.UFDLListSorting;
import adams.flow.source.valuedefinition.AbstractUFDLSpreadSheetBasedSoftDeleteValueDefinition;
import adams.flow.source.valuedefinition.UFDLDatasetChooser;
import adams.flow.source.valuedefinition.UFDLDockerImageChooser;
import adams.flow.source.valuedefinition.UFDLPretrainedModelChooser;
import adams.flow.standalone.UFDLConnection;
import adams.gui.core.BaseButton;
import adams.gui.core.BaseDialog;
import adams.gui.core.BasePanel;
import adams.gui.core.BaseTextField;
import adams.gui.core.ParameterPanel;
import adams.gui.core.PropertiesParameterPanel;
import adams.gui.core.PropertiesParameterPanel.PropertyType;
import com.github.waikatoufdl.ufdl4j.action.Domains.Domain;
import com.github.waikatoufdl.ufdl4j.action.Frameworks.Framework;
import com.github.waikatoufdl.ufdl4j.action.JobTemplates.JobTemplate;
import com.github.waikatoufdl.ufdl4j.action.Jobs.Job;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 <!-- globalinfo-start -->
 * Generates a dialog for the user to fill based on the received job template. This input is used to generate the job.
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
 * </pre>
 *
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console; Note: the enclosing
 * &nbsp;&nbsp;&nbsp;actor handler must have this enabled as well.
 * &nbsp;&nbsp;&nbsp;default: false
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
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class UFDLCreateJob
  extends AbstractInteractiveTransformerDialog {

  private static final long serialVersionUID = 7467922709474210365L;

  public static final String PROPS_PREFIX_INPUT = "input-";

  public static final String PROPS_PREFIX_PARAM = "param-";

  public static final String PROPS_SUFFIX_DOCKERIMAGE = "dockerimage";

  public static final String TYPE_BOOL = "bool";

  public static final String TYPE_INT = "int";

  public static final String TYPE_FLOAT = "float";

  public static final String TYPE_STR = "str";

  public static final String TYPE_DATASET = "dataset";

  public static final String TYPE_MODEL = "model";

  public static final String PROPS_DESCRIPTION = "description";

  /** the connection to use. */
  protected transient UFDLConnection m_Connection;

  /** the job template info. */
  protected ParameterPanel m_PanelInfo;

  /** the properties parameter panel. */
  protected PropertiesParameterPanel m_PropertiesPanel;

  /** whether the dialog got accepted. */
  protected boolean m_Accepted;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Generates a dialog for the user to fill based on the received job template. This input is used to generate the job.";
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
    if (m_PropertiesPanel != null)
      m_PropertiesPanel.clearPropertyTypes();
  }

  /**
   * Creates the panel to display in the dialog.
   *
   * @return		the panel
   */
  @Override
  protected BasePanel newPanel() {
    BasePanel 	result;

    result = new BasePanel(new BorderLayout());

    m_PanelInfo = new ParameterPanel();
    m_PanelInfo.setBorder(BorderFactory.createTitledBorder("Template info"));
    result.add(m_PanelInfo, BorderLayout.NORTH);

    m_PropertiesPanel = new PropertiesParameterPanel();
    result.add(m_PropertiesPanel, BorderLayout.CENTER);

    return result;
  }

  /**
   * Interprets the input/parameter specs and adds them to the panel.
   *
   * @param isInput	whether input or parameter
   * @param specs	the specs to use
   * @param props	for storing default values
   * @param domain	the data domain
   * @param framework 	the framework
   * @throws Exception	if setting up of panel fails
   */
  protected void addToPanel(boolean isInput, List<Map<String,String>> specs, Properties props, Domain domain, Framework framework) throws Exception {
    String			name;
    String			type;
    String			value;
    String			help;
    AbstractUFDLSpreadSheetBasedSoftDeleteValueDefinition dataset;
    UFDLPretrainedModelChooser 	model;
    UFDLDockerImageChooser	dockerImage;
    String 			propsPrefix;
    String			displPrefix;
    DomainFilter		domainFilter;
    GenericFilter		genericFilter;

    // filter
    domainFilter = new DomainFilter();
    domainFilter.setDomain(domain.getPK());

    if (isInput) {
      propsPrefix = PROPS_PREFIX_INPUT;
      displPrefix = "Input: ";
    }
    else {
      propsPrefix = PROPS_PREFIX_PARAM;
      displPrefix = "Parameter: ";
    }

    for (Map<String,String> spec : specs) {
      name  = spec.get("name");
      type  = spec.get("type");
      help  = spec.getOrDefault("help", "");
      value = "";
      if (!isInput)
	value = spec.getOrDefault("default", "");
      m_PropertiesPanel.setLabel(propsPrefix + name, displPrefix + name);
      if (!help.isEmpty())
        m_PropertiesPanel.setHelp(propsPrefix + name, help);
      switch (type) {
	case TYPE_BOOL:
	  m_PropertiesPanel.addPropertyType(propsPrefix + name, PropertyType.BOOLEAN);
	  if (isInput)
	    value = "false";
	  break;
	case TYPE_INT:
	  m_PropertiesPanel.addPropertyType(propsPrefix + name, PropertyType.INTEGER);
	  if (isInput)
	    value = "-1";
	  break;
	case TYPE_FLOAT:
	  m_PropertiesPanel.addPropertyType(propsPrefix + name, PropertyType.DOUBLE);
	  if (isInput)
	    value = "0.0";
	  break;
	case TYPE_STR:
	  m_PropertiesPanel.addPropertyType(propsPrefix + name, PropertyType.STRING);
	  break;
	case TYPE_DATASET:
	  dataset = new UFDLDatasetChooser();
	  dataset.setFilter(domainFilter);
	  dataset.setSorting(UFDLListSorting.BY_ID_ONLY);
	  dataset.setFlowContext(this);
	  dataset.setName(propsPrefix + name);
	  dataset.setDisplay(displPrefix + name);
	  dataset.addToPanel(m_PropertiesPanel);
	  if (isInput)
	    value = "-1";
	  break;
	case TYPE_MODEL:
	  model = new UFDLPretrainedModelChooser();
	  model.setFilter(ObjectCopyHelper.copyObject(domainFilter));
	  model.setFlowContext(this);
	  model.setName(propsPrefix + name);
	  model.setDisplay(displPrefix + name);
	  model.addToPanel(m_PropertiesPanel);
	  if (isInput)
	    value = "-1";
	  break;
	default:
	  getLogger().warning("Unhandled type '" + type + "' for input '" + name + "'!");
	  m_PropertiesPanel.addPropertyType(propsPrefix + name, PropertyType.STRING);
	  break;
      }
      props.setProperty(propsPrefix + name, value);
    }

    // docker image
    if (!isInput) {
      name  = PROPS_SUFFIX_DOCKERIMAGE;
      value = "-1";
      // filter for domain/framework
      genericFilter = new GenericFilter()
	.addExpression(new And()
	  .addSubExpression(new ExactInteger("domain", domain.getPK()))
	  .addSubExpression(new ExactInteger("framework", framework.getPK()))
	)
	.addOrder(new OrderBy("name"));
      dockerImage = new UFDLDockerImageChooser();
      dockerImage.setSorting(UFDLListSorting.BY_ID_ONLY);
      dockerImage.setFlowContext(this);
      dockerImage.setName(propsPrefix + name);
      dockerImage.setDisplay(displPrefix + name);
      dockerImage.setFilter(genericFilter);
      dockerImage.setHelp("The docker image to use for executing the job.");
      dockerImage.addToPanel(m_PropertiesPanel);
      props.setProperty(propsPrefix + name, value);
    }
  }

  /**
   * Creates a new read-only text field with the specified text and adds it to the info panel.
   *
   * @param label	the label to use
   * @param text	the text to display
   */
  protected void addInfo(String label, String text) {
    BaseTextField 	textField;

    textField = new BaseTextField();
    textField.setEditable(false);
    textField.setText(text);

    m_PanelInfo.addParameter(label, textField);
  }

  /**
   * Populates the properties panel using the job template.
   *
   * @param template	the template to use
   * @throws Exception	if setting up of panel fails
   */
  protected void updatePanel(JobTemplate template) throws Exception {
    Domain 		domain;
    Framework		framework;
    License 		license;
    Properties 		props;

    // clear
    m_PanelInfo.clearParameters();
    m_PropertiesPanel.clearPropertyTypes();

    // resolve
    domain = m_Connection.getClient().domains().load(template.getDomain());
    if (domain == null)
      throw new IllegalStateException("Cannot resolve domain: " + template.getDomain());
    framework = m_Connection.getClient().frameworks().load(template.getFramework());
    if (framework == null)
      throw new IllegalStateException("Failed to load framework: " + template.getFramework());
    license = m_Connection.getClient().licenses().load(template.getLicense());
    if (license == null)
      throw new IllegalStateException("Failed to load license: " + template.getLicense());

    // info
    addInfo("Name", template.getName());
    addInfo("Version", "" + template.getVersion());
    addInfo("Domain", template.getDomain());
    addInfo("Framework", framework.getName() + "/" + framework.getVersion());
    addInfo("License", license.getName());
    addInfo("Executor class", template.getExecutorClass());
    if (!template.getRequiredPackages().isEmpty())
      addInfo("Required packages", template.getRequiredPackages());

    props = new Properties();
    addToPanel(true, template.getInputs(), props, domain, framework);
    addToPanel(false, template.getParameters(), props, domain, framework);
    m_PropertiesPanel.addPropertyType(PROPS_DESCRIPTION, PropertyType.STRING);
    m_PropertiesPanel.setLabel(PROPS_DESCRIPTION, "Description");
    props.setProperty(PROPS_DESCRIPTION, "");
    m_PropertiesPanel.setProperties(props);
  }

  /**
   * Hook method after the dialog got created.
   *
   * @param dialog	the dialog that got just created
   * @param panel	the panel displayed in the frame
   */
  protected void postCreateDialog(final BaseDialog dialog, BasePanel panel) {
    BaseButton buttonOK;
    BaseButton	buttonCancel;
    JPanel panelButtons;

    panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    dialog.getContentPane().add(panelButtons, BorderLayout.SOUTH);

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
   * Creates a job from the template and the provided inputs/parameters.
   *
   * @param template	the template the job is based on
   * @param props	the inputs/parameters selected by the user
   * @return		the job, null if failed to create
   * @throws Exception	if API calls fails
   */
  protected Job createJob(JobTemplate template, Properties props) throws Exception {
    Job			result;
    int			dockerImage;
    Map<String,String>	inputs;
    Map<String,String>	params;
    String		name;
    String		value;
    String		description;

    if (isLoggingEnabled())
      getLogger().info("Data for job: " + props);

    dockerImage = -1;
    inputs      = new HashMap<>();
    params      = new HashMap<>();
    description = props.getProperty(PROPS_DESCRIPTION, "");
    for (String key: props.keySetAll()) {
      value = props.getProperty(key);
      if (key.startsWith(PROPS_PREFIX_INPUT)) {
        name = key.substring(PROPS_PREFIX_INPUT.length());
	inputs.put(name, value);
      }
      else if (key.startsWith(PROPS_PREFIX_PARAM)) {
        name = key.substring(PROPS_PREFIX_PARAM.length());
        if (name.equals(PROPS_SUFFIX_DOCKERIMAGE))
	  dockerImage = props.getInteger(key, -1);
	else
	  params.put(name, value);
      }
    }

    result = m_Connection.getClient().jobTemplates().newJob(template.getPK(), dockerImage, inputs, params, description);
    if (isLoggingEnabled())
      getLogger().info("Job: " + result);

    return result;
  }

  /**
   * Performs the interaction with the user.
   *
   * @return		true if successfully interacted
   */
  @Override
  public boolean doInteract() {
    JobTemplate 	template;
    Job			job;

    m_Accepted = false;

    try {
      if (m_InputToken.hasPayload(Integer.class))
	template = m_Connection.getClient().jobTemplates().load(m_InputToken.getPayload(Integer.class));
      else
	template = (JobTemplate) m_InputToken.getPayload();
      if (isLoggingEnabled())
        getLogger().info("Template: " + template);
      if (template != null)
	updatePanel(template);
    }
    catch (Exception e) {
      getLogger().log(Level.SEVERE, "Failed to configure view!", e);
      return false;
    }

    registerWindow(m_Dialog, m_Dialog.getTitle());
    m_Dialog.setVisible(true);
    deregisterWindow(m_Dialog);

    if (m_Accepted) {
      try {
        job = createJob(template, m_PropertiesPanel.getProperties());
	if (job != null)
	  m_OutputToken = new Token(job);
	else
	  getLogger().severe("Failed to create job!");
      }
      catch (Exception e) {
        getLogger().log(Level.SEVERE, "Failed to create job!", e);
        return false;
      }
    }

    return m_Accepted;
  }
}
