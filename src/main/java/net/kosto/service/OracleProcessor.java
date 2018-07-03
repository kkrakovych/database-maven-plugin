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

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.kosto.configuration.Configuration;
import net.kosto.configuration.model.DatabaseObject;
import net.kosto.configuration.model.DatabaseScript;
import net.kosto.configuration.oracle.OracleDatabase;
import net.kosto.configuration.oracle.OracleObject;
import net.kosto.configuration.oracle.OracleSchema;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.kosto.configuration.Configuration.DEFAULT_SERVICE_DIRECTORY;
import static net.kosto.util.DateUtils.FORMATTER_DATE_TIME;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;

public class OracleProcessor implements Processor {

    private static final String ORACLE = "oracle";
    private static final String COMMON = "common";
    private static final String DATABASE = "database";
    private static final String SCHEMA = "schema";
    private static final String OBJECT = "object";
    private static final String SCRIPT = "script";
    private static final String FILES = "files";

    /** Full configuration object. */
    private final Configuration configuration;
    /** Template configuration. */
    private final freemarker.template.Configuration templateConfiguration;
    /** Template configuration parameters. */
    private final Map<String, Object> templateParameters;

    OracleProcessor(Configuration configuration) {
        this.configuration = configuration;
        // Create template processor configuration
        TemplateProcessor templateProcessor = TemplateProcessor.getInstance();
        templateConfiguration = templateProcessor.getConfiguration();
        // Set up template processor configuration
        templateParameters = new HashMap<>();
        templateParameters.put("buildVersion", configuration.getBuildVersion());
        templateParameters.put("buildTimestamp", configuration.getBuildTimestamp().format(FORMATTER_DATE_TIME));
        templateParameters.put("serviceDirectory", configuration.getServiceDirectory());
        templateParameters.put(DATABASE, configuration.getOracle());
    }

    @Override
    public void process() throws MojoExecutionException {
        processMainScripts();
        processServiceScripts();
        processDatabase();
    }

    private void processMainScripts() throws MojoExecutionException {
        Path directory = FileUtils.createDirectories(configuration.getOutputDirectory());
        processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE));
    }

    private void processServiceScripts() throws MojoExecutionException {
        Path directory = FileUtils.createDirectories(configuration.getOutputDirectory(), configuration.getServiceDirectory());
        processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, DEFAULT_SERVICE_DIRECTORY, COMMON));
    }

    private void processDatabase() throws MojoExecutionException {
        OracleDatabase database = configuration.getOracle();
        Path directory = FileUtils.createDirectories(database.getOutputDirectoryFull());
        processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, DEFAULT_SERVICE_DIRECTORY, DATABASE));

        processSchemes();
    }

    private void processSchemes() throws MojoExecutionException {
        for (OracleSchema schema : configuration.getOracle().getSchemes()) {
            Path directory = FileUtils.createDirectories(schema.getOutputDirectoryFull());
            templateParameters.put(SCHEMA, schema);
            processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, DEFAULT_SERVICE_DIRECTORY, SCHEMA));

            processObjects(schema);
            processScripts(schema);
        }
    }

    private void processObjects(OracleSchema schema) throws MojoExecutionException {
        for (OracleObject object : schema.getObjects())
            processItem(object, OBJECT);
    }

    private void processScripts(OracleSchema schema) throws MojoExecutionException {
        if (schema.getScripts() != null)
            for (DatabaseScript script : schema.getScripts())
                processItem(script, SCRIPT);
    }

    private <T extends DatabaseObject> void processItem(T item, String itemType) throws MojoExecutionException {
        Path source = Paths.get(item.getSourceDirectoryFull());
        Path directory = FileUtils.createDirectories(item.getOutputDirectoryFull());
        templateParameters.put(itemType, item);
        if (itemType.equals(SCRIPT))
            templateParameters.put(FILES, FileUtils.getFileNamesWithCheckSum(source, item.getFileMask()));
        else
            templateParameters.put(FILES, FileUtils.getFileNames(source, item.getFileMask()));
        processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, DEFAULT_SERVICE_DIRECTORY, itemType));
        processSourceFiles(directory, FileUtils.getFiles(source, item.getFileMask()));
    }

    private void processTemplateFiles(Path directory, List<Path> files) throws MojoExecutionException {
        for (Path file : files) {
            Path fileName = file.getFileName();
            if (fileName == null)
                continue;
            // Process file name via template
            String name = fileName.toString();
            if (name.contains("${")) {
                StringWriter writer = new StringWriter();
                try {
                    Template template = new Template(name, name, templateConfiguration);
                    template.process(templateParameters, writer);
                    name = writer.toString();
                } catch (IOException x) {
                    throw new MojoExecutionException("Failed to get template file name.", x);
                } catch (TemplateException x) {
                    throw new MojoExecutionException("Failed to process template file name.", x);
                }
            }
            // Process file via template
            Path output = Paths.get(directory.toString(), name);
            try {
                // Line below is for defence against execution on OS Windows
                String templateName = file.toString().replaceAll("\\\\", UNIX_SEPARATOR);
                Template template = templateConfiguration.getTemplate(templateName);
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
    }

    private void processSourceFiles(Path directory, List<Path> files) throws MojoExecutionException {
        for (Path file : files) {
            Path target = Paths.get(directory.toString(), file.getFileName().toString());
            // TODO: add new options - select how we process source code
            //       1. BOM symbol remover
            //       2. End of SQL command checks
            //       3. & symbol processing
            if (true) {
                List<String> source = FileUtils.readFileSourceCode(file);
                FileUtils.writeFileSourceCode(target, source);
            } else {
                try {
                    Files.copy(file, target, REPLACE_EXISTING, COPY_ATTRIBUTES);
                } catch (IOException x) {
                    throw new MojoExecutionException("Failed to copy source file.", x);
                }
            }
        }
    }
}
