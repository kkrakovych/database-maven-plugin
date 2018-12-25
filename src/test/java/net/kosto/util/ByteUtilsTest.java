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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ByteUtilsTest {

  @Test
  @DisplayName("printHexBinary: Null Value.")
  void testPrintHexBinary01() {
    Executable executable = () -> ByteUtils.printHexBinary(null);

    Throwable result = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Attribute data should be not null.", result.getMessage());
  }

  @Test
  @DisplayName("printHexBinary: Zero Value.")
  void testPrintHexBinary02() {
    byte[] data = new byte[]{(byte) 0};

    String expected = "00";
    String actual = ByteUtils.printHexBinary(data);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("printHexBinary: Normal Value.")
  void testPrintHexBinary03() {
    byte[] data = new byte[]{(byte) 10};

    String expected = "0A";
    String actual = ByteUtils.printHexBinary(data);
    assertEquals(expected, actual);
  }
}
