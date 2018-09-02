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
import static net.kosto.configuration.ValidateError.DUPLICATED_ATTRIBUTE;
import static net.kosto.configuration.ValidateError.EMPTY_LIST_ATTRIBUTE;
import static net.kosto.configuration.ValidateError.SEMI_DEFINED_ATTRIBUTES;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.INDEX;
import static net.kosto.util.StringUtils.SCHEMA;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
   * Validates database item attribute with mandatory preliminary actions.
   * <ul>
   * <li>
   * Some attributes like {@link AbstractDatabaseItem#defineSymbol},
   * if was set in parent database item, should have the same value in related
   * child one.
   * </li>
   * <li>
   * Some attributes like {@link AbstractDatabaseItem#executeDirectory}
   * should be amended properly dependent on values from parent database items.
   * </li>
   * </ul>
   *
   * @param item Database item for validation.
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected void validateAttribute(final DatabaseItem item) throws MojoExecutionException {
    // Attributes for propagation from parent database item to child one.
    if (item.getDefineSymbol() == null) {
      item.setDefineSymbol(defineSymbol);
    }
    if (item.getIgnoreDefine() == null) {
      item.setIgnoreDefine(ignoreDefine);
    }
    // Attributes dependent on values from parent database item.
    item.setExecuteDirectory(executeDirectory);
    item.setSourceDirectoryFull(sourceDirectoryFull);
    item.setOutputDirectoryFull(outputDirectoryFull);
    // Executes database item validation.
    item.validate();
  }

  /**
   * Checks complex database items' attributes for common cases.
   *
   * @param items     Database items.
   * @param attribute Attribute name.
   * @param <T>       Any class with {@link DatabaseItem} interface implemented.
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected <T extends DatabaseItem> void checkMandatory(final List<T> items, final String attribute) throws MojoExecutionException {
    if (items != null) {
      if (items.isEmpty()) {
        throw new MojoExecutionException(EMPTY_LIST_ATTRIBUTE.message(attribute, SCHEMA));
      }
      if (isSemiDefinedIndex(items)) {
        throw new MojoExecutionException(SEMI_DEFINED_ATTRIBUTES.message(attribute, SCHEMA, INDEX));
      }
      if (isDuplicateIndex(items)) {
        throw new MojoExecutionException(DUPLICATED_ATTRIBUTE.message(attribute, SCHEMA, INDEX));
      }
      indexNatural(items);
    }
  }

  /**
   * Checks for semi-defined index attribute within list of database items.
   * <p>
   * We treat indexes as semi-defined for list of database items
   * when some of them are defined and others are not.
   *
   * @param items Database items.
   * @param <T>   Any class with {@link DatabaseItem} interface implemented.
   * @return If indexes are semi-defined {@code true}, otherwise {@code false}.
   */
  private <T extends DatabaseItem> boolean isSemiDefinedIndex(final List<T> items) {
    final int count = (int) items.stream().filter(item -> item.getIndex() == null).count();
    return count > 0 && items.size() != count;
  }

  /**
   * Checks for duplicate index attribute within list of database items.
   * {@code Null} values are not treated as duplicate.
   *
   * @param items Database items.
   * @param <T>   Any class with {@link DatabaseItem} interface implemented.
   * @return If duplicate index found {@code true}, otherwise {@code false}.
   */
  private <T extends DatabaseItem> boolean isDuplicateIndex(final List<T> items) {
    boolean result = false;

    final Set<Integer> indexes = new HashSet<>();
    for (final DatabaseItem item : items) {
      if (indexes.contains(item.getOrder())) {
        result = true;
        break;
      }
      if (item.getOrder() != null) {
        indexes.add(item.getOrder());
      }
    }

    return result;
  }

  /**
   * Indexes list of database items in natural order.
   *
   * @param items Database items.
   * @param <T>   Any class with {@link DatabaseItem} interface implemented.
   */
  private <T extends DatabaseItem> void indexNatural(final List<T> items) {
    int order = 0;
    for (final T item : items) {
      if (item.getIndex() != null) {
        break;
      }
      item.setIndex(order++);
    }
  }
}
