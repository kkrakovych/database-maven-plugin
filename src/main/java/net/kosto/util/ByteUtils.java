/*
 * Copyright 2018 Kostyantyn Krakovych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.kosto.util;

public final class ByteUtils {

  private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

  private static final String ATTRIBUTE_DATA_SHOULD_BE_NOT_NULL = "Attribute data should be not null.";

  private ByteUtils() {
  }

  /**
   * Converts an array of bytes into a string.
   * <p>
   * Source code from {@code javax.xml.bind} package to minimize dependencies.
   * Because since {@code Java 9} the package is not in class path by default.
   *
   * @param data An array of bytes.
   * @return A string containing a lexical representation of xsd:hexBinary.
   * @throws IllegalArgumentException if {@code data} is null.
   */
  public static String printHexBinary(byte[] data) {
    if (data == null) {
      throw new IllegalArgumentException(ATTRIBUTE_DATA_SHOULD_BE_NOT_NULL);
    }

    StringBuilder r = new StringBuilder(data.length * 2);
    for (byte b : data) {
      r.append(hexCode[(b >> 4) & 0xF]);
      r.append(hexCode[(b & 0xF)]);
    }
    return r.toString();
  }
}
