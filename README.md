# fcms-widgets

Small collection of useful Java widgets and utility classes.

## Utility classes

* Package `nz.ac.waikato.cms.core`

  * `BrowserHelper` - for launching browsers with URLs
  * `FileUtils` - methods for closing streams/readers, file extension handling
  * `OS` - determining underlying OS (Android/Linux/Mac/Windows)
  * `PropsUtils` - for loading/saving properties files
  
* Package `nz.ac.waikato.cms.gui.core`

  * `GUIHelper` - methods for determining parent components, loading images/icons, etc.
  * `JListHelper` - methods for moving elements in `JList`
  * `JTableHelper` - methods calculating column widhts of a `JTable`
  * `KeyUtils` - convenience methods for `KeyEvent` events
  * `MouseUtils` - convenience methods for `MouseEvent` events

## Widgets

* Package `nz.ac.waikato.cms.gui.core`

  * `ApprovalDialog` - dialog template for dialogs with Approve/Cancel/Discard buttons
    (can be individually renamed, hidden/displayed)
  * `BaseDialog` - dialog with 3-part initialization (`initialize`, `initGUI`, `finishInit`),
    hook methods for show (`beforeShow`, `afterShow`) and hide (`beforeHide`, `afterHide`)
  * `BaseDirectoryChooser` - directory chooser with bookmarks panel  
  * `BaseFileChooser` - file chooser with bookmarks panel
  * `BaseFrame` - frame with 3-part initialization (`initialize`, `initGUI`, `finishInit`)
  * `BasePanel` - panel with 3-part initialization (`initialize`, `initGUI`, `finishInit`)
  * `BaseScrollPane` - scroll pane with sensible scroll units
  * `BaseTextPane` - extended `javax.swing.JTextPane` making it easy to append lines with different fonts/colors
  * `BaseTextPaneWithWordWrap` - extended `BaseTextPane` supporting word wrap
  * `DetachablePanel` - meta-panel that can detach the content in a separate dialog (and then re-attach it again)
  * `DialogWithButtons` - dialog with panel at bottom of window for easy placement of buttons
  * `DirectoryChooserPanel` - text field with associated button for selecting directory
  * `FileChooserPanel` - text field with associated button for selecting file
  * `FontChooser` - a chooser dialog for fonts
  * `FontChooserPanel` - panel that allows selection of fonts via a `FontChooser` dialog
  * `FontChooserPanelWithPreview` - previews the selected font with some sample text
  * `MultiPagePane` - pane that works like `javax.swing.JTabbedPane`, but lists the pages 
    on the left-hand side (better use of space on widescreen monitors)
  * `ParameterPanel` - panel for displaying multiple parameters to be entered
  * `PropertiesParameterPanel` - based on `ParameterPanel`, but backed by `java.util.Properties` 
    for getting/setting the parameters
  * `SetupPanel` - ancestor for panels that load/save setups from/to properties files
  
## Examples

### ApprovalDialog

```java
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import nz.ac.waikato.cms.gui.core.ApprovalDialog;
...
// panel with content to display, e.g, a question
JPanel panel = new JPanel(new BorderLayout());
panel.add(new JLabel("Some question to ask?"), BorderLayout.CENTER);

// configure and display dialog
ApprovalDialog dialog = new ApprovalDialog((java.awt.Frame) null, true);
dialog.setDefaultCloseOperation(ApprovalDialog.DISPOSE_ON_CLOSE);
dialog.setTitle("Please confirm");
dialog.getContentPane().add(panel, BorderLayout.CENTER);
dialog.pack();
dialog.setLocationRelativeTo(null);
dialog.setVisible(true);
if (dialog.getOption() == ApprovalDialog.APPROVE_OPTION)
  System.out.println("Approved!");
else
  System.out.println("Nope...");
```

### PropertiesParameterPanel

```java
import nz.ac.waikato.cms.gui.core.ApprovalDialog;
import nz.ac.waikato.cms.gui.core.GUIHelper;
import nz.ac.waikato.cms.gui.core.PropertiesParameterPanel;
import nz.ac.waikato.cms.gui.core.PropertiesParameterPanel.PropertyType;
import java.util.Properties;
...
PropertiesParameterPanel panel = new PropertiesParameterPanel();

// properties
panel.addPropertyType("name", PropertyType.STRING);
panel.setLabel("name", "New name");
panel.setHelp("name", "The name for the environment");

panel.addPropertyType("java", PropertyType.FILE);
panel.setLabel("java", "Java executable");
panel.setHelp("java", "System default is used when pointing to a directory");

panel.addPropertyType("memory", PropertyType.STRING);
panel.setLabel("memory", "Heap size");
panel.setHelp("memory", "System default is used when empty");

panel.addPropertyType("weka", PropertyType.FILE);
panel.setLabel("weka", "Weka jar");
panel.setHelp("weka", "The weka jar to use for the environment, cannot be empty");

// define order of parameters
panel.setPropertyOrder(new String[]{
  "name",
  "java",
  "memory",
  "weka",
});

// initial values
Properties props = new Properties();
props.setProperty("name", "coolname");
props.setProperty("java", "/usr/bin/java");
props.setProperty("memory", "4g");
props.setProperty("weka", "/some/where/weka.jar");
panel.setProperties(props);

// configure dialog and prompt user
ApprovalDialog dialog = new ApprovalDialog((java.awt.Frame) null, true);
dialog.setDefaultCloseOperation(ApprovalDialog.DISPOSE_ON_CLOSE);
dialog.setTitle("Enter parameters");
dialog.getContentPane().add(panel, BorderLayout.CENTER);
dialog.pack();
dialog.setLocationRelativeTo(null);
dialog.setVisible(true);

// output user provided parameters
if (dialog.getOption() == ApprovalDialog.APPROVE_OPTION)
  System.out.println(panel.getProperties());
else
  System.out.println("Canceled");
```
  
## Maven

Add the following dependency to your `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.waikato</groupId>
      <artifactId>fcms-widgets</artifactId>
      <version>0.0.15</version>
    </dependency>
```