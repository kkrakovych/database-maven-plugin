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

package net.kosto.configuration.oracle;

import net.kosto.configuration.ValidateAction;
import net.kosto.configuration.model.DatabaseObject;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;
import static net.kosto.configuration.model.DatabaseObjectType.DATABASE;

/**
 * {@code OracleDatabase} represents Oracle database configuration.
 * <p>
 * Provides access to database name, type, source directory, whether to ignore source directory,
 * list of schemes, full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleDatabase#sourceDirectory} = {@link OracleDatabase#name}</li>
 * <li>{@link OracleDatabase#type} = {@link net.kosto.configuration.model.DatabaseObjectType#DATABASE}</li>
 * <li>{@link OracleDatabase#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * </ul>
 */
public class OracleDatabase extends DatabaseObject implements ValidateAction {

    /** List of schemes. */
    private List<OracleSchema> schemes;

    public List<OracleSchema> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<OracleSchema> schemes) {
        this.schemes = schemes;
    }

    @Override
    public String toString() {
        return "oracle{" +
            "name=" + getName() +
            ", type=" + getType() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", schemes=" + getSchemes() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            '}';
    }

    @Override
    public void validate() throws MojoExecutionException {
        checkMandatoryValues();
        setDefaultValues();

        getSchemes().sort(Comparator.comparing(OracleSchema::getIndex));

        for (OracleSchema schema : schemes) {
            schema.setDirectoryFull(getSourceDirectoryFull(), getOutputDirectoryFull());
            schema.validate();
        }
    }

    /**
     * Checks {@code OracleDatabase} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    private void checkMandatoryValues() throws MojoExecutionException {
        if (getName() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.name"));
        if (getSchemes() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schemes"));
        if (getSchemes().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schemes", "schema"));
    }

    /**
     * Sets default values for {@code OracleDatabase} configuration.
     */
    private void setDefaultValues() {
        if (getSourceDirectory() == null || getSourceDirectory().isEmpty())
            setSourceDirectory(getName());
        setType(DATABASE);
        if (getIgnoreDirectory() == null)
            setIgnoreDirectory(FALSE);
    }
}
