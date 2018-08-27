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
import static net.kosto.configuration.ValidateError.MISSING_ATTRIBUTE;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.ORACLE_SCHEMES;

import java.util.Comparator;
import java.util.List;

import net.kosto.configuration.model.AbstractDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents Oracle database configuration.
 * <p>
 * Default values for missing attributes' values:
 * <li>{@link OracleDatabase#sourceDirectory} = {@link OracleDatabase#name}</li>
 * <li>{@link OracleDatabase#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleDatabase#defineSymbol} = {@link net.kosto.util.StringUtils#AMPERSAND}</li>
 * <li>{@link OracleDatabase#ignoreDefine} = {@link Boolean#FALSE}</li>
 */
public class OracleDatabase extends AbstractDatabaseItem {

  /**
   * Oracle database schemes' configurations.
   */
  private List<OracleSchema> schemes;

  /**
   * Constructs instance and sets default values.
   */
  public OracleDatabase() {
    super();
  }

  public List<OracleSchema> getSchemes() {
    return schemes;
  }

  public void setSchemes(final List<OracleSchema> schemes) {
    this.schemes = schemes;
  }

  @Override
  public String toString() {
    return "OracleDatabase{" +
        "schemes=" + schemes +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (schemes == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTE.message(ORACLE_SCHEMES));
    }
    checkMandatory(schemes, ORACLE_SCHEMES);
  }

  @Override
  protected void setDefaultValues() {
    if (getName() == null) {
      setName(DATABASE);
    }
    if (getDefineSymbol() == null) {
      setDefineSymbol(AMPERSAND);
    }
    if (getIgnoreDefine() == null) {
      setIgnoreDefine(FALSE);
    }
    if (getSourceDirectory() == null || getSourceDirectory().isEmpty()) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getName());
    }
  }

  @Override
  protected void processAttributes() throws MojoExecutionException {
    if (schemes != null) {
      schemes.sort(Comparator.comparingInt(OracleSchema::getOrder));

      for (final OracleSchema schema : schemes) {
        validateAttribute(schema);
      }
    }
  }
}
