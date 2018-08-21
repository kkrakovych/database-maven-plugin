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

package net.kosto.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.kosto.Package.SERVICE_DIRECTORY;
import static net.kosto.configuration.model.DatabaseType.ORACLE;
import static net.kosto.util.DateUtils.DTF_DATE_TIME;
import static net.kosto.util.DateUtils.DTF_DATE_TIME_SEAMLESS;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.kosto.configuration.Configuration;
import net.kosto.configuration.model.DatabaseObject;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;
import net.kosto.util.ZipUtils;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents basic database configuration processor.
 */
public abstract class AbstractProcessor implements Processor {

  protected static final String COMMON = "common";
  protected static final String DATABASE = "database";
  protected static final String SCHEMA = "schema";
  protected static final String OBJECT = "object";
  protected static final String SCRIPT = "script";
  private static final String FILES = "files";

  /**
   * Configuration object.
   */
  private final Configuration configuration;
  /**
   * Template configuration.
   */
  private final freemarker.template.Configuration templateConfiguration;
  /**
   * Template configuration parameters.
   */
  private final Map<String, Object> templateParameters;
  /**
   * Files for packing into result zip file.
   */
  private final List<String> zipFiles = new ArrayList<>();

  /**
   * Constructs instance and sets default values.
   */
  /* package */ AbstractProcessor(final Configuration configuration) {
    this.configuration = configuration;
    // Create template processor configuration
    final TemplateProcessor templateProcessor = TemplateProcessor.getInstance();
    templateConfiguration = templateProcessor.getConfiguration();
    // Set up template processor configuration
    templateParameters = new HashMap<>();
    templateParameters.put("buildVersion", configuration.getBuildVersion());
    templateParameters.put("buildTimestamp", configuration.getBuildTimestamp().format(DTF_DATE_TIME));
    templateParameters.put("serviceDirectory", configuration.getServiceDirectory());
    templateParameters.put(DATABASE, configuration.getDatabase());
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  protected Map<String, Object> getTemplateParameters() {
    return templateParameters;
  }

  protected <T extends DatabaseObject> void processItem(final T item, final String baseDirectory, final String itemType) throws MojoExecutionException {
    final Path source = item.getSourceDirectoryFull();
    final Path directory = FileUtils.createDirectories(item.getOutputDirectoryFull());
    templateParameters.put(itemType, item);
    if (itemType.equals(SCRIPT)) {
      templateParameters.put(FILES, FileUtils.getFileNamesWithCheckSum(source, item.getFileMask()));
    } else {
      templateParameters.put(FILES, FileUtils.getFileNames(source, item.getFileMask()));
    }
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
      final String fileName = processTemplateFileName(file.getFileName());
      final Path output = directory.resolve(fileName);
      addZipFile(output);
      processTemplateFile(file, output);
    }
  }

  private String processTemplateFileName(final Path fileName) throws MojoExecutionException {
    String name = fileName.toString();
    if (name.contains("${")) {
      final StringWriter writer = new StringWriter();
      try {
        final Template template = new Template(name, name, templateConfiguration);
        template.process(templateParameters, writer);
        name = writer.toString();
      } catch (IOException x) {
        throw new MojoExecutionException("Failed to get template file name.", x);
      } catch (TemplateException x) {
        throw new MojoExecutionException("Failed to process template file name.", x);
      }
    }
    return name;
  }

  private void processTemplateFile(final Path fileName, final Path output) throws MojoExecutionException {
    try {
      // Line below is for defence against execution on OS Windows
      final String templateName = fileName.toString().replaceAll("\\\\", UNIX_SEPARATOR);
      final Template template = templateConfiguration.getTemplate(templateName);
      try (
          BufferedWriter writer = Files.newBufferedWriter(output, UTF_8)
      ) {
        template.process(templateParameters, writer);
        writer.flush();
      }
    } catch (IOException x) {
      throw new MojoExecutionException("Failed to get template file.", x);
    } catch (TemplateException x) {
      throw new MojoExecutionException("Failed to process template file.", x);
    }
  }

  private void processSourceFiles(final Path directory, final List<Path> files) throws MojoExecutionException {
    for (final Path file : files) {
      final Path output = directory.resolve(file.getFileName());
      addZipFile(output);
      processSourceFile(file, output);
    }
  }

  private void processSourceFile(final Path file, final Path output) throws MojoExecutionException {
    if (configuration.getDatabaseType().equals(ORACLE)) {
      final List<String> source = FileUtils.readFileSourceCode(file);
      FileUtils.writeFileSourceCode(output, source);
    } else {
      try {
        Files.copy(file, output, REPLACE_EXISTING, COPY_ATTRIBUTES);
      } catch (IOException x) {
        throw new MojoExecutionException("Failed to copy source file.", x);
      }
    }
  }

  private void addZipFile(final Path output) {
    final Path basePath = configuration.getOutputDirectory();
    final Path relativePath = basePath.relativize(output);
    zipFiles.add(relativePath.toString());
  }

  private void processZipFile(final String zipFileName) throws MojoExecutionException {
    final Path baseDirectory = configuration.getOutputDirectory();
    final Path zipFile = baseDirectory.resolve(zipFileName);
    ZipUtils.compress(zipFile, baseDirectory, zipFiles);
  }

  @Override
  public void process() throws MojoExecutionException {
    processInstallScripts();
    processServiceScripts();
    processDatabase();

    final StringBuilder zipFileName = new StringBuilder()
        .append(getConfiguration().getDatabase().getName())
        .append("-")
        .append(getConfiguration().getBuildVersion())
        .append("-")
        .append(getConfiguration().getBuildTimestamp().format(DTF_DATE_TIME_SEAMLESS))
        .append(".zip");
    processZipFile(zipFileName.toString());
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
