/*
 * Copyright 2019 Kostyantyn Krakovych
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

package net.kosto.configuration.model.clickhouse;

import static java.lang.Boolean.FALSE;
import static net.kosto.util.Error.MISSING_TWO_ATTRIBUTES;
import static net.kosto.util.StringUtils.CLICKHOUSE_SCHEMA_OBJECTS;
import static net.kosto.util.StringUtils.CLICKHOUSE_SCHEMA_SCRIPTS;
import static net.kosto.util.StringUtils.COLON;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.OBJECT;
import static net.kosto.util.StringUtils.SCHEMA;
import static net.kosto.util.StringUtils.SCRIPT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

import net.kosto.configuration.model.AbstractDatabaseItem;
import net.kosto.configuration.model.DatabaseItem;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.configuration.model.common.CommonItem;
import net.kosto.configuration.model.common.CommonSchema;

/**
 * Represents ClickHouse database schema configuration.
 * <p>
 * TODO: Review the comment.
 * Default values for missing attributes' values:
 * <ul>
 * <li>...</li>
 * <li>...</li>
 * </ul>
 */
public class ClickHouseSchema extends AbstractDatabaseItem {

  /**
   * ClickHouse database schema objects' configuration.
   */
  private List<DatabaseItem> objects;
  /**
   * ClickHouse database schema scripts' configuration.
   */
  private List<DatabaseItem> scripts;

  /**
   * Constructs instance and sets default values.
   */
  public ClickHouseSchema() {
    super();
  }

  /**
   * Constructs instance and sets default values.
   *
   * @param item Common database item.
   */
  public ClickHouseSchema(final CommonDatabaseItem item) {
    super(item);

    List<CommonItem> commonObjects = ((CommonSchema) item).getObjects();
    if (commonObjects != null && !commonObjects.isEmpty()) {
      objects = new ArrayList<>();
      for (CommonDatabaseItem object : commonObjects) {
        objects.add(new ClickHouseObject(object));
      }
    }

    List<CommonItem> commonScripts = ((CommonSchema) item).getScripts();
    if (commonScripts != null && !commonScripts.isEmpty()) {
      scripts = new ArrayList<>();
      for (CommonDatabaseItem script : commonScripts) {
        scripts.add(new ClickHouseScript(script));
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

  @Override
  public String toString() {
    return "ClickHouseSchema{" +
        "objects=" + objects +
        ", scripts=" + scripts +
        "} " + super.toString();
  }

  @Override
  protected void checkMandatoryValues() throws MojoExecutionException {
    if (objects == null && scripts == null) {
      throw new MojoExecutionException(MISSING_TWO_ATTRIBUTES.message(CLICKHOUSE_SCHEMA_OBJECTS, CLICKHOUSE_SCHEMA_SCRIPTS));
    }
    checkMandatory(objects, CLICKHOUSE_SCHEMA_OBJECTS, OBJECT);
    checkMandatory(scripts, CLICKHOUSE_SCHEMA_SCRIPTS, SCRIPT);
  }

  @Override
  protected void setDefaultValues() {
    if (getName() == null) {
      setName(SCHEMA);
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
  }
}
