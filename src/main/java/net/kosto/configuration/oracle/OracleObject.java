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
import net.kosto.configuration.model.DatabaseObjectType;
import org.apache.maven.plugin.MojoExecutionException;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;

/**
 * {@code OracleObject} represents Oracle object configuration.
 * <p>
 * Provides access to object index, type, source directory, whether to ignore source directory,
 * file mask, full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleObject#sourceDirectory} = {@link DatabaseObjectType#getSourceDirectory()}</li>
 * <li>{@link OracleObject#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleObject#fileMask} = {@code "*.sql"}</li>
 * </ul>
 */
public class OracleObject extends DatabaseObject implements ValidateAction {

    @Override
    public String toString() {
        return "object{" +
            "index=" + getIndex() +
            ", type=" + getType() +
            ", sourceDirectory=" + getSourceDirectory() +
            ", ignoreDirectory=" + getIgnoreDirectory() +
            ", fileMask=" + getFileMask() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            "}";
    }

    @Override
    public void validate() throws MojoExecutionException {
        checkMandatoryValues();
        setDefaultValues();
    }

    /**
     * Checks {@code OracleObject} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    private void checkMandatoryValues() throws MojoExecutionException {
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.object.index"));
        if (getType() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.object.type"));
    }

    /**
     * Sets default values for {@code OracleObject} configuration.
     */
    private void setDefaultValues() {
        if (getSourceDirectory() == null || getSourceDirectory().isEmpty())
            setSourceDirectory(getType().getSourceDirectory());
        if (getIgnoreDirectory() == null)
            setIgnoreDirectory(FALSE);
        if (getFileMask() == null)
            setFileMask(FILE_MASK_SQL);
    }
}
