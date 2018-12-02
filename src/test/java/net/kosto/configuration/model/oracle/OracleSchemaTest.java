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

import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import net.kosto.util.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@FixMethodOrder(value = NAME_ASCENDING)
public class OracleSchemaTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private OracleSchema init01ValidateSchemaEmpty() {
    OracleSchema schema = new OracleSchema();
    schema.setSourceDirectoryFull(FileUtils.ROOT_PATH);
    schema.setOutputDirectoryFull(FileUtils.ROOT_PATH);

    return schema;
  }

  @Test
  public void test01ValidateSchemaEmpty() throws MojoExecutionException {
    thrown.expect(MojoExecutionException.class);
    thrown.expectMessage("Either \"oracle.schema.objects\" or \"oracle.schema.scripts\" attribute should be specified.");

    init01ValidateSchemaEmpty().validate();
  }
}
