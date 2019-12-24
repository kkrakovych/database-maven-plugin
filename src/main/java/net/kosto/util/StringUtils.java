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

package net.kosto.util;

/**
 * Contains support constants and methods to work with strings.
 */
public final class StringUtils {

  /**
   * Not null empty string.
   */
  public static final String EMPTY_STRING = "";
  /**
   * Underscore symbol.
   */
  public static final String UNDERSCORE = "_";
  /**
   * Colon symbol.
   */
  public static final String COLON = ":";
  /**
   * Ampersand symbol.
   */
  public static final String AMPERSAND = "&";
  /**
   * Keyword for database.
   */
  public static final String DATABASE = "database";
  /**
   * Keyword for schema.
   */
  public static final String SCHEMA = "schema";
  /**
   * Keyword for object.
   */
  public static final String OBJECT = "object";
  /**
   * Keyword for script.
   */
  public static final String SCRIPT = "script";
  /**
   * Keyword for index.
   */
  public static final String INDEX = "index";
  /**
   * Keyword for common.
   */
  public static final String COMMON = "common";
  /**
   * Keyword for files.
   */
  public static final String FILES = "files";

  /**
   * Keyword for ClickHouse.
   */
  public static final String CLICKHOUSE = "clickhouse";
  public static final String CLICKHOUSE_SCHEMES = "clickhouse.schemes";
  public static final String CLICKHOUSE_OBJECTS = "clickhouse.objects";
  public static final String CLICKHOUSE_SCHEMA_OBJECTS = "clickhouse.schema.objects";
  public static final String CLICKHOUSE_SCHEMA_OBJECT_TYPE = "clickhouse.schema.object.type";
  public static final String CLICKHOUSE_SCRIPTS = "clickhouse.scripts";
  public static final String CLICKHOUSE_SCHEMA_SCRIPTS = "clickhouse.schema.scripts";
  public static final String CLICKHOUSE_SCHEMA_SCRIPT_TYPE = "clickhouse.schema.script.type";
  public static final String CLICKHOUSE_SCHEMA_SCRIPT_CONDITION = "clickhouse.schema.script.condition";

  /**
   * Keyword for Oracle.
   */
  public static final String ORACLE = "oracle";
  public static final String ORACLE_SCHEMES = "oracle.schemes";
  public static final String ORACLE_SCHEMA_OBJECTS = "oracle.schema.objects";
  public static final String ORACLE_SCHEMA_OBJECT_TYPE = "oracle.schema.object.type";
  public static final String ORACLE_SCHEMA_SCRIPTS = "oracle.schema.scripts";
  public static final String ORACLE_SCHEMA_SCRIPT_TYPE = "oracle.schema.script.type";
  public static final String ORACLE_SCHEMA_SCRIPT_CONDITION = "oracle.schema.script.condition";

  /**
   * Keyword for PostgreSQL.
   */
  public static final String POSTGRESQL = "postgresql";
  public static final String POSTGRESQL_SCHEMES = "postgresql.schemes";
  public static final String POSTGRESQL_OBJECTS = "postgresql.objects";
  public static final String POSTGRESQL_SCHEMA_OBJECTS = "postgresql.schema.objects";
  public static final String POSTGRESQL_SCHEMA_OBJECT_TYPE = "postgresql.schema.object.type";
  public static final String POSTGRESQL_SCRIPTS = "postgresql.scripts";
  public static final String POSTGRESQL_SCHEMA_SCRIPTS = "postgresql.schema.scripts";
  public static final String POSTGRESQL_SCHEMA_SCRIPT_TYPE = "postgresql.schema.script.type";
  public static final String POSTGRESQL_SCHEMA_SCRIPT_CONDITION = "postgresql.schema.script.condition";

  private StringUtils() {
  }

  /**
   * Returns enum's element matched with specified value.
   *
   * @param enumClass Enum's class.
   * @param value     Value for matching with enum's element.
   * @param <T>       Enum.
   * @return Enum's element.
   */
  public static <T extends Enum<T>> T getEnumElement(final Class<T> enumClass, final String value) {
    T result = null;

    if (value != null) {
      try {
        result = Enum.valueOf(enumClass, value);
      } catch (IllegalArgumentException x) {
        // If corresponding enum's element not found in enum, do nothing.
      }
    }

    return result;
  }
}
