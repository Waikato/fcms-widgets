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
 * PropertiesParameterPanel.java
 * Copyright (C) 2013-2017 University of Waikato, Hamilton, New Zealand
 */
package nz.ac.waikato.cms.gui.core;

import nz.ac.waikato.cms.core.FileUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Displays all properties in a props file as parameters (alphabetically
 * sorted if no custom order for properties provided).
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @see #setPropertyOrder(List)
 */
public class PropertiesParameterPanel
  extends BasePanel {

  /** for serialization. */
  private static final long serialVersionUID = -822178750857036833L;

  /** the default width for choosers. */
  public final static int DEFAULT_WIDTH_CHOOSERS = 250;

  /**
   * The various data types a property can have.
   *
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   * @version $Revision$
   */
  public enum PropertyType {
    /** boolean. */
    BOOLEAN,
    /** integer. */
    INTEGER,
    /** long. */
    LONG,
    /** double. */
    DOUBLE,
    /** string. */
    STRING,
    /** file. */
    FILE,
    /** file (absolute path). */
    FILE_ABSOLUTE,
    /** directory. */
    DIRECTORY,
    /** directory (absolute path). */
    DIRECTORY_ABSOLUTE,
    /** manually maintained list of string values. */
    LIST,
    /** string representing a comma-separated list (fixed list). */
    COMMA_SEPARATED_LIST_FIXED,
    /** string representing a blank-separated list (fixed list). */
    BLANK_SEPARATED_LIST_FIXED,
    /** custom component. */
    CUSTOM_COMPONENT,
  }

  /** the panel for the properties. */
  protected ParameterPanel m_PanelProperties;

  /** the identifiers of the property. */
  protected List<String> m_Identifiers;

  /** the property/property type relation. */
  protected HashMap<String,PropertyType> m_PropertyTypes;

  /** the actual property/property type relation. */
  protected HashMap<String,PropertyType> m_ActualPropertyTypes;

  /** the property/chooser relation. */
  protected HashMap<String,AbstractChooserPanel> m_Choosers;

  /** the property/lists relation. */
  protected HashMap<String,String[]> m_Lists;

  /** the property/help relation. */
  protected HashMap<String,String> m_Help;

  /** the property/label relation. */
  protected HashMap<String,String> m_Label;

  /** the property/component relation. */
  protected HashMap<String,Component> m_Component;

  /** the custom order for the properties. */
  protected List<String> m_Order;

  /** the panel for the buttons. */
  protected JPanel m_PanelButtons;

  /** the load props button. */
  protected JButton m_ButtonLoad;

  /** the save props button. */
  protected JButton m_ButtonSave;

  /** the filechooser for loading/saving properties. */
  protected BaseFileChooser m_FileChooser;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Identifiers         = new ArrayList<>();
    m_PropertyTypes       = new HashMap<>();
    m_ActualPropertyTypes = new HashMap<>();
    m_Choosers            = new HashMap<>();
    m_Lists               = new HashMap<>();
    m_Help                = new HashMap<>();
    m_Label               = new HashMap<>();
    m_Component           = new HashMap<>();
    m_Order               = new ArrayList<>();
    m_FileChooser         = null;
  }

  /**
   * For initializing the GUI.
   */
  @Override
  protected void initGUI() {
    JPanel	panel;

    super.initGUI();

    setLayout(new BorderLayout());

    m_PanelProperties = new ParameterPanel();
    add(new BaseScrollPane(m_PanelProperties), BorderLayout.CENTER);

    m_PanelButtons = new JPanel(new BorderLayout());
    add(m_PanelButtons, BorderLayout.SOUTH);

    panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    m_PanelButtons.add(panel, BorderLayout.WEST);

    m_ButtonLoad = new JButton(GUIHelper.getIcon("open.gif"));
    m_ButtonLoad.addActionListener((ActionEvent e) -> loadProperties());
    panel.add(m_ButtonLoad);

    m_ButtonSave = new JButton(GUIHelper.getIcon("save.gif"));
    m_ButtonSave.addActionListener((ActionEvent e) -> saveProperties());
    panel.add(m_ButtonSave);
  }

  /**
   * finishes the initialization.
   */
  @Override
  protected void finishInit() {
    super.finishInit();
    setButtonPanelVisible(false);
  }

  /**
   * Removes all property/property type relations.
   */
  public void clearPropertyTypes() {
    m_PropertyTypes.clear();
    m_ActualPropertyTypes.clear();
    m_Choosers.clear();
    m_Lists.clear();
    m_Help.clear();
  }

  /**
   * Removes all properties.
   */
  protected void clearProperties() {
    m_Identifiers.clear();
    m_PanelProperties.clearParameters();
  }

  /**
   * Adds a property.
   *
   * @param identifier	the unique identifier of the property
   * @param label	the label to add, the mnemonic to use is preceded by "_"
   * @param comp	the component to add
   * @throws IllegalArgumentException	if the identifier already exists
   */
  public void addProperty(String identifier, String label, Component comp) {
    if (m_Identifiers.contains(identifier))
      throw new IllegalArgumentException("Identifier '" + identifier + "' already present!");
    m_Identifiers.add(identifier);
    m_PanelProperties.addParameter(label, comp);
  }

  /**
   * Adds the chooser panel at the end.
   *
   * @param identifier	the unique identifier of the property
   * @param label	the label to add, the mnemonic to use is preceded by "_"
   * @param chooser	the chooser panel to add
   * @throws IllegalArgumentException	if the identifier already exists
   */
  public void addProperty(String identifier, String label, AbstractChooserPanel chooser) {
    if (m_Identifiers.contains(identifier))
      throw new IllegalArgumentException("Identifier '" + identifier + "' already present!");
    m_Identifiers.add(identifier);
    m_PanelProperties.addParameter(label, chooser);
  }

  /**
   * Returns the component at the specified location.
   *
   * @param index	the index of the specified location
   * @return		the component at the position
   */
  public Component getProperty(int index) {
    return m_PanelProperties.getParameter(index);
  }

  /**
   * Returns the component associated with the identifier.
   *
   * @param identifier	the identifier of the property to return
   * @return		the associated component, null if none found
   */
  public Component getProperty(String identifier) {
    int		index;

    index = m_Identifiers.indexOf(identifier);
    if (index == -1)
      return null;
    else
      return m_PanelProperties.getParameter(index);
  }

  /**
   * Returns the number of properties currently displayed.
   *
   * @return		the number of properties
   */
  public int getPropertyCount() {
    return m_PanelProperties.getParameterCount();
  }

  /**
   * Associates the property type with the specified property.
   *
   * @param property	the property to associate a type with
   * @param type	the property type
   */
  public void addPropertyType(String property, PropertyType type) {
    m_PropertyTypes.put(property, type);
  }

  /**
   * Checks whether a property type has been specified for a particular
   * property.
   *
   * @param property	the property to associate a type with
   * @return		true if a type has been specified
   */
  public boolean hasPropertyType(String property) {
    return m_PropertyTypes.containsKey(property);
  }

  /**
   * Checks whether a property type has been specified for a particular
   * property.
   *
   * @param property	the property to associate a type with
   * @return		true if a type has been specified
   */
  public PropertyType getPropertyType(String property) {
    if (hasPropertyType(property))
      return m_PropertyTypes.get(property);
    else
      return PropertyType.STRING;
  }

  /**
   * Checks whether a property type has been specified for a particular
   * property.
   *
   * @param property	the property to associate a type with
   * @return		true if a type has been specified
   */
  public PropertyType getActualPropertyType(String property) {
    if (m_ActualPropertyTypes.containsKey(property))
      return m_ActualPropertyTypes.get(property);
    else
      return PropertyType.STRING;
  }

  /**
   * Sets the order for the properties.
   *
   * @param value	the ordered property names
   */
  public void setPropertyOrder(String[] value) {
    setPropertyOrder(Arrays.asList(value));
  }

  /**
   * Sets the order for the properties.
   *
   * @param value	the ordered property names
   */
  public void setPropertyOrder(List<String> value) {
    m_Order.clear();
    m_Order.addAll(value);
  }

  /**
   * Returns the order for the properties.
   *
   * @return		the ordered property names
   */
  public List<String> getPropertyOrder() {
    return m_Order;
  }

  /**
   * Checks whether a chooser has been specified for a particular
   * property.
   *
   * @param property	the property check
   * @return		true if a chooser has been specified
   */
  public boolean hasChooser(String property) {
    return m_Choosers.containsKey(property);
  }

  /**
   * Associates the chooser with a particular property.
   *
   * @param property	the property to associate the chooser with
   * @param value	the chooser to use
   */
  public void setChooser(String property, AbstractChooserPanel value) {
    m_Choosers.put(property, value);
  }

  /**
   * Returns the chooser associated with a particular
   * property.
   *
   * @param property	the property to get the chooser for
   * @return		the chooser, null if none available
   */
  public AbstractChooserPanel getChooser(String property) {
    return m_Choosers.get(property);
  }

  /**
   * Checks whether a custom component has been specified for a particular
   * property.
   *
   * @param property	the property check
   * @return		true if a custom component has been specified
   */
  public boolean hasComponent(String property) {
    return m_Component.containsKey(property);
  }

  /**
   * Associates the custom component with a particular property.\
   * Must have setText(String) and getText() methods!
   *
   * @param property	the property to associate the chooser with
   * @param value	the custom component to use
   */
  public void setComponent(String property, Component value) {
    m_Component.put(property, value);
  }

  /**
   * Returns the custom component associated with a particular
   * property.
   *
   * @param property	the property to get the chooser for
   * @return		the custom component, null if none available
   */
  public Component getComponent(String property) {
    return m_Component.get(property);
  }

  /**
   * Checks whether a list has been specified for a particular
   * property.
   *
   * @param property	the property check
   * @return		true if a list has been specified
   */
  public boolean hasList(String property) {
    return m_Lists.containsKey(property);
  }

  /**
   * Associates the list with a particular property.
   *
   * @param property	the property to associate the list with
   * @param value	the list to use
   */
  public void setList(String property, String[] value) {
    m_Lists.put(property, value);
  }

  /**
   * Returns the list associated with a particular
   * property.
   *
   * @param property	the property to get the list for
   * @return		the list, null if none available
   */
  public String[] getList(String property) {
    return m_Lists.get(property);
  }

  /**
   * Checks whether a help has been specified for a particular
   * property.
   *
   * @param property	the property check
   * @return		true if a help has been specified
   */
  public boolean hasHelp(String property) {
    return m_Help.containsKey(property);
  }

  /**
   * Associates the help with a particular property.
   *
   * @param property	the property to associate the help with
   * @param value	the help to use
   */
  public void setHelp(String property, String value) {
    m_Help.put(property, value);
  }

  /**
   * Returns the help associated with a particular
   * property.
   *
   * @param property	the property to get the help for
   * @return		the help, null if none available
   */
  public String getHelp(String property) {
    return m_Help.get(property);
  }

  /**
   * Checks whether a label has been specified for a particular
   * property.
   *
   * @param property	the property check
   * @return		true if a label has been specified
   */
  public boolean hasLabel(String property) {
    return m_Label.containsKey(property);
  }

  /**
   * Associates the label with a particular property.
   *
   * @param property	the property to associate the label with
   * @param value	the label to use
   */
  public void setLabel(String property, String value) {
    m_Label.put(property, value);
  }

  /**
   * Returns the label associated with a particular
   * property.
   *
   * @param property	the property to get the label for
   * @return		the label, null if none available
   */
  public String getLabel(String property) {
    return m_Label.get(property);
  }

  /**
   * Makes sure that the specified property type can be actually displayed.
   *
   * @param key		the property
   * @param type	the type
   * @return		the (potentially) fixed type
   */
  protected PropertyType fixPropertyType(String key, PropertyType type) {
    if (type == PropertyType.CUSTOM_COMPONENT) {
      if (!hasComponent(key))
	type = PropertyType.STRING;
    }
    else if (type == PropertyType.LIST) {
      if (!hasList(key))
	type = PropertyType.STRING;
    }
    return type;
  }

  /**
   * Updates the tool tip.
   *
   * @param comp	the component to update
   * @param help	the tip text to use
   */
  protected void updateToolTipText(Component comp, String help) {
    if (comp instanceof JComponent)
      ((JComponent) comp).setToolTipText(help);
  }

  /**
   * Updates the text of the component.
   *
   * @param comp	the component to update
   * @param text	the text to set
   */
  protected void setText(Component comp, String text) {
    Method	method;

    if (comp instanceof JTextComponent) {
      ((JTextComponent) comp).setText(text);
    }
    else {
      try {
	method = comp.getClass().getMethod("setText", String.class);
	method.invoke(comp, text);
      }
      catch (Exception e) {
	throw new IllegalStateException("Class " + comp.getClass().getName() + " has no setText(String) method!");
      }
    }
  }

  /**
   * Returns the text from the component.
   *
   * @param comp	the component to get the text from
   * @return		the obtained text
   */
  protected String getText(Component comp) {
    Method	method;

    if (comp instanceof JTextComponent) {
      return ((JTextComponent) comp).getText();
    }
    else {
      try {
	method = comp.getClass().getMethod("getText");
	return (String) method.invoke(comp);
      }
      catch (Exception e) {
	throw new IllegalStateException("Class " + comp.getClass().getName() + " has no getText() method that returns a String object!");
      }
    }
  }

  /**
   * Sets the properties to base the properties on.
   *
   * @param value	the properties to use
   */
  public void setProperties(Properties value) {
    List<String>		keys;
    JCheckBox			checkbox;
    JSpinner			spinner;
    PropertyType		type;
    DirectoryChooserPanel	dirPanel;
    FileChooserPanel		filePanel;
    JComboBox			combo;
    Component			comp;
    String			help;
    String			label;

    clearProperties();
    keys = new ArrayList<>(value.stringPropertyNames());
    keys.removeAll(m_Order);
    Collections.sort(keys);
    keys.addAll(0, m_Order);
    for (String key: keys) {
      type = fixPropertyType(key, getPropertyType(key));
      help = getHelp(key);

      m_ActualPropertyTypes.put(key, type);
      label = hasLabel(key) ? getLabel(key) : key;

      try {
        switch (type) {
          case DOUBLE: {
            final JTextField textfield = new JTextField(20);
            textfield.setText(value.getProperty(key));
            textfield.setToolTipText(help);
            textfield.setBorder(BorderFactory.createEtchedBorder());
            textfield.getDocument().addDocumentListener(new DocumentListener() {
              @Override
              public void removeUpdate(DocumentEvent e) {
                check(e);
              }
              @Override
              public void insertUpdate(DocumentEvent e) {
                check(e);
              }
              @Override
              public void changedUpdate(DocumentEvent e) {
                check(e);
              }
              protected void check(DocumentEvent e) {
                String text = textfield.getText();
                boolean isDouble = true;
                try {
                  Double.parseDouble(text);
		}
		catch (Exception ex) {
                  isDouble = false;
		}
                if ((text.length() == 0) || isDouble)
                  textfield.setBorder(BorderFactory.createEtchedBorder());
                else
                  textfield.setBorder(BorderFactory.createLineBorder(Color.RED));
              }
            });
            addProperty(key, label, textfield);
            break;
          }
          case STRING:
            final JTextField textfield = new JTextField();
            textfield.setToolTipText(help);
            addProperty(key, label, textfield);
            break;
          case BOOLEAN:
            checkbox = new JCheckBox();
            checkbox.setSelected(Boolean.parseBoolean(value.getProperty(key)));
            checkbox.setToolTipText(help);
            addProperty(key, label, checkbox);
            break;
          case INTEGER:
            spinner = new JSpinner();
            spinner.setValue(Integer.parseInt(value.getProperty(key)));
            spinner.setToolTipText(help);
            addProperty(key, label, spinner);
            break;
          case LONG:
            spinner = new JSpinner();
            spinner.setValue(Long.parseLong(value.getProperty(key)));
            spinner.setToolTipText(help);
            addProperty(key, label, spinner);
            break;
          case DIRECTORY:
          case DIRECTORY_ABSOLUTE:
            dirPanel = new DirectoryChooserPanel();
            dirPanel.setCurrent(new File(value.getProperty(key)));
            dirPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH_CHOOSERS, dirPanel.getPreferredSize().height));
            dirPanel.setToolTipText(help);
            dirPanel.setInlineEditingEnabled(true);
            addProperty(key, label, dirPanel);
            break;
          case FILE:
          case FILE_ABSOLUTE:
            filePanel = new FileChooserPanel();
            filePanel.setCurrent(new File(value.getProperty(key)));
            filePanel.setPreferredSize(new Dimension(DEFAULT_WIDTH_CHOOSERS, filePanel.getPreferredSize().height));
            filePanel.setToolTipText(help);
            filePanel.setInlineEditingEnabled(true);
            addProperty(key, label, filePanel);
            break;
          case LIST:
          case BLANK_SEPARATED_LIST_FIXED:
          case COMMA_SEPARATED_LIST_FIXED:
            if (type == PropertyType.BLANK_SEPARATED_LIST_FIXED)
              combo = new JComboBox(value.getProperty(key).split(" "));
            else if (type == PropertyType.COMMA_SEPARATED_LIST_FIXED)
              combo = new JComboBox(value.getProperty(key).split(","));
            else
              combo = new JComboBox(getList(key));
            combo.setSelectedItem(value.getProperty(key));
            combo.setToolTipText(help);
            addProperty(key, label, combo);
            break;
	  case CUSTOM_COMPONENT:
	    comp = getComponent(key);
	    setText(comp, value.getProperty(key));
	    updateToolTipText(comp, help);
            addProperty(key, label, getComponent(key));
            break;
          default:
            throw new IllegalStateException("Unhandled property type (property '" + keys + "'): " + type);
        }
      }
      catch (Exception e) {
        System.err.println("Failed to set property/type: '" + key + "'/" + type);
        e.printStackTrace();
      }
    }
    invalidate();
    validate();
    repaint();
  }

  /**
   * Returns the currently display properties as a properties object.
   *
   * @return		the properties
   */
  public Properties getProperties() {
    Properties			result;
    int				i;
    Component			comp;
    PropertyType		type;
    JTextField			textfield;
    JCheckBox			checkbox;
    JSpinner			spinner;
    DirectoryChooserPanel	dirPanel;
    FileChooserPanel		filePanel;
    JComboBox			comboEnum;
    String			key;

    result = new Properties();

    for (i = 0; i < getPropertyCount(); i++) {
      comp = getProperty(i);
      key  = m_Identifiers.get(i);
      type = getActualPropertyType(key);

      switch (type) {
        case DOUBLE:
          textfield = (JTextField) comp;
          result.setProperty(key, textfield.getText());
          break;
        case STRING:
          textfield = (JTextField) comp;
          result.setProperty(key, textfield.getText());
          break;
        case BOOLEAN:
          checkbox = (JCheckBox) comp;
          result.setProperty(key, "" + checkbox.isSelected());
          break;
        case INTEGER:
          spinner = (JSpinner) comp;
          result.setProperty(key, "" + ((Number) spinner.getValue()).intValue());
          break;
        case LONG:
          spinner = (JSpinner) comp;
          result.setProperty(key, "" + ((Number) spinner.getValue()).longValue());
          break;
        case DIRECTORY:
          dirPanel = (DirectoryChooserPanel) comp;
          result.setProperty(key, dirPanel.getCurrent().getPath());
          break;
        case DIRECTORY_ABSOLUTE:
          dirPanel = (DirectoryChooserPanel) comp;
          result.setProperty(key, dirPanel.getCurrent().getAbsolutePath());
          break;
        case FILE:
          filePanel = (FileChooserPanel) comp;
          result.setProperty(key, filePanel.getCurrent().getPath());
          break;
        case FILE_ABSOLUTE:
          filePanel = (FileChooserPanel) comp;
          result.setProperty(key, filePanel.getCurrent().getAbsolutePath());
          break;
        case LIST:
        case BLANK_SEPARATED_LIST_FIXED:
        case COMMA_SEPARATED_LIST_FIXED:
          comboEnum = (JComboBox) comp;
          if (comboEnum.getSelectedIndex() > -1)
            result.setProperty(key, "" + comboEnum.getSelectedItem());
          break;
	case CUSTOM_COMPONENT:
          result.setProperty(key, getText(comp));
	  break;
        default:
          throw new IllegalStateException("Unhandled property type (property '" + key + "'): " + type);
      }
    }

    return result;
  }

  /**
   * Returns the file chooser to use for loading/saving of props files.
   *
   * @return		the file chooser
   */
  protected synchronized BaseFileChooser getFileChooser() {
    FileFilter	filter;

    if (m_FileChooser == null) {
      m_FileChooser = new BaseFileChooser();
      filter        = ExtensionFileFilter.getPropertiesFileFilter();
      m_FileChooser.addChoosableFileFilter(filter);
      m_FileChooser.setFileFilter(filter);
    }

    return m_FileChooser;
  }

  /**
   * Loads properties from a file, prompts the user to select props file.
   */
  protected void loadProperties() {
    int			retVal;
    Properties		props;
    FileReader		freader;
    BufferedReader	breader;

    retVal = getFileChooser().showOpenDialog(this);
    if (retVal != BaseFileChooser.APPROVE_OPTION)
      return;

    freader = null;
    breader = null;
    props   = new Properties();
    try {
      freader = new FileReader(getFileChooser().getSelectedFile().getAbsolutePath());
      breader = new BufferedReader(freader);
      props.load(breader);
    }
    catch (Exception e) {
      GUIHelper.showErrorMessage(this, "Failed to load properties from: " + getFileChooser().getSelectedFile());
      return;
    }
    finally {
      FileUtils.closeQuietly(breader);
      FileUtils.closeQuietly(freader);
    }

    setProperties(props);
  }

  /**
   * Saves properties to a file, prompts the user to select props file.
   */
  protected void saveProperties() {
    int			retVal;
    Properties		props;
    FileWriter		fwriter;
    BufferedWriter	bwriter;

    retVal = getFileChooser().showSaveDialog(this);
    if (retVal != BaseFileChooser.APPROVE_OPTION)
      return;

    fwriter = null;
    bwriter = null;
    props   = getProperties();
    try {
      fwriter = new FileWriter(getFileChooser().getSelectedFile().getAbsolutePath());
      bwriter = new BufferedWriter(fwriter);
      props.store(bwriter, null);
    }
    catch (Exception e) {
      GUIHelper.showErrorMessage(this, "Failed to save properties to: " + getFileChooser().getSelectedFile());
    }
    finally {
      FileUtils.closeQuietly(bwriter);
      FileUtils.closeQuietly(fwriter);
    }
  }

  /**
   * Sets the visibility state of the buttons panel (load/save).
   *
   * @param value	true if to show buttons
   */
  public void setButtonPanelVisible(boolean value) {
    m_PanelButtons.setVisible(value);
  }

  /**
   * Returns the visibility state of the buttons panel (load/save).
   *
   * @return		true if buttons displayed
   */
  public boolean isButtonPanelVisible() {
    return m_PanelButtons.isVisible();
  }

  /**
   * Adds the change listener.
   *
   * @param l		the change listener
   */
  public void addChangeListener(ChangeListener l) {
    m_PanelProperties.addChangeListener(l);
  }

  /**
   * Removes the change listener.
   *
   * @param l		the change listener
   */
  public void removeChangeListener(ChangeListener l) {
    m_PanelProperties.removeChangeListener(l);
  }
}