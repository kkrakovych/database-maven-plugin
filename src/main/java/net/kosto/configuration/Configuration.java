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

package net.kosto.configuration;

import net.kosto.configuration.model.DatabaseType;
import net.kosto.configuration.oracle.OracleDatabase;
import org.apache.maven.plugin.MojoExecutionException;

import java.time.LocalDateTime;

import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;
import static net.kosto.configuration.model.DatabaseType.ORACLE;

/**
 * {@code Configuration} represents main configuration.
 */
public class Configuration implements ValidateAction {

    public static final String DEFAULT_SERVICE_DIRECTORY = "service";

    /** Current build version. */
    private final String buildVersion;
    /** Current build timestamp. */
    private final LocalDateTime buildTimestamp;
    /** Root source directory. */
    private final String sourceDirectory;
    /** Root output directory. */
    private final String outputDirectory;
    /** Service directory. */
    private final String serviceDirectory;
    /** Database type. */
    private final DatabaseType databaseType;
    /** Oracle database configuration. */
    private OracleDatabase oracle;

    /**
     * Constructs {@code Configuration} with {@link Builder} parameters.
     *
     * @param builder
     */
    private Configuration(Builder builder) {
        this.buildVersion = builder.buildVersion;
        this.buildTimestamp = builder.buildTimestamp;
        this.sourceDirectory = builder.sourceDirectory;
        this.outputDirectory = builder.outputDirectory;
        this.serviceDirectory = builder.serviceDirectory;
        this.databaseType = builder.databaseType;
        this.oracle = builder.oracle;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public LocalDateTime getBuildTimestamp() {
        return buildTimestamp;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getServiceDirectory() {
        return serviceDirectory;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public OracleDatabase getOracle() {
        return oracle;
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "buildVersion=" + buildVersion +
            ", buildTimestamp=" + buildTimestamp +
            ", sourceDirectory=" + sourceDirectory +
            ", outputDirectory=" + outputDirectory +
            ", serviceDirectory=" + serviceDirectory +
            ", databaseType=" + databaseType +
            ", oracle=" + oracle +
            '}';
    }

    @Override
    public void validate() throws MojoExecutionException {
        switch (databaseType) {
            case ORACLE:
                oracle.setDirectoryFull(getSourceDirectory(), getOutputDirectory());
                oracle.validate();
                break;
            default:
                throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle"));
        }
    }

    /**
     * {@code Builder} supports creation of {@link Configuration} object.
     */
    public static class Builder {

        /** Current build version. */
        private String buildVersion;
        /** Current build timestamp. */
        private LocalDateTime buildTimestamp;
        /** Root source directory. */
        private String sourceDirectory;
        /** Root output directory. */
        private String outputDirectory;
        /** Service directory. */
        private String serviceDirectory;
        /** Database type. */
        private DatabaseType databaseType;
        /** Oracle database configuration. */
        private OracleDatabase oracle;

        public Builder buildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
            return this;
        }

        public Builder buildTimestamp(LocalDateTime buildTimestamp) {
            this.buildTimestamp = buildTimestamp;
            return this;
        }

        public Builder sourceDirectory(String sourceDirectory) {
            this.sourceDirectory = sourceDirectory;
            return this;
        }

        public Builder outputDirectory(String outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public Builder serviceDirectory(String serviceDirectory) {
            this.serviceDirectory = serviceDirectory;
            return this;
        }

        public Builder databaseType(DatabaseType databaseType) {
            this.databaseType = databaseType;
            return this;
        }

        public Builder oracle(OracleDatabase oracle) {
            this.oracle = oracle;
            return this;
        }

        /**
         * Creates new {@code Configuration} based on previously specified parameters.
         *
         * @return {@code Configuration} object.
         */
        public Configuration build() {
            setDefaultValues();

            return new Configuration(this);
        }

        /**
         * Sets default values for {@code Configuration}.
         */
        private void setDefaultValues() {
            if (serviceDirectory == null || serviceDirectory.isEmpty())
                serviceDirectory = DEFAULT_SERVICE_DIRECTORY;
            if (oracle != null)
                databaseType = ORACLE;
        }
    }
}
