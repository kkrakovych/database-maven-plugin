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

package net.kosto.configuration.model;

import net.kosto.configuration.ValidateAction;
import org.apache.maven.plugin.MojoExecutionException;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;

/**
 * {@code DatabaseScript} represents database script configuration.
 */
public class DatabaseScript extends DatabaseObject implements ValidateAction {

    /** Database script's type. */
    private DatabaseScriptType type;
    /** Database script's condition. */
    private DatabaseScriptCondition condition;

    public DatabaseScriptType getType() {
        return type;
    }

    public void setType(DatabaseScriptType type) {
        this.type = type;
    }

    public DatabaseScriptCondition getCondition() {
        return condition;
    }

    public void setCondition(DatabaseScriptCondition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "DatabaseScript{" +
            "type=" + getType() +
            ", condition=" + getCondition() +
            ", index=" + getIndex() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", fileMask=" + getFileMask() +
            ", executeDirectory=" + getExecuteDirectory() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            '}';
    }

    @Override
    public void validate() throws MojoExecutionException {
        checkMandatoryValues();
        setDefaultValues();
    }

    /**
     * Checks {@code DatabaseScript} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    private void checkMandatoryValues() throws MojoExecutionException {
        if (getType() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.script.type"));
        if (getCondition() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.script.condition"));
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.script.index"));
    }

    /**
     * Sets default values for {@code DatabaseScript} configuration.
     */
    private void setDefaultValues() {
        if (getIgnoreDirectory() == null)
            setIgnoreDirectory(FALSE);
        if (getFileMask() == null)
            setFileMask(FILE_MASK_SQL);
        if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory())
            setSourceDirectory(getIgnoreDirectory() ? "" : getType().getSourceDirectory());
        postProcessDirectories();
    }
}
