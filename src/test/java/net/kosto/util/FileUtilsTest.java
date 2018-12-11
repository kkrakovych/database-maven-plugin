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

package net.kosto.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileUtilsTest {

  private static final Path DIRECTORY = Paths.get("unit-tests");
  private static final String[] DIRECTORIES = {"sub-directory-1", "sub-directory-2"};

  private Path path;

  @AfterEach
  void tearDown() throws IOException {
    cleanUp();
    assertFalse(path.toFile().exists());
  }

  void cleanUp() throws IOException {
    Path index = path;
    while (index != null) {
      Files.delete(index);
      index = index.getParent();
    }
  }

  @Test
  @DisplayName("Create single directory.")
  void test01() throws MojoExecutionException {
    path = FileUtils.createDirectories(DIRECTORY);
    assertTrue(path.toFile().exists());
  }

  @Test
  @DisplayName("Create multiply directories.")
  void test02() throws MojoExecutionException {
    path = FileUtils.createDirectories(DIRECTORY, DIRECTORIES);
    assertTrue(path.toFile().exists());
  }
}
