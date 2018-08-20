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
 * Represents database script.
 * <p>
 * Provides access to database script's attributes and methods.
 */
public abstract class AbstractDatabaseScript extends AbstractDatabaseObject {

  /**
   * Database script's type.
   */
  private DatabaseScriptType type;
  /**
   * Database script's condition.
   */
  private DatabaseScriptCondition condition;

  /**
   * Constructs instance and sets default values.
   */
  public AbstractDatabaseScript() {
    super();
  }

  public DatabaseScriptType getType() {
    return type;
  }

  public void setType(final DatabaseScriptType type) {
    this.type = type;
  }

  public DatabaseScriptCondition getCondition() {
    return condition;
  }

  public void setCondition(final DatabaseScriptCondition condition) {
    this.condition = condition;
  }

  @Override
  public String toString() {
    return "AbstractDatabaseScript{" +
        "type=" + type +
        ", condition=" + condition +
        "} " + super.toString();
  }
}
