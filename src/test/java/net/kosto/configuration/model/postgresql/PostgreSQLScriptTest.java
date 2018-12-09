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
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_SCRIPT_CONDITION;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_SCRIPT_TYPE;
import static net.kosto.util.StringUtils.UNDERSCORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.DatabaseScriptCondition;
import net.kosto.configuration.model.DatabaseScriptType;
import net.kosto.util.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class PostgreSQLScriptTest {

  private DatabaseItem script;

  @BeforeEach
  void setUp() {
    script = new PostgreSQLScript();
    script.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    script.setOutputDirectoryFull(FileUtils.ROOT_PATH);
    script.setType(DatabaseScriptType.ONE_TIME.name());
    script.setCondition(DatabaseScriptCondition.BEFORE.name());
  }

  @Test
  @DisplayName("Script - Missing attribute Type.")
  void test01() {
    script.setType(null);

    Executable executable = script::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_ATTRIBUTE.message(POSTGRESQL_SCHEMA_SCRIPT_TYPE), result.getMessage());
  }

  @Test
  @DisplayName("Script - Missing attribute Condition.")
  void test02() {
    script.setCondition(null);

    Executable executable = script::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_ATTRIBUTE.message(POSTGRESQL_SCHEMA_SCRIPT_CONDITION), result.getMessage());
  }

  @Test
  @DisplayName("Default values.")
  void test03() throws MojoExecutionException {
    script.validate();

    assertNull(script.getIndex());
    assertNull(script.getOrder());
    assertNull(script.getName());
    assertEquals(DatabaseScriptType.ONE_TIME.name(), script.getType());
    assertEquals(DatabaseScriptCondition.BEFORE.name(), script.getCondition());
    assertEquals(FILE_MASK_SQL, script.getFileMask());
    String path = DatabaseScriptType.ONE_TIME.getSourceDirectory() + UNDERSCORE + DatabaseScriptCondition.BEFORE.getSourceDirectory();
    assertEquals(path, script.getSourceDirectory());
    assertFalse(script.getIgnoreDirectory());
    assertEquals(COLON, script.getDefineSymbol());
    assertFalse(script.getIgnoreDefine());
    assertEquals(UNIX_SEPARATOR + path + UNIX_SEPARATOR, script.getExecuteDirectory());
    assertEquals(Paths.get(UNIX_SEPARATOR, path), script.getSourceDirectoryFull());
    assertEquals(Paths.get(UNIX_SEPARATOR, path), script.getOutputDirectoryFull());
  }
}
