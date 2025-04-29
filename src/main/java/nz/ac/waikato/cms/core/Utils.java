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
 * Utils.java
 * Copyright (C) 2008-2025 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing some simple utility methods.
 *
 * @author Eibe Frank
 * @author Yong Wang
 * @author Len Trigg
 * @author Julien Prados
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Utils {

  /** the indicator for arrays. */
  public static final String ARRAY_INDICATOR = "[]";

  /**
   * Returns the basic class of an array class (handles multi-dimensional
   * arrays).
   * @param c        the array to inspect
   * @return         the class of the innermost elements
   */
  public static Class getArrayClass(Class c) {
    if (c.getComponentType().isArray())
      return getArrayClass(c.getComponentType());
    else
      return c.getComponentType();
  }

  /**
   * Returns the dimensions of the given array. Even though the
   * parameter is of type "Object" one can hand over primitve arrays, e.g.
   * int[3] or double[2][4].
   *
   * @param array       the array to determine the dimensions for
   * @return            the dimensions of the array
   */
  public static int getArrayDimensions(Class array) {
    if (array.getComponentType().isArray())
      return 1 + getArrayDimensions(array.getComponentType());
    else
      return 1;
  }

  /**
   * Returns the dimensions of the given array. Even though the
   * parameter is of type "Object" one can hand over primitve arrays, e.g.
   * int[3] or double[2][4].
   *
   * @param array       the array to determine the dimensions for
   * @return            the dimensions of the array
   */
  public static int getArrayDimensions(Object array) {
    return getArrayDimensions(array.getClass());
  }

  /**
   * Returns the given Array in a string representation. Even though the
   * parameter is of type "Object" one can hand over primitve arrays, e.g.
   * int[3] or double[2][4].
   *
   * @param array       the array to return in a string representation
   * @param outputClass	whether to output the class name instead of calling
   * 			the object's "toString()" method
   * @return            the array as string
   */
  public static String arrayToString(Object array, boolean outputClass) {
    StringBuilder	result;
    int			dimensions;
    int			i;
    Object		obj;

    result     = new StringBuilder();
    dimensions = getArrayDimensions(array);

    if (dimensions == 0) {
      result.append("null");
    }
    else if (dimensions == 1) {
      for (i = 0; i < Array.getLength(array); i++) {
	if (i > 0)
	  result.append(",");
	if (Array.get(array, i) == null) {
	  result.append("null");
	}
	else {
	  obj = Array.get(array, i);
	  if (outputClass) {
	    if (obj instanceof Class)
	      result.append(((Class) obj).getName());
	    else
	      result.append(obj.getClass().getName());
	  }
	  else {
	    result.append(obj.toString());
	  }
	}
      }
    }
    else {
      for (i = 0; i < Array.getLength(array); i++) {
	if (i > 0)
	  result.append(",");
	result.append("[" + arrayToString(Array.get(array, i)) + "]");
      }
    }

    return result.toString();
  }

  /**
   * Returns the given Array in a string representation. Even though the
   * parameter is of type "Object" one can hand over primitve arrays, e.g.
   * int[3] or double[2][4].
   *
   * @param array       the array to return in a string representation
   * @return            the array as string
   */
  public static String arrayToString(Object array) {
    return arrayToString(array, false);
  }

  /**
   * Flattens the list into a single, long string. The separator string gets
   * added between the objects, but not after the last one.
   *
   * @param lines	the lines to flatten
   * @param sep		the separator
   * @return		the generated string
   */
  public static String flatten(List lines, String sep) {
    return flatten(lines.toArray(new Object[lines.size()]), sep);
  }

  /**
   * Flattens the array into a single, long string. The separator string gets
   * added between the objects, but not after the last one. Uses the "toString()"
   * method of the objects to turn them into a string.
   *
   * @param lines	the lines to flatten
   * @param sep		the separator
   * @return		the generated string
   */
  public static String flatten(Object[] lines, String sep) {
    StringBuilder	result;
    int			i;

    result = new StringBuilder();

    for (i = 0; i < lines.length; i++) {
      if (i > 0)
	result.append(sep);
      result.append(lines[i].toString());
    }

    return result.toString();
  }

  /**
   * Turns a class name into a Class instance. Arrays are indicated by "[]" at
   * the end of the name. Multiple array indicators can be used.
   *
   * @param classname	the class name to return the Class instance for
   * @return		the generated class instance, null if failed to create
   */
  public static Class stringToClass(String classname) {
    Class	result;
    String	arrays;
    int		arrayDim;
    int		i;

    result = null;

    arrayDim = 0;
    if (classname.endsWith(ARRAY_INDICATOR)) {
      arrays    = classname.substring(classname.indexOf(ARRAY_INDICATOR));
      arrays    = arrays.replace("][", "],[");
      arrayDim  = arrays.split(",").length;
      classname = classname.substring(0, classname.indexOf(ARRAY_INDICATOR));
    }

    try {
      result = Class.forName(classname);
      for (i = 0; i < arrayDim; i++)
	result = Array.newInstance(result, 0).getClass();
    }
    catch (Exception e) {
      result = null;
    }

    return result;
  }

  /**
   * Creates a new array of the specified element type with the specified
   * number of elements. Arrays are indicated by "[]" at
   * the end of the clas name. Multiple array indicators can be used.
   * E.g., newArray("java.lang.Double[]", 5) will generate "Double[5][]".
   *
   * @param elementClass	the class type for the array elements
   * @param length 		the length of the array
   * @return			the generated array instance, null if failed to create
   */
  public static Object newArray(String elementClass, int length) {
    Object	result;

    try {
      result = Array.newInstance(stringToClass(elementClass), length);
    }
    catch (Exception e) {
      result = null;
    }

    return result;
  }

  /**
   * Turns a class into a string.
   *
   * @param c		the class to turn into a string
   * @return		the string
   */
  public static String classToString(Class c) {
    String	result;
    int		dim;
    int		i;

    if (c.isArray()) {
      dim    = getArrayDimensions(c);
      result = getArrayClass(c).getName();
      for (i = 0; i < dim; i++)
	result += ARRAY_INDICATOR;
    }
    else {
      result = c.getName();
    }

    return result;
  }

  /**
   * Turns a class array into a string.
   *
   * @param c		the class array to turn into a string
   * @return		the string
   */
  public static String classesToString(Class[] c) {
    return classesToString(c, ", ");
  }

  /**
   * Turns a class array into a string.
   *
   * @param c		the class array to turn into a string
   * @param separator	the separator between the classes
   * @return		the string
   */
  public static String classesToString(Class[] c, String separator) {
    StringBuilder	result;

    result = new StringBuilder();
    for (Class cls: c) {
      if (result.length() > 0)
	result.append(separator);
      result.append(classToString(cls));
    }

    return result.toString();
  }

  /**
   * Turns the double array into a float array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static float[] toFloat(double[] array) {
    float[]	result;
    int		i;

    result = new float[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = (float) array[i];

    return result;
  }

  /**
   * Turns the float array into a double array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static double[] toDouble(float[] array) {
    double[]	result;
    int		i;

    result = new double[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = array[i];

    return result;
  }

  /**
   * Pads the string with a padding character with to at most "width" width.
   * Does not truncate the string.
   *
   * @param s		the string to pad
   * @param padding	the padding character
   * @param width	the maximum width
   * @return		the padded string
   */
  public static String padLeft(String s, char padding, int width) {
    return padLeft(s, padding, width, false);
  }

  /**
   * Pads the string with a padding character with to at most "width" width.
   * Truncating, if string is longer than "width", is optional.
   *
   * @param s		the string to pad
   * @param padding	the padding character
   * @param width	the maximum width
   * @param truncate	if true then the string can be truncated (on the left)
   * 			to fit width
   * @return		the padded string
   */
  public static String padLeft(String s, char padding, int width, boolean truncate) {
    StringBuilder	result;

    result = new StringBuilder(s);

    // pad
    while (result.length() < width)
      result.insert(0, padding);

    // truncate
    if (truncate) {
      if (result.length() > width)
	result.delete(0, result.length() - width);
    }

    return result.toString();
  }

  /**
   * Returns the stacktrace of the throwable as string.
   *
   * @param t		the throwable to get the stacktrace for
   * @return		the stacktrace
   */
  public static String throwableToString(Throwable t) {
    return throwableToString(t, -1);
  }

  /**
   * Returns the stacktrace of the throwable as string.
   *
   * @param t		the throwable to get the stacktrace for
   * @param maxLines	the maximum number of lines to print, &lt;= 0 for all
   * @return		the stacktrace
   */
  public static String throwableToString(Throwable t, int maxLines) {
    StringWriter	writer;
    StringBuilder	result;
    String[]		lines;
    int			i;

    writer = new StringWriter();
    t.printStackTrace(new PrintWriter(writer));

    if (maxLines > 0) {
      result = new StringBuilder();
      lines  = writer.toString().split("\n");
      for (i = 0; i < maxLines; i++) {
	if (i > 0)
	  result.append("\n");
	result.append(lines[i]);
      }
    }
    else {
      result = new StringBuilder(writer.toString());
    }

    return result.toString();
  }

  /**
   * Returns the current stack trace.
   *
   * @param maxDepth	the maximum depth of the stack trace, &lt;= 0 for full trace
   * @return		the stack trace as string (multiple lines)
   */
  public static String getStackTrace(int maxDepth) {
    StringBuilder	result;
    Throwable		th;
    StackTraceElement[]	trace;
    int			i;

    result = new StringBuilder();
    th     = new Throwable();
    th.fillInStackTrace();
    trace  = th.getStackTrace();
    if (maxDepth <= 0)
      maxDepth = trace.length - 1;
    maxDepth++;  // we're starting at 1 not 0
    maxDepth = Math.min(maxDepth, trace.length);
    for (i = 1; i < maxDepth; i++) {
      if (i > 1)
	result.append("\n");
      result.append(trace[i]);
    }

    return result.toString();
  }

  /**
   * Converts the given decimal number into a different base.
   *
   * @param n		the decimal number to convert
   * @param base	the base
   * @return		the digits in the new base; index refers to power,
   * 			ie, 0 = base^0, 3 = base^3
   */
  public static List<Integer> toBase(int n, int base) {
    List<Integer>	result;
    int			current;
    int			times;
    int			remainder;

    result  = new ArrayList<Integer>();
    current = n;
    do {
      times     = current / base;
      remainder = current - times * base;
      result.add(remainder);
      current   = times;
    }
    while (times > 0);

    return result;
  }

  /**
   * Splits the row into separate cells based on the delimiter character.
   * String.split(regexp) does not work with empty cells (eg ",,,").
   *
   * @param line	the row to split
   * @param delimiter	the delimiter to use
   * @return		the cells
   */
  public static String[] split(String line, char delimiter) {
    return split(line, "" + delimiter);
  }

  /**
   * Splits the row into separate cells based on the delimiter string.
   * String.split(regexp) does not work with empty cells (eg ",,,").
   *
   * @param line	the row to split
   * @param delimiter	the delimiter to use
   * @return		the cells
   */
  public static String[] split(String line, String delimiter) {
    ArrayList<String>	result;
    int			lastPos;
    int			currPos;

    result  = new ArrayList<String>();
    lastPos = -1;
    while ((currPos = line.indexOf(delimiter, lastPos + 1)) > -1) {
      result.add(line.substring(lastPos + 1, currPos));
      lastPos = currPos;
    }
    result.add(line.substring(lastPos + 1));

    return result.toArray(new String[result.size()]);
  }
}
