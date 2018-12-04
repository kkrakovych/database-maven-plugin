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

package net.kosto.configuration;

import static net.kosto.configuration.model.DatabaseType.ORACLE;
import static net.kosto.configuration.model.DatabaseType.POSTGRESQL;
import static net.kosto.util.Error.UNKNOWN_DATABASE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.kosto.configuration.model.DatabaseType;
import net.kosto.configuration.model.common.CommonDatabase;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;

class ConfigurationTest {

  @Mock
  private CommonDatabase database = Mockito.mock(CommonDatabase.class);

  @Test
  @DisplayName("Unknown database type.")
  void test01() {
    Executable executable = new Configuration.Builder()::build;
    Throwable result = assertThrows(MojoExecutionException.class, executable);
    assertEquals(UNKNOWN_DATABASE_TYPE.message(), result.getMessage());
  }

  @Test
  @DisplayName("Oracle - Database type.")
  void test02() throws MojoExecutionException {
    DatabaseType databaseType = new Configuration.Builder()
        .setOracle(database)
        .build()
        .getDatabaseType();
    assertEquals(ORACLE, databaseType);
  }

  @Test
  @DisplayName("PostgreSQL - Database type.")
  void test03() throws MojoExecutionException {
    DatabaseType databaseType = new Configuration.Builder()
        .setPostgresql(database)
        .build()
        .getDatabaseType();
    assertEquals(POSTGRESQL, databaseType);
  }
}
