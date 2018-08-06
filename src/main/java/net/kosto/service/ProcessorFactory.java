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
import org.apache.maven.plugin.MojoExecutionException;

import static net.kosto.service.ServiceError.UNKNOWN_DATABASE_TYPE;

public class ProcessorFactory {

    private ProcessorFactory() {
    }

    public static Processor getProcessor(Configuration configuration) throws MojoExecutionException {
        switch (configuration.getDatabaseType()) {
            case ORACLE:
                return new OracleProcessor(configuration);
            case POSTGRESQL:
                return new PostgreSQLProcessor(configuration);
            default:
                throw new MojoExecutionException(UNKNOWN_DATABASE_TYPE);
        }
    }
}
