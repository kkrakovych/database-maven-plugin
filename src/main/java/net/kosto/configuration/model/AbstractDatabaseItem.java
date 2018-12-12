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

import static net.kosto.util.Error.DUPLICATED_ATTRIBUTE;
import static net.kosto.util.Error.EMPTY_LIST_ATTRIBUTE;
import static net.kosto.util.Error.SEMI_DEFINED_ATTRIBUTES;
import static net.kosto.util.FileUtils.UNIX_SEPARATOR;
import static net.kosto.util.StringUtils.INDEX;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.kosto.configuration.model.common.AbstractCommonDatabaseItem;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents database item.
 * Provides access to database item's attributes and methods.
 */
public abstract class AbstractDatabaseItem extends AbstractCommonDatabaseItem implements DatabaseItem {

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
    this.executeDirectory = UNIX_SEPARATOR;
  }

  /**
   * Constructs instance and sets default values.
   *
   * @param item Common database item.
   */
  public AbstractDatabaseItem(final CommonDatabaseItem item) {
    this();
    setIgnoreServiceTables(item.getIgnoreServiceTables());
    setIndex(item.getIndex());
    setName(item.getName());
    setType(item.getType());
    setCondition(item.getCondition());
    setFileMask(item.getFileMask());
    setSourceDirectory(item.getSourceDirectory());
    setIgnoreDirectory(item.getIgnoreDirectory());
    setDefineSymbol(item.getDefineSymbol());
    setIgnoreDefine(item.getIgnoreDefine());
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
        "executeDirectory='" + executeDirectory + '\'' +
        ", sourceDirectoryFull=" + sourceDirectoryFull +
        ", outputDirectoryFull=" + outputDirectoryFull +
        "} " + super.toString();
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
   * It should check attributes for mandatory values and rise exception in case value is missing.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected abstract void checkMandatoryValues() throws MojoExecutionException;

  /**
   * Sets default values for database item's attributes.
   */
  protected abstract void setDefaultValues();

  /**
   * Processes paths for full source and output, and execute directories taking into account specified parameters and {@link #getIgnoreDirectory()} option.
   */
  private void processDirectoryAttributes() {
    if (!getIgnoreDirectory()) {
      this.executeDirectory = (UNIX_SEPARATOR + executeDirectory + UNIX_SEPARATOR + getSourceDirectory() + UNIX_SEPARATOR).replaceAll(UNIX_SEPARATOR + "{2,}", UNIX_SEPARATOR);
      this.sourceDirectoryFull = sourceDirectoryFull.resolve(getSourceDirectory());
      this.outputDirectoryFull = outputDirectoryFull.resolve(getSourceDirectory());
    }
  }

  /**
   * Processes attributes if necessary.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected void processAttributes() throws MojoExecutionException {
    // Some database items do not require the processing therefore the method is not abstract.
  }

  /**
   * Validates database item attribute with mandatory preliminary actions.
   * <ul>
   * <li>Some attributes like {@link AbstractDatabaseItem#getDefineSymbol()}, if was set in parent database item, should have the same value in related child one.</li>
   * <li>Some attributes like {@link AbstractDatabaseItem#executeDirectory} should be amended properly dependent on values from parent database items. </li>
   * </ul>
   *
   * @param item Database item for validation.
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected void validateAttribute(final DatabaseItem item) throws MojoExecutionException {
    // Attributes for propagation from parent database item to child one.
    if (item.getDefineSymbol() == null) {
      item.setDefineSymbol(getDefineSymbol());
    }
    if (item.getIgnoreDefine() == null) {
      item.setIgnoreDefine(getIgnoreDefine());
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
   * @param items        Database items.
   * @param attribute    Attribute name.
   * @param subAttribute Sub attribute name.
   * @param <T>          Any class with {@link DatabaseItem} interface implemented.
   * @throws MojoExecutionException If expected exception occurs.
   */
  protected <T extends DatabaseItem> void checkMandatory(final List<T> items, final String attribute, final String subAttribute) throws MojoExecutionException {
    if (items != null) {
      if (items.isEmpty()) {
        throw new MojoExecutionException(EMPTY_LIST_ATTRIBUTE.message(attribute, subAttribute));
      }
      if (isSemiDefinedIndex(items)) {
        throw new MojoExecutionException(SEMI_DEFINED_ATTRIBUTES.message(attribute, subAttribute, INDEX));
      }
      if (isDuplicateIndex(items)) {
        throw new MojoExecutionException(DUPLICATED_ATTRIBUTE.message(attribute, subAttribute, INDEX));
      }
      indexNatural(items);
    }
  }

  /**
   * Checks for semi-defined index attribute within list of database items.
   * <p>
   * We treat indexes as semi-defined for list of database items when some of them are defined and others are not.
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
      Integer order = item.getOrder();
      if (indexes.contains(order)) {
        result = true;
        break;
      }
      if (order != null) {
        indexes.add(order);
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
