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
import static net.kosto.util.Error.MISSING_THREE_ATTRIBUTES;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.DATABASE;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.POSTGRESQL_OBJECTS;
import static net.kosto.util.StringUtils.POSTGRESQL_SCHEMES;
import static net.kosto.util.StringUtils.POSTGRESQL_SCRIPTS;
import static net.kosto.util.StringUtils.SCHEMA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.model.AbstractDatabaseItem;
import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.common.CommonDatabase;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.configuration.model.common.CommonItem;
import net.kosto.configuration.model.common.CommonSchema;

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
   * PostgreSQL database schemes' configurations.
   */
  private List<DatabaseItem> schemes;

  /**
   * Constructs instance and sets default values.
   */
  public PostgreSQLDatabase() {
    super();
  }

  /**
   * Constructs instance and sets default values.
   *
   * @param item Common database item.
   */
  public PostgreSQLDatabase(final CommonDatabaseItem item) {
    super(item);

    List<CommonItem> commonObjects = ((CommonDatabase) item).getObjects();
    List<CommonItem> commonScripts = ((CommonDatabase) item).getScripts();
    List<CommonSchema> commonSchemes = ((CommonDatabase) item).getSchemes();

    if (commonSchemes != null) {
      schemes = new ArrayList<>();
      for (CommonDatabaseItem schema : commonSchemes) {
        schemes.add(new PostgreSQLSchema(schema));
      }
    }

    if (commonObjects != null || commonScripts != null) {
      if (schemes == null) {
        schemes = new ArrayList<>();
      }

      final PostgreSQLSchema schema = new PostgreSQLSchema();
      schema.setName("public");
      schema.setSourceDirectory(getSourceDirectory());
      schema.setIgnoreDirectory(TRUE);
      schema.setExecuteDirectory(getExecuteDirectory());
      schema.setSourceDirectoryFull(getSourceDirectoryFull());
      schema.setOutputDirectoryFull(getOutputDirectoryFull());

      if (commonObjects != null) {
        List<DatabaseItem> objects = new ArrayList<>();
        for (CommonDatabaseItem object : commonObjects) {
          objects.add(new PostgreSQLObject(object));
        }
        schema.setObjects(objects);
      }

      if (commonScripts != null) {
        List<DatabaseItem> scripts = new ArrayList<>();
        for (CommonDatabaseItem script : commonScripts) {
          scripts.add(new PostgreSQLScript(script));
        }
        schema.setScripts(scripts);
      }

      schemes.add(0, schema);
    }
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
        ", schemes=" + schemes +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (schemes == null) {
      throw new MojoExecutionException(MISSING_THREE_ATTRIBUTES.message(POSTGRESQL_OBJECTS, POSTGRESQL_SCRIPTS, POSTGRESQL_SCHEMES));
    }
    checkMandatory(schemes, POSTGRESQL_SCHEMES, SCHEMA);
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
    if (schemes != null) {
      schemes.sort(Comparator.comparingInt(DatabaseItem::getOrder));

      for (final DatabaseItem schema : schemes) {
        validateAttribute(schema);
      }
    }
  }
}
