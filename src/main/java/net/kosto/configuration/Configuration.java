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
import static net.kosto.service.ProcessorError.UNKNOWN_DATABASE_TYPE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.DatabaseType;
import net.kosto.configuration.model.oracle.OracleDatabase;
import net.kosto.configuration.model.postgresql.PostgreSQLDatabase;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents main configuration.
 */
public class Configuration implements Validator {

  /**
   * Current build version.
   * Represents project version defined in pom.xml file.
   */
  private String buildVersion;
  /**
   * Current build timestamp.
   * Represents timestamp of source code packaging start.
   */
  private LocalDateTime buildTimestamp;
  /**
   * Full path for root source directory.
   * Represents directory containing pom.xml file.
   */
  private Path sourceDirectory;
  /**
   * Full path for root output directory.
   * Represents top level output directory.
   */
  private Path outputDirectory;
  /**
   * Relative path name for service directory.
   * Represents directory for service scripts required by database deploy script.
   */
  private String serviceDirectory;
  /**
   * Database type.
   * Value depends on database configuration.
   */
  private DatabaseType databaseType;
  /**
   * Database configuration.
   */
  private DatabaseItem database;

  private Configuration() {
    super();
  }

  public String getBuildVersion() {
    return buildVersion;
  }

  private void setBuildVersion(final String buildVersion) {
    this.buildVersion = buildVersion;
  }

  public LocalDateTime getBuildTimestamp() {
    return buildTimestamp;
  }

  private void setBuildTimestamp(final LocalDateTime buildTimestamp) {
    this.buildTimestamp = buildTimestamp;
  }

  /**
   * Returns build timestamp formatted with specified date time formatter.
   *
   * @param dtf Date time formatter.
   * @return Formatted build timestamp.
   */
  public String getBuildTimestamp(final DateTimeFormatter dtf) {
    return buildTimestamp.format(dtf);
  }

  public Path getSourceDirectory() {
    return sourceDirectory;
  }

  private void setSourceDirectory(final Path sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public Path getOutputDirectory() {
    return outputDirectory;
  }

  private void setOutputDirectory(final Path outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public String getServiceDirectory() {
    return serviceDirectory;
  }

  private void setServiceDirectory(final String serviceDirectory) {
    this.serviceDirectory = serviceDirectory;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  private void setDatabaseType(final DatabaseType databaseType) {
    this.databaseType = databaseType;
  }

  public DatabaseItem getDatabase() {
    return database;
  }

  private void setDatabase(final DatabaseItem database) {
    this.database = database;
  }

  @Override
  public String toString() {
    return "Configuration{" +
        "buildVersion='" + buildVersion + '\'' +
        ", buildTimestamp=" + buildTimestamp +
        ", sourceDirectory=" + sourceDirectory +
        ", outputDirectory=" + outputDirectory +
        ", serviceDirectory='" + serviceDirectory + '\'' +
        ", databaseType=" + databaseType +
        ", database=" + database +
        '}';
  }

  @Override
  public void validate() throws MojoExecutionException {
    database.setSourceDirectoryFull(sourceDirectory);
    database.setOutputDirectoryFull(outputDirectory);
    database.validate();
  }

  /**
   * Supports creation of {@link Configuration} object.
   */
  @SuppressWarnings({"PMD.AccessorClassGeneration", "PMD.AccessorMethodGeneration"})
  public static class Builder {

    /**
     * Supports instance creation for main configuration.
     */
    private final Configuration configuration;

    /**
     * Constructs instance and sets default values.
     */
    public Builder() {
      super();
      configuration = new Configuration();
    }

    /**
     * Sets build version.
     *
     * @param buildVersion Build version.
     * @return {@link Builder} instance.
     */
    public Builder buildVersion(final String buildVersion) {
      configuration.setBuildVersion(buildVersion);
      return this;
    }

    /**
     * Sets build timestamp.
     *
     * @param buildTimestamp Build timestamp.
     * @return {@link Builder} instance.
     */
    public Builder buildTimestamp(final LocalDateTime buildTimestamp) {
      configuration.setBuildTimestamp(buildTimestamp);
      return this;
    }

    /**
     * Sets full path name for root source directory.
     *
     * @param sourceDirectory Full path name for root source directory.
     * @return {@link Builder} instance.
     */
    public Builder sourceDirectory(final String sourceDirectory) {
      configuration.setSourceDirectory(Paths.get(sourceDirectory));
      return this;
    }

    /**
     * Sets full path name for root output directory.
     *
     * @param outputDirectory Full path name for root output directory.
     * @return {@link Builder} instance.
     */
    public Builder outputDirectory(final String outputDirectory) {
      configuration.setOutputDirectory(Paths.get(outputDirectory));
      return this;
    }

    /**
     * Sets relative path name for service directory.
     *
     * @param serviceDirectory Relative path name to service directory.
     * @return {@link Builder} instance.
     */
    public Builder serviceDirectory(final String serviceDirectory) {
      configuration.setServiceDirectory(serviceDirectory);
      return this;
    }

    /**
     * Sets database as Oracle if it has set up in plugin configuration.
     *
     * @param oracle Oracle database configuration.
     * @return {@link Builder} instance.
     */
    public Builder oracle(final OracleDatabase oracle) {
      if (oracle != null && configuration.getDatabaseType() == null) {
        configuration.setDatabaseType(ORACLE);
        configuration.setDatabase(oracle);
      }
      return this;
    }

    /**
     * Sets database as PostgreSQL if it has set up in plugin configuration.
     *
     * @param postgresql PostgreSQL database configuration.
     * @return {@link Builder} instance.
     */
    public Builder postgresql(final PostgreSQLDatabase postgresql) {
      if (postgresql != null && configuration.getDatabaseType() == null) {
        configuration.setDatabaseType(POSTGRESQL);
        configuration.setDatabase(postgresql);
      }
      return this;
    }

    /**
     * Returns {@link Configuration} instance based on parameters.
     *
     * @return {@link Configuration} instance.
     * @throws MojoExecutionException If expected exception occurs.
     */
    public Configuration build() throws MojoExecutionException {
      if (configuration.getDatabaseType() == null) {
        throw new MojoExecutionException(UNKNOWN_DATABASE_TYPE.message());
      }

      return configuration;
    }
  }
}
