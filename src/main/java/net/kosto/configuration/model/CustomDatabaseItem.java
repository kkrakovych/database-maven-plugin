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

import java.nio.file.Path;

import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.service.validator.Validator;

public interface CustomDatabaseItem extends CommonDatabaseItem, Validator {

  /**
   * Returns relative path name for execute directory.
   *
   * @return Relative path name for execute directory.
   */
  String getExecuteDirectory();

  /**
   * Sets relative path name for execute directory.
   *
   * @param executeDirectory Relative path name for execute directory.
   */
  void setExecuteDirectory(final String executeDirectory);

  /**
   * Returns full path to item's source directory.
   *
   * @return Full path to item's source directory.
   */
  Path getSourceDirectoryFull();

  /**
   * Sets full path to item's source directory.
   *
   * @param sourceDirectoryFull Full path to item's source directory.
   */
  void setSourceDirectoryFull(final Path sourceDirectoryFull);

  /**
   * Returns full path to item's output directory.
   *
   * @return Full path to item's output directory.
   */
  Path getOutputDirectoryFull();

  /**
   * Sets full path to item's output directory.
   *
   * @param outputDirectoryFull Full path to item's output directory.
   */
  void setOutputDirectoryFull(final Path outputDirectoryFull);
}
