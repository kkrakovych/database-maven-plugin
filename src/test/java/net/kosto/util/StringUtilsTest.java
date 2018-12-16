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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.kosto.configuration.model.DatabaseScriptType;

class StringUtilsTest {

  @Test
  @DisplayName("getEnumElement: Null Value")
  void testGetEnumElement01() {
    assertNull(StringUtils.getEnumElement(DatabaseScriptType.class, null));
  }

  @Test
  @DisplayName("getEnumElement: Bad Value")
  void testGetEnumElement02() {
    assertNull(StringUtils.getEnumElement(DatabaseScriptType.class, "bad"));
  }

  @Test
  @DisplayName("getEnumElement: Normal Value Lowercase")
  void testGetEnumElement03() {
    assertNull(StringUtils.getEnumElement(DatabaseScriptType.class, "reusable"));
  }

  @Test
  @DisplayName("getEnumElement: Normal Value Uppercase")
  void testGetEnumElement04() {
    DatabaseScriptType expected = DatabaseScriptType.REUSABLE;
    DatabaseScriptType actual = StringUtils.getEnumElement(DatabaseScriptType.class, "REUSABLE");

    assertEquals(expected, actual);
  }
}
