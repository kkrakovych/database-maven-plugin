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

package net.kosto.configuration.model.postgresql;

import static java.lang.Boolean.FALSE;
import static net.kosto.util.Error.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_OBJECT_TYPE;

import net.kosto.configuration.model.AbstractCustomDatabaseItem;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents PostgreSQL database schema's object configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link PostgreSQLObject#sourceDirectory} = {@link PostgreSQLObjectType#getSourceDirectory()}</li>
 * <li>{@link PostgreSQLObject#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLObject#defineSymbol} = {@link net.kosto.util.StringUtils#COLON}</li>
 * <li>{@link PostgreSQLObject#ignoreDefine} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLObject#fileMask} = {@link net.kosto.util.FileUtils#FILE_MASK_SQL}</li>
 * </ul>
 */
public class PostgreSQLObject extends AbstractCustomDatabaseItem {

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLObject() {
    super();
  }

  public PostgreSQLObject(CommonDatabaseItem item) {
    super();
    setIndex(item.getIndex());
    setName(item.getName());
    setType(item.getType());
    setCondition(item.getCondition());
    setFileMask(item.getFileMask());
    setSourceDirectory(item.getSourceDirectory());
    setIgnoreDirectory(item.getIgnoreDirectory());
    setDefineSymbol(item.getDefineSymbol());
    setIgnoreDefine(item.getIgnoreDefine());
  }

  /**
   * Returns database object type.
   *
   * @return Database object type.
   */
  public PostgreSQLObjectType getObjectType() {
    PostgreSQLObjectType result = null;

    if (getType() != null) {
      try {
        result = PostgreSQLObjectType.valueOf(getType());
      } catch (IllegalArgumentException x) {
        // If corresponding database object type not found in enum, do nothing.
      }
    }

    return result;
  }

  @Override
  public String toString() {
    return "PostgreSQLObject{} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (getObjectType() == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(POSTGRESQL_SCHEMA_OBJECT_TYPE));
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
    if (getObjectType() != null && !getIgnoreDirectory() && (getSourceDirectory() == null || getSourceDirectory().isEmpty())) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getObjectType().getSourceDirectory());
    }
  }
}
