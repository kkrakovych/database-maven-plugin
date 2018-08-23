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

package net.kosto.configuration.model.postgresql;

/**
 * Provides list of all supported PostgreSQL database object types.
 * <p>
 * Each database object type has default value
 * for relative {@link PostgreSQLObject#sourceDirectory} path.
 */
public enum PostgreSQLObjectType {
  VIEW("views");

  /**
   * Default relative {@link PostgreSQLObject#sourceDirectory} path.
   */
  private final String sourceDirectory;

  /**
   * Constructs instance and sets default values.
   *
   * @param sourceDirectory Default relative path.
   */
  PostgreSQLObjectType(final String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }
}
