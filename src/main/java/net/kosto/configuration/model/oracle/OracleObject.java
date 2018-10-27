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

package net.kosto.configuration.model.oracle;

import static java.lang.Boolean.FALSE;
import static net.kosto.service.validator.ValidatorError.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.ORACLE_SCHEMA_OBJECT_TYPE;

import net.kosto.configuration.model.AbstractDatabaseObject;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents Oracle database schema's object configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link OracleObject#sourceDirectory} = {@link OracleObjectType#getSourceDirectory()}</li>
 * <li>{@link OracleObject#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleObject#defineSymbol} = {@link net.kosto.util.StringUtils#AMPERSAND}</li>
 * <li>{@link OracleObject#ignoreDefine} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleObject#fileMask} = {@link net.kosto.util.FileUtils#FILE_MASK_SQL}</li>
 * </ul>
 */
public class OracleObject extends AbstractDatabaseObject {

  /**
   * Oracle database schema object's type.
   */
  private OracleObjectType type;

  /**
   * Constructs instance and sets default values.
   */
  public OracleObject() {
    super();
  }

  public OracleObjectType getType() {
    return type;
  }

  public void setType(final OracleObjectType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "OracleObject{" +
        "type=" + type +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (type == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(ORACLE_SCHEMA_OBJECT_TYPE));
    }
  }

  @Override
  protected void setDefaultValues() {
    if (getDefineSymbol() == null) {
      setDefineSymbol(AMPERSAND);
    }
    if (getIgnoreDefine() == null) {
      setIgnoreDefine(FALSE);
    }
    if (type != null && !getIgnoreDirectory() && (getSourceDirectory() == null || getSourceDirectory().isEmpty())) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : type.getSourceDirectory());
    }
  }
}
