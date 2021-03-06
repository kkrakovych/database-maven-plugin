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

package net.kosto.service.validator;

import static net.kosto.util.Error.UNKNOWN_DATABASE_TYPE;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.Configuration;

/**
 * Supports creation of configuration validator dependent on database type.
 */
public final class ValidatorFactory {

  private ValidatorFactory() {
  }

  /**
   * Creates configuration validator dependent on database type.
   *
   * @param configuration Database configuration.
   * @return Configuration validator.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static Validator getProcessor(final Configuration configuration) throws MojoExecutionException {
    Validator result;

    switch (configuration.getDatabaseType()) {
      case CLICKHOUSE:
        result = new ClickHouseValidator(configuration);
        break;
      case ORACLE:
        result = new OracleValidator(configuration);
        break;
      case POSTGRESQL:
        result = new PostgreSQLValidator(configuration);
        break;
      default:
        throw new MojoExecutionException(UNKNOWN_DATABASE_TYPE.message());
    }

    return result;
  }
}
