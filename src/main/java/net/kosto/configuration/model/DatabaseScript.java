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

/**
 * {@code DatabaseScript} represents database script configuration.
 */
public abstract class DatabaseScript extends DatabaseObject {

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
            ", defineSymbol=" + getDefineSymbol() +
            ", ignoreDefine=" + getIgnoreDefine() +
            ", fileMask=" + getFileMask() +
            ", executeDirectory=" + getExecuteDirectory() +
            ", sourceDirectoryFull=" + getSourceDirectoryFull() +
            ", outputDirectoryFull=" + getOutputDirectoryFull() +
            '}';
    }
}
