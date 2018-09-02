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
import static java.lang.Boolean.TRUE;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.POSTGRESQL_OBJECTS;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMES;
import static net.kosto.util.StringUtils.POSTGRESQL_SCRIPTS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.kosto.configuration.model.AbstractDatabaseItem;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents PostgreSQL database configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link PostgreSQLDatabase#sourceDirectory} = {@link PostgreSQLDatabase#name}</li>
 * <li>{@link PostgreSQLDatabase#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLDatabase#defineSymbol} = {@link net.kosto.util.StringUtils#COLON}</li>
 * <li>{@link PostgreSQLDatabase#ignoreDefine} = {@link Boolean#FALSE}</li>
 * </ul>
 */
public class PostgreSQLDatabase extends AbstractDatabaseItem {

  /**
   * PostgreSQL database public schema objects' configuration.
   */
  private List<PostgreSQLObject> objects;
  /**
   * PostgreSQL database public schema scripts' configuration.
   */
  private List<PostgreSQLScript> scripts;
  /**
   * PostgreSQL database schemes' configurations.
   */
  private List<PostgreSQLSchema> schemes;

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLDatabase() {
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

  public List<PostgreSQLSchema> getSchemes() {
    return schemes;
  }

  public void setSchemes(final List<PostgreSQLSchema> schemes) {
    this.schemes = schemes;
  }

  @Override
  public String toString() {
    return "PostgreSQLDatabase{" +
        "objects=" + objects +
        ", scripts=" + scripts +
        ", schemes=" + schemes +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    checkMandatory(objects, POSTGRESQL_OBJECTS);
    checkMandatory(scripts, POSTGRESQL_SCRIPTS);
    checkMandatory(schemes, POSTGRESQL_SCHEMES);
  }

  @Override
  protected void setDefaultValues() {
    if (getName() == null) {
      setName(DATABASE);
    }
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
      objects.sort(Comparator.comparingInt(PostgreSQLObject::getOrder));

      for (final PostgreSQLObject object : objects) {
        validateAttribute(object);
      }
    }

    if (scripts != null) {
      scripts.sort(Comparator.comparingInt(PostgreSQLScript::getOrder));

      for (final PostgreSQLScript script : scripts) {
        validateAttribute(script);
      }
    }

    if (schemes != null) {
      for (final PostgreSQLSchema schema : schemes) {
        validateAttribute(schema);
      }
    }

    // Public schema
    if (objects != null || scripts != null) {
      if (schemes == null) {
        schemes = new ArrayList<>();
      }

      final PostgreSQLSchema schema = new PostgreSQLSchema();
      schema.setIndex(schemes.stream().mapToInt(PostgreSQLSchema::getIndex).min().orElse(0));
      schema.setName("public");
      schema.setSourceDirectory(getSourceDirectory());
      schema.setIgnoreDirectory(TRUE);
      schema.setExecuteDirectory(getExecuteDirectory());
      schema.setSourceDirectoryFull(getSourceDirectoryFull());
      schema.setOutputDirectoryFull(getOutputDirectoryFull());
      schema.setObjects(objects);
      schema.setScripts(scripts);

      schemes.add(schema);
    }

    if (schemes != null) {
      schemes.sort(Comparator.comparingInt(PostgreSQLSchema::getOrder));
    }
  }
}
