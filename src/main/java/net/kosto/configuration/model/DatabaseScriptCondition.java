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
 * Provides list of all supported database script execution conditions.
 * <p>
 * Each database script type has default value for relative {@link AbstractDatabaseItem#getSourceDirectory()} path.
 */
public enum DatabaseScriptCondition {

  /**
   * Database script should be executed after source code deploy.
   */
  AFTER("after"),
  /**
   * Database script should be executed before source code deploy.
   */
  BEFORE("before");

  /**
   * Default relative path name for {@link AbstractDatabaseItem#getSourceDirectory()}.
   */
  private final String sourceDirectory;

  /**
   * Constructs instance and sets default values.
   *
   * @param sourceDirectory Default relative path.
   */
  DatabaseScriptCondition(final String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  /**
   * Returns relative path name for source directory.
   *
   * @return Relative path name for source directory.
   */
  public String getSourceDirectory() {
    return sourceDirectory;
  }
}
