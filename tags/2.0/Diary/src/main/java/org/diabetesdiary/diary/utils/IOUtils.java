/*
 *   Copyright (C) 2006-2007 Jiri Majer. All Rights Reserved.
 *   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   This code is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License version 2 only, as
 *   published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.diabetesdiary.diary.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Pavel Cernocky
 */
public abstract class IOUtils {

  public static void closeQuietly(Reader r) {
    if (r != null) {
      try {
        r.close();
      }
      catch (Exception e) {
        // ignore
      }
    }
  }

  public static void closeQuietly(Writer w) {
    if (w != null) {
      try {
        w.close();
      }
      catch (Exception e) {
        // ignore
      }
    }
  }

  public static void closeQuietly(InputStream is) {
    if (is != null) {
      try {
        is.close();
      }
      catch (Exception e) {
        // ignore
      }
    }
  }

  public static void closeQuietly(OutputStream os) {
    if (os != null) {
      try {
        os.close();
      }
      catch (Exception e) {
        // ignore
      }
    }
  }

  public static long inputToOutputStream(InputStream is, OutputStream os, int bufSize, boolean closeInputStream, boolean closeOutputStream) throws IOException {
    try {
      long totalCount = 0;

      byte[] buffer = new byte[bufSize];
      int cnt;
      while ((cnt = is.read(buffer)) != -1) {
        os.write(buffer, 0, cnt);
        totalCount += cnt;
      }

      return totalCount;
    } finally {
      if (closeInputStream) {
        try {
          is.close();
        } catch (IOException e) {}
      }
      if (closeOutputStream) {
        try {
          os.close();
        } catch (IOException e) {}
      }
    }
  }

  public static long readerToWriter(Reader r, Writer w, int bufSize, boolean closeReader, boolean closeWriter) throws IOException {
    try {
      long totalCount = 0;

      char[] buffer = new char[bufSize];
      int cnt;
      while ((cnt = r.read(buffer)) != -1) {
        w.write(buffer, 0, cnt);
        totalCount += cnt;
      }

      return totalCount;
    } finally {
      if (closeReader) {
        try {
          r.close();
        } catch (IOException e) {}
      }
      if (closeWriter) {
        try {
          w.close();
        } catch (IOException e) {}
      }
    }
  }

}