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
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.EMPTY_STRING;

import java.util.Comparator;
import java.util.List;

import net.kosto.configuration.model.AbstractDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents Oracle database schema configuration.
 * <p>
 * Default values for missing attributes' values:
 * <li>{@link OracleSchema#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleSchema#sourceDirectory} = {@link OracleSchema#name}</li>
 * <li>{@link OracleSchema#defineSymbol} = {@link net.kosto.util.StringUtils#AMPERSAND}</li>
 * <li>{@link OracleSchema#ignoreDefine} = {@link Boolean#FALSE}</li>
 */
public class OracleSchema extends AbstractDatabaseItem {

  /**
   * Oracle database schema objects' configuration.
   */
  private List<OracleObject> objects;
  /**
   * Oracle database schem scripts' configuration.
   */
  private List<OracleScript> scripts;

  /**
   * Constructs instance and sets default values.
   */
  public OracleSchema() {
    super();
  }

  public List<OracleObject> getObjects() {
    return objects;
  }

  public void setObjects(final List<OracleObject> objects) {
    this.objects = objects;
  }

  public List<OracleScript> getScripts() {
    return scripts;
  }

  public void setScripts(final List<OracleScript> scripts) {
    this.scripts = scripts;
  }

  @Override
  public String toString() {
    return "OracleSchema{" +
        "objects=" + objects +
        ", scripts=" + scripts +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (getIndex() == null) {
      throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.index"));
    }
    if (getName() == null) {
      throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("oracle.schema.name"));
    }
    if (objects != null && objects.isEmpty()) {
      throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schema.objects", "object"));
    }
    if (scripts != null && scripts.isEmpty()) {
      throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("oracle.schema.scripts", "script"));
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
    if ((getSourceDirectory() == null || getSourceDirectory().isEmpty()) && !getIgnoreDirectory()) {
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getName());
    }
  }

  @Override
  protected void processAttributes() throws MojoExecutionException {
    if (objects != null) {
      objects
          .sort(
              Comparator
                  .comparingInt(OracleObject::getIndex)
                  .thenComparing(OracleObject::getType)
          );

      for (final OracleObject object : objects) {
        validateAttribute(object);
      }
    }

    if (scripts != null) {
      scripts
          .sort(
              Comparator
                  .comparing(OracleScript::getCondition, Comparator.reverseOrder())
                  .thenComparingInt(OracleScript::getIndex)
          );

      for (final OracleScript script : scripts) {
        validateAttribute(script);
      }
    }
  }
}
