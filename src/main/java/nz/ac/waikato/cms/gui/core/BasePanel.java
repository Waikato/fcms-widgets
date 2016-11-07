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
 * BasePanel.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.gui.core;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.LayoutManager;

/**
 * Ancestor for panels.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class BasePanel
  extends JPanel {

  /**
   * Initializes the panel with BorderLayout.
   */
  public BasePanel() {
    this(new BorderLayout());
  }

  /**
   * Initializes the panel.
   *
   * @param layout	the layout to use
   */
  public BasePanel(LayoutManager layout) {
    super(layout);
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
  }

  /**
   * Finishes up the initialization.
   */
  protected void finishInit() {
  }
}
