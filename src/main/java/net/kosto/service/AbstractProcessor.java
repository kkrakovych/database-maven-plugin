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
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;
import net.kosto.util.ZipUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

public class AbstractProcessor {

    protected static final String COMMON = "common";
    protected static final String DATABASE = "database";
    protected static final String SCHEMA = "schema";
    protected static final String OBJECT = "object";
    protected static final String SCRIPT = "script";
    private static final String FILES = "files";

    /** Full configuration object. */
    private final Configuration configuration;
    /** Template configuration. */
    private final freemarker.template.Configuration templateConfiguration;
    /** Template configuration parameters. */
    private final Map<String, Object> templateParameters;
    /** List of files for packing in result zip file. */
    private final List<String> zipFiles = new ArrayList<>();

    public Configuration getConfiguration() {
        return configuration;
    }

    protected Map<String, Object> getTemplateParameters() {
        return templateParameters;
    }

    AbstractProcessor(Configuration configuration) {
        this.configuration = configuration;
        // Create template processor configuration
        TemplateProcessor templateProcessor = TemplateProcessor.getInstance();
        templateConfiguration = templateProcessor.getConfiguration();
        // Set up template processor configuration
        templateParameters = new HashMap<>();
        templateParameters.put("buildVersion", configuration.getBuildVersion());
        templateParameters.put("buildTimestamp", configuration.getBuildTimestamp().format(FORMATTER_DATE_TIME));
        templateParameters.put("serviceDirectory", configuration.getServiceDirectory());
    }

    protected <T extends DatabaseObject> void processItem(T item, String baseDirectory, String itemType) throws MojoExecutionException {
        Path source = Paths.get(item.getSourceDirectoryFull());
        Path directory = FileUtils.createDirectories(item.getOutputDirectoryFull());
        templateParameters.put(itemType, item);
        if (itemType.equals(SCRIPT))
            templateParameters.put(FILES, FileUtils.getFileNamesWithCheckSum(source, item.getFileMask()));
        else
            templateParameters.put(FILES, FileUtils.getFileNames(source, item.getFileMask()));
        processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, baseDirectory, DEFAULT_SERVICE_DIRECTORY, itemType));
        processSourceFiles(directory, FileUtils.getFiles(source, item.getFileMask()));
    }

    protected void processTemplateFiles(List<Path> files) throws MojoExecutionException {
        Path directory = FileUtils.createDirectories(configuration.getOutputDirectory(), configuration.getServiceDirectory());
        processTemplateFiles(directory, files);
    }

    protected void processTemplateFiles(Path directory, List<Path> files) throws MojoExecutionException {
        for (Path file : files) {
            if (file.getFileName() == null)
                continue;
            String fileName = processTemplateFileName(file.getFileName());
            Path output = Paths.get(directory.toString(), fileName);
            addZipFile(output);
            processTemplateFile(file, output);
        }
    }

    private String processTemplateFileName(Path fileName) throws MojoExecutionException {
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
        return name;
    }

    private void processTemplateFile(Path fileName, Path output) throws MojoExecutionException {
        try {
            // Line below is for defence against execution on OS Windows
            String templateName = fileName.toString().replaceAll("\\\\", UNIX_SEPARATOR);
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

    private void processSourceFiles(Path directory, List<Path> files) throws MojoExecutionException {
        for (Path file : files) {
            Path output = Paths.get(directory.toString(), file.getFileName().toString());
            addZipFile(output);
            processSourceFile(file, output);
        }
    }

    private void processSourceFile(Path file, Path output) throws MojoExecutionException {
        if (true) {
            List<String> source = FileUtils.readFileSourceCode(file);
            FileUtils.writeFileSourceCode(output, source);
        } else {
            try {
                Files.copy(file, output, REPLACE_EXISTING, COPY_ATTRIBUTES);
            } catch (IOException x) {
                throw new MojoExecutionException("Failed to copy source file.", x);
            }
        }
    }

    private void addZipFile(Path output) {
        Path basePath = Paths.get(configuration.getOutputDirectory());
        Path relativePath = basePath.relativize(output);
        zipFiles.add(relativePath.toString());
    }

    protected void processZipFile(String zipFileName) throws MojoExecutionException {
        Path zipFile = Paths.get(configuration.getOutputDirectory(), zipFileName);
        ZipUtils.compress(zipFile.toString(), configuration.getOutputDirectory(), zipFiles);
    }
}
