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

package net.kosto.configuration.model.postgresql;

import static net.kosto.util.Error.MISSING_ATTRIBUTE;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_OBJECT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.util.FileUtils;

class PostgreSQLObjectTest {

  private DatabaseItem object;

  @BeforeEach
  void setUp() {
    object = new PostgreSQLObject();
    object.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    object.setOutputDirectoryFull(FileUtils.ROOT_PATH);
    object.setType(PostgreSQLObjectType.VIEW.name());
  }

  @Test
  @DisplayName("Object - Missing attribute.")
  void test01() {
    object.setType(null);

    Executable executable = object::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_ATTRIBUTE.message(POSTGRESQL_SCHEMA_OBJECT_TYPE), result.getMessage());
  }

  @Test
  @DisplayName("Default values.")
  void test02() throws MojoExecutionException {
    object.validate();

    assertNull(object.getIndex());
    assertNull(object.getOrder());
    assertNull(object.getName());
    assertEquals(PostgreSQLObjectType.VIEW.name(), object.getType());
    assertNull(object.getCondition());
    assertEquals(FILE_MASK_SQL, object.getFileMask());
    assertEquals(PostgreSQLObjectType.VIEW.getSourceDirectory(), object.getSourceDirectory());
    assertFalse(object.getIgnoreDirectory());
    assertEquals(COLON, object.getDefineSymbol());
    assertFalse(object.getIgnoreDefine());
    assertEquals(UNIX_SEPARATOR + PostgreSQLObjectType.VIEW.getSourceDirectory() + UNIX_SEPARATOR, object.getExecuteDirectory());
    assertEquals(File.separator + PostgreSQLObjectType.VIEW.getSourceDirectory(), object.getSourceDirectoryFull().toString());
    assertEquals(File.separator + PostgreSQLObjectType.VIEW.getSourceDirectory(), object.getOutputDirectoryFull().toString());
  }
}
