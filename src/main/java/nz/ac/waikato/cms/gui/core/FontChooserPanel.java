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
 * FontChooserPanel.java
 * Copyright (C) 2015-2018 University of Waikato, Hamilton, New Zealand
 */

package nz.ac.waikato.cms.gui.core;

import java.awt.Font;

/**
 * A panel that contains a text field with the current Font
 * and a button for bringing up a Font dialog.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class FontChooserPanel
  extends AbstractChooserPanel<Font> {

  /** for serialization. */
  private static final long serialVersionUID = -8755020252465094120L;

  /** the dialog to use. */
  protected FontChooser m_FontChooser;

  /**
   * Initializes the panel with the default sans font.
   */
  public FontChooserPanel() {
    this(Font.decode("sans"));
  }

  /**
   * Initializes the panel with the given font.
   *
   * @param font	the color to use
   */
  public FontChooserPanel(Font font) {
    super();

    setCurrent(font);
  }

  /**
   * Returns the number of columns in the selection text field.
   *
   * @return		the number of columns
   */
  protected int getSelectionColumns() {
    return 10;
  }

  /**
   * Performs the actual choosing of an object.
   *
   * @return		the chosen object or null if none chosen
   */
  protected Font doChoose() {
    if (m_FontChooser == null) {
      if (GUIHelper.getParentDialog(this) != null)
        m_FontChooser = new FontChooser(GUIHelper.getParentDialog(this));
      else
        m_FontChooser = new FontChooser(GUIHelper.getParentFrame(this));
    }

    m_FontChooser.setCurrent(getCurrent());
    m_FontChooser.setVisible(true);

    return m_FontChooser.getCurrent();
  }

  /**
   * Converts the value into its string representation.
   *
   * @param value	the value to convert
   * @return		the generated string
   */
  protected String toString(Font value) {
    return encodeFont(value);
  }

  /**
   * Converts the string representation into its object representation.
   *
   * @param value	the string value to convert
   * @return		the generated object
   */
  protected Font fromString(String value) {
    return Font.decode(value);
  }

  /**
   * Turns a font into a string representation that can get parsed with
   * {@link Font#decode(String)} again.
   *
   * @param f		the font to turn into a string
   * @return		the font
   */
  public static String encodeFont(Font f) {
    String	result;
    String	face;

    if (f.isBold() && f.isItalic())
      face = "BOLDITALIC";
    else if (f.isBold())
      face = "BOLD";
    else if (f.isItalic())
      face = "ITALIC";
    else
      face = "PLAIN";

    result = f.getName() + "-" + face + "-" + f.getSize();

    return result;
  }
}
