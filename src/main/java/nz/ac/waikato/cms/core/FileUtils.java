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
 * FileUtils.java
 * Copyright (C) 2009-2018 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for I/O related actions.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 10824 $
 */
public class FileUtils {

  /** the maximum length for an extension. */
  protected static Integer MAX_EXTENSION_LENGTH = 6;

  /** the ignored extension suffixes. */
  protected static String[] IGNORED_EXTENSION_SUFFIXES = new String[]{"7z", "bz2", "gz"};

  /**
   * Replaces the extension of the given file with the new one. Leave the
   * new extension empty if you want to remove the extension.
   * Always removes ignored extension suffixes first from the filename.
   * 
   * @param file	the file to replace the extension for
   * @param newExt	the new extension (incl dot), empty string to remove extension
   * @return		the updated file
   */
  public static File replaceExtension(File file, String newExt) {
    return new File(replaceExtension(file.getAbsolutePath(), newExt));
  }

  /**
   * Replaces the extension of the given file with the new one. Leave the
   * new extension empty if you want to remove the extension.
   * Always removes ignored extension suffixes first from the filename.
   * 
   * @param file	the file to replace the extension for
   * @param newExt	the new extension (incl dot), empty string to remove extension
   * @return		the updated file
   */
  public static String replaceExtension(String file, String newExt) {
    String	result;
    int		index;
    
    result = file;
    
    index = file.lastIndexOf('.');
    if (index > -1) {
      if (newExt.length() > 0)
	result = file.substring(0, index) + newExt;
      else
	result = file.substring(0, index);
    }
    
    return result;
  }

  /**
   * Returns the extension of the file, if any.
   *
   * @param file	the file to get the extension from
   * @return		the extension (no dot), null if none available
   */
  public static String getExtension(File file) {
    return getExtension(file.getAbsolutePath());
  }

  /**
   * Returns the extension of the file, if any.
   *
   * @param filename	the file to get the extension from
   * @return		the extension (no dot), null if none available
   */
  public static String getExtension(String filename) {
    String[]	result;

    result = getExtensions(filename);

    if (result != null)
      return result[0];
    else
      return null;
  }

  /**
   * Returns the extensions of the file, if any.
   * Returns "txt.gz" and "gz", for instance, for file "hello_world.txt.gz".
   * The longer extension always comes first.
   *
   * @param file	the file to get the extensions from
   * @return		the extensions (no dot), null if none available
   */
  public static String[] getExtensions(File file) {
    return getExtensions(file.getAbsolutePath());
  }

  /**
   * Returns the extensions of the file, if any.
   * Removes ignored file extension suffixes like "gz" first.
   *
   * @param filename	the file to get the extensions from
   * @return		the extensions (no dot), null if none available
   */
  public static String[] getExtensions(String filename) {
    List<String> 	result;
    int			pos;
    int			posNext;
    String		shortened;

    if (filename.indexOf('.') == -1)
      return null;

    result = new ArrayList<>();

    shortened = removeIgnoredExtensionSuffixes(filename);
    pos       = filename.lastIndexOf('.', shortened.length() - 1);
    result.add(filename.substring(pos + 1));

    posNext = filename.lastIndexOf('.', pos - 1);
    if ((posNext > -1) && (pos - posNext <= MAX_EXTENSION_LENGTH))
      result.add(filename.substring(posNext + 1));

    return result.toArray(new String[result.size()]);
  }

  /**
   * Removes any ignored extension suffixes from the filename.
   *
   * @param filename	the filename to process
   * @return		the processed filename
   */
  public static String removeIgnoredExtensionSuffixes(String filename) {
    String	result;

    result  = filename;

    // remove ignored suffixes
    for (String suffix: IGNORED_EXTENSION_SUFFIXES) {
      if (result.endsWith("." + suffix))
	result = result.substring(0, result.length() - suffix.length() - 1);
    }

    return result;
  }

  /**
   * Closes the stream, if possible, suppressing any exception.
   *
   * @param is		the stream to close
   */
  public static void closeQuietly(InputStream is) {
    if (is != null) {
      try {
	is.close();
      }
      catch (Exception e) {
	// ignored
      }
    }
  }

  /**
   * Closes the stream, if possible, suppressing any exception.
   *
   * @param os		the stream to close
   */
  public static void closeQuietly(OutputStream os) {
    if (os != null) {
      try {
	os.flush();
      }
      catch (Exception e) {
	// ignored
      }
      try {
	os.close();
      }
      catch (Exception e) {
	// ignored
      }
    }
  }

  /**
   * Closes the reader, if possible, suppressing any exception.
   *
   * @param reader	the reader to close
   */
  public static void closeQuietly(Reader reader) {
    if (reader != null) {
      try {
	reader.close();
      }
      catch (Exception e) {
	// ignored
      }
    }
  }

  /**
   * Closes the writer, if possible, suppressing any exception.
   *
   * @param writer	the writer to close
   */
  public static void closeQuietly(Writer writer) {
    if (writer != null) {
      try {
	writer.flush();
      }
      catch (Exception e) {
	// ignored
      }
      try {
	writer.close();
      }
      catch (Exception e) {
	// ignored
      }
    }
  }

  /**
   * Deletes the specified file. If the file represents a directory, then this
   * will get deleted recursively.
   *
   * @param file	the file/dir to delete
   * @return		true if successfully deleted
   */
  public static boolean delete(File file) {
    boolean	result;
    File[]	files;

    result = true;

    if (file.isDirectory()) {
      files = file.listFiles();
      if (files != null) {
	for (File f : files) {
	  if (f.getName().equals(".") || f.getName().equals(".."))
	    continue;
	  result = delete(f);
	  if (!result)
	    return false;
	}
      }
    }

    result = file.delete();

    return result;
  }

  /**
   * Copies or moves files and directories (recursively).
   * If targetLocation does not exist, it will be created.
   * <br><br>
   * Original code from <a href="http://www.java-tips.org/java-se-tips/java.io/how-to-copy-a-directory-from-one-location-to-another-loc.html" target="_blank">Java-Tips.org</a>.
   *
   * @param sourceLocation	the source file/dir
   * @param targetLocation	the target file/dir
   * @param move		if true then the source files/dirs get deleted
   * 				as soon as copying finished
   * @param atomic		whether to perform an atomic move operation
   * @return			false if failed to delete when moving or failed to create target directory
   * @throws IOException	if copying/moving fails
   */
  public static boolean copyOrMove(File sourceLocation, File targetLocation, boolean move, boolean atomic) throws IOException {
    String[] 		children;
    int 		i;
    Path source;
    Path 		target;

    if (sourceLocation.isDirectory()) {
      if (!targetLocation.exists()) {
	if (!targetLocation.mkdir())
	  return false;
      }

      children = sourceLocation.list();
      for (i = 0; i < children.length; i++) {
        if (!copyOrMove(
            new File(sourceLocation.getAbsoluteFile(), children[i]),
            new File(targetLocation.getAbsoluteFile(), children[i]),
            move, atomic))
          return false;
      }

      if (move)
        return sourceLocation.delete();
      else
	return true;
    }
    else {
      source = FileSystems.getDefault().getPath(sourceLocation.getAbsolutePath());
      if (targetLocation.isDirectory())
        target = FileSystems.getDefault().getPath(targetLocation.getAbsolutePath() + File.separator + sourceLocation.getName());
      else
        target = FileSystems.getDefault().getPath(targetLocation.getAbsolutePath());
      if (move) {
	if (atomic)
	  Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
	else
	  Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
      }
      else {
	Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
      }
      return true;
    }
  }

  /**
   * Writes the given object to the specified file. The message is either
   * appended or replaces the current content of the file.
   *
   * @param filename	the file to write to
   * @param obj		the object to write
   * @param append	whether to append the message or not
   * @param encoding	the encoding to use, null for default
   * @return		true if writing was successful
   */
  public static String writeToFileMsg(String filename, Object obj, boolean append, String encoding) {
    String			result;
    List<String> lines;
    StandardOpenOption[]	options;

    result = null;
    lines = new ArrayList<>();
    lines.add("" + obj);
    if (append)
      options = new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE};
    else
      options = new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    try {
      if (encoding == null)
	Files.write(new File(filename).toPath(), lines, options);
      else
	Files.write(new File(filename).toPath(), lines, Charset.forName(encoding), options);
    }
    catch (Exception e) {
      result = "Failed to write to '" + filename + "'\n" + e;
    }

    return result;
  }
}
