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
 * FileChooserPanel.java
 * Copyright (C) 2008-2018 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.gui.core;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * A panel that contains a text field with the current file/directory and a
 * button for bringing up a BaseFileChooser.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class FileChooserPanel
  extends AbstractChooserPanel<File> {

  /** for serialization. */
  private static final long serialVersionUID = -8755020252465094120L;

  /** the JFileChooser for selecting a file. */
  protected BaseFileChooser m_FileChooser;

  /** whether to use the open or save dialog. */
  protected boolean m_UseSaveDialog;

  /**
   * Initializes the panel with no file.
   */
  public FileChooserPanel() {
    this("");
  }

  /**
   * Initializes the panel with the given filename/directory.
   *
   * @param path	the filename/directory to use
   */
  public FileChooserPanel(String path) {
    this(new File(path));
  }

  /**
   * Initializes the panel with the given filename/directory.
   *
   * @param path	the filename/directory to use
   */
  public FileChooserPanel(File path) {
    super();

    setCurrent(path);
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_FileChooser   = new BaseFileChooser();
    m_UseSaveDialog = false;
  }

  /**
   * Performs the actual choosing of an object.
   *
   * @return		the chosen object or null if none chosen
   */
  @Override
  protected File doChoose() {
    m_FileChooser.setSelectedFile(getCurrent());

    if (m_UseSaveDialog) {
      if (m_FileChooser.showSaveDialog(m_Self) == BaseFileChooser.APPROVE_OPTION) {
        return m_FileChooser.getSelectedFile();
      }
      else {
        return null;
      }
    }
    else {
      if (m_FileChooser.showOpenDialog(m_Self) == BaseFileChooser.APPROVE_OPTION) {
        return m_FileChooser.getSelectedFile();
      }
      else {
        return null;
      }
    }
  }

  /**
   * Converts the value into its string representation.
   *
   * @param value	the value to convert
   * @return		the generated string
   */
  @Override
  protected String toString(File value) {
    String 	result;

    result = value.getAbsolutePath();
    if (result.equals(System.getProperty("user.dir")))
      result = ".";

    return result;
  }

  /**
   * Converts the string representation into its object representation.
   *
   * @param value	the string value to convert
   * @return		the generated object
   */
  @Override
  protected File fromString(String value) {
    return new File(value);
  }

  /**
   * Sets the selection mode, whether only files or directories, or both can
   * be selected. FILES_ONLY is the default.
   *
   * @param value	the mode
   */
  public void setFileSelectionMode(int value) {
    m_FileChooser.setFileSelectionMode(value);
  }

  /**
   * Returns the selection mode, whether only files or directories, or both
   * can be selected. FILES_ONLY is the default.
   *
   * @return		the mode
   * @see		JFileChooser#FILES_ONLY
   * @see		JFileChooser#DIRECTORIES_ONLY
   * @see		JFileChooser#FILES_AND_DIRECTORIES
   */
  public int getFileSelectionMode() {
    return m_FileChooser.getFileSelectionMode();
  }

  /**
   * Adds the given file filter to the filechooser.
   *
   * @param value	the file filter to add
   */
  public void addChoosableFileFilter(FileFilter value) {
    FileFilter	current;

    current = m_FileChooser.getFileFilter();
    m_FileChooser.addChoosableFileFilter(value);
    m_FileChooser.setFileFilter(current);
  }

  /**
   * Removes the specified file filter from the filechooser.
   *
   * @param value	the file filter to remove
   */
  public void removeChoosableFileFilter(FileFilter value) {
    m_FileChooser.removeChoosableFileFilter(value);
  }

  /**
   * Removes all file filters from the filechooser.
   */
  public void removeChoosableFileFilters() {
    FileFilter[]	filters;

    filters = m_FileChooser.getChoosableFileFilters();
    for (FileFilter filter: filters)
      m_FileChooser.removeChoosableFileFilter(filter);
  }

  /**
   * Returns all choosable file filters.
   *
   * @return		the current file filters
   */
  public FileFilter[] getChoosableFileFilters() {
    return m_FileChooser.getChoosableFileFilters();
  }

  /**
   * Sets whether the "accept all files" filter is used.
   *
   * @param value	if true then the filter will be used
   */
  public void setAcceptAllFileFilterUsed(boolean value) {
    m_FileChooser.setAcceptAllFileFilterUsed(value);
  }

  /**
   * Returns whether the "accept all files" filter is used.
   *
   * @return		true if the filter is used
   */
  public boolean isAcceptAllFileFilterUsed() {
    return m_FileChooser.isAcceptAllFileFilterUsed();
  }

  /**
   * Sets the active file filter.
   *
   * @param value	the file filter to select
   */
  public void setFileFilter(FileFilter value) {
    m_FileChooser.setFileFilter(value);
  }

  /**
   * Returns the active file filter.
   *
   * @return		the current file filter
   */
  public FileFilter getFileFilter() {
    return m_FileChooser.getFileFilter();
  }

  /**
   * Sets the current directory to use for the file chooser.
   *
   * @param value	the current directory
   */
  public void setCurrentDirectory(File value) {
    m_FileChooser.setCurrentDirectory(value);
  }

  /**
   * Returns the current directory in use by the file chooser.
   *
   * @return		the current directory
   */
  public File getCurrentDirectory() {
    return m_FileChooser.getCurrentDirectory();
  }

  /**
   * Sets the current value.
   *
   * @param value	the value to use, can be null
   * @return		true if successfully set
   */
  @Override
  public boolean setCurrent(File value) {
    boolean	result;

    result = super.setCurrent(value);
    m_FileChooser.setSelectedFile(getCurrent().getAbsoluteFile());

    return result;
  }

  /**
   * Sets whether to use the save or open dialog.
   *
   * @param value	if true the save dialog is used
   */
  public void setUseSaveDialog(boolean value) {
    m_UseSaveDialog = value;
  }

  /**
   * Returns whether the save or open dialog is used.
   *
   * @return	true if the save dialog is used
   */
  public boolean getUseSaveDialog() {
    return m_UseSaveDialog;
  }
}
