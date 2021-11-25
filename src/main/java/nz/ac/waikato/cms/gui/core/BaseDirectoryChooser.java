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
 * BaseDirectoryChooser.java
 * Copyright (C) 2010-2021 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.gui.core;

import com.jidesoft.swing.FolderChooser;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Extended version of the com.jidesoft.swing.FolderChooser.
 *
 * @author FracPete (fracpete at waikat dot ac dot nz)
 */
public class BaseDirectoryChooser
  extends FolderChooser {

  /** for serialization. */
  private static final long serialVersionUID = -7252242971482953986L;
  
  /** the bookmarks. */
  protected DirectoryBookmarks.FileChooserBookmarksPanel m_PanelBookmarks;

  /** the button for showing/hiding the bookmarks. */
  protected JButton m_ButtonBookmarks;

  /**
   * Creates a BaseDirectoryChooser pointing to the user's home directory.
   */
  public BaseDirectoryChooser() {
    super();
    initialize();
  }

  /**
   * Creates a BaseDirectoryChooser using the given File as the path.
   *
   * @param currentDirectory	the directory to start in
   */
  public BaseDirectoryChooser(File currentDirectory) {
    super(currentDirectory.getAbsoluteFile());
    initialize();
  }

  /**
   * Creates a BaseDirectoryChooser using the given current directory and
   * FileSystemView.
   *
   * @param currentDirectory	the directory to start in
   * @param fsv			the view to use
   */
  public BaseDirectoryChooser(File currentDirectory, FileSystemView fsv) {
    super(currentDirectory.getAbsoluteFile(), fsv);
    initialize();
  }

  /**
   * Creates a BaseDirectoryChooser using the given FileSystemView.
   *
   * @param fsv			the view to use
   */
  public BaseDirectoryChooser(FileSystemView fsv) {
    super(fsv);
    initialize();
  }

  /**
   * Creates a BaseDirectoryChooser using the given path.
   *
   * @param currentDirectoryPath	the directory to start in
   */
  public BaseDirectoryChooser(String currentDirectoryPath) {
    super(currentDirectoryPath);
    initialize();
  }

  /**
   * Creates a BaseDirectoryChooser using the given path and FileSystemView.
   *
   * @param currentDirectoryPath	the directory to start in
   * @param fsv				the view to use
   */
  public BaseDirectoryChooser(String currentDirectoryPath, FileSystemView fsv) {
    super(currentDirectoryPath, fsv);
    initialize();
  }

  /**
   * For initializing some stuff.
   * <br><br>
   * Default implementation does nothing.
   */
  protected void initialize() {
    JComponent		accessory;

    setRecentListVisible(false);
   
    accessory = createAccessoryPanel();
    if (accessory != null)
      setAccessory(accessory);
    
    showBookmarks(false);
    setPreferredSize(new Dimension(400, 500));
  }

  /**
   * Creates an accessory panel displayed next to the files.
   * 
   * @return		the panel or null if none available
   */
  protected JComponent createAccessoryPanel() {
    JPanel	result;
    JPanel	panel;
    
    result = new JPanel(new BorderLayout());
    
    m_ButtonBookmarks = new JButton(GUIHelper.getIcon("arrow-head-up.png"));
    m_ButtonBookmarks.setBorder(BorderFactory.createEmptyBorder());
    m_ButtonBookmarks.setPreferredSize(new Dimension(18, 18));
    m_ButtonBookmarks.setBorderPainted(false);
    m_ButtonBookmarks.setContentAreaFilled(false);
    m_ButtonBookmarks.setFocusPainted(false);
    m_ButtonBookmarks.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	showBookmarks(!m_PanelBookmarks.isVisible());
      }
    });
    
    panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panel.add(m_ButtonBookmarks);
    result.add(panel, BorderLayout.NORTH);
    
    m_PanelBookmarks = new DirectoryBookmarks.FileChooserBookmarksPanel();
    m_PanelBookmarks.setOwner(this);
    m_PanelBookmarks.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 0));
    
    result.add(m_PanelBookmarks, BorderLayout.CENTER);
    
    return result;
  }

  /**
   * Either displays or hides the bookmarks.
   * 
   * @param value	true if to show bookmarks
   */
  protected void showBookmarks(boolean value) {
    m_PanelBookmarks.setVisible(value);
    if (m_PanelBookmarks.isVisible())
      m_ButtonBookmarks.setIcon(GUIHelper.getIcon("arrow-head-up.png"));
    else
      m_ButtonBookmarks.setIcon(GUIHelper.getIcon("arrow-head-down.png"));
  }
  
  /**
   * Does nothing.
   *
   * @param filter	ignored
   */
  @Override
  public void addChoosableFileFilter(FileFilter filter) {
  }

  @Override
  public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
    m_PanelBookmarks.reload();
    m_PanelBookmarks.updateButtons();
    return super.showDialog(parent, approveButtonText);
  }

  /**
   * For testing only.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    BaseDirectoryChooser chooser = new BaseDirectoryChooser();
    chooser.setCurrentDirectory(new File(System.getProperty("java.io.tmpdir")));
    if (chooser.showOpenDialog(null) == BaseDirectoryChooser.APPROVE_OPTION)
      System.out.println(chooser.getSelectedFile());
  }
}
