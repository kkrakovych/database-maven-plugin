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

import java.text.MessageFormat;

/**
 * Represents all possible errors with associated messages.
 */
public enum Error {

  UNKNOWN_DATABASE_TYPE("Unknown database type."),
  MISSING_ATTRIBUTE("Attribute \"{0}\" should be specified."),
  MISSING_TWO_ATTRIBUTES("Either \"{0}\" or \"{1}\" attribute should be specified."),
  MISSING_THREE_ATTRIBUTES("Either \"{0}\", or \"{1}\", or \"{2}\" attribute should be specified."),
  EMPTY_LIST_ATTRIBUTE("Attribute \"{0}\" should contain at least one \"{1}\"."),
  SEMI_DEFINED_ATTRIBUTES("Attribute \"{2}\" should be either specified for every \"{1}\" in \"{0}\" or missing."),
  DUPLICATED_ATTRIBUTE("Attribute \"{2}\" should be unique for every \"{1}\" in \"{0}\"."),
  ONE_TIME_SCRIPT_VS_IGNORE_SERVICE_TABLES("ONE_TIME scripts are not allowed if ignore service tables option is true.");

  /**
   * Message format.
   */
  private final MessageFormat messageFormat;

  /**
   * Constructs instance and sets default values.
   *
   * @param message Message.
   */
  Error(final String message) {
    this.messageFormat = new MessageFormat(message);
  }

  /**
   * Returns formatted processing error message.
   *
   * @param parameters Optional parameters for formatted message.
   * @return Formatted message.
   */
  public String message(final String... parameters) {
    return messageFormat.format(parameters);
  }
}
