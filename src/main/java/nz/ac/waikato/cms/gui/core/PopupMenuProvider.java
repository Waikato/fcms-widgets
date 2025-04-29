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
 * PopupMenuProvider.java
 * Copyright (C) 2016-2025 University of Waikato, Hamilton, New Zealand
 */
package nz.ac.waikato.cms.gui.core;

import javax.swing.JPopupMenu;

/**
 * Interface for classes that offer a popup menu.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface PopupMenuProvider {

  /**
   * Returns the popup menu.
   *
   * @return 		the menu
   */
  public JPopupMenu getPopupMenu();
}
