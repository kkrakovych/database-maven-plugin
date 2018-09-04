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
public abstract class AbstractDatabaseScript extends AbstractDatabaseObject implements DatabaseScript {

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

  @Override
  public Integer getOrder() {
    Integer result = 0;

    if (condition != null) {
      switch (condition) {
        case BEFORE:
          result = 1000 + getIndex();
          break;
        case AFTER:
          result = 2000 + getIndex();
          break;
        default:
          result = result + getIndex();
      }
    }

    return result;
  }

  @Override
  public DatabaseScriptType getType() {
    return type;
  }

  @Override
  public void setType(final DatabaseScriptType type) {
    this.type = type;
  }

  @Override
  public DatabaseScriptCondition getCondition() {
    return condition;
  }

  @Override
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
