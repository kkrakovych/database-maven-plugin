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

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.kosto.Package.SERVICE_DIRECTORY;
import static net.kosto.configuration.model.DatabaseType.ORACLE;
import static net.kosto.util.DateUtils.DTF_DATE_TIME;
import static net.kosto.util.DateUtils.DTF_DATE_TIME_SEAMLESS;
import static net.kosto.util.FileUtils.FILE_MASK_SH;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.FILES;
import static net.kosto.util.StringUtils.SCRIPT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.Configuration;
import net.kosto.configuration.model.DatabaseItem;
import net.kosto.service.TemplateService;
import net.kosto.service.ZipService;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;

/**
 * Represents basic database configuration processor.
 */
public abstract class AbstractProcessor implements Processor {

  public static final String FAILED_COPY_FILE = "Failed to copy source file.";
  public static final String FAILED_SET_EXECUTABLE = "Failed to set file as executable.";

  /**
   * Database configuration.
   */
  private final Configuration configuration;
  /**
   * Template service.
   */
  private final TemplateService templateService;
  /**
   * Zip service.
   */
  private final ZipService zipService;

  /**
   * Constructs instance and sets default values.
   *
   * @param configuration Main configuration.
   */
  AbstractProcessor(final Configuration configuration) {
    this.configuration = configuration;

    templateService = TemplateService.getInstance();
    templateService.putParameter("buildVersion", configuration.getBuildVersion());
    templateService.putParameter("buildTimestamp", configuration.getBuildTimestamp(DTF_DATE_TIME));
    templateService.putParameter("logFileName", configuration.getLogFileName());
    templateService.putParameter("serviceDirectory", configuration.getServiceDirectory());
    templateService.putParameter(DATABASE, configuration.getDatabase());

    final String zipFileName = configuration.getDatabase().getName() + "-" +
        configuration.getBuildVersion() + "-" +
        configuration.getBuildTimestamp(DTF_DATE_TIME_SEAMLESS) + ".zip";
    zipService = new ZipService(zipFileName, configuration.getOutputDirectory());
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  protected TemplateService getTemplateService() {
    return templateService;
  }

  protected <T extends DatabaseItem> void processItem(final T item, final String baseDirectory, final String itemType) throws MojoExecutionException {
    final Path source = item.getSourceDirectoryFull();
    final Path directory = FileUtils.createDirectories(item.getOutputDirectoryFull());
    templateService.putParameter(itemType, item);
    if (SCRIPT.equals(itemType)) {
      templateService.putParameter(FILES, FileUtils.getFileNamesWithCheckSum(source, item.getFileMask()));
    } else {
      templateService.putParameter(FILES, FileUtils.getFileNames(source, item.getFileMask()));
    }
    processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SH, baseDirectory, SERVICE_DIRECTORY, itemType));
    processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, baseDirectory, SERVICE_DIRECTORY, itemType));
    processSourceFiles(directory, FileUtils.getFiles(source, item.getFileMask()));
  }

  protected void processTemplateFiles(final List<Path> files) throws MojoExecutionException {
    final Path directory = FileUtils.createDirectories(configuration.getOutputDirectory(), configuration.getServiceDirectory());
    processTemplateFiles(directory, files);
  }

  protected void processTemplateFiles(final Path directory, final List<Path> files) throws MojoExecutionException {
    for (final Path file : files) {
      if (file.getFileName() == null) {
        continue;
      }
      final String fileName = templateService.process(file.getFileName().toString());
      final Path output = directory.resolve(fileName);
      templateService.process(file, output);

      // It's required for ClickHouse data processing
      // It does not look as a good approach
      // Make sense to review it in future
      if (fileName.endsWith(".sh")) {
        try {
          Set<PosixFilePermission> perms = Files.getPosixFilePermissions(output);
          perms.add(PosixFilePermission.OWNER_EXECUTE);
          perms.add(PosixFilePermission.GROUP_EXECUTE);
          perms.add(PosixFilePermission.OTHERS_EXECUTE);
          Files.setPosixFilePermissions(output, perms);
        } catch (IOException x) {
          throw new MojoExecutionException(FAILED_SET_EXECUTABLE, x);
        }
      }

      zipService.add(output);
    }
  }

  private void processSourceFiles(final Path directory, final List<Path> files) throws MojoExecutionException {
    for (final Path file : files) {
      final Path output = directory.resolve(file.getFileName());
      processSourceFile(file, output);
      zipService.add(output);
    }
  }

  /**
   * Copies source file with additional processing if required.
   *
   * @param source Full path to source file.
   * @param target Full path to target file.
   * @throws MojoExecutionException If expected exception occurs.
   */
  private void processSourceFile(final Path source, final Path target) throws MojoExecutionException {
    if (configuration.getDatabaseType() == ORACLE) {
      final List<String> lines = FileUtils.readFileSourceCode(source);
      FileUtils.writeFileSourceCode(target, lines);
    } else {
      try {
        Files.copy(source, target, REPLACE_EXISTING, COPY_ATTRIBUTES);
      } catch (IOException x) {
        throw new MojoExecutionException(FAILED_COPY_FILE, x);
      }
    }
  }

  @Override
  public void process() throws MojoExecutionException {
    processInstallScripts();
    processServiceScripts();
    processDatabase();
    zipService.compress();
  }

  /**
   * Processes all files related to install scripts.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected abstract void processInstallScripts() throws MojoExecutionException;

  /**
   * Processes all files related to service scripts.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected abstract void processServiceScripts() throws MojoExecutionException;

  /**
   * Processes all files related to database itself.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected abstract void processDatabase() throws MojoExecutionException;
}
