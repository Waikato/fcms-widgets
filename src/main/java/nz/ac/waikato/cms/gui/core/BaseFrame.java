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
 * BaseFrame.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.gui.core;

import javax.swing.JFrame;

/**
 * Ancestor for frames.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class BaseFrame
  extends JFrame {

  /**
   * Initializes the frame with no title.
   */
  public BaseFrame() {
    super();
    initialize();
    initGUI();
    finishInit();
  }

  /**
   * Initializes the frame with the specified title.
   *
   * @param title	the title to use
   */
  public BaseFrame(String title) {
    super(title);
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
    setIconImage(GUIHelper.getIcon("waikato.png").getImage());
  }

  /**
   * Finishes up the initialization.
   */
  protected void finishInit() {
  }
}
