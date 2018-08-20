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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.DatabaseType;
import net.kosto.configuration.model.oracle.OracleDatabase;
import net.kosto.configuration.model.postgresql.PostgreSQLDatabase;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents main configuration.
 */
public class Configuration implements ValidateItem {

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

  public LocalDateTime getBuildTimestamp() {
    return buildTimestamp;
  }

  public Path getSourceDirectory() {
    return sourceDirectory;
  }

  public Path getOutputDirectory() {
    return outputDirectory;
  }

  public String getServiceDirectory() {
    return serviceDirectory;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  public DatabaseItem getDatabase() {
    return database;
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
    database.setSourceDirectoryFull(getSourceDirectory());
    database.setOutputDirectoryFull(getOutputDirectory());
    database.validate();
  }

  /**
   * Supports creation of {@link Configuration} object.
   */
  public static class Builder {

    /** Support instance for main configuration. */
    final private Configuration configuration;

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
    public Builder setBuildVersion(final String buildVersion) {
      configuration.buildVersion = buildVersion;
      return this;
    }

    /**
     * Sets build timestamp.
     *
     * @param buildTimestamp Build timestamp.
     * @return {@link Builder} instance.
     */
    public Builder setBuildTimestamp(final LocalDateTime buildTimestamp) {
      configuration.buildTimestamp = buildTimestamp;
      return this;
    }

    /**
     * Sets full path name for root source directory.
     *
     * @param sourceDirectory Full path name for root source directory.
     * @return {@link Builder} instance.
     */
    public Builder setSourceDirectory(final String sourceDirectory) {
      configuration.sourceDirectory = Paths.get(sourceDirectory);
      return this;
    }

    /**
     * Sets full path name for root output directory.
     *
     * @param outputDirectory Full path name for root output directory.
     * @return {@link Builder} instance.
     */
    public Builder setOutputDirectory(final String outputDirectory) {
      configuration.outputDirectory = Paths.get(outputDirectory);
      return this;
    }

    /**
     * Sets relative path name for service directory.
     *
     * @param serviceDirectory Relative path name to service directory.
     * @return {@link Builder} instance.
     */
    public Builder setServiceDirectory(final String serviceDirectory) {
      configuration.serviceDirectory = serviceDirectory;
      return this;
    }

    /**
     * Sets database as Oracle if it has set up in plugin configuration.
     *
     * @param oracle Oracle database configuration.
     * @return {@link Builder} instance.
     */
    public Builder setOracle(final OracleDatabase oracle) {
      if (oracle != null && configuration.databaseType == null) {
        configuration.databaseType = ORACLE;
        configuration.database = oracle;
      }
      return this;
    }

    /**
     * Sets database as PostgreSQL if it has set up in plugin configuration.
     *
     * @param postgresql PostgreSQL database configuration.
     * @return {@link Builder} instance.
     */
    public Builder setPostgresql(final PostgreSQLDatabase postgresql) {
      if (postgresql != null && configuration.databaseType == null) {
        configuration.databaseType = POSTGRESQL;
        configuration.database = postgresql;
      }
      return this;
    }

    /**
     * Returns {@link Configuration} instance based on parameters.
     *
     * @return {@link Configuration} instance.
     */
    public Configuration build() {
      return configuration;
    }
  }
}
