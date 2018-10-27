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
import static net.kosto.service.validator.ValidateError.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_SCRIPT_CONDITION;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMA_SCRIPT_TYPE;

import net.kosto.configuration.model.AbstractDatabaseScript;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents PostgreSQL database schema's script configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link PostgreSQLScript#sourceDirectory} = {@link net.kosto.configuration.model.DatabaseScriptType#getSourceDirectory()}</li>
 * <li>{@link PostgreSQLScript#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLScript#defineSymbol} = {@link net.kosto.util.StringUtils#COLON}</li>
 * <li>{@link PostgreSQLScript#ignoreDefine} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLScript#fileMask} = {@link net.kosto.util.FileUtils#FILE_MASK_SQL}</li>
 * </ul>
 */
public class PostgreSQLScript extends AbstractDatabaseScript {

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLScript() {
    super();
  }

  @Override
  public String toString() {
    return "PostgreSQLScript{} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (getType() == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(POSTGRESQL_SCHEMA_SCRIPT_TYPE));
    }
    if (getCondition() == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(POSTGRESQL_SCHEMA_SCRIPT_CONDITION));
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
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getType().getSourceDirectory());
    }
  }
}
