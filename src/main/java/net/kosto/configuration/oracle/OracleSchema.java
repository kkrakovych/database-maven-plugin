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
import net.kosto.configuration.model.DatabaseScript;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;

/**
 * {@code OracleSchema} represents Oracle schema configuration.
 * <p>
 * Provides access to schema index, name, source directory, whether to ignore source directory,
 * list of objects, full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleSchema#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleSchema#sourceDirectory} = {@link OracleSchema#name}</li>
 * </ul>
 */
public class OracleSchema extends DatabaseObject implements ValidateAction {

    private List<OracleObject> objects;
    private List<DatabaseScript> scripts;

    public List<OracleObject> getObjects() {
        return objects;
    }

    public void setObjects(List<OracleObject> objects) {
        this.objects = objects;
    }

    public List<DatabaseScript> getScripts() {
        return scripts;
    }

    public void setScripts(List<DatabaseScript> scripts) {
        this.scripts = scripts;
    }

    @Override
    public String toString() {
        return "OracleSchema{" +
            "index=" + getIndex() +
            ", name=" + getName() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", executeDirectory=" + getExecuteDirectory() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            ", objects=" + getObjects() +
            ", scripts=" + getScripts() +
            "}";
    }

    @Override
    public void validate() throws MojoExecutionException {
        checkMandatoryValues();
        setDefaultValues();

        getObjects().sort(Comparator.comparingInt(OracleObject::getIndex).thenComparing(OracleObject::getType));

        for (OracleObject object : objects) {
            object.setExecuteDirectory(getExecuteDirectory());
            object.setSourceDirectoryFull(getSourceDirectoryFull());
            object.setOutputDirectoryFull(getOutputDirectoryFull());
            object.validate();
        }

        if (scripts != null)
            for (DatabaseScript script : scripts) {
                script.setExecuteDirectory(getExecuteDirectory());
                script.setSourceDirectoryFull(getSourceDirectoryFull());
                script.setOutputDirectoryFull(getOutputDirectoryFull());
                script.validate();
            }
    }

    /**
     * Checks {@code OracleSchema} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    private void checkMandatoryValues() throws MojoExecutionException {
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.index"));
        if (getName() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.name"));
        if (getObjects() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.objects"));
        if (getObjects().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schema.objects", "object"));
        if (getScripts() != null && getScripts().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schema.scripts", "script"));
    }

    /**
     * Sets default values for {@code OracleSchema} configuration.
     */
    private void setDefaultValues() {
        if (getIgnoreDirectory() == null)
            setIgnoreDirectory(FALSE);
        if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory())
            setSourceDirectory(getIgnoreDirectory() ? "" : getName());
        postProcessDirectories();
    }
}
