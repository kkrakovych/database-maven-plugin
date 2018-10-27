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

package net.kosto.service.processor;

import java.text.MessageFormat;

/**
 * Represents all possible processing errors with associated messages.
 */
public enum ProcessorError {

  UNKNOWN_DATABASE_TYPE("Unknown database type.");

  /**
   * Message format.
   */
  private final MessageFormat messageFormat;

  /**
   * Constructs instance and sets default values.
   *
   * @param message Message.
   */
  ProcessorError(final String message) {
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
