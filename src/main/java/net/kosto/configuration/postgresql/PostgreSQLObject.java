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

import net.kosto.configuration.model.AbstractDatabaseObject;
import org.apache.maven.plugin.MojoExecutionException;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;

public class PostgreSQLObject extends AbstractDatabaseObject {

    private PostgreSQLObjectType type;

    public PostgreSQLObjectType getType() {
        return type;
    }

    public void setType(PostgreSQLObjectType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PostgreSQLObject{" +
            "index=" + getIndex() +
            ", type=" + getType() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", defineSymbol=" + getDefineSymbol() +
            ", ignoreDefine=" + getIgnoreDefine() +
            ", fileMask=" + getFileMask() +
            ", executeDirectory=" + getExecuteDirectory() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            "}";
    }

    protected void checkMandatoryValues() throws MojoExecutionException {
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.schema.object.index"));
        if (getType() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.schema.object.type"));
    }

    protected void setDefaultValues() {
        if (getDefineSymbol() == null)
            setDefineSymbol(":");
        if (getIgnoreDefine() == null)
            setIgnoreDefine(FALSE);
        if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory())
            setSourceDirectory(getIgnoreDirectory() ? "" : getType().getSourceDirectory());
    }
}
