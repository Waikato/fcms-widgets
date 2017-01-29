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
 * BrowserHelper.java
 * Copyright (C) 2006-2017 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.core;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * A little helper class for browser related stuff.
 * <br><br>
 * The <code>openURL</code> method was originally based on
 * <a href="http://www.centerkey.com/java/browser/" target="_blank">Bare Bones Browser Launch</a>,
 * which has been placed in the public domain.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 10824 $
 */
public class BrowserHelper {

  public final static String[] LINUX_BROWSERS = { "firefox", "google-chrome",
    "opera", "konqueror", "epiphany", "mozilla", "netscape" };

  /**
   * Opens the URL in the system's default browser.
   *
   * @param url		the URL to open
   * @return		null if executed OK, otherwise error message
   */
  public static synchronized String openURL(String url) {
    return openURL(null, url);
  }

  /**
   * Opens the URL in the system's default browser.
   *
   * @param parent	the parent component
   * @param url		the URL to open
   * @return		null if executed OK, otherwise error message
   */
  public static synchronized String openURL(Component parent, String url) {
    return openURL(parent, url, true);
  }

  /**
   * Opens the URL in the system's default browser.
   *
   * @param parent	the parent component
   * @param url		the URL to open
   * @param showDialog	whether to display a dialog in case of an error or
   * 			just print the error to the console
   * @return		null if executed OK, otherwise error message
   */
  public static synchronized String openURL(Component parent, String url, boolean showDialog) {
    String	result;

    result = null;

    try {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
        Desktop.getDesktop().browse(new URI(url));
      }
      else {
        System.err.println("Desktop or browse action not supported, using fallback to determine browser.");

        // Mac OS
        if (OS.isMac()) {
          Class fileMgr = Class.forName("com.apple.eio.FileManager");
          Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
          openURL.invoke(null, new Object[]{url});
        }
        // Windows
        else if (OS.isWindows()) {
          Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        }
        // assume Unix or Linux
        else {
          String browser = null;
          for (int count = 0; count < LINUX_BROWSERS.length; count++) {
            // look for binaries and take first that's available
            if (Runtime.getRuntime().exec(new String[] {"which", LINUX_BROWSERS[count]}).waitFor() == 0) {
              browser = LINUX_BROWSERS[count];
              break;
            }
          }
          if (browser == null)
            throw new Exception("Could not find web browser");
          else
            Runtime.getRuntime().exec(new String[]{browser, url});
        }
      }
    }
    catch (Exception e) {
      result = "Error attempting to launch web browser:\n" + e.getMessage();

      if (showDialog)
        JOptionPane.showMessageDialog(
          parent,
          result,
          "Error",
          JOptionPane.ERROR_MESSAGE);
      else
        System.err.println(result);
    }

    return result;
  }

  /**
   * Opens the URL in a custom browser.
   *
   * @param parent	the parent component
   * @param cmd		the browser command-line
   * @param url		the URL to open
   * @param showDialog	whether to display a dialog in case of an error or
   * 			just print the error to the console
   * @return		null if executed OK, otherwise error message
   */
  public static synchronized String openURL(Component parent, String cmd, String url, boolean showDialog) {
    String	result;

    result = null;

    try {
      Runtime.getRuntime().exec(cmd + " " + url);
    }
    catch (Exception e) {
      result = "Error attempting to launch web browser '" + cmd + "':\n" + e.getMessage();

      if (showDialog)
        JOptionPane.showMessageDialog(
          parent,
          result,
          "Error",
          JOptionPane.ERROR_MESSAGE);
      else
        System.err.println(result);
    }

    return result;
  }
}
