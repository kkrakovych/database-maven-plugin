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

package net.kosto.configuration;

import java.text.MessageFormat;

/**
 * {@code ValidateError} represents all possible validation errors with associated messages.
 */
public enum ValidateError {
    MISSING_PARAMETER("Parameter \"{0}\" should be specified."),
    EMPTY_LIST_PARAMETER("Parameter \"{0}\" should contain at least one \"{1}\".");

    /** Validate error message. */
    private final String message;

    /**
     * Constructs {@code ValidateError} with associated message.
     *
     * @param message Message.
     */
    ValidateError(String message) {
        this.message = message;
    }

    /**
     * Returns formatted error validation message.
     *
     * @param parameters Optional parameters for formatted message.
     * @return Formatted message.
     */
    public String getFormattedMessage(String... parameters) {
        MessageFormat mf = new MessageFormat(message);
        return mf.format(parameters);
    }
}
