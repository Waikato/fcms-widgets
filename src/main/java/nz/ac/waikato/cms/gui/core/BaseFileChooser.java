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

/**
 * BaseFileChooser.java
 * Copyright (C) 2015-2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.gui.core;

import javax.swing.JFileChooser;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;

/**
 * FileChooser with a bookmarks panel.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class BaseFileChooser
  extends JFileChooser {

  /** the panel with the bookmarks. */
  protected DirectoryBookmarks.FileChooserBookmarksPanel m_PanelBookmarks;

  /**
   * Initializes the file chooser.
   */
  public BaseFileChooser() {
    super();
    initialize();
    initGUI();
    finishInit();
  }

  /**
   * Initializes the file chooser with the current directory.
   *
   * @param currentDir	the current directory to use
   */
  public BaseFileChooser(String currentDir) {
    super(currentDir);
    initialize();
    initGUI();
    finishInit();
  }

  /**
   * Initializes the file chooser with the current directory.
   *
   * @param currentDir	the current directory to use
   */
  public BaseFileChooser(File currentDir) {
    super(currentDir);
    initialize();
    initGUI();
    finishInit();
  }

  /**
   * Initializes the members.
   */
  protected void initialize() {
  }

  /**
   * Initializes the widgets.
   */
  protected void initGUI() {
    m_PanelBookmarks = new DirectoryBookmarks.FileChooserBookmarksPanel();
    m_PanelBookmarks.setOwner(this);
    setAccessory(m_PanelBookmarks);

    setPreferredSize(new Dimension(750, 500));
  }

  /**
   * Finishes up the initialization.
   */
  protected void finishInit() {
  }

  @Override
  public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
    m_PanelBookmarks.reload();
    return super.showDialog(parent, approveButtonText);
  }
}
