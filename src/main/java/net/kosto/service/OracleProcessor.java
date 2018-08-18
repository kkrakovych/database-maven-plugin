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
import net.kosto.configuration.model.oracle.OracleObject;
import net.kosto.configuration.model.oracle.OracleSchema;
import net.kosto.configuration.model.oracle.OracleScript;
import net.kosto.util.FileUtils;
import net.kosto.util.ResourceUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.nio.file.Path;

import static net.kosto.Package.SERVICE_DIRECTORY;
import static net.kosto.util.DateUtils.FORMATTER_DATE_TIME_STRING;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;

public class OracleProcessor extends AbstractProcessor implements Processor {

    private static final String ORACLE = "oracle";

    OracleProcessor(Configuration configuration) {
        super(configuration);
        getTemplateParameters().put(DATABASE, configuration.getOracle());
    }

    @Override
    public void process() throws MojoExecutionException {
        processInstallScripts();
        processServiceScripts();
        processDatabase();

        StringBuilder zipFileName = new StringBuilder()
            .append(getConfiguration().getOracle().getName())
            .append("-")
            .append(getConfiguration().getBuildVersion())
            .append("-")
            .append(getConfiguration().getBuildTimestamp().format(FORMATTER_DATE_TIME_STRING))
            .append(".zip");
        processZipFile(zipFileName.toString());
    }

    private void processInstallScripts() throws MojoExecutionException {
        Path directory = FileUtils.createDirectories(getConfiguration().getOutputDirectory());
        processTemplateFiles(directory, ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE));
    }

    private void processServiceScripts() throws MojoExecutionException {
        processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, SERVICE_DIRECTORY, COMMON));
    }

    private void processDatabase() throws MojoExecutionException {
        processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, SERVICE_DIRECTORY, DATABASE));

        processSchemes();
    }

    private void processSchemes() throws MojoExecutionException {
        for (OracleSchema schema : getConfiguration().getOracle().getSchemes()) {
            getTemplateParameters().put(SCHEMA, schema);
            processTemplateFiles(ResourceUtils.getFiles(FILE_MASK_SQL, ORACLE, SERVICE_DIRECTORY, SCHEMA));

            processObjects(schema);
            processScripts(schema);
        }
    }

    private void processObjects(OracleSchema schema) throws MojoExecutionException {
        if (schema.getObjects() != null)
            for (OracleObject object : schema.getObjects())
                processItem(object, ORACLE, OBJECT);
    }

    private void processScripts(OracleSchema schema) throws MojoExecutionException {
        if (schema.getScripts() != null)
            for (OracleScript script : schema.getScripts())
                processItem(script, ORACLE, SCRIPT);
    }
}
