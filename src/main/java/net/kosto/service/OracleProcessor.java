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
import net.kosto.util.DateUtils;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;

public class OracleProcessor implements Processor {

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
        templateParameters.put("buildTimestamp", configuration.getBuildTimestamp().format(DateUtils.FORMATTER_DATE_TIME));
        templateParameters.put("serviceDirectory", configuration.getServiceDirectory());
        templateParameters.put("database", configuration.getOracle());
    }

    @Override
    public void process() throws MojoExecutionException {
        processServiceScripts();
    }

    private void processServiceScripts() throws MojoExecutionException {
        Path directory = FileUtils.createDirectories(configuration.getOutputDirectory());
        List<Path> files = ResourceUtils.getFiles(FILE_MASK_SQL, "oracle");
        processFiles(directory, files);

        directory = FileUtils.createDirectories(configuration.getOutputDirectory(), configuration.getServiceDirectory());
        files = ResourceUtils.getFiles(FILE_MASK_SQL, "oracle", "service");
        processFiles(directory, files);
    }

    private void processFiles(Path directory, List<Path> files) throws MojoExecutionException {
        for (Path file : files) {
            Path fileName = file.getFileName();
            if (fileName == null)
                continue;
            Path output = Paths.get(directory.toString(), fileName.toString());

            try {
                Template template = templateConfiguration.getTemplate(file.toString());
                try (
                    BufferedWriter writer = Files.newBufferedWriter(output, UTF_8)
                ) {
                    template.process(templateParameters, writer);
                    writer.flush();
                }
            } catch (IOException x) {
                throw new MojoExecutionException("Failed to get template.", x);
            } catch (TemplateException x) {
                throw new MojoExecutionException("Failed to process template.", x);
            }
        }
    }
}
