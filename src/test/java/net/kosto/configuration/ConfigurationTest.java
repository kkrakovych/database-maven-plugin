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
import static org.junit.Assert.assertEquals;

import net.kosto.configuration.model.common.CommonDatabase;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigurationTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void test01BuildUnknownDatabaseType() throws MojoExecutionException {
    thrown.expect(MojoExecutionException.class);
    thrown.expectMessage("Unknown database type.");

    new Configuration.Builder()
        .build();
  }

  @Test
  public void test02BuildOracleEmpty() throws MojoExecutionException {
    CommonDatabase database = new CommonDatabase();

    Configuration configuration = new Configuration.Builder()
        .setOracle(database)
        .build();

    assertEquals(ORACLE, configuration.getDatabaseType());
  }

  @Test
  public void test03BuildPostgreSQLEmpty() throws MojoExecutionException {
    CommonDatabase database = new CommonDatabase();

    Configuration configuration = new Configuration.Builder()
        .setPostgresql(database)
        .build();

    assertEquals(POSTGRESQL, configuration.getDatabaseType());
  }
}
