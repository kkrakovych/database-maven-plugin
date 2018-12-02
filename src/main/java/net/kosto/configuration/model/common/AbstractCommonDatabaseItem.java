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

package net.kosto.configuration.model.common;

import static java.lang.Boolean.FALSE;
import static net.kosto.util.FileUtils.FILE_MASK_SQL;

/**
 * Represents common database item.
 * <p>
 * Provides access to common database item's attributes and methods.
 */
public abstract class AbstractCommonDatabaseItem implements CommonDatabaseItem {

  /**
   * Database item's index in a list.
   * <p>
   * Affects processing order.
   */
  private Integer index;
  /**
   * Database item's name.
   */
  private String name;
  /**
   * Database item's type.
   */
  private String type;
  /**
   * Database item's condition.
   */
  private String condition;
  /**
   * Database item's file mask.
   * <p>
   * Default value is {@link net.kosto.util.FileUtils#FILE_MASK_SQL}
   */
  private String fileMask;
  /**
   * Database item's relative path name for source directory.
   */
  private String sourceDirectory;
  /**
   * Whether to ignore {@link #sourceDirectory} path.
   * <p>
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
   * Constructs instance and sets default values.
   */
  public AbstractCommonDatabaseItem() {
    super();
    this.ignoreDirectory = FALSE;
    this.fileMask = FILE_MASK_SQL;
  }

  @Override
  public Integer getIndex() {
    return index;
  }

  @Override
  public void setIndex(Integer index) {
    this.index = index;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getCondition() {
    return condition;
  }

  @Override
  public void setCondition(String condition) {
    this.condition = condition;
  }

  @Override
  public String getFileMask() {
    return fileMask;
  }

  @Override
  public void setFileMask(String fileMask) {
    this.fileMask = fileMask;
  }

  @Override
  public String getSourceDirectory() {
    return sourceDirectory;
  }

  @Override
  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  @Override
  public Boolean getIgnoreDirectory() {
    return ignoreDirectory;
  }

  @Override
  public void setIgnoreDirectory(Boolean ignoreDirectory) {
    this.ignoreDirectory = ignoreDirectory;
  }

  @Override
  public String getDefineSymbol() {
    return defineSymbol;
  }

  @Override
  public void setDefineSymbol(String defineSymbol) {
    this.defineSymbol = defineSymbol;
  }

  @Override
  public Boolean getIgnoreDefine() {
    return ignoreDefine;
  }

  @Override
  public void setIgnoreDefine(Boolean ignoreDefine) {
    this.ignoreDefine = ignoreDefine;
  }

  @Override
  public String toString() {
    return "AbstractCommonDatabaseItem{" +
        "index=" + index +
        ", name='" + name + '\'' +
        ", type='" + type + '\'' +
        ", condition='" + condition + '\'' +
        ", fileMask='" + fileMask + '\'' +
        ", sourceDirectory='" + sourceDirectory + '\'' +
        ", ignoreDirectory=" + ignoreDirectory +
        ", defineSymbol='" + defineSymbol + '\'' +
        ", ignoreDefine=" + ignoreDefine +
        '}';
  }
}
