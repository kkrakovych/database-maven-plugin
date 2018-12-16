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
import static net.kosto.util.Error.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.ORACLE_SCHEMA_OBJECT_TYPE;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.model.AbstractDatabaseItem;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.util.StringUtils;

/**
 * Represents Oracle database schema's object configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link OracleObject#getSourceDirectory()} = {@link OracleObjectType#getSourceDirectory()}</li>
 * <li>{@link OracleObject#getIgnoreDirectory()} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleObject#getDefineSymbol()} = {@link net.kosto.util.StringUtils#AMPERSAND}</li>
 * <li>{@link OracleObject#getIgnoreDefine()} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleObject#getFileMask()} = {@link net.kosto.util.FileUtils#FILE_MASK_SQL}</li>
 * </ul>
 */
public class OracleObject extends AbstractDatabaseItem {

  /**
   * Constructs instance and sets default values.
   */
  public OracleObject() {
    super();
  }

  /**
   * Constructs instance and sets default values.
   *
   * @param item Common database item.
   */
  public OracleObject(final CommonDatabaseItem item) {
    super(item);
  }

  /**
   * Returns database object type.
   *
   * @return Database object type.
   */
  public OracleObjectType getObjectType() {
    return StringUtils.getEnumElement(OracleObjectType.class, getType());
  }

  @Override
  public String toString() {
    return "OracleObject{} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (getObjectType() == null) {
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
    if (getObjectType() != null && !getIgnoreDirectory() && (getSourceDirectory() == null || getSourceDirectory().isEmpty())) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getObjectType().getSourceDirectory());
    }
  }
}
