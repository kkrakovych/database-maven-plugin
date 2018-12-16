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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.DatabaseType;
import net.kosto.configuration.model.common.CommonDatabase;
import net.kosto.configuration.model.oracle.OracleDatabase;
import net.kosto.configuration.model.postgresql.PostgreSQLDatabase;
import net.kosto.service.validator.Validator;

/**
 * Represents main configuration.
 */
public final class Configuration implements Validator {

  /**
   * Current build timestamp.
   * Represents timestamp of source code packaging start.
   */
  private LocalDateTime buildTimestamp;
  /**
   * Database migration script log file name.
   * If not set log file name will be generated automatically.
   */
  private String logFileName;
  /**
   * Relative path name for service directory.
   * Represents directory for service scripts required by database deploy script.
   */
  private String serviceDirectory;
  /**
   * Current build version.
   * Represents project version defined in pom.xml file.
   */
  private String buildVersion;
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
   * Database type.
   * Value depends on database configuration.
   */
  private DatabaseType databaseType;
  /**
   * Database configuration.
   */
  private DatabaseItem database;

  /**
   * Constructs instance and sets default values.
   */
  private Configuration() {
    super();
  }

  public LocalDateTime getBuildTimestamp() {
    return buildTimestamp;
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

  public String getLogFileName() {
    return logFileName;
  }

  public String getServiceDirectory() {
    return serviceDirectory;
  }

  public String getBuildVersion() {
    return buildVersion;
  }

  public Path getSourceDirectory() {
    return sourceDirectory;
  }

  public Path getOutputDirectory() {
    return outputDirectory;
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
        "buildTimestamp=" + buildTimestamp +
        ", logFileName='" + logFileName + '\'' +
        ", serviceDirectory='" + serviceDirectory + '\'' +
        ", buildVersion='" + buildVersion + '\'' +
        ", sourceDirectory=" + sourceDirectory +
        ", outputDirectory=" + outputDirectory +
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
     * Sets log file name.
     *
     * @param logFileName Log file name.
     * @return {@link Builder} instance.
     */
    public Builder setLogFileName(final String logFileName) {
      configuration.logFileName = logFileName;
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
     * Sets Oracle database configuration.
     *
     * @param oracle Oracle database configuration.
     * @return {@link Builder} instance.
     */
    public Builder setOracle(final CommonDatabase oracle) {
      if (oracle != null && configuration.databaseType == null) {
        configuration.databaseType = ORACLE;
        configuration.database = new OracleDatabase(oracle);
      }
      return this;
    }

    /**
     * Sets PostgreSQL database configuration.
     *
     * @param postgresql PostgreSQL database configuration.
     * @return {@link Builder} instance.
     */
    public Builder setPostgresql(final CommonDatabase postgresql) {
      if (postgresql != null && configuration.databaseType == null) {
        configuration.databaseType = POSTGRESQL;
        configuration.database = new PostgreSQLDatabase(postgresql);
      }
      return this;
    }

    /**
     * Returns {@link Configuration} instance based on previously specified parameters.
     *
     * @return {@link Configuration} instance.
     * @throws MojoExecutionException If expected exception occurs.
     */
    public Configuration build() throws MojoExecutionException {
      if (configuration.databaseType == null) {
        throw new MojoExecutionException(UNKNOWN_DATABASE_TYPE.message());
      }
      return configuration;
    }
  }
}
