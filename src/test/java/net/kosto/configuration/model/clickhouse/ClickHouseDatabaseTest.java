/*
 * Copyright 2019 Kostyantyn Krakovych
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

package net.kosto.configuration.model.clickhouse;

import static net.kosto.util.Error.DUPLICATED_ATTRIBUTE;
import static net.kosto.util.Error.EMPTY_LIST_ATTRIBUTE;
import static net.kosto.util.Error.MISSING_THREE_ATTRIBUTES;
import static net.kosto.util.Error.SEMI_DEFINED_ATTRIBUTES;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.CLICKHOUSE_OBJECTS;
import static net.kosto.util.StringUtils.CLICKHOUSE_SCHEMES;
import static net.kosto.util.StringUtils.CLICKHOUSE_SCRIPTS;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.INDEX;
import static net.kosto.util.StringUtils.SCHEMA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.util.FileUtils;

class ClickHouseDatabaseTest {

  private DatabaseItem database;
  private List<DatabaseItem> schemes;
  @Mock
  private DatabaseItem object = Mockito.mock(ClickHouseObject.class);

  @BeforeEach
  private void setUp() {
    database = new ClickHouseDatabase();
    database.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    database.setOutputDirectoryFull(FileUtils.ROOT_PATH);

    schemes = new ArrayList<>();
    ((ClickHouseDatabase) database).setSchemes(schemes);

    for (int index = 0; index < 2; index++) {
      DatabaseItem schema = new ClickHouseSchema();
      ((ClickHouseSchema) schema).setObjects(Collections.singletonList(object));
      ((ClickHouseDatabase) database).getSchemes().add(schema);
    }
  }

  @Test
  @DisplayName("Object/Script/Schema - Missing attributes.")
  void test01() {
    ((ClickHouseDatabase) database).setSchemes(null);

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(MISSING_THREE_ATTRIBUTES.message(CLICKHOUSE_OBJECTS, CLICKHOUSE_SCRIPTS, CLICKHOUSE_SCHEMES), result.getMessage());
  }

  @Test
  @DisplayName("Schema - Empty list attribute.")
  void test02() {
    schemes.clear();

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(EMPTY_LIST_ATTRIBUTE.message(CLICKHOUSE_SCHEMES, SCHEMA), result.getMessage());
  }

  @Test
  @DisplayName("Schema - Semi defined attributes.")
  void test03() {
    schemes.get(0).setIndex(1);

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(SEMI_DEFINED_ATTRIBUTES.message(CLICKHOUSE_SCHEMES, SCHEMA, INDEX), result.getMessage());
  }

  @Test
  @DisplayName("Schema - Duplicated attribute.")
  void test04() {
    schemes.forEach(scheme -> scheme.setIndex(1));

    Executable executable = database::validate;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(DUPLICATED_ATTRIBUTE.message(CLICKHOUSE_SCHEMES, SCHEMA, INDEX), result.getMessage());
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
    assertEquals(File.separator + database.getName(), database.getSourceDirectoryFull().toString());
    assertEquals(File.separator + database.getName(), database.getOutputDirectoryFull().toString());

    int index = 0;
    for (DatabaseItem schema : ((ClickHouseDatabase) database).getSchemes()) {
      assertEquals(index++, (int) schema.getOrder());
    }
  }
}
