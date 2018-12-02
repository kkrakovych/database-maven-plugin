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
import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.common.CommonDatabase;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.configuration.model.common.CommonItem;
import net.kosto.configuration.model.common.CommonSchema;
import net.kosto.configuration.model.oracle.OracleObject;
import net.kosto.configuration.model.oracle.OracleSchema;
import net.kosto.configuration.model.oracle.OracleScript;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents PostgreSQL database configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link PostgreSQLDatabase#getSourceDirectory()} = {@link PostgreSQLDatabase#getName()}</li>
 * <li>{@link PostgreSQLDatabase#getIgnoreDirectory()} = {@link Boolean#FALSE}</li>
 * <li>{@link PostgreSQLDatabase#getDefineSymbol()} = {@link net.kosto.util.StringUtils#COLON}</li>
 * <li>{@link PostgreSQLDatabase#getIgnoreDefine()} = {@link Boolean#FALSE}</li>
 * </ul>
 */
public class PostgreSQLDatabase extends AbstractDatabaseItem {

  /**
   * PostgreSQL database public schema objects' configuration.
   */
  private List<DatabaseItem> objects;
  /**
   * PostgreSQL database public schema scripts' configuration.
   */
  private List<DatabaseItem> scripts;
  /**
   * PostgreSQL database schemes' configurations.
   */
  private List<DatabaseItem> schemes;

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLDatabase() {
    super();
  }

  public PostgreSQLDatabase(CommonDatabaseItem item) {
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

    List<CommonItem> commonObjects = ((CommonDatabase) item).getObjects();
    if (commonObjects != null && !commonObjects.isEmpty()) {
      objects = new ArrayList<>();
      for (CommonDatabaseItem object : commonObjects) {
        objects.add(new OracleObject(object));
      }
    }

    List<CommonItem> commonScripts = ((CommonDatabase) item).getScripts();
    if (commonScripts != null && !commonScripts.isEmpty()) {
      scripts = new ArrayList<>();
      for (CommonDatabaseItem script : commonScripts) {
        scripts.add(new OracleScript(script));
      }
    }

    List<CommonSchema> commonSchemes = ((CommonDatabase) item).getSchemes();
    if (commonSchemes != null && !commonSchemes.isEmpty()) {
      schemes = new ArrayList<>();
      for (CommonDatabaseItem schema : commonSchemes) {
        schemes.add(new OracleSchema(schema));
      }
    }
  }

  public List<DatabaseItem> getObjects() {
    return objects;
  }

  public void setObjects(final List<DatabaseItem> objects) {
    this.objects = objects;
  }

  public List<DatabaseItem> getScripts() {
    return scripts;
  }

  public void setScripts(final List<DatabaseItem> scripts) {
    this.scripts = scripts;
  }

  public List<DatabaseItem> getSchemes() {
    return schemes;
  }

  public void setSchemes(final List<DatabaseItem> schemes) {
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
      objects.sort(Comparator.comparingInt(DatabaseItem::getOrder));

      for (final DatabaseItem object : objects) {
        validateAttribute(object);
      }
    }

    if (scripts != null) {
      scripts.sort(Comparator.comparingInt(DatabaseItem::getOrder));

      for (final DatabaseItem script : scripts) {
        validateAttribute(script);
      }
    }

    if (schemes != null) {
      for (final DatabaseItem schema : schemes) {
        validateAttribute(schema);
      }
    }

    // Public schema
    if (objects != null || scripts != null) {
      if (schemes == null) {
        schemes = new ArrayList<>();
      }

      final PostgreSQLSchema schema = new PostgreSQLSchema();
      schema.setIndex(schemes.stream().mapToInt(DatabaseItem::getIndex).min().orElse(0));
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
      schemes.sort(Comparator.comparingInt(DatabaseItem::getOrder));
    }
  }
}
