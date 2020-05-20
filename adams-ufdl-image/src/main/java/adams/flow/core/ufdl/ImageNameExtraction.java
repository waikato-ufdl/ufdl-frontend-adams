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
 * ImageNameExtraction.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.flow.core.ufdl;

import adams.core.io.FileUtils;
import adams.core.io.PlaceholderFile;

import java.io.File;

/**
 * How to extract image names.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public enum ImageNameExtraction {
  /** just the name. */
  NAME,
  /** name and parent dir. */
  NAME_AND_PARENT,
  /** the full path. */
  FULL_PATH;

  /**
   * Extracts the name according to the handling.
   *
   * @param file	the file to process
   * @return		the generated string
   */
  public String extract(String file) {
    return extract(new PlaceholderFile(file));
  }

  /**
   * Extracts the name according to the handling.
   *
   * @param file	the file to process
   * @return		the generated string
   */
  public String extract(File file) {
    switch (this) {
      case NAME:
        return file.getName();
      case NAME_AND_PARENT:
        if (file.getParentFile() == null)
          return file.getName();
        else
	  return file.getParentFile().getName() + "/" + file.getName();
      case FULL_PATH:
        return FileUtils.useForwardSlashes(file.getAbsolutePath());
      default:
        throw new IllegalStateException("Unhandled type: " + this);
    }
  }
}
