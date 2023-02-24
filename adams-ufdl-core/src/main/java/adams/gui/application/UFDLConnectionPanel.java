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
 * UFDLConnectionPanel.java
 * Copyright (C) 2019-2023 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.application;

import adams.core.Constants;
import adams.core.Properties;
import adams.core.base.BasePassword;
import adams.core.base.BaseURL;
import adams.core.io.FileUtils;
import adams.env.Environment;
import adams.env.UfdlDefinition;
import adams.gui.core.BaseCheckBox;
import adams.gui.core.BaseObjectTextField;
import adams.gui.core.BasePasswordField;
import adams.gui.core.BaseTextField;
import adams.gui.core.ParameterPanel;
import adams.ml.ufdl.UfdlHelper;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

/**
 * Panel for configuring the system-wide UFDL settings.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class UFDLConnectionPanel
  extends AbstractPreferencesPanel {

  /** for serialization. */
  private static final long serialVersionUID = -7937644706618374284L;

  /** the parameters. */
  protected ParameterPanel m_PanelParameters;

  /** the default UFDL host. */
  protected BaseObjectTextField<BaseURL> m_TextHost;

  /** the username. */
  protected BaseTextField m_TextUser;

  /** the SMTP password. */
  protected BasePasswordField m_TextPassword;

  /** Whether to show the password. */
  protected BaseCheckBox m_CheckBoxShowPassword;

  /**
   * Initializes the members.
   */
  @Override
  protected void initGUI() {
    super.initGUI();

    setLayout(new BorderLayout());

    m_PanelParameters = new ParameterPanel();
    add(m_PanelParameters, BorderLayout.CENTER);

    m_TextHost = new BaseObjectTextField<>(new BaseURL());
    m_TextHost.setObject(new BaseURL(UfdlHelper.getHost()));
    m_PanelParameters.addParameter("_Host", m_TextHost);

    m_TextUser = new BaseTextField(10);
    m_TextUser.setText(UfdlHelper.getUser());
    m_PanelParameters.addParameter("_User", m_TextUser);

    m_TextPassword = new BasePasswordField(20);
    m_TextPassword.setText(UfdlHelper.getPassword().getValue());
    m_TextPassword.setEchoChar(Constants.PASSWORD_CHAR);
    m_PanelParameters.addParameter("_Password", m_TextPassword);

    m_CheckBoxShowPassword = new BaseCheckBox();
    m_CheckBoxShowPassword.setSelected(false);
    m_CheckBoxShowPassword.addActionListener((ActionEvent e) -> {
      if (m_CheckBoxShowPassword.isSelected())
        m_TextPassword.setEchoChar((char) 0);
      else
        m_TextPassword.setEchoChar(Constants.PASSWORD_CHAR);
    });
    m_PanelParameters.addParameter("Sho_w Password", m_CheckBoxShowPassword);
  }

  /**
   * Turns the parameters in the GUI into a properties object.
   *
   * @return		the properties
   */
  protected Properties toProperties() {
    Properties	result;

    result = new Properties();

    result.setProperty(UfdlHelper.HOST, m_TextHost.getObject().getValue());
    result.setProperty(UfdlHelper.USER, m_TextUser.getText());
    result.setPassword(UfdlHelper.PASSWORD, new BasePassword(new String(m_TextPassword.getPassword())));

    return result;
  }

  /**
   * The title of the preference panel.
   * 
   * @return		the title
   */
  @Override
  public String getTitle() {
    return "UFDL";
  }

  /**
   * Returns whether the panel requires a wrapper scrollpane/panel for display.
   * 
   * @return		true if wrapper required
   */
  @Override
  public boolean requiresWrapper() {
    return true;
  }
  
  /**
   * Activates the twitter setup.
   * 
   * @return		null if successfully activated, otherwise error message
   */
  @Override
  public String activate() {
    boolean	result;

    result = UfdlHelper.writeProperties(toProperties());
    if (result)
      return null;
    else
      return "Failed to save UFDL setup to " + UfdlHelper.FILENAME + "!";
  }

  /**
   * Returns whether the panel supports resetting the options.
   *
   * @return		true if supported
   */
  public boolean canReset() {
    String	props;

    props = Environment.getInstance().getCustomPropertiesFilename(UfdlDefinition.KEY);
    return (props != null) && FileUtils.fileExists(props);
  }

  /**
   * Resets the settings to their default.
   *
   * @return		null if successfully reset, otherwise error message
   */
  public String reset() {
    String	props;

    props = Environment.getInstance().getCustomPropertiesFilename(UfdlDefinition.KEY);
    if ((props != null) && FileUtils.fileExists(props)) {
      if (!FileUtils.delete(props))
	return "Failed to remove custom UFDL properties: " + props;
    }

    return null;
  }
}
