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

package net.kosto;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

import java.time.LocalDateTime;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import net.kosto.configuration.Configuration;
import net.kosto.configuration.model.common.CommonDatabase;
import net.kosto.service.processor.Processor;
import net.kosto.service.processor.ProcessorFactory;

/**
 * Controls source code packaging into database migration script.
 */
@Mojo(name = "package", defaultPhase = PROCESS_SOURCES)
public class Package extends AbstractMojo {

  /**
   * Default value for {@link #serviceDirectory} attribute and service scripts' resource path.
   */
  public static final String SERVICE_DIRECTORY = "service";

  /**
   * Database migration script build timestamp.
   * Represents timestamp of source code packaging start.
   * <p>
   * Default value is {@code LocalDateTime.now()}.
   */
  private final LocalDateTime buildTimestamp;
  /**
   * Database migration script log file name.
   * If not set log file name will be generated automatically.
   * <p>
   * Default value is {@code Null}.
   */
  @Parameter
  private final String logFileName;
  /**
   * Relative path name for service directory.
   * Represents directory for service scripts required by database deploy script.
   * <p>
   * Default value is {@value SERVICE_DIRECTORY}.
   */
  @Parameter
  private final String serviceDirectory;
  /**
   * Database migration script build version.
   * Represents project version defined in pom.xml file.
   */
  @Parameter(property = "project.version", required = true)
  private String buildVersion;
  /**
   * Full path name for root source directory.
   * Represents directory containing pom.xml file.
   */
  @Parameter(property = "basedir", required = true)
  private String sourceDirectory;
  /**
   * Full path name for root output directory.
   * Represents top level output directory.
   * <p>
   * Default value is {@code "target"}.
   */
  @Parameter(property = "project.build.directory", required = true)
  private String outputDirectory;
  /**
   * Oracle database configuration.
   */
  @Parameter
  private CommonDatabase oracle;
  /**
   * PostgreSQL database configuration.
   */
  @Parameter
  private CommonDatabase postgresql;

  /**
   * Constructs instance and sets default values.
   */
  public Package() {
    super();
    this.buildTimestamp = LocalDateTime.now();
    this.logFileName = null;
    this.serviceDirectory = SERVICE_DIRECTORY;
  }

  @Override
  public String toString() {
    return "Package{" +
        "buildTimestamp=" + buildTimestamp +
        ", logFileName='" + logFileName + '\'' +
        ", serviceDirectory='" + serviceDirectory + '\'' +
        ", buildVersion='" + buildVersion + '\'' +
        ", sourceDirectory='" + sourceDirectory + '\'' +
        ", outputDirectory='" + outputDirectory + '\'' +
        ", oracle=" + oracle +
        ", postgresql=" + postgresql +
        '}';
  }

  /**
   * Executes source code packaging into database migration script.
   * Takes into account configuration specified in pom.xml file.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  @Override
  public void execute() throws MojoExecutionException {
    final Configuration configuration = new Configuration.Builder()
        .setBuildTimestamp(buildTimestamp)
        .setLogFileName(logFileName)
        .setServiceDirectory(serviceDirectory)
        .setBuildVersion(buildVersion)
        .setSourceDirectory(sourceDirectory)
        .setOutputDirectory(outputDirectory)
        .setOracle(oracle)
        .setPostgresql(postgresql)
        .build();
    configuration.validate();

    final Processor processor = ProcessorFactory.getProcessor(configuration);
    processor.process();
  }
}
