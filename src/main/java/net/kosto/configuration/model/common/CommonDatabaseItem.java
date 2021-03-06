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

/**
 * Provides interface required by any common database item.
 */
public interface CommonDatabaseItem {

  /**
   * Returns whether to ignore service tables.
   *
   * @return {@link Boolean#TRUE} if service tables should be ignored; otherwise {@link Boolean#FALSE}.
   */
  Boolean getIgnoreServiceTables();

  /**
   * Sets whether to ignore service tables.
   *
   * @param ignoreServiceTables {@link Boolean#TRUE} if service tables should be ignored; otherwise {@link Boolean#FALSE}.
   */
  void setIgnoreServiceTables(Boolean ignoreServiceTables);

  /**
   * Returns database item's index in a list.
   *
   * @return Database item's index.
   */
  Integer getIndex();

  /**
   * Sets database item's index in a list.
   *
   * @param index Database item's index.
   */
  void setIndex(final Integer index);

  /**
   * Returns database item's order in a list.
   * <p>
   * In general the order matches database item's index, though it could be different in case it is required.
   * As an example, for scripts a script condition should be taken into account.
   *
   * @return Database item's order.
   */
  default Integer getOrder() {
    return getIndex();
  }

  /**
   * Returns database item's name.
   *
   * @return Database item's name.
   */
  String getName();

  /**
   * Sets database item's name.
   *
   * @param name Database item's name.
   */
  void setName(final String name);

  /**
   * Returns database item's type.
   *
   * @return Database item's type.
   */
  String getType();

  /**
   * Sets database item's type.
   *
   * @param type Database item's type.
   */
  void setType(final String type);

  /**
   * Returns database item's condition.
   *
   * @return Database item's condition.
   */
  String getCondition();

  /**
   * Sets database item's condition.
   *
   * @param condition Database item's condition.
   */
  void setCondition(final String condition);

  /**
   * Returns database item's file mask.
   *
   * @return Database item's file mask.
   */
  String getFileMask();

  /**
   * Sets database item's file mask.
   *
   * @param fileMask Database item's file mask.
   */
  void setFileMask(final String fileMask);

  /**
   * Returns database item's relative path name for source directory.
   *
   * @return Relative path name for source directory.
   */
  String getSourceDirectory();

  /**
   * Sets database item's relative path name for source directory.
   *
   * @param sourceDirectory Relative path name for source directory.
   */
  void setSourceDirectory(final String sourceDirectory);

  /**
   * Returns whether to ignore source directory path.
   *
   * @return {@link Boolean#TRUE} if directory should be ignored; otherwise {@link Boolean#FALSE}.
   */
  Boolean getIgnoreDirectory();

  /**
   * Sets whether to ignore source directory path.
   *
   * @param ignoreDirectory {@link Boolean#TRUE} if directory should be ignored; otherwise {@link Boolean#FALSE}.
   */
  void setIgnoreDirectory(final Boolean ignoreDirectory);

  /**
   * Returns symbol to define variable in scripts.
   *
   * @return Symbol to define variable.
   */
  String getDefineSymbol();

  /**
   * Sets symbol to define variable in scripts.
   *
   * @param defineSymbol Symbol to define variable.
   */
  void setDefineSymbol(final String defineSymbol);

  /**
   * Returns whether to ignore define variable symbol in scripts.
   *
   * @return {@link Boolean#TRUE} if define variable symbol should be ignored; otherwise {@link Boolean#FALSE}.
   */
  Boolean getIgnoreDefine();

  /**
   * Sets whether to ignore define variable symbol in scripts.
   *
   * @param ignoreDefine {@link Boolean#TRUE} if define variable symbol should be ignored; otherwise {@link Boolean#FALSE}.
   */
  void setIgnoreDefine(final Boolean ignoreDefine);
}
