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
 * LicenseFilterPanel.java
 * Copyright (C) 2020-2022 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.licenses;

import adams.core.EnumHelper;
import adams.core.net.HtmlUtils;
import adams.gui.core.BaseButton;
import adams.gui.core.BaseList;
import adams.gui.core.BaseListWithButtons;
import adams.gui.core.BasePanel;
import adams.gui.core.BaseScrollPane;
import adams.gui.help.HelpFrame;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Condition;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Domain;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Limitation;
import com.github.waikatoufdl.ufdl4j.action.Licenses.Permission;
import com.github.waikatoufdl.ufdl4j.core.CustomDisplayEnum;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allows the user to filter a list of licenses based on permissions,
 * limitations and conditions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class LicenseFilterPanel
  extends BasePanel {

  private static final long serialVersionUID = -775575078142364656L;

  /**
   * The cell renderer.
   */
  public static class EnumRenderer
    extends DefaultListCellRenderer {

    private static final long serialVersionUID = 662711521384106051L;

    /** the border for no focus. */
    protected Border m_BorderNoFocus;

    /** the border for focused. */
    protected Border m_BorderFocused;

    /**
     * Returns the rendering component.
     *
     * @param list		the list this renderer is for
     * @param value		the current list value
     * @param index		the index of the value
     * @param isSelected	whether the item is selected
     * @param cellHasFocus	whether the cell has the focus
     * @return			the rendering component
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Component		result;
      JLabel 		label;
      CustomDisplayEnum item;

      if (m_BorderNoFocus == null) {
        m_BorderNoFocus = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        m_BorderFocused = BorderFactory.createLineBorder(list.getSelectionBackground().darker(), 1);
      }

      result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      label  = (JLabel) result;
      if (cellHasFocus)
        label.setBorder(m_BorderFocused);
      else
        label.setBorder(m_BorderNoFocus);
      item = (CustomDisplayEnum) list.getModel().getElementAt(index);
      label.setText(item.toDisplay());

      return result;
    }
  }

  /**
   * For selecting items.
   */
  public static class SelectionPanel<T extends Enum>
    extends BasePanel {

    private static final long serialVersionUID = -7396270177909526762L;

    /** the label. */
    protected JLabel m_LabelInfo;

    /** the list of items. */
    protected BaseList m_ListItems;

    /** the model. */
    protected DefaultListModel<T> m_ModelItems;

    /** the underlying items. */
    protected T[] m_Items;

    /**
     * Initializes the members.
     */
    @Override
    protected void initialize() {
      super.initialize();

      m_ModelItems = new DefaultListModel<>();
      m_Items      = null;
    }

    /**
     * Initializes the widgets.
     */
    @Override
    protected void initGUI() {
      JPanel	panel;

      super.initGUI();

      setLayout(new BorderLayout());

      panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      add(panel, BorderLayout.NORTH);
      m_LabelInfo = new JLabel();
      panel.add(m_LabelInfo);

      m_ListItems = new BaseList(m_ModelItems);
      m_ListItems.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      m_ListItems.setCellRenderer(new EnumRenderer());
      add(new BaseScrollPane(m_ListItems));
    }

    /**
     * Sets the info text to display above the list.
     *
     * @param value	the text
     */
    public void setInfoText(String value) {
      m_LabelInfo.setText(value);
    }

    /**
     * Returns the info text displayed above the list.
     *
     * @return		the text
     */
    public String getInfoText() {
      return m_LabelInfo.getText();
    }

    /**
     * Sets the items to display.
     *
     * @param value	the items
     */
    public void setItems(T[] value) {
      if (value == null)
        throw new IllegalArgumentException("Items cannot be null!");
      if (value.length == 0)
        throw new IllegalArgumentException("At least one items needs to be provided!");
      m_Items = value;
      m_ModelItems.clear();
      for (T item: value)
        m_ModelItems.addElement(item);
    }

    /**
     * Sets the selected items.
     *
     * @param value	the selected items, can be null
     */
    public void setSelectedItems(T[] value) {
      TIntList	sel;
      int	i;
      Object[]	values;

      if (value == null) {
	m_ListItems.setSelectedIndices(new int[0]);
	return;
      }

      sel    = new TIntArrayList();
      values = EnumHelper.getValues(m_Items[0]);
      for (T item: value) {
	for (i = 0; i < values.length; i++) {
	  if (item.equals(values[i])) {
	    sel.add(i);
	    break;
	  }
	}
      }

      m_ListItems.setSelectedIndices(sel.toArray());
    }

    /**
     * Returns the selected items.
     *
     * @return		the selected items, null if no items set
     */
    public T[] getSelectedItems() {
      Object	result;
      int[]	sel;
      int	i;

      if (m_Items == null)
        return null;

      sel    = m_ListItems.getSelectedIndices();
      result = Array.newInstance(EnumHelper.determineClass(m_Items[0]), sel.length);
      for (i = 0; i < sel.length; i++)
        Array.set(result, i, m_Items[sel[i]]);

      return (T[]) result;
    }

    /**
     * Adds the listener for changes in the selection of items.
     *
     * @param l		the listener to add
     */
    public void addListSelectionListener(ListSelectionListener l) {
      m_ListItems.addListSelectionListener(l);
    }

    /**
     * Removes the listener for changes in the selection of items.
     *
     * @param l		the listener to remove
     */
    public void removeListSelectionListener(ListSelectionListener l) {
      m_ListItems.removeListSelectionListener(l);
    }
  }

  /**
   * The cell renderer.
   */
  public static class LicenseRenderer
    extends DefaultListCellRenderer {

    private static final long serialVersionUID = 662711521384106051L;

    /** the border for no focus. */
    protected Border m_BorderNoFocus;

    /** the border for focused. */
    protected Border m_BorderFocused;

    /**
     * Returns the rendering component.
     *
     * @param list		the list this renderer is for
     * @param value		the current list value
     * @param index		the index of the value
     * @param isSelected	whether the item is selected
     * @param cellHasFocus	whether the cell has the focus
     * @return			the rendering component
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Component		result;
      JLabel 		label;
      License 		license;

      if (m_BorderNoFocus == null) {
        m_BorderNoFocus = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        m_BorderFocused = BorderFactory.createLineBorder(list.getSelectionBackground().darker(), 1);
      }

      result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      label  = (JLabel) result;
      if (cellHasFocus)
        label.setBorder(m_BorderFocused);
      else
        label.setBorder(m_BorderNoFocus);
      license = (License) list.getModel().getElementAt(index);
      label.setText(license.getName());

      return result;
    }
  }

  /** the domains. */
  protected SelectionPanel<Domain> m_PanelDomains;

  /** the permissions. */
  protected SelectionPanel<Permission> m_PanelPermissions;

  /** the limitations. */
  protected SelectionPanel<Limitation> m_PanelLimitations;

  /** the conditions. */
  protected SelectionPanel<Condition> m_PanelConditions;

  /** the list with matching licenses. */
  protected BaseListWithButtons m_ListMatches;

  /** the model with the filtered licenses. */
  protected DefaultListModel<License> m_ModelMatches;

  /** the button for selecting all licenses. */
  protected BaseButton m_ButtonAll;

  /** the button for selecting no licenses. */
  protected BaseButton m_ButtonNone;

  /** the button for inverting the matchin. */
  protected BaseButton m_ButtonInvert;

  /** the button for displaying details of the license. */
  protected BaseButton m_ButtonLicenseDetails;

  /** the licenses. */
  protected License[] m_Licenses;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Licenses     = new License[0];
    m_ModelMatches = new DefaultListModel<>();
  }

  /**
   * Initializes the widgets.
   */
  @Override
  protected void initGUI() {
    JPanel	panel;

    super.initGUI();

    setLayout(new BorderLayout());

    // permissions/limitations/conditions
    panel = new JPanel(new GridLayout(1, 4));
    add(panel, BorderLayout.NORTH);

    m_PanelDomains = new SelectionPanel<>();
    m_PanelDomains.setItems(Domain.values());
    m_PanelDomains.setInfoText("Domain");
    m_PanelDomains.addListSelectionListener((ListSelectionEvent e) -> apply());
    panel.add(m_PanelDomains);

    m_PanelPermissions = new SelectionPanel<>();
    m_PanelPermissions.setItems(Permission.values());
    m_PanelPermissions.setInfoText("<html><font color='green'>Allows</font></html>");
    m_PanelPermissions.addListSelectionListener((ListSelectionEvent e) -> apply());
    panel.add(m_PanelPermissions);
    
    m_PanelLimitations = new SelectionPanel<>();
    m_PanelLimitations.setItems(Limitation.values());
    m_PanelLimitations.setInfoText("<html><font color='red'>No</font></html>");
    m_PanelLimitations.addListSelectionListener((ListSelectionEvent e) -> apply());
    panel.add(m_PanelLimitations);
    
    m_PanelConditions = new SelectionPanel<>();
    m_PanelConditions.setItems(Condition.values());
    m_PanelConditions.setInfoText("<html><font color='blue'>Must</font></html>");
    m_PanelConditions.addListSelectionListener((ListSelectionEvent e) -> apply());
    panel.add(m_PanelConditions);

    // matches
    m_ListMatches = new BaseListWithButtons(m_ModelMatches);
    m_ListMatches.addListSelectionListener((ListSelectionEvent e) -> updateButtons());
    m_ListMatches.getComponent().setCellRenderer(new LicenseRenderer());
    add(m_ListMatches, BorderLayout.CENTER);

    m_ButtonLicenseDetails = new BaseButton("Details");
    m_ButtonLicenseDetails.addActionListener((ActionEvent e) -> viewDetails());
    m_ListMatches.addToButtonsPanel(m_ButtonLicenseDetails);

    m_ListMatches.addToButtonsPanel(new JLabel(""));

    m_ButtonAll = new BaseButton("All");
    m_ButtonAll.addActionListener((ActionEvent e) -> m_ListMatches.selectAll());
    m_ListMatches.addToButtonsPanel(m_ButtonAll);

    m_ButtonInvert = new BaseButton("Invert");
    m_ButtonInvert.addActionListener((ActionEvent e) -> m_ListMatches.invertSelection());
    m_ListMatches.addToButtonsPanel(m_ButtonInvert);

    m_ButtonNone = new BaseButton("None");
    m_ButtonNone.addActionListener((ActionEvent e) -> m_ListMatches.selectNone());
    m_ListMatches.addToButtonsPanel(m_ButtonNone);
  }

  /**
   * Sets all the available licenses.
   *
   * @param value	the licenses
   */
  public void setLicenses(License[] value) {
    m_Licenses = value;
    apply();
  }

  /**
   * Returns all the available licenses.
   *
   * @return		the licenses
   */
  public License[] getLicenses() {
    return m_Licenses;
  }

  /**
   * Clears all the filters.
   */
  public void clearFilters() {
    setSelectedDomains(null);
    setSelectedPermissions(null);
    setSelectedLimitations(null);
    setSelectedConditions(null);
  }

  /**
   * Selects the domains.
   * 
   * @param value	the domains
   */
  public void setSelectedDomains(Domain[] value) {
    m_PanelDomains.setSelectedItems(value);
    apply();
  }

  /**
   * Returns the selected domains.
   * 
   * @return		the domains
   */
  public Domain[] getSelectedDomains() {
    return m_PanelDomains.getSelectedItems();
  }

  /**
   * Selects the permissions.
   * 
   * @param value	the permissions
   */
  public void setSelectedPermissions(Permission[] value) {
    m_PanelPermissions.setSelectedItems(value);
    apply();
  }

  /**
   * Returns the selected permissions.
   * 
   * @return		the permissions
   */
  public Permission[] getSelectedPermissions() {
    return m_PanelPermissions.getSelectedItems();
  }

  /**
   * Selects the limitations.
   * 
   * @param value	the limitations
   */
  public void setSelectedLimitations(Limitation[] value) {
    m_PanelLimitations.setSelectedItems(value);
    apply();
  }

  /**
   * Returns the selected limitations.
   * 
   * @return		the limitations
   */
  public Limitation[] getSelectedLimitations() {
    return m_PanelLimitations.getSelectedItems();
  }

  /**
   * Selects the conditions.
   * 
   * @param value	the conditions
   */
  public void setSelectedConditions(Condition[] value) {
    m_PanelConditions.setSelectedItems(value);
    apply();
  }

  /**
   * Returns the selected conditions.
   * 
   * @return		the conditions
   */
  public Condition[] getSelectedConditions() {
    return m_PanelConditions.getSelectedItems();
  }

  /**
   * Sets the licenses to select initially.
   *
   * @param value	the licenses
   */
  public void setSelectedLicenses(License[] value) {
    TIntList	sel;
    int		i;

    sel = new TIntArrayList();
    for (License l: value) {
      for (i = 0; i < m_Licenses.length; i++) {
        if (l.getName().equals(m_Licenses[i].getName())) {
	  sel.add(i);
	  break;
	}
      }
    }

    m_ListMatches.setSelectedIndices(sel.toArray());
  }

  /**
   * Returns all the currently selected licenses.
   *
   * @return		the licenses
   */
  public License[] getSelectedLicenses() {
    List<License> result;

    result = new ArrayList<>();
    for (Object value : m_ListMatches.getSelectedValuesList())
      result.add((License) value);

    return result.toArray(new License[0]);
  }

  /**
   * Displays the details of a license.
   */
  protected void viewDetails() {
    License		selected;
    StringBuilder 	content;
    String html;

    if (m_ListMatches.getSelectedIndices().length != 1)
      return;

    selected = (License) m_ListMatches.getSelectedValue();

    content = new StringBuilder();
    content.append(selected.getName()).append("\n");
    content.append("\n");
    content.append("Domains:\n");
    for (Domain p: selected.getDomains())
      content.append("- ").append(p.toDisplay()).append("\n");
    content.append("\n");
    content.append("Permissions:\n");
    for (Permission p: selected.getPermissions())
      content.append("- ").append(p.toDisplay()).append("\n");
    content.append("\n");
    content.append("Limitations:\n");
    for (Limitation l : selected.getLimitations())
      content.append("- ").append(l.toDisplay()).append("\n");
    content.append("\n");
    content.append("Conditions:\n");
    for (Condition c : selected.getConditions())
      content.append("- ").append(c.toDisplay()).append("\n");
    content.append("\n");
    content.append("Full license text:\n");
    content.append(selected.getURL());

    html = content.toString();
    html = HtmlUtils.markUpURLs(html, true);
    html = HtmlUtils.convertLines(html, false);

    HelpFrame.showHelp(selected.getName(), html, true);
  }

  /**
   * Updates the state of the buttons.
   */
  protected void updateButtons() {
    boolean	hasLicenses;

    hasLicenses = (m_Licenses.length > 0);
    m_ButtonAll.setEnabled(hasLicenses);
    m_ButtonNone.setEnabled(hasLicenses && (m_ListMatches.getSelectedIndices().length > 0));
    m_ButtonInvert.setEnabled(hasLicenses && (m_ListMatches.getSelectedIndices().length > 0));
    m_ButtonLicenseDetails.setEnabled(hasLicenses && (m_ListMatches.getSelectedIndices().length == 1));
  }

  /**
   * Returns whether the license is matching the selected permissions/limitations/conditions.
   *
   * @param license	the license to check
   * @param domains 	the domains that the license must meet
   * @param permissions	the permissions that the license must meet
   * @param limitations	the limitations that the license must meet
   * @param conditions	the conditions that the license must meet
   * @return		true if a match
   */
  protected boolean isMatch(License license, Set<Domain> domains, Set<Permission> permissions, Set<Limitation> limitations, Set<Condition> conditions) {
    if (!license.getDomains().containsAll(domains))
      return false;
    if (!license.getPermissions().containsAll(permissions))
      return false;
    if (!license.getLimitations().containsAll(limitations))
      return false;
    if (!license.getConditions().containsAll(conditions))
      return false;

    return true;
  }

  /**
   * Applies the selected permissions/limitations/conditions to the available
   * licenses.
   */
  protected void apply() {
    List<License>	matches;
    Set<Domain>		domains;
    Set<Permission> 	permissions;
    Set<Limitation>	limitations;
    Set<Condition>	conditions;

    domains     = new HashSet<>(Arrays.asList(m_PanelDomains.getSelectedItems()));
    permissions = new HashSet<>(Arrays.asList(m_PanelPermissions.getSelectedItems()));
    limitations = new HashSet<>(Arrays.asList(m_PanelLimitations.getSelectedItems()));
    conditions  = new HashSet<>(Arrays.asList(m_PanelConditions.getSelectedItems()));

    matches = new ArrayList<>();
    for (License license: m_Licenses) {
      if (isMatch(license, domains, permissions, limitations, conditions))
        matches.add(license);
    }

    m_ListMatches.getSelectionModel().clearSelection();
    m_ModelMatches.clear();
    for (License license: matches)
      m_ModelMatches.addElement(license);

    updateButtons();
  }

  /**
   * Sets the selection mode.
   *
   * @param value	whether to select single or more licenses
   */
  public void setSelectionMode(int value) {
    m_ListMatches.setSelectionMode(value);
    m_ButtonAll.setVisible(value != ListSelectionModel.SINGLE_SELECTION);
    m_ButtonInvert.setVisible(value != ListSelectionModel.SINGLE_SELECTION);
    m_ButtonNone.setVisible(value != ListSelectionModel.SINGLE_SELECTION);
  }

  /**
   * Returns the selection mode.
   *
   * @return		whether to select single or more licenses
   */
  public int getSelectionMode() {
    return m_ListMatches.getSelectionMode();
  }
}
