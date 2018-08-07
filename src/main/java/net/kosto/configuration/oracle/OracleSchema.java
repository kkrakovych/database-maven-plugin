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
import net.kosto.configuration.model.DatabaseBaseObject;
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
 * lists of objects and scripts, execute directory and full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleSchema#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleSchema#sourceDirectory} = {@link OracleSchema#name}</li>
 * </ul>
 */
public class OracleSchema extends DatabaseBaseObject implements ValidateAction {

    /** List of objects for deploy. */
    private List<OracleObject> objects;
    /** List of scripts for deploy. */
    private List<OracleScript> scripts;

    public List<OracleObject> getObjects() {
        return objects;
    }

    public void setObjects(List<OracleObject> objects) {
        this.objects = objects;
    }

    public List<OracleScript> getScripts() {
        return scripts;
    }

    public void setScripts(List<OracleScript> scripts) {
        this.scripts = scripts;
    }

    @Override
    public String toString() {
        return "OracleSchema{" +
            "index=" + getIndex() +
            ", name=" + getName() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", defineSymbol=" + getDefineSymbol() +
            ", ignoreDefine=" + getIgnoreDefine() +
            ", executeDirectory=" + getExecuteDirectory() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            ", objects=" + getObjects() +
            ", scripts=" + getScripts() +
            "}";
    }

    /**
     * Checks {@code OracleSchema} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    protected void checkMandatoryValues() throws MojoExecutionException {
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.index"));
        if (getName() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.name"));
        if (getObjects() != null && getObjects().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schema.objects", "object"));
        if (getScripts() != null && getScripts().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schema.scripts", "script"));
    }

    /**
     * Sets default values for {@code OracleSchema} configuration.
     */
    protected void setDefaultValues() {
        if (getDefineSymbol() == null)
            setDefineSymbol("&");
        if (getIgnoreDefine() == null)
            setIgnoreDefine(FALSE);
        if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory())
            setSourceDirectory(getIgnoreDirectory() ? "" : getName());
    }

    @Override
    protected void processAttributes() throws MojoExecutionException {
        if (objects != null) {
            getObjects()
                .sort(
                    Comparator
                        .comparingInt(OracleObject::getIndex)
                        .thenComparing(OracleObject::getType)
                );

            for (OracleObject object : objects) {
                if (object.getDefineSymbol() == null)
                    object.setDefineSymbol(getDefineSymbol());
                if (object.getIgnoreDefine() == null)
                    object.setIgnoreDefine(getIgnoreDefine());
                object.setExecuteDirectory(getExecuteDirectory());
                object.setSourceDirectoryFull(getSourceDirectoryFull());
                object.setOutputDirectoryFull(getOutputDirectoryFull());
                object.validate();
            }
        }

        if (scripts != null) {
            getScripts()
                .sort(
                    Comparator
                        .comparing(OracleScript::getCondition, Comparator.reverseOrder())
                        .thenComparingInt(OracleScript::getIndex)
                );

            for (OracleScript script : scripts) {
                if (script.getDefineSymbol() == null)
                    script.setDefineSymbol(getDefineSymbol());
                if (script.getIgnoreDefine() == null)
                    script.setIgnoreDefine(getIgnoreDefine());
                script.setExecuteDirectory(getExecuteDirectory());
                script.setSourceDirectoryFull(getSourceDirectoryFull());
                script.setOutputDirectoryFull(getOutputDirectoryFull());
                script.validate();
            }
        }
    }
}
