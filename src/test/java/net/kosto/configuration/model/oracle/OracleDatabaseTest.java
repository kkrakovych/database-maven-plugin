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

import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.DATABASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.kosto.configuration.model.CustomDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@FixMethodOrder(value = NAME_ASCENDING)
public class OracleDatabaseTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private OracleDatabase init01ValidateDatabaseEmpty() {
    OracleDatabase database = new OracleDatabase();
    database.setSourceDirectoryFull(Paths.get(UNIX_SEPARATOR));
    database.setOutputDirectoryFull(Paths.get(UNIX_SEPARATOR));

    return database;
  }

  private OracleDatabase init02ValidateSchemesEmpty() {
    OracleDatabase database = init01ValidateDatabaseEmpty();

    List<CustomDatabaseItem> schemes = new ArrayList<>();
    database.setSchemes(schemes);

    return database;
  }

  private OracleDatabase init03ValidateSchemesIndexSemiDefined() {
    OracleDatabase database = init01ValidateDatabaseEmpty();

    List<CustomDatabaseItem> schemes = new ArrayList<>();
    for (int index = 0; index < 2; index++) {
      OracleSchema schema = new OracleSchema();
      if (index == 1) {
        schema.setIndex(1);
      }
      schemes.add(schema);
    }

    database.setSchemes(schemes);

    return database;
  }

  private OracleDatabase init04ValidateSchemesIndexDuplicated() {
    OracleDatabase database = init01ValidateDatabaseEmpty();

    List<CustomDatabaseItem> schemes = new ArrayList<>();
    for (int index = 0; index < 2; index++) {
      OracleSchema schema = new OracleSchema();
      schema.setIndex(1);
      schemes.add(schema);
    }

    database.setSchemes(schemes);

    return database;
  }

  private OracleDatabase init05DefaultValuesDatabase() {
    OracleDatabase database = init01ValidateDatabaseEmpty();

    List<CustomDatabaseItem> schemes = new ArrayList<>();
    for (int index = 0; index < 2; index++) {
      OracleSchema schema = new OracleSchema();
      schemes.add(schema);
    }

    database.setSchemes(schemes);

    return database;
  }

  @Test
  public void test01ValidateDatabaseEmpty() throws MojoExecutionException {
    thrown.expect(MojoExecutionException.class);
    thrown.expectMessage("Attribute \"oracle.schemes\" should be specified.");

    init01ValidateDatabaseEmpty().validate();
  }

  @Test
  public void test02ValidateSchemesEmpty() throws MojoExecutionException {
    thrown.expect(MojoExecutionException.class);
    thrown.expectMessage("Attribute \"oracle.schemes\" should contain at least one \"schema\"");

    init02ValidateSchemesEmpty().validate();
  }

  @Test
  public void test03ValidateSchemesIndexSemiDefined() throws MojoExecutionException {
    thrown.expect(MojoExecutionException.class);
    thrown.expectMessage("Attribute \"index\" should be either specified for every \"schema\" in \"oracle.schemes\" or missing.");

    init03ValidateSchemesIndexSemiDefined().validate();
  }

  @Test
  public void test04ValidateSchemesIndexDuplicated() throws MojoExecutionException {
    thrown.expect(MojoExecutionException.class);
    thrown.expectMessage("Attribute \"index\" should be unique for every \"schema\" in \"oracle.schemes\".");

    init04ValidateSchemesIndexDuplicated().validate();
  }

  @Ignore
  @Test
  public void test05DefaultValuesDatabase() throws MojoExecutionException {
    OracleDatabase database = init05DefaultValuesDatabase();
    database.validate();

    assertEquals(DATABASE, database.getName());
    assertFalse(database.getIgnoreDirectory());
    assertEquals(AMPERSAND, database.getDefineSymbol());
    assertFalse(database.getIgnoreDefine());
    assertEquals(UNIX_SEPARATOR + DATABASE + UNIX_SEPARATOR, database.getExecuteDirectory());

    Integer index = 0;
    for (CustomDatabaseItem schema : database.getSchemes()) {
      assertEquals(index++, schema.getOrder());
    }
  }
}
