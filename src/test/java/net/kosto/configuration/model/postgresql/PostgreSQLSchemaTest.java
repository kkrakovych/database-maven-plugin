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

import static net.kosto.configuration.model.DatabaseScriptCondition.AFTER;
import static net.kosto.configuration.model.DatabaseScriptCondition.BEFORE;
import static net.kosto.configuration.model.DatabaseScriptType.ONE_TIME;
import static net.kosto.configuration.model.DatabaseScriptType.REUSABLE;
import static net.kosto.util.Error.DUPLICATED_ATTRIBUTE;
import static net.kosto.util.Error.EMPTY_LIST_ATTRIBUTE;
import static net.kosto.util.Error.MISSING_TWO_ATTRIBUTES;
import static net.kosto.util.Error.SEMI_DEFINED_ATTRIBUTES;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.INDEX;
import static net.kosto.util.StringUtils.OBJECT;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_OBJECTS;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_SCRIPTS;
import static net.kosto.util.StringUtils.SCHEMA;
import static net.kosto.util.StringUtils.SCRIPT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.util.FileUtils;

class PostgreSQLSchemaTest {

  private DatabaseItem schema;
  private List<DatabaseItem> objects;
  private List<DatabaseItem> scripts;

  @BeforeEach
  private void setUp() {
    schema = new PostgreSQLSchema();
    schema.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    schema.setOutputDirectoryFull(FileUtils.ROOT_PATH);

    objects = new ArrayList<>();
    ((PostgreSQLSchema) schema).setObjects(objects);

    for (int index = 0; index < 2; index++) {
      DatabaseItem object = new PostgreSQLObject();
      object.setType(PostgreSQLObjectType.VIEW.name());
      ((PostgreSQLSchema) schema).getObjects().add(object);
    }

    scripts = new ArrayList<>();
    ((PostgreSQLSchema) schema).setScripts(scripts);

    for (int index = 0; index < 2; index++) {
      DatabaseItem script = new PostgreSQLScript();
      script.setType(ONE_TIME.name());
      script.setCondition(BEFORE.name());
      ((PostgreSQLSchema) schema).getScripts().add(script);
    }

    for (int index = 0; index < 2; index++) {
      PostgreSQLScript script = new PostgreSQLScript();
      script.setType(REUSABLE.name());
      script.setCondition(AFTER.name());
      ((PostgreSQLSchema) schema).getScripts().add(script);
    }
  }

  @Test
  @DisplayName("Object/Script - Missing attributes.")
  void test01() {
    ((PostgreSQLSchema) schema).setObjects(null);
    ((PostgreSQLSchema) schema).setScripts(null);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_TWO_ATTRIBUTES.message(POSTGRESQL_SCHEMA_OBJECTS, POSTGRESQL_SCHEMA_SCRIPTS), result.getMessage());
  }

  @Test
  @DisplayName("Object - Empty list attribute.")
  void test02() {
    ((PostgreSQLSchema) schema).getObjects().clear();
    ((PostgreSQLSchema) schema).setScripts(null);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(EMPTY_LIST_ATTRIBUTE.message(POSTGRESQL_SCHEMA_OBJECTS, OBJECT), result.getMessage());
  }

  @Test
  @DisplayName("Object - Semi defined attributes.")
  void test03() {
    objects.get(0).setIndex(1);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(SEMI_DEFINED_ATTRIBUTES.message(POSTGRESQL_SCHEMA_OBJECTS, OBJECT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Object - Duplicated attribute.")
  void test04() {
    objects.forEach(scheme -> scheme.setIndex(1));

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(DUPLICATED_ATTRIBUTE.message(POSTGRESQL_SCHEMA_OBJECTS, OBJECT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Script - Empty list attribute.")
  void test05() {
    ((PostgreSQLSchema) schema).setObjects(null);
    ((PostgreSQLSchema) schema).getScripts().clear();

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(EMPTY_LIST_ATTRIBUTE.message(POSTGRESQL_SCHEMA_SCRIPTS, SCRIPT), result.getMessage());
  }

  @Test
  @DisplayName("Script - Semi defined attributes.")
  void test06() {
    scripts.get(0).setIndex(1);

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(SEMI_DEFINED_ATTRIBUTES.message(POSTGRESQL_SCHEMA_SCRIPTS, SCRIPT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Script - Duplicated attribute.")
  void test07() {
    scripts.forEach(scheme -> scheme.setIndex(1));

    Executable executable = schema::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(DUPLICATED_ATTRIBUTE.message(POSTGRESQL_SCHEMA_SCRIPTS, SCRIPT, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Default values.")
  void test08() throws MojoExecutionException {
    schema.validate();

    assertNull(schema.getIndex());
    assertNull(schema.getOrder());
    assertEquals(SCHEMA, schema.getName());
    assertNull(schema.getType());
    assertNull(schema.getCondition());
    assertEquals(FILE_MASK_SQL, schema.getFileMask());
    assertEquals(schema.getName(), schema.getSourceDirectory());
    assertFalse(schema.getIgnoreDirectory());
    assertEquals(COLON, schema.getDefineSymbol());
    assertFalse(schema.getIgnoreDefine());
    assertEquals(UNIX_SEPARATOR + SCHEMA + UNIX_SEPARATOR, schema.getExecuteDirectory());
    assertEquals(Paths.get(UNIX_SEPARATOR, schema.getName()), schema.getSourceDirectoryFull());
    assertEquals(Paths.get(UNIX_SEPARATOR, schema.getName()), schema.getOutputDirectoryFull());

    int index;

    index = 0;
    for (DatabaseItem object : ((PostgreSQLSchema) schema).getObjects()) {
      assertEquals(index++, (int) object.getOrder());
    }

    index = 0;
    for (DatabaseItem scripts : ((PostgreSQLSchema) schema).getScripts()) {
      assertEquals((index++) + (scripts.getCondition().equals(BEFORE.name()) ? 1000 : 2000), (int) scripts.getOrder());
    }
  }
}
