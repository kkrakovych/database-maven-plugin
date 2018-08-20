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
 * Provides list of all supported databases.
 */
public enum DatabaseType {

  /**
   * Oracle database
   *
   * @see <a href="https://en.wikipedia.org/wiki/Oracle_Database">Oracle Database</a>
   */
  ORACLE,
  /**
   * PostgreSQL database
   *
   * @see <a href="https://en.wikipedia.org/wiki/PostgreSQL">PostgreSQL Database</a>
   */
  POSTGRESQL
}
