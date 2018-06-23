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

import java.nio.file.Paths;

/**
 * {@code DatabaseObject} represents basic database object configuration.
 * <p>
 * Provides access to basic database object's attributes and methods.
 */
public abstract class DatabaseObject {

    /** Database object's index in a list. Affects processing order. */
    private Integer index;
    /** Database object's name. */
    private String name;
    /** Database object's type. */
    private DatabaseObjectType type;
    /** Database object's relative source directory path. */
    private String sourceDirectory;
    /** Whether to ignore specified {@link #sourceDirectory} path. */
    private Boolean ignoreDirectory;
    /** Database object's file mask. */
    private String fileMask;

    /** Full path to object's source directory. */
    private String sourceDirectoryFull;
    /** Full path to object's output directory. */
    private String outputDirectoryFull;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatabaseObjectType getType() {
        return type;
    }

    public void setType(DatabaseObjectType type) {
        this.type = type;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public Boolean getIgnoreDirectory() {
        return ignoreDirectory;
    }

    public void setIgnoreDirectory(Boolean ignoreDirectory) {
        this.ignoreDirectory = ignoreDirectory;
    }

    public String getFileMask() {
        return fileMask;
    }

    public void setFileMask(String fileMask) {
        this.fileMask = fileMask;
    }

    public String getSourceDirectoryFull() {
        return sourceDirectoryFull;
    }

    public String getOutputDirectoryFull() {
        return outputDirectoryFull;
    }

    /**
     * Sets full path to source and output directories taking into account specified parameters and {@link #ignoreDirectory} option.
     *
     * @param sourceDirectoryFull Full path to source directory.
     * @param outputDirectoryFull Full path to output directory.
     */
    public void setDirectoryFull(String sourceDirectoryFull, String outputDirectoryFull) {
        if (getIgnoreDirectory() != null && !getIgnoreDirectory()) {
            this.sourceDirectoryFull = Paths.get(sourceDirectoryFull, getSourceDirectory()).toString();
            this.outputDirectoryFull = Paths.get(outputDirectoryFull, getSourceDirectory()).toString();
        } else {
            this.sourceDirectoryFull = sourceDirectoryFull;
            this.outputDirectoryFull = outputDirectoryFull;
        }
    }

    @Override
    public String toString() {
        return "DatabaseObject{" +
            "index=" + index +
            ", name=" + name +
            ", type=" + type +
            ", sourceDirectory=" + sourceDirectory +
            ", ignoreDirectory=" + ignoreDirectory +
            ", fileMask=" + fileMask +
            ", sourceDirectoryFull=" + sourceDirectoryFull +
            ", outputDirectoryFull=" + outputDirectoryFull +
            '}';
    }
}
