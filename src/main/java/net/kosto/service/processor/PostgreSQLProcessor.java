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

package net.kosto.service.processor;

import static net.kosto.Package.SERVICE_DIRECTORY;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.StringUtils.COMMON;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.OBJECT;
import static net.kosto.util.StringUtils.POSTGRESQL;
import static net.kosto.util.StringUtils.SCHEMA;
import static net.kosto.util.StringUtils.SCRIPT;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.Configuration;
import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.postgresql.PostgreSQLDatabase;
import net.kosto.configuration.model.postgresql.PostgreSQLSchema;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;

/**
 * Controls packaging into database deploy script for PostgreSQL database configuration.
 */
public class PostgreSQLProcessor extends AbstractProcessor {

  /**
   * Constructs instance and sets default values.
   */
  /* package */ PostgreSQLProcessor(final Configuration configuration) {
    super(configuration);
  }

  @Override
  protected void processInstallScripts() throws MojoExecutionException {
    final Path directory = FileUtils.createDirectories(getConfiguration().getOutputDirectory());
    processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL));
  }

  @Override
  protected void processServiceScripts() throws MojoExecutionException {
    processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL, SERVICE_DIRECTORY, COMMON));
  }

  @Override
  protected void processDatabase() throws MojoExecutionException {
    processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL, SERVICE_DIRECTORY, DATABASE));

    processSchemes();
  }

  /**
   * Processes all database schemes.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  private void processSchemes() throws MojoExecutionException {
    List<DatabaseItem> schemes = ((PostgreSQLDatabase) getConfiguration().getDatabase()).getSchemes();
    if (schemes != null && !schemes.isEmpty()) {
      for (final DatabaseItem schema : schemes) {
        getTemplateService().putParameter(SCHEMA, schema);
        processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL, SERVICE_DIRECTORY, SCHEMA));
        processObjects((PostgreSQLSchema) schema);
        processScripts((PostgreSQLSchema) schema);
      }
    }
  }

  /**
   * Processes database object files in database schema.
   *
   * @param schema Database schema.
   * @throws MojoExecutionException If expected exception occurs.
   */
  private void processObjects(final PostgreSQLSchema schema) throws MojoExecutionException {
    if (schema.getObjects() != null) {
      for (final DatabaseItem object : schema.getObjects()) {
        processItem(object, POSTGRESQL, OBJECT);
      }
    }
  }

  /**
   * Processes script files in schema.
   *
   * @param schema Database schema.
   * @throws MojoExecutionException If expected exception occurs.
   */
  private void processScripts(final PostgreSQLSchema schema) throws MojoExecutionException {
    if (schema.getScripts() != null) {
      for (final DatabaseItem script : schema.getScripts()) {
        processItem(script, POSTGRESQL, SCRIPT);
      }
    }
  }
}
