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
 * GUIHelper.java
 * Copyright (C) 2015-2017 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.gui.core;

import nz.ac.waikato.cms.core.Utils;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Helper class for GUI related stuff.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class GUIHelper {

  /** the mnemonic character indicator. */
  public final static char MNEMONIC_INDICATOR = '_';

  /**
   * Adds the path of the images directory to the name of the image.
   *
   * @param name	the name of the image to add the path to
   * @return		the full path of the image
   */
  public static String getImageFilename(String name) {
    return "nz/ac/waikato/cms/gui/images/" + name;
  }

  /**
   * Returns an ImageIcon from the given name.
   *
   * @param name	the filename without path
   * @return		the ImageIcon or null if not available
   */
  public static ImageIcon getIcon(String name) {
    String	filename;

    filename = getImageFilename(name);
    if (filename != null)
      return new ImageIcon(ClassLoader.getSystemClassLoader().getResource(filename));
    else
      return null;
  }

  /**
   * Tries to determine the parent this panel is part of.
   *
   * @param cont	the container to get the parent for
   * @param parentClass	the class of the parent to obtain
   * @return		the parent if one exists or null if not
   */
  public static Object getParent(Container cont, Class parentClass) {
    Container	result;
    Container	parent;

    result = null;

    parent = cont;
    while (parent != null) {
      if (parentClass.isInstance(parent)) {
	result = parent;
	break;
      }
      else {
	parent = parent.getParent();
      }
    }

    return result;
  }

  /**
   * Tries to determine the frame the container is part of.
   *
   * @param cont	the container to get the frame for
   * @return		the parent frame if one exists or null if not
   */
  public static Frame getParentFrame(Container cont) {
    return (Frame) getParent(cont, Frame.class);
  }

  /**
   * Tries to determine the frame the component is part of.
   *
   * @param comp	the component to get the frame for
   * @return		the parent frame if one exists or null if not
   */
  public static Frame getParentFrame(Component comp) {
    if (comp instanceof Container)
      return (Frame) getParent((Container) comp, Frame.class);
    else
      return null;
  }

  /**
   * Tries to determine the dialog this container is part of.
   *
   * @param cont	the container to get the dialog for
   * @return		the parent dialog if one exists or null if not
   */
  public static Dialog getParentDialog(Container cont) {
    return (Dialog) getParent(cont, Dialog.class);
  }

  /**
   * Tries to determine the dialog this component is part of.
   *
   * @param comp	the component to get the dialog for
   * @return		the parent dialog if one exists or null if not
   */
  public static Dialog getParentDialog(Component comp) {
    if (comp instanceof Container)
      return (Dialog) getParent((Container) comp, Dialog.class);
    else
      return null;
  }

  /**
   * Tries to determine the internal frame this container is part of.
   *
   * @param cont	the container to start with
   * @return		the parent internal frame if one exists or null if not
   */
  public static JInternalFrame getParentInternalFrame(Container cont) {
    return (JInternalFrame) getParent(cont, JInternalFrame.class);
  }

  /**
   * Tries to determine the internal frame this component is part of.
   *
   * @param comp	the component to start with
   * @return		the parent internal frame if one exists or null if not
   */
  public static JInternalFrame getParentInternalFrame(Component comp) {
    if (comp instanceof Container)
      return (JInternalFrame) getParent((Container) comp, JInternalFrame.class);
    else
      return null;
  }

  /**
   * Tries to determine the component this panel is part of in this order:
   * 1. dialog, 2. child, 3. frame.
   *
   * @param comp	the component to get the parent component for, must
   * 			be a container actually
   * @return		the parent component if one exists or null if not
   * @see		#getParentDialog(Container)
   * @see		#getParentFrame(Container)
   */
  public static Component getParentComponent(Component comp) {
    Component	result;
    Container	cont;

    if (comp == null)
      return null;

    if (comp instanceof Container)
      cont = (Container) comp;
    else
      return null;

    result = getParentDialog(cont);
    if (result == null)
      result = getParentFrame(cont);

    return result;
  }

  /**
   * Closes the parent dialog/frame of this container.
   *
   * @param cont	the container to close the parent for
   */
  public static void closeParent(Container cont) {
    Dialog		dialog;
    Frame		frame;
    JFrame jframe;
    JInternalFrame	jintframe;
    int		i;
    WindowListener[] 	listeners;
    WindowEvent event;

    if (getParentDialog(cont) != null) {
      dialog = getParentDialog(cont);
      dialog.setVisible(false);
    }
    else if (getParentFrame(cont) != null) {
      jintframe = getParentInternalFrame(cont);
      if (jintframe != null) {
	jintframe.doDefaultCloseAction();
      }
      else {
	frame = getParentFrame(cont);
	if (frame instanceof JFrame) {
	  jframe = (JFrame) frame;
	  if (jframe.getDefaultCloseOperation() == JFrame.HIDE_ON_CLOSE)
	    jframe.setVisible(false);
	  else if (jframe.getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE)
	    jframe.dispose();
	  else if (jframe.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE)
	    System.exit(0);

	  // notify listeners
	  listeners = jframe.getWindowListeners();
	  event     = new WindowEvent(jframe, WindowEvent.WINDOW_CLOSED);
	  for (i = 0; i < listeners.length; i++)
	    listeners[i].windowClosed(event);
	}
	else {
	  frame.dispose();
	}
      }
    }
  }

  /**
   * Checks the caption whether an underscore "_" is present to indicate
   * that the following character is to act as mnemonic.
   *
   * @param caption	the caption to analyze
   * @return		true if an underscore is present
   * @see		#MNEMONIC_INDICATOR
   */
  public static boolean hasMnemonic(String caption) {
    return (caption.indexOf(MNEMONIC_INDICATOR) > -1);
  }

  /**
   * Returns the mnemonic for this caption, preceded by an underscore "_".
   *
   * @param caption	the caption to extract
   * @return		the extracted mnemonic, \0 if none available
   * @see		#MNEMONIC_INDICATOR
   */
  public static char getMnemonic(String caption) {
    int		pos;

    pos = caption.indexOf(MNEMONIC_INDICATOR);
    if ((pos > -1) && (pos < caption.length() - 1))
      return caption.charAt(pos + 1);
    else
      return '\0';
  }

  /**
   * Removes the mnemonic indicator in this caption.
   *
   * @param caption	the caption to process
   * @return		the processed caption
   * @see		#MNEMONIC_INDICATOR
   */
  public static String stripMnemonic(String caption) {
    return caption.replace("" + MNEMONIC_INDICATOR, "");
  }

  /**
   * Displays an error message with the default title "Error".
   *
   * @param parent	the parent, to make the dialog modal; can be null
   * @param msg		the error message to display
   * @param t 		the exception to append to the message
   */
  public static void showErrorMessage(Component parent, String msg, Throwable t) {
    showErrorMessage(parent, msg + "\n" + Utils.throwableToString(t), "Error");
  }

  /**
   * Displays an error message with the default title "Error".
   *
   * @param parent	the parent, to make the dialog modal; can be null
   * @param msg		the error message to display
   */
  public static void showErrorMessage(Component parent, String msg) {
    showErrorMessage(parent, msg, "Error");
  }

  /**
   * Displays an error message.
   *
   * @param parent	the parent, to make the dialog modal; can be null
   * @param msg		the error message to display
   * @param title	the title of the error message
   */
  public static void showErrorMessage(Component parent, final String msg, String title) {
    JOptionPane.showMessageDialog(
      parent,
      msg,
      title,
      JOptionPane.ERROR_MESSAGE);
  }
}
