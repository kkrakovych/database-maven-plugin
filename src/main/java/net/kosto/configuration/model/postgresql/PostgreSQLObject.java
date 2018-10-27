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
import static net.kosto.service.validator.ValidatorError.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_OBJECT_TYPE;

import net.kosto.configuration.model.AbstractDatabaseObject;
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
public class PostgreSQLObject extends AbstractDatabaseObject {

  /**
   * PostgreSQL database schema object's type.
   */
  private PostgreSQLObjectType type;

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLObject() {
    super();
  }

  public PostgreSQLObjectType getType() {
    return type;
  }

  public void setType(final PostgreSQLObjectType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "PostgreSQLObject{" +
        "type=" + type +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (type == null) {
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
    if (type != null && !getIgnoreDirectory() && (getSourceDirectory() == null || getSourceDirectory().isEmpty())) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : type.getSourceDirectory());
    }
  }
}
