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
 * SetupPanel.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.gui.core;

import nz.ac.waikato.cms.core.Project;
import nz.ac.waikato.cms.core.PropsUtils;

import java.awt.LayoutManager;
import java.io.File;
import java.util.Properties;

/**
 * Panel with support for handling setup via props file.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class SetupPanel
  extends BasePanel {

  /**
   * Initializes the panel with BorderLayout.
   */
  public SetupPanel() {
    super();
  }

  /**
   * Initializes the panel.
   *
   * @param layout	the layout to use
   */
  public SetupPanel(LayoutManager layout) {
    super(layout);
  }

  /**
   * Finishes up the initialization.
   */
  @Override
  protected void finishInit() {
    super.finishInit();
    loadSetup();
  }

  /**
   * Maps the properties back to the GUI.
   *
   * @param props       the properties to use
   */
  protected abstract void propsToGUI(Properties props);

  /**
   * Attempts to load a previous setup from a props file.
   */
  protected void loadSetup() {
    Properties props;

    props = new Properties();
    PropsUtils.load(props, Project.getHome() + File.separator + getClass().getSimpleName() + ".props");
    propsToGUI(props);
  }

  /**
   * Maps the GUI to a properties object.
   *
   * @return            the properties
   */
  protected abstract Properties guiToProps();

  /**
   * Saves the current setup in a props file.
   */
  protected void saveSetup() {
    Properties		props;

    props = guiToProps();
    PropsUtils.save(props, Project.getHome() + File.separator + getClass().getSimpleName() + ".props");
  }
}
