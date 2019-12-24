/*
 * Copyright 2019 Kostyantyn Krakovych
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

package net.kosto.configuration.model.clickhouse;

import static java.lang.Boolean.FALSE;
import static net.kosto.util.Error.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.CLICKHOUSE_SCHEMA_SCRIPT_CONDITION;
import static net.kosto.util.StringUtils.CLICKHOUSE_SCHEMA_SCRIPT_TYPE;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.UNDERSCORE;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.model.AbstractDatabaseItem;
import net.kosto.configuration.model.DatabaseScriptCondition;
import net.kosto.configuration.model.DatabaseScriptType;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.util.StringUtils;

/**
 * Represents ClickHouse database schema's script configuration.
 * <p>
 * TODO: Review the comment.
 * Default values for missing attributes' values:
 * <ul>
 * <li>...</li>
 * <li>...</li>
 * </ul>
 */
public class ClickHouseScript extends AbstractDatabaseItem {

  /**
   * Constructs instance and sets default values.
   */
  public ClickHouseScript() {
    super();
  }

  /**
   * Constructs instance and sets default values.
   *
   * @param item Common database item.
   */
  public ClickHouseScript(final CommonDatabaseItem item) {
    super(item);
  }

  /**
   * Returns database script type.
   *
   * @return Database script type.
   */
  public DatabaseScriptType getScriptType() {
    return StringUtils.getEnumElement(DatabaseScriptType.class, getType());
  }

  /**
   * Returns database script condition.
   *
   * @return Database script condition.
   */
  public DatabaseScriptCondition getScriptCondition() {
    return StringUtils.getEnumElement(DatabaseScriptCondition.class, getCondition());
  }

  @Override
  public Integer getOrder() {
    Integer result = getIndex();

    if (getIndex() != null && getScriptCondition() != null) {
      switch (getScriptCondition()) {
        case BEFORE:
          result = 1000 + getIndex();
          break;
        case AFTER:
          result = 2000 + getIndex();
          break;
        default:
          result = getIndex();
      }
    }

    return result;
  }

  @Override
  public String toString() {
    return "ClickHouseScript{} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (getType() == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(CLICKHOUSE_SCHEMA_SCRIPT_TYPE));
    }
    if (getCondition() == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(CLICKHOUSE_SCHEMA_SCRIPT_CONDITION));
    }
  }

  @Override
  protected void setDefaultValues() {
    if (getDefineSymbol() == null) {
      setDefineSymbol(COLON);
    }
    if (getIgnoreDefine() == null) {
      setIgnoreDefine(FALSE);
    }
    if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory()) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getScriptType().getSourceDirectory() + UNDERSCORE + getScriptCondition().getSourceDirectory());
    }
  }
}
