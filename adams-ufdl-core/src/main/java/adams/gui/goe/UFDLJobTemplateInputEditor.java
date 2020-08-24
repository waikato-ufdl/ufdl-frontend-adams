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
 * UFDLJobTemplateInputEditor.java
 * Copyright (C) 2020 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.goe;

import adams.core.option.parsing.BaseObjectParsing;
import adams.flow.core.UFDLJobTemplateInput;
import adams.gui.core.BaseButton;
import adams.gui.core.BaseTextField;
import adams.gui.core.GUIHelper;
import adams.gui.core.ParameterPanel;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Editor specifically designed for entering inputs for UFDL job templates.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class UFDLJobTemplateInputEditor
  extends BaseObjectEditor
  implements MultiSelectionEditor {

  /** the text field with the name. */
  protected BaseTextField m_TextName;

  /** the text field with the type. */
  protected BaseTextField m_TextType;

  /** the text field with the options. */
  protected BaseTextField m_TextOptions;

  /**
   * Gets the custom editor component.
   *
   * @return 		the editor
   */
  @Override
  protected JComponent createCustomEditor() {
    JPanel		panelAll;
    ParameterPanel  	panelParams;
    JPanel 		panelButtons;
    BaseButton 		buttonOK;
    BaseButton 		buttonClose;

    panelAll    = new JPanel(new BorderLayout());
    panelParams	= new ParameterPanel();
    panelAll.add(panelParams, BorderLayout.CENTER);
    m_TextName = new BaseTextField(30);
    panelParams.addParameter("_Name", m_TextName);
    m_TextType = new BaseTextField(30);
    panelParams.addParameter("Type", m_TextType);
    m_TextOptions = new BaseTextField(30);
    panelParams.addParameter("Options", m_TextOptions);

    panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelAll.add(panelButtons, BorderLayout.SOUTH);

    buttonOK = new BaseButton("OK");
    buttonOK.setMnemonic('O');
    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	acceptInput();
      }
    });
    panelButtons.add(buttonOK);

    buttonClose = new BaseButton("Cancel");
    buttonClose.setMnemonic('C');
    buttonClose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	discardInput();
      }
    });
    panelButtons.add(buttonClose);

    return panelAll;
  }

  /**
   * Accepts the input and closes the dialog.
   */
  protected void acceptInput() {
    String 	s;

    s = m_TextName.getText() + UFDLJobTemplateInput.SEPARATOR + m_TextType.getText() + UFDLJobTemplateInput.SEPARATOR + m_TextOptions.getText();
    if (isValid(s) && !isUnchanged(s))
      setValue(parse(s));
    closeDialog(APPROVE_OPTION);
  }

  /**
   * Initializes the display of the value.
   */
  @Override
  protected void initForDisplay() {
    UFDLJobTemplateInput input;

    resetChosenOption();
    
    input = (UFDLJobTemplateInput) getValue();

    if (!m_TextName.getText().equals(input.nameValue()))
      m_TextName.setText(input.nameValue());
    if (!m_TextType.getText().equals(input.typeValue()))
      m_TextType.setText(input.typeValue());
    if (!m_TextOptions.getText().equals(input.optionsValue()))
      m_TextOptions.setText(input.optionsValue());
    m_TextName.grabFocus();
  }

  /**
   * Returns the string to paint.
   *
   * @return		the string
   * @see		#paintValue(Graphics, Rectangle)
   */
  @Override
  protected String getStringToPaint() {
    return ((UFDLJobTemplateInput) getValue()).stringValue();
  }

  /**
   * Returns a custom string representation of the object.
   *
   * @param obj		the object to turn into a string
   * @return		the string representation
   */
  @Override
  public String toCustomStringRepresentation(Object obj) {
    return BaseObjectParsing.toString(null, obj);
  }

  /**
   * Returns an object created from the custom string representation.
   *
   * @param str		the string to turn into an object
   * @return		the object
   */
  @Override
  public Object fromCustomStringRepresentation(String str) {
    return BaseObjectParsing.valueOf(null, str);
  }

  /**
   * Returns the selected objects.
   *
   * @param parent	the parent container
   * @return		the objects
   */
  @Override
  public Object[] getSelectedObjects(Container parent) {
    UFDLJobTemplateInput[]		result;
    MultiLineValueDialog	dialog;
    List<String> 		lines;
    int				i;

    if (GUIHelper.getParentDialog(parent) != null)
      dialog = new MultiLineValueDialog(GUIHelper.getParentDialog(parent));
    else
      dialog = new MultiLineValueDialog(GUIHelper.getParentFrame(parent));
    dialog.setInfoText("Enter the input definitions, one per line:");
    dialog.setLocationRelativeTo(parent);
    dialog.setVisible(true);

    lines  = dialog.getValues();
    result = new UFDLJobTemplateInput[lines.size()];
    for (i = 0; i < lines.size(); i++)
      result[i] = (UFDLJobTemplateInput) parse(lines.get(i));

    return result;
  }
}
