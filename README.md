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

  * `BaseDirectoryChooser` - directory chooser with bookmarks panel  
  * `BaseFileChooser` - file chooser with bookmarks panel
  * `BaseFrame` - frame with 3-part initialization (`initialize`, `initGUI`, `finishInit`)
  * `BasePanel` - panel with 3-part initialization (`initialize`, `initGUI`, `finishInit`)
  * `BaseScrollPane` - scroll pane with sensible scroll units
  * `DirectoryChooserPanel` - text field with associated button for selecting directory
  * `FileChooserPanel` - text field with associated button for selecting file
  * `SetupPanel` - ancestor for panels that load/save setups from/to properties files
  * `ParameterPanel` - panel for displaying multiple parameters to be entered
  * `PropertiesParameterPanel` - based on `ParameterPanel`, but backed by `java.util.Properties` 
    for getting/setting the parameters
  
## Maven

Add the following dependency to your `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.waikato</groupId>
      <artifactId>fcms-widgets</artifactId>
      <version>0.0.6</version>
    </dependency>
```