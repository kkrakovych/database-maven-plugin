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

package net.kosto.configuration.postgresql;

import net.kosto.configuration.model.DatabaseBaseObject;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;

public class PostgreSQLSchema extends DatabaseBaseObject {

    private List<PostgreSQLObject> objects;
    private List<PostgreSQLScript> scripts;

    public List<PostgreSQLObject> getObjects() {
        return objects;
    }

    public void setObjects(List<PostgreSQLObject> objects) {
        this.objects = objects;
    }

    public List<PostgreSQLScript> getScripts() {
        return scripts;
    }

    public void setScripts(List<PostgreSQLScript> scripts) {
        this.scripts = scripts;
    }

    @Override
    public String toString() {
        return "PostgreSQLSchema{" +
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

    protected void checkMandatoryValues() throws MojoExecutionException {
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.schema.index"));
        if (getName() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.schema.name"));
        if (getObjects() != null && getObjects().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.schema.objects", "object"));
        if (getScripts() != null && getScripts().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.schema.scripts", "script"));
    }

    protected void setDefaultValues() {
        if (getDefineSymbol() == null)
            setDefineSymbol(":");
        if (getIgnoreDefine() == null)
            setIgnoreDefine(FALSE);
        if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory())
            setSourceDirectory(getIgnoreDirectory() ? "" : getName());
    }

    @Override
    protected void processAttributes() throws MojoExecutionException {
        if (objects != null) {
            objects
                .sort(
                    Comparator
                        .comparingInt(PostgreSQLObject::getIndex)
                        .thenComparing(PostgreSQLObject::getType)
                );

            for (PostgreSQLObject object : objects)
                validateAttribute(object);
        }

        if (scripts != null) {
            scripts
                .sort(
                    Comparator
                        .comparing(PostgreSQLScript::getCondition, Comparator.reverseOrder())
                        .thenComparingInt(PostgreSQLScript::getIndex)
                );

            for (PostgreSQLScript script : scripts)
                validateAttribute(script);
        }
    }
}
