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

package net.kosto.configuration.model.common;

import java.util.List;

/**
 * Represents common database schema configuration.
 */
public class CommonSchema extends AbstractCommonDatabaseItem {

  /**
   * Database schema objects' configuration.
   */
  private List<CommonItem> objects;
  /**
   * Database schema scripts' configuration.
   */
  private List<CommonItem> scripts;

  /**
   * Constructs instance and sets default values.
   */
  public CommonSchema() {
    super();
  }

  public List<CommonItem> getObjects() {
    return objects;
  }

  public void setObjects(List<CommonItem> objects) {
    this.objects = objects;
  }

  public List<CommonItem> getScripts() {
    return scripts;
  }

  public void setScripts(List<CommonItem> scripts) {
    this.scripts = scripts;
  }

  @Override
  public String toString() {
    return "CommonSchema{" +
        "objects=" + objects +
        ", scripts=" + scripts +
        "} " + super.toString();
  }
}
