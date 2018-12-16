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

package net.kosto.configuration.model.oracle;

import static net.kosto.configuration.model.DatabaseScriptCondition.AFTER;
import static net.kosto.configuration.model.DatabaseScriptCondition.BEFORE;
import static net.kosto.configuration.model.DatabaseScriptType.ONE_TIME;
import static net.kosto.configuration.model.DatabaseScriptType.REUSABLE;
import static net.kosto.util.Error.DUPLICATED_ATTRIBUTE;
import static net.kosto.util.Error.EMPTY_LIST_ATTRIBUTE;
import static net.kosto.util.Error.MISSING_TWO_ATTRIBUTES;
import static net.kosto.util.Error.ONE_TIME_SCRIPT_VS_IGNORE_SERVICE_TABLES;
import static net.kosto.util.Error.SEMI_DEFINED_ATTRIBUTES;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.INDEX;
import static net.kosto.util.StringUtils.OBJECT;
import static net.kosto.util.StringUtils.ORACLE_SCHEMA_OBJECTS;
import static net.kosto.util.StringUtils.ORACLE_SCHEMA_SCRIPTS;
import static net.kosto.util.StringUtils.SCHEMA;
import static net.kosto.util.StringUtils.SCRIPT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.DatabaseScriptType;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.configuration.model.common.CommonSchema;
import net.kosto.util.FileUtils;

class OracleSchemaTest {

  private DatabaseItem schema;
  private List<DatabaseItem> objects;
  private List<DatabaseItem> scripts;

  @BeforeEach
  private void setUp() {
    schema = new OracleSchema();
    schema.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    schema.setOutputDirectoryFull(FileUtils.ROOT_PATH);

    objects = new ArrayList<>();
    ((OracleSchema) schema).setObjects(objects);

    for (int index = 0; index < 2; index++) {
      DatabaseItem object = new OracleObject();
      object.setType(OracleObjectType.VIEW.name());
      ((OracleSchema) schema).getObjects().add(object);
    }

    scripts = new ArrayList<>();
    ((OracleSchema) schema).setScripts(scripts);

    for (int index = 0; index < 2; index++) {
      DatabaseItem script = new OracleScript();
      script.setType(ONE_TIME.name());
      script.setCondition(BEFORE.name());
      ((OracleSchema) schema).getScripts().add(script);
    }

    for (int index = 0; index < 2; index++) {
      OracleScript script = new OracleScript();
      script.setType(REUSABLE.name());
      script.setCondition(AFTER.name());
      ((OracleSchema) schema).getScripts().add(script);
    }
  }

  @Test
  @DisplayName("Object/Script - Missing attributes.")
  void test01() {
    ((OracleSchema) schema).setObjects(null);
    ((OracleSchema) schema).setScripts(null);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_TWO_ATTRIBUTES.message(ORACLE_SCHEMA_OBJECTS, ORACLE_SCHEMA_SCRIPTS), result.getMessage());
  }

  @Test
  @DisplayName("Object - Empty list attribute.")
  void test02() {
    ((OracleSchema) schema).getObjects().clear();
    ((OracleSchema) schema).setScripts(null);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(EMPTY_LIST_ATTRIBUTE.message(ORACLE_SCHEMA_OBJECTS, OBJECT), result.getMessage());
  }

  @Test
  @DisplayName("Object - Semi defined attributes.")
  void test03() {
    objects.get(0).setIndex(1);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(SEMI_DEFINED_ATTRIBUTES.message(ORACLE_SCHEMA_OBJECTS, OBJECT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Object - Duplicated attribute.")
  void test04() {
    objects.forEach(scheme -> scheme.setIndex(1));

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(DUPLICATED_ATTRIBUTE.message(ORACLE_SCHEMA_OBJECTS, OBJECT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Script - Empty list attribute.")
  void test05() {
    ((OracleSchema) schema).setObjects(null);
    ((OracleSchema) schema).getScripts().clear();

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(EMPTY_LIST_ATTRIBUTE.message(ORACLE_SCHEMA_SCRIPTS, SCRIPT), result.getMessage());
  }

  @Test
  @DisplayName("Script - Semi defined attributes.")
  void test06() {
    scripts.get(0).setIndex(1);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(SEMI_DEFINED_ATTRIBUTES.message(ORACLE_SCHEMA_SCRIPTS, SCRIPT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Script - Duplicated attribute.")
  void test07() {
    scripts.forEach(scheme -> scheme.setIndex(1));

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(DUPLICATED_ATTRIBUTE.message(ORACLE_SCHEMA_SCRIPTS, SCRIPT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Default values.")
  void test08() throws MojoExecutionException {
    schema.validate();

    assertEquals(false, schema.getIgnoreServiceTables());
    assertNull(schema.getIndex());
    assertNull(schema.getOrder());
    assertEquals(SCHEMA, schema.getName());
    assertNull(schema.getType());
    assertNull(schema.getCondition());
    assertEquals(FILE_MASK_SQL, schema.getFileMask());
    assertEquals(schema.getName(), schema.getSourceDirectory());
    assertFalse(schema.getIgnoreDirectory());
    assertEquals(AMPERSAND, schema.getDefineSymbol());
    assertFalse(schema.getIgnoreDefine());
    assertEquals(UNIX_SEPARATOR + SCHEMA + UNIX_SEPARATOR, schema.getExecuteDirectory());
    assertEquals(File.separator + schema.getName(), schema.getSourceDirectoryFull().toString());
    assertEquals(File.separator + schema.getName(), schema.getOutputDirectoryFull().toString());

    int index;

    index = 0;
    for (DatabaseItem object : ((OracleSchema) schema).getObjects()) {
      assertEquals(index++, (int) object.getOrder());
    }

    index = 0;
    for (DatabaseItem scripts : ((OracleSchema) schema).getScripts()) {
      assertEquals((index++) + (scripts.getCondition().equals(BEFORE.name()) ? 1000 : 2000), (int) scripts.getOrder());
    }
  }

  @Test
  @DisplayName("Ignore service tables with one time script.")
  void test09() {
    schema.setIgnoreServiceTables(true);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(ONE_TIME_SCRIPT_VS_IGNORE_SERVICE_TABLES.message(), result.getMessage());
  }

  @Test
  @DisplayName("Ignore service tables without one time script.")
  void test10() {
    schema.setIgnoreServiceTables(true);
    ((OracleSchema) schema).getScripts().stream()
        .filter(script -> ONE_TIME.equals(DatabaseScriptType.valueOf(script.getType())))
        .forEach(script -> script.setType(REUSABLE.name()));

    Executable executable = schema::validate;
    assertDoesNotThrow(executable);
  }

  @Test
  @DisplayName("Custom values.")
  void test11() {
    CommonDatabaseItem item = new CommonSchema();
    item.setIgnoreServiceTables(true);
    item.setIndex(1);
    item.setName("testName");
    item.setType("testType");
    item.setCondition("testCondition");
    item.setFileMask("testFileMask");
    item.setSourceDirectory("testSourceDirectory");
    item.setIgnoreDirectory(true);
    item.setDefineSymbol("t");
    item.setIgnoreDefine(true);

    OracleSchema schema = new OracleSchema(item);
    assertEquals(item.getIgnoreServiceTables(), schema.getIgnoreServiceTables());
    assertEquals(item.getIndex(), schema.getIndex());
    assertEquals(item.getName(), schema.getName());
    assertEquals(item.getType(), schema.getType());
    assertEquals(item.getCondition(), schema.getCondition());
    assertEquals(item.getFileMask(), schema.getFileMask());
    assertEquals(item.getSourceDirectory(), schema.getSourceDirectory());
    assertEquals(item.getIgnoreDirectory(), schema.getIgnoreDirectory());
    assertEquals(item.getDefineSymbol(), schema.getDefineSymbol());
    assertEquals(item.getIgnoreDefine(), schema.getIgnoreDefine());
  }
}
