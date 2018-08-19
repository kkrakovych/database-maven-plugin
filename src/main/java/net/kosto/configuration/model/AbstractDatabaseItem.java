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

import java.nio.file.Paths;

import static java.lang.Boolean.FALSE;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;

/**
 * {@code DatabaseBaseObject} represents basic database object configuration.
 * <p>
 * Provides access to basic database object's attributes and methods.
 */
public abstract class AbstractDatabaseItem implements ValidateAction {

    /** Database object's index in a list. Affects processing order. */
    private Integer index;
    /** Database object's name. */
    private String name;
    /** Database object's relative source directory path. */
    private String sourceDirectory;
    /** Whether to ignore specified {@link #sourceDirectory} path. */
    private Boolean ignoreDirectory = FALSE;
    /** Symbol to define variable. */
    private String defineSymbol;
    /** Whether to ignore define variable symbol. */
    private Boolean ignoreDefine;
    /** Relative path to execute directory. */
    private String executeDirectory = UNIX_SEPARATOR;
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

    public String getDefineSymbol() {
        return defineSymbol;
    }

    public void setDefineSymbol(String defineSymbol) {
        this.defineSymbol = defineSymbol;
    }

    public Boolean getIgnoreDefine() {
        return ignoreDefine;
    }

    public void setIgnoreDefine(Boolean ignoreDefine) {
        this.ignoreDefine = ignoreDefine;
    }

    public String getExecuteDirectory() {
        return executeDirectory;
    }

    public void setExecuteDirectory(String executeDirectory) {
        this.executeDirectory = executeDirectory;
    }

    public String getSourceDirectoryFull() {
        return sourceDirectoryFull;
    }

    public void setSourceDirectoryFull(String sourceDirectoryFull) {
        this.sourceDirectoryFull = sourceDirectoryFull;
    }

    public String getOutputDirectoryFull() {
        return outputDirectoryFull;
    }

    public void setOutputDirectoryFull(String outputDirectoryFull) {
        this.outputDirectoryFull = outputDirectoryFull;
    }

    @Override
    public String toString() {
        return "DatabaseBaseObject{" +
            "index=" + index +
            ", name=" + name +
            ", sourceDirectory=" + sourceDirectory +
            ", ignoreDirectory=" + ignoreDirectory +
            ", defineSymbol=" + defineSymbol +
            ", ignoreDefine=" + ignoreDefine +
            ", executeDirectory=" + executeDirectory +
            ", sourceDirectoryFull=" + sourceDirectoryFull +
            ", outputDirectoryFull=" + outputDirectoryFull +
            '}';
    }

    public void validate() throws MojoExecutionException {
        checkMandatoryValues();
        setDefaultValues();
        processDirectoryAttributes();
        processAttributes();
    }

    protected abstract void checkMandatoryValues() throws MojoExecutionException;

    protected abstract void setDefaultValues();

    /**
     * Post processes paths to full source and output, and execute directories
     * taking into account specified parameters and {@link #ignoreDirectory} option.
     */
    private void processDirectoryAttributes() {
        if (!getIgnoreDirectory()) {
            this.executeDirectory = (UNIX_SEPARATOR + executeDirectory + UNIX_SEPARATOR + sourceDirectory + UNIX_SEPARATOR).replaceAll(UNIX_SEPARATOR + "{2,}", UNIX_SEPARATOR);
            this.sourceDirectoryFull = Paths.get(sourceDirectoryFull, sourceDirectory).toString();
            this.outputDirectoryFull = Paths.get(outputDirectoryFull, sourceDirectory).toString();
        }
    }

    protected void processAttributes() throws MojoExecutionException {
    }

    protected <T extends AbstractDatabaseItem> void validateAttribute(T attribute) throws MojoExecutionException {
        if (attribute.getDefineSymbol() == null)
            attribute.setDefineSymbol(defineSymbol);
        if (attribute.getIgnoreDefine() == null)
            attribute.setIgnoreDefine(ignoreDefine);
        attribute.setExecuteDirectory(executeDirectory);
        attribute.setSourceDirectoryFull(sourceDirectoryFull);
        attribute.setOutputDirectoryFull(outputDirectoryFull);
        attribute.validate();
    }
}
