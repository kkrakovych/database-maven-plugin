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

import static net.kosto.util.FileUtils.FILE_MASK_SQL;

/**
 * Represents database object.
 * <p>
 * Provides access to database object's attributes and methods.
 */
public abstract class AbstractDatabaseObject extends AbstractDatabaseItem implements DatabaseObject {

  /**
   * Database object's file mask.
   */
  private String fileMask;

  /**
   * Constructs instance and sets default values.
   */
  public AbstractDatabaseObject() {
    super();
    this.fileMask = FILE_MASK_SQL;
  }

  @Override
  public String getFileMask() {
    return fileMask;
  }

  @Override
  public void setFileMask(final String fileMask) {
    this.fileMask = fileMask;
  }

  @Override
  public String toString() {
    return "AbstractDatabaseObject{" +
        "fileMask='" + fileMask + '\'' +
        "} " + super.toString();
  }
}
