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

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;
import static freemarker.template.Configuration.VERSION_2_3_28;
import static freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.US;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.kosto.Package;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Controls template processing.
 */
public final class TemplateService {

  private static final String FAILED_TEMPLATE_FILE_NAME = "Failed to get template file name.";
  private static final String FAILED_PROCESS_FILE_NAME = "Failed to process template file name.";
  private static final String FAILED_TEMPLATE_FILE = "Failed to get template file.";
  private static final String FAILED_PROCESS_FILE = "Failed to process template file.";

  /**
   * Template service instance.
   */
  private static TemplateService instance;
  /**
   * Template service configuration.
   */
  private final Configuration configuration;
  /**
   * Template service configuration parameters.
   */
  private final Map<String, Object> parameters;

  /**
   * Constructs instance and sets default values.
   */
  private TemplateService() {
    configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    configuration.setClassForTemplateLoading(Package.class, UNIX_SEPARATOR);
    configuration.setIncompatibleImprovements(VERSION_2_3_28);
    configuration.setDefaultEncoding(UTF_8.name());
    configuration.setLocale(US);
    configuration.setTemplateExceptionHandler(RETHROW_HANDLER);

    parameters = new HashMap<>();
  }

  /**
   * Returns template service instance.
   *
   * @return Template service instance.
   */
  public static synchronized TemplateService getInstance() {
    if (instance == null) {
      instance = new TemplateService();
    }
    return instance;
  }

  /**
   * Puts key-value pair to template service parameters' list.
   *
   * @param key   Parameter's key.
   * @param value Parameter's value.
   */
  public void putParameter(final String key, final Object value) {
    parameters.put(key, value);
  }

  /**
   * Processes string with existent template configuration and parameters.
   *
   * @param source String for processing.
   * @return Processed string.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public String process(final String source) throws MojoExecutionException {
    String result = source;

    if (source.contains("${")) {
      final StringWriter writer = new StringWriter();
      try {
        final Template template = new Template(source, source, configuration);
        template.process(parameters, writer);
        result = writer.toString();
      } catch (IOException x) {
        throw new MojoExecutionException(FAILED_TEMPLATE_FILE_NAME, x);
      } catch (TemplateException x) {
        throw new MojoExecutionException(FAILED_PROCESS_FILE_NAME, x);
      }
    }

    return result;
  }

  /**
   * Processes file with existent template configuration and parameters.
   *
   * @param source Source path to file for processing.
   * @param target Target path to file for processed file output.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public void process(final Path source, final Path target) throws MojoExecutionException {
    try {
      // Line below is for defence against execution on OS Windows
      final String templateName = source.toString().replaceAll("\\\\", UNIX_SEPARATOR);
      final Template template = configuration.getTemplate(templateName);
      try (
          BufferedWriter writer = Files.newBufferedWriter(target, UTF_8)
      ) {
        template.process(parameters, writer);
        writer.flush();
      }
    } catch (IOException x) {
      throw new MojoExecutionException(FAILED_TEMPLATE_FILE, x);
    } catch (TemplateException x) {
      throw new MojoExecutionException(FAILED_PROCESS_FILE, x);
    }
  }
}
