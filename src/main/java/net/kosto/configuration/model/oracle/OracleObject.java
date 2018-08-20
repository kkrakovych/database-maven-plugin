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

import net.kosto.configuration.model.AbstractDatabaseObject;
import org.apache.maven.plugin.MojoExecutionException;

import static java.lang.Boolean.FALSE;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;

/**
 * {@code OracleObject} represents Oracle object configuration.
 * <p>
 * Provides access to object index, type, source directory, whether to ignore source directory,
 * file mask, execute directory and full paths to source and output directories.
 * <p>
 * Default values for missing attributes:
 * <ul>
 * <li>{@link OracleObject#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleObject#fileMask} = {@link net.kosto.util.FileUtils#FILE_MASK_SQL}</li>
 * <li>{@link OracleObject#sourceDirectory} = {@link OracleObjectType#getSourceDirectory()}</li>
 * </ul>
 */
public class OracleObject extends AbstractDatabaseObject {

    /** Database object's type. */
    private OracleObjectType type;

    public OracleObjectType getType() {
        return type;
    }

    public void setType(OracleObjectType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OracleObject{" +
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

    /**
     * Checks {@code OracleObject} configuration for mandatory values.
     *
     * @throws MojoExecutionException If a validation exception occurred.
     */
    protected void checkMandatoryValues() throws MojoExecutionException {
        if (getIndex() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.object.index"));
        if (getType() == null)
            throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.object.type"));
    }

    /**
     * Sets default values for {@code OracleObject} configuration.
     */
    protected void setDefaultValues() {
        if (getDefineSymbol() == null)
            setDefineSymbol("&");
        if (getIgnoreDefine() == null)
            setIgnoreDefine(FALSE);
        if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory())
            setSourceDirectory(getIgnoreDirectory() ? "" : getType().getSourceDirectory());
    }
}