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

package net.kosto.configuration.model.postgresql;

import net.kosto.configuration.model.AbstractDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;

public class PostgreSQLDatabase extends AbstractDatabaseItem {

    private List<PostgreSQLObject> objects;
    private List<PostgreSQLScript> scripts;
    private List<PostgreSQLSchema> schemes;

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

    public List<PostgreSQLSchema> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<PostgreSQLSchema> schemes) {
        this.schemes = schemes;
    }

    @Override
    public String toString() {
        return "PostgreSQLDatabase{" +
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
            ", schemes=" + getSchemes() +
            "}";
    }

    protected void checkMandatoryValues() throws MojoExecutionException {
        if (getName() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.name"));
        if (getObjects() != null && getObjects().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.objects", "object"));
        if (getScripts() != null && getScripts().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.scripts", "script"));
        if (getSchemes() != null && getSchemes().isEmpty())
            throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.schemes", "schema"));
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

        if (schemes != null) {
            for (PostgreSQLSchema schema : schemes)
                validateAttribute(schema);
        }

        // Public schema
        if (objects != null || scripts != null) {
            if (schemes == null)
                schemes = new ArrayList<>();

            PostgreSQLSchema schema = new PostgreSQLSchema();
            schema.setIndex(schemes.stream().mapToInt(PostgreSQLSchema::getIndex).min().orElse(0));
            schema.setName("public");
            schema.setSourceDirectory(getSourceDirectory());
            schema.setIgnoreDirectory(TRUE);
            schema.setExecuteDirectory(getExecuteDirectory());
            schema.setSourceDirectoryFull(getSourceDirectoryFull());
            schema.setOutputDirectoryFull(getOutputDirectoryFull());
            schema.setObjects(objects);
            schema.setScripts(scripts);

            schemes.add(schema);
        }

        if (schemes != null) {
            schemes
                .sort(
                    Comparator
                        .comparingInt(PostgreSQLSchema::getIndex)
                        .thenComparing(PostgreSQLSchema::getName)
                );
        }
    }
}
