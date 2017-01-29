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
 * PropsUtils.java
 * Copyright (C) 2015-2017 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Helper class for properties files.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class PropsUtils {

  /**
   * Loads the properties from the given file.
   *
   * @param props	the properties object to use
   * @param filename	the file to load the properties from
   * @return		true if successfully loaded
   */
  public static boolean load(Properties props, String filename) {
    boolean		result;
    BufferedInputStream stream;
    File 		file;

    result = true;

    try {
      props.clear();
      file = new File(filename);
      if (file.exists()) {
	stream = new BufferedInputStream(new FileInputStream(filename));
	props.load(stream);
	stream.close();
      }
      else {
	System.err.println("file '" + filename + "' does not exist - skipped!");
      }
    }
    catch (Exception e) {
      props.clear();
      result = false;
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Saves the properties to the given file.
   *
   * @param props	the properties object to use
   * @param filename	the file to save the properties to
   * @return		true if successfully written
   */
  public static boolean save(Properties props, String filename) {
    return save(props, filename, null);
  }

  /**
   * Saves the properties to the given file.
   *
   * @param props	the properties object to use
   * @param filename	the file to save the properties to
   * @param comment	the comment to use, can be null
   * @return		true if successfully written
   */
  public static boolean save(Properties props, String filename, String comment) {
    boolean			result;
    BufferedOutputStream 	stream;
    FileOutputStream 		fos;

    result = true;

    fos    = null;
    stream = null;
    try {
      stream = new BufferedOutputStream(new FileOutputStream(filename));
      props.store(stream, comment);
      stream.flush();
    }
    catch (Exception e) {
      result = false;
      e.printStackTrace();
    }
    finally {
      FileUtils.closeQuietly(stream);
      FileUtils.closeQuietly(fos);
    }

    return result;
  }
}
