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

import static net.kosto.util.Error.DUPLICATED_ATTRIBUTE;
import static net.kosto.util.Error.EMPTY_LIST_ATTRIBUTE;
import static net.kosto.util.Error.MISSING_THREE_ATTRIBUTES;
import static net.kosto.util.Error.SEMI_DEFINED_ATTRIBUTES;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.INDEX;
import static net.kosto.util.StringUtils.POSTGRESQL_OBJECTS;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMES;
import static net.kosto.util.StringUtils.POSTGRESQL_SCRIPTS;
import static net.kosto.util.StringUtils.SCHEMA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.util.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;

class PostgreSQLDatabaseTest {

  private DatabaseItem database;
  private List<DatabaseItem> schemes;
  @Mock
  private DatabaseItem object = Mockito.mock(PostgreSQLObject.class);

  @BeforeEach
  private void setUp() {
    database = new PostgreSQLDatabase();
    database.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    database.setOutputDirectoryFull(FileUtils.ROOT_PATH);

    schemes = new ArrayList<>();
    ((PostgreSQLDatabase) database).setSchemes(schemes);

    for (int index = 0; index < 2; index++) {
      DatabaseItem schema = new PostgreSQLSchema();
      ((PostgreSQLSchema) schema).setObjects(Collections.singletonList(object));
      ((PostgreSQLDatabase) database).getSchemes().add(schema);
    }
  }

  @Test
  @DisplayName("Object/Script/Schema - Missing attributes.")
  void test01() {
    ((PostgreSQLDatabase) database).setSchemes(null);

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_THREE_ATTRIBUTES.message(POSTGRESQL_OBJECTS, POSTGRESQL_SCRIPTS, POSTGRESQL_SCHEMES), result.getMessage());
  }

  @Test
  @DisplayName("Schema - Empty list attribute.")
  void test02() {
    schemes.clear();

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(EMPTY_LIST_ATTRIBUTE.message(POSTGRESQL_SCHEMES, SCHEMA), result.getMessage());
  }

  @Test
  @DisplayName("Schema - Semi defined attributes.")
  void test03() {
    schemes.get(0).setIndex(1);

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(SEMI_DEFINED_ATTRIBUTES.message(POSTGRESQL_SCHEMES, SCHEMA, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Schema - Duplicated attribute.")
  void test04() {
    schemes.forEach(scheme -> scheme.setIndex(1));

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(DUPLICATED_ATTRIBUTE.message(POSTGRESQL_SCHEMES, SCHEMA, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Default values.")
  void test05() throws MojoExecutionException {
    database.validate();

    assertNull(database.getIndex());
    assertNull(database.getOrder());
    assertEquals(DATABASE, database.getName());
    assertNull(database.getType());
    assertNull(database.getCondition());
    assertEquals(FILE_MASK_SQL, database.getFileMask());
    assertEquals(database.getName(), database.getSourceDirectory());
    assertFalse(database.getIgnoreDirectory());
    assertEquals(COLON, database.getDefineSymbol());
    assertFalse(database.getIgnoreDefine());
    assertEquals(UNIX_SEPARATOR + DATABASE + UNIX_SEPARATOR, database.getExecuteDirectory());
    assertEquals(Paths.get(UNIX_SEPARATOR, database.getName()), database.getSourceDirectoryFull());
    assertEquals(Paths.get(UNIX_SEPARATOR, database.getName()), database.getOutputDirectoryFull());

    int index = 0;
    for (DatabaseItem schema : ((PostgreSQLDatabase) database).getSchemes()) {
      assertEquals(index++, (int) schema.getOrder());
    }
  }
}
