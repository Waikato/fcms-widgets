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
 * DirectoryBookmarks.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.gui.core;

import com.googlecode.jfilechooserbookmarks.AbstractBookmarksPanel;
import com.googlecode.jfilechooserbookmarks.AbstractFactory;
import com.googlecode.jfilechooserbookmarks.AbstractPropertiesHandler;
import com.googlecode.jfilechooserbookmarks.DefaultFactory;
import nz.ac.waikato.cms.core.OS;

import java.io.File;

/**
 * Custom directory bookmarks handling.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class DirectoryBookmarks {

  public static class PropertiesHandler
    extends AbstractPropertiesHandler {

    protected String getFilename() {
      if (OS.isWindows())
        return System.getProperty("user.home") + File.separator + "fcms.props";
      else
        return System.getProperty("user.home") + File.separator + ".config" + File.separator + "fcms" + File.separator + "bookmarks.props";
    }
  }

  public static class Factory
    extends DefaultFactory {

    public AbstractPropertiesHandler newPropertiesHandler() {
      return new PropertiesHandler();
    }
  }

  public static class FileChooserBookmarksPanel
    extends AbstractBookmarksPanel {

    protected AbstractFactory newFactory() {
      return new Factory();
    }
  }
}
