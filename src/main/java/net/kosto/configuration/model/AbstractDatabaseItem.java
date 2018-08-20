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

import static java.lang.Boolean.FALSE;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;

import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents basic database item.
 * <p>
 * Provides access to basic database item's attributes and methods.
 */
public abstract class AbstractDatabaseItem implements DatabaseItem {

  /**
   * Database item's index in a list.
   * Affects processing order.
   */
  private Integer index;
  /**
   * Database item's name.
   */
  private String name;
  /**
   * Database item's relative path name for source directory.
   */
  private String sourceDirectory;
  /**
   * Whether to ignore {@link #sourceDirectory} path.
   * Default value is {@link Boolean#FALSE}
   */
  private Boolean ignoreDirectory;
  /**
   * Symbol to define variable in scripts.
   */
  private String defineSymbol;
  /**
   * Whether to ignore define variable symbol in scripts.
   */
  private Boolean ignoreDefine;
  /**
   * Relative path name for execute directory.
   */
  private String executeDirectory;
  /**
   * Full path to item's source directory.
   */
  private Path sourceDirectoryFull;
  /**
   * Full path to item's output directory.
   */
  private Path outputDirectoryFull;

  /**
   * Constructs instance and sets default values.
   */
  public AbstractDatabaseItem() {
    super();
    this.ignoreDirectory = FALSE;
    this.executeDirectory = UNIX_SEPARATOR;
  }

  @Override
  public Integer getIndex() {
    return index;
  }

  @Override
  public void setIndex(final Integer index) {
    this.index = index;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String getSourceDirectory() {
    return sourceDirectory;
  }

  @Override
  public void setSourceDirectory(final String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  @Override
  public Boolean getIgnoreDirectory() {
    return ignoreDirectory;
  }

  @Override
  public void setIgnoreDirectory(final Boolean ignoreDirectory) {
    this.ignoreDirectory = ignoreDirectory;
  }

  @Override
  public String getDefineSymbol() {
    return defineSymbol;
  }

  @Override
  public void setDefineSymbol(final String defineSymbol) {
    this.defineSymbol = defineSymbol;
  }

  @Override
  public Boolean getIgnoreDefine() {
    return ignoreDefine;
  }

  @Override
  public void setIgnoreDefine(final Boolean ignoreDefine) {
    this.ignoreDefine = ignoreDefine;
  }

  @Override
  public String getExecuteDirectory() {
    return executeDirectory;
  }

  @Override
  public void setExecuteDirectory(final String executeDirectory) {
    this.executeDirectory = executeDirectory;
  }

  @Override
  public Path getSourceDirectoryFull() {
    return sourceDirectoryFull;
  }

  @Override
  public void setSourceDirectoryFull(final Path sourceDirectoryFull) {
    this.sourceDirectoryFull = sourceDirectoryFull;
  }

  @Override
  public Path getOutputDirectoryFull() {
    return outputDirectoryFull;
  }

  @Override
  public void setOutputDirectoryFull(final Path outputDirectoryFull) {
    this.outputDirectoryFull = outputDirectoryFull;
  }

  @Override
  public String toString() {
    return "AbstractDatabaseItem{" +
        "index=" + index +
        ", name='" + name + '\'' +
        ", sourceDirectory='" + sourceDirectory + '\'' +
        ", ignoreDirectory=" + ignoreDirectory +
        ", defineSymbol='" + defineSymbol + '\'' +
        ", ignoreDefine=" + ignoreDefine +
        ", executeDirectory='" + executeDirectory + '\'' +
        ", sourceDirectoryFull=" + sourceDirectoryFull +
        ", outputDirectoryFull=" + outputDirectoryFull +
        '}';
  }

  @Override
  public void validate() throws MojoExecutionException {
    checkMandatoryValues();
    setDefaultValues();
    processDirectoryAttributes();
    processAttributes();
  }

  /**
   * Checks mandatory database item's attributes values.
   * <p>
   * It should check attributes for mandatory values and rise exception
   * in case value is missing.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected abstract void checkMandatoryValues() throws MojoExecutionException;

  /**
   * Sets default values for database item's attributes.
   */
  protected abstract void setDefaultValues();

  /**
   * Processes paths for full source and output, and execute directories
   * taking into account specified parameters and {@link #ignoreDirectory} option.
   */
  private void processDirectoryAttributes() {
    if (!ignoreDirectory) {
      this.executeDirectory = (UNIX_SEPARATOR + executeDirectory + UNIX_SEPARATOR + sourceDirectory + UNIX_SEPARATOR).replaceAll(UNIX_SEPARATOR + "{2,}", UNIX_SEPARATOR);
      this.sourceDirectoryFull = sourceDirectoryFull.resolve(sourceDirectory);
      this.outputDirectoryFull = outputDirectoryFull.resolve(sourceDirectory);
    }
  }

  /**
   * Processes attributes if necessary.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected void processAttributes() throws MojoExecutionException {
    // Some database items do not require the processing
    // therefore the method is not abstract.
  }

  /**
   * Validates database items' attribute with mandatory preliminary actions.
   * <li>
   * Some attributes like {@link AbstractDatabaseItem#defineSymbol},
   * if was set in parent database item, should have the same value in related
   * child one.
   * </li>
   * <li>
   * Some attributes like {@link AbstractDatabaseItem#executeDirectory}
   * should be amended properly dependent on values from parent database items.
   * </li>
   *
   * @param attribute Attribute for validation.
   * @param <T>       Attribute type.
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected <T extends AbstractDatabaseItem> void validateAttribute(final T attribute) throws MojoExecutionException {
    // Attributes for propagation from parent database item to child one.
    if (attribute.getDefineSymbol() == null) {
      attribute.setDefineSymbol(defineSymbol);
    }
    if (attribute.getIgnoreDefine() == null) {
      attribute.setIgnoreDefine(ignoreDefine);
    }
    // Attributes dependent on values from parent database item.
    attribute.setExecuteDirectory(executeDirectory);
    attribute.setSourceDirectoryFull(sourceDirectoryFull);
    attribute.setOutputDirectoryFull(outputDirectoryFull);
    // Executes database item validation.
    attribute.validate();
  }
}
