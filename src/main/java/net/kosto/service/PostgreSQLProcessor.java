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

import net.kosto.configuration.Configuration;
import net.kosto.configuration.model.postgresql.PostgreSQLObject;
import net.kosto.configuration.model.postgresql.PostgreSQLSchema;
import net.kosto.configuration.model.postgresql.PostgreSQLScript;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.nio.file.Path;

import static net.kosto.configuration.Configuration.DEFAULT_SERVICE_DIRECTORY;
import static net.kosto.util.DateUtils.FORMATTER_DATE_TIME_STRING;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;

public class PostgreSQLProcessor extends AbstractProcessor implements Processor {

    private static final String POSTGRESQL = "postgresql";

    PostgreSQLProcessor(Configuration configuration) {
        super(configuration);
        getTemplateParameters().put(DATABASE, configuration.getPostgreSQL());
    }

    @Override
    public void process() throws MojoExecutionException {
        processInstallScripts();
        processServiceScripts();
        processDatabase();

        StringBuilder zipFileName = new StringBuilder()
            .append(getConfiguration().getPostgreSQL().getName())
            .append("-")
            .append(getConfiguration().getBuildVersion())
            .append("-")
            .append(getConfiguration().getBuildTimestamp().format(FORMATTER_DATE_TIME_STRING))
            .append(".zip");
        processZipFile(zipFileName.toString());
    }

    private void processInstallScripts() throws MojoExecutionException {
        Path directory = FileUtils.createDirectories(getConfiguration().getOutputDirectory());
        processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL));
    }

    private void processServiceScripts() throws MojoExecutionException {
        processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL, DEFAULT_SERVICE_DIRECTORY, COMMON));
    }

    private void processDatabase() throws MojoExecutionException {
        processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL, DEFAULT_SERVICE_DIRECTORY, DATABASE));

        processSchemes();
    }

    private void processSchemes() throws MojoExecutionException {
        for (PostgreSQLSchema schema : getConfiguration().getPostgreSQL().getSchemes()) {
            getTemplateParameters().put(SCHEMA, schema);
            processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, POSTGRESQL, DEFAULT_SERVICE_DIRECTORY, SCHEMA));

            processObjects(schema);
            processScripts(schema);
        }
    }

    private void processObjects(PostgreSQLSchema schema) throws MojoExecutionException {
        if (schema.getObjects() != null)
            for (PostgreSQLObject object : schema.getObjects())
                processItem(object, POSTGRESQL, OBJECT);
    }

    private void processScripts(PostgreSQLSchema schema) throws MojoExecutionException {
        if (schema.getScripts() != null)
            for (PostgreSQLScript script : schema.getScripts())
                processItem(script, POSTGRESQL, SCRIPT);
    }
}
