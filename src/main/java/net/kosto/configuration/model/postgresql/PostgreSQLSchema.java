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
import static net.kosto.configuration.ValidateError.EMPTY_LIST_PARAMETER;
import static net.kosto.configuration.ValidateError.MISSING_PARAMETER;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.EMPTY_STRING;

import java.util.Comparator;
import java.util.List;

import net.kosto.configuration.model.AbstractDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents PostgreSQL database schema configuration.
 * <p>
 * Default values for missing attributes' values:
 * <li>{@link PostgreSQLSchema#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLSchema#sourceDirectory} = {@link PostgreSQLSchema#name}</li>
 * <li>{@link PostgreSQLSchema#defineSymbol} = {@link net.kosto.util.StringUtils#COLON}</li>
 * <li>{@link PostgreSQLSchema#ignoreDefine} = {@link Boolean#FALSE}</li>
 */
public class PostgreSQLSchema extends AbstractDatabaseItem {

  /**
   * PostgreSQL database schema objects' configuration.
   */
  private List<PostgreSQLObject> objects;
  /**
   * PostgreSQL database schema scripts' configuration.
   */
  private List<PostgreSQLScript> scripts;

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLSchema() {
    super();
  }

  public List<PostgreSQLObject> getObjects() {
    return objects;
  }

  public void setObjects(final List<PostgreSQLObject> objects) {
    this.objects = objects;
  }

  public List<PostgreSQLScript> getScripts() {
    return scripts;
  }

  public void setScripts(final List<PostgreSQLScript> scripts) {
    this.scripts = scripts;
  }

  @Override
  public String toString() {
    return "PostgreSQLSchema{" +
        "objects=" + objects +
        ", scripts=" + scripts +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (getIndex() == null) {
      throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.schema.index"));
    }
    if (getName() == null) {
      throw new MojoExecutionException(MISSING_PARAMETER.getFormattedMessage("postgresql.schema.name"));
    }
    if (objects != null && objects.isEmpty()) {
      throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.schema.objects", "object"));
    }
    if (scripts != null && scripts.isEmpty()) {
      throw new MojoExecutionException(EMPTY_LIST_PARAMETER.getFormattedMessage("postgresql.schema.scripts", "script"));
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
      setSourceDirectory(getIgnoreDirectory() ? EMPTY_STRING : getName());
    }
  }

  @Override
  protected void processAttributes() throws MojoExecutionException {
    if (objects != null) {
      objects
          .sort(
              Comparator
                  .comparingInt(PostgreSQLObject::getIndex)
                  .thenComparing(PostgreSQLObject::getType)
          );

      for (final PostgreSQLObject object : objects) {
        validateAttribute(object);
      }
    }

    if (scripts != null) {
      scripts
          .sort(
              Comparator
                  .comparing(PostgreSQLScript::getCondition, Comparator.reverseOrder())
                  .thenComparingInt(PostgreSQLScript::getIndex)
          );

      for (final PostgreSQLScript script : scripts) {
        validateAttribute(script);
      }
    }
  }
}
