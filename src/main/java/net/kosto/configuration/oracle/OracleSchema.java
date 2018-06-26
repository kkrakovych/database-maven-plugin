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
import static net.kosto.configuration.model.DatabaseObjectType.SCHEMA;

/**
 * {@code OracleSchema} represents Oracle schema configuration.
 * <p>
 * Provides access to schema index, name, source directory, whether to ignore source directory,
 * list of objects, full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleSchema#sourceDirectory} = {@link OracleSchema#name}</li>
 * <li>{@link OracleSchema#type} = {@link net.kosto.configuration.model.DatabaseObjectType#SCHEMA}</li>
 * <li>{@link OracleSchema#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * </ul>
 */
public class OracleSchema extends DatabaseObject implements ValidateAction {

    private List<OracleObject> objects;

    public List<OracleObject> getObjects() {
        return objects;
    }

    public void setObjects(List<OracleObject> objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "OracleSchema{" +
            "index=" + getIndex() +
            ", name=" + getName() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", objects=" + getObjects() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            "}";
    }

    @Override
    public void validate() throws MojoExecutionException {
        checkMandatoryValues();
        setDefaultValues();

        getObjects().sort(Comparator.comparingInt(OracleObject::getIndex).thenComparing(OracleObject::getType));

        for (OracleObject object : objects) {
            object.setDirectoryFull(getSourceDirectoryFull(), getOutputDirectoryFull());
            object.validate();
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
    }

    /**
     * Sets default values for {@code OracleSchema} configuration.
     */
    private void setDefaultValues() {
        if (getSourceDirectory() == null || getSourceDirectory().isEmpty())
            setSourceDirectory(getName());
        setType(SCHEMA);
        if (getIgnoreDirectory() == null)
            setIgnoreDirectory(FALSE);
        setDirectoryFull(getSourceDirectoryFull(), getOutputDirectoryFull());
    }
}
