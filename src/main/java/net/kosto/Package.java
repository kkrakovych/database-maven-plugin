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

import net.kosto.configuration.Configuration;
import net.kosto.configuration.oracle.OracleDatabase;
import net.kosto.service.Processor;
import net.kosto.service.ProcessorFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.time.LocalDateTime;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Mojo(name = "package", defaultPhase = PROCESS_SOURCES)
public class Package extends AbstractMojo {

    @Parameter(property = "project.version", required = true)
    private String buildVersion;

    private LocalDateTime buildTimestamp = LocalDateTime.now();

    @Parameter(property = "basedir", required = true)
    private String sourceDirectory;

    @Parameter(property = "project.build.directory", required = true)
    private String outputDirectory;

    @Parameter(property = "service.directory")
    private String serviceDirectory;

    @Parameter
    private OracleDatabase oracle;

    public void execute() throws MojoExecutionException {
        Configuration configuration = new Configuration.Builder()
            .buildVersion(buildVersion)
            .buildTimestamp(buildTimestamp)
            .sourceDirectory(sourceDirectory)
            .outputDirectory(outputDirectory)
            .serviceDirectory(serviceDirectory)
            .oracle(oracle)
            .build();
        configuration.validate();

        Processor processor = ProcessorFactory.getProcessor(configuration);
        processor.process();
    }
}
