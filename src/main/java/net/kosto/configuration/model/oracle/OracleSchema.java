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
import static net.kosto.util.Error.MISSING_ATTRIBUTES;
import static net.kosto.util.StringUtils.AMPERSAND;
import static net.kosto.util.StringUtils.EMPTY_STRING;
import static net.kosto.util.StringUtils.ORACLE_SCHEMA_OBJECTS;
import static net.kosto.util.StringUtils.ORACLE_SCHEMA_SCRIPTS;
import static net.kosto.util.StringUtils.SCHEMA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.kosto.configuration.model.AbstractCustomDatabaseItem;
import net.kosto.configuration.model.CustomDatabaseItem;
import net.kosto.configuration.model.common.CommonDatabaseItem;
import net.kosto.configuration.model.common.CommonItem;
import net.kosto.configuration.model.common.CommonSchema;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Represents Oracle database schema configuration.
 * <p>
 * Default values for missing attributes' values:
 * <ul>
 * <li>{@link OracleSchema#sourceDirectory} = {@link OracleSchema#name}</li>
 * <li>{@link OracleSchema#ignoreDirectory} = {@link Boolean#FALSE}</li>
 * <li>{@link OracleSchema#defineSymbol} = {@link net.kosto.util.StringUtils#AMPERSAND}</li>
 * <li>{@link OracleSchema#ignoreDefine} = {@link Boolean#FALSE}</li>
 * </ul>
 */
public class OracleSchema extends AbstractCustomDatabaseItem {

  /**
   * Oracle database schema objects' configuration.
   */
  private List<CustomDatabaseItem> objects;
  /**
   * Oracle database schema scripts' configuration.
   */
  private List<CustomDatabaseItem> scripts;

  /**
   * Constructs instance and sets default values.
   */
  public OracleSchema() {
    super();
  }

  public OracleSchema(CommonDatabaseItem item) {
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

    List<CommonItem> commonObjects = ((CommonSchema) item).getObjects();
    if (commonObjects != null && !commonObjects.isEmpty()) {
      objects = new ArrayList<>();
      for (CommonDatabaseItem object : commonObjects) {
        objects.add(new OracleObject(object));
      }
    }

    List<CommonItem> commonScripts = ((CommonSchema) item).getScripts();
    if (commonScripts != null && !commonScripts.isEmpty()) {
      scripts = new ArrayList<>();
      for (CommonDatabaseItem script : commonScripts) {
        scripts.add(new OracleScript(script));
      }
    }
  }

  public List<CustomDatabaseItem> getObjects() {
    return objects;
  }

  public void setObjects(final List<CustomDatabaseItem> objects) {
    this.objects = objects;
  }

  public List<CustomDatabaseItem> getScripts() {
    return scripts;
  }

  public void setScripts(final List<CustomDatabaseItem> scripts) {
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
    if (objects == null && scripts == null) {
      throw new MojoExecutionException(MISSING_ATTRIBUTES.message(ORACLE_SCHEMA_OBJECTS, ORACLE_SCHEMA_SCRIPTS));
    }
    checkMandatory(objects, ORACLE_SCHEMA_OBJECTS);
    checkMandatory(scripts, ORACLE_SCHEMA_SCRIPTS);
  }

  @Override
  protected void setDefaultValues() {
    if (getName() == null) {
      setName(SCHEMA);
    }
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
      objects.sort(Comparator.comparingInt(CustomDatabaseItem::getOrder));

      for (final CustomDatabaseItem object : objects) {
        validateAttribute(object);
      }
    }

    if (scripts != null) {
      scripts.sort(Comparator.comparingInt(CustomDatabaseItem::getOrder));

      for (final CustomDatabaseItem script : scripts) {
        validateAttribute(script);
      }
    }
  }
}
