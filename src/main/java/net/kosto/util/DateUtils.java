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

import java.time.format.DateTimeFormatter;

/**
 * Contains support constants and methods to work with dates.
 */
public final class DateUtils {

  /**
   * Standard date time mask.
   */
  private static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
  /**
   * Seamless date time mask without separators.
   */
  private static final String DATE_TIME_SEAMLESS = "yyyyMMddHHmmss";

  /**
   * Date time formatter for standard mask {@value DATE_TIME}.
   */
  public static final DateTimeFormatter DTF_DATE_TIME = DateTimeFormatter.ofPattern(DATE_TIME);
  /**
   * Date time formatter for seamless date time mask {@value DATE_TIME_SEAMLESS}.
   */
  public static final DateTimeFormatter DTF_DATE_TIME_SEAMLESS = DateTimeFormatter.ofPattern(DATE_TIME_SEAMLESS);

  private DateUtils() {
  }
}
