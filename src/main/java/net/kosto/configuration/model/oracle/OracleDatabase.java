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

package net.kosto.configuration.model.oracle;

import net.kosto.configuration.model.AbstractDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;

/**
 * {@code OracleDatabase} represents Oracle database configuration.
 * <p>
 * Provides access to database name, type, source directory, whether to ignore source directory,
 * list of schemes, execute directory and full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleDatabase#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleDatabase#sourceDirectory} = {@link OracleDatabase#name}</li>
 * </ul>
 */
public class OracleDatabase extends AbstractDatabaseItem {

    /** List of schemes for deploy. */
    private List<OracleSchema> schemes;

    public List<OracleSchema> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<OracleSchema> schemes) {
        this.schemes = schemes;
    }

    @Override
    public String toString() {
        return "OracleDatabase{" +
            "name=" + getName() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", defineSymbol=" + getDefineSymbol() +
            ", ignoreDefine=" + getIgnoreDefine() +
            ", executeDirectory=" + getExecuteDirectory() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            ", schemes=" + getSchemes() +
            '}';
    }

    /**
     * Checks {@code OracleDatabase} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    protected void checkMandatoryValues() throws MojoExecutionException {
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
    protected void setDefaultValues() {
        if (getDefineSymbol() == null)
            setDefineSymbol("&");
        if (getIgnoreDefine() == null)
            setIgnoreDefine(FALSE);
        if (getSourceDirectory() == null || getSourceDirectory().isEmpty())
            setSourceDirectory(getIgnoreDirectory() ? "" : getName());
    }

    @Override
    protected void processAttributes() throws MojoExecutionException {
        if (schemes != null) {
            schemes
                .sort(
                    Comparator
                        .comparingInt(OracleSchema::getIndex)
                        .thenComparing(OracleSchema::getName)
                );

            for (OracleSchema schema : schemes)
                validateAttribute(schema);
        }
    }
}
