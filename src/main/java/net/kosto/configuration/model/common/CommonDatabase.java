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
 * Represents common database configuration.
 */
public class CommonDatabase extends AbstractCommonDatabaseItem {

  /**
   * Database public schema objects' configuration.
   */
  private List<CommonItem> objects;
  /**
   * Database public schema scripts' configuration.
   */
  private List<CommonItem> scripts;
  /**
   * Database schemes' configurations.
   */
  private List<CommonSchema> schemes;

  /**
   * Constructs instance and sets default values.
   */
  public CommonDatabase() {
    super();
  }

  public List<CommonItem> getObjects() {
    return objects;
  }

  public void setObjects(final List<CommonItem> objects) {
    this.objects = objects;
  }

  public List<CommonItem> getScripts() {
    return scripts;
  }

  public void setScripts(final List<CommonItem> scripts) {
    this.scripts = scripts;
  }

  public List<CommonSchema> getSchemes() {
    return schemes;
  }

  public void setSchemes(final List<CommonSchema> schemes) {
    this.schemes = schemes;
  }

  @Override
  public String toString() {
    return "CommonDatabase{" +
        "objects=" + objects +
        ", scripts=" + scripts +
        ", schemes=" + schemes +
        "} " + super.toString();
  }
}
