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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class FileUtilsTest {

  private static final Path DIRECTORY = Paths.get("unit-tests");
  private static final String[] DIRECTORIES = {"sub-directory-1", "sub-directory-2"};

  private void cleanUpDirectories(final Path path) throws IOException {
    Path index = path;
    while (index != null) {
      Files.delete(index);
      index = index.getParent();
    }
  }

  @Test
  public void createDirectories01SingleDirectory() throws MojoExecutionException, IOException {
    final Path result = FileUtils.createDirectories(DIRECTORY);
    assertTrue(result.toFile().exists());

    cleanUpDirectories(result);
    assertFalse(result.toFile().exists());
  }

  @Test
  public void createDirectories02MultiplyDirectories() throws MojoExecutionException, IOException {
    final Path result = FileUtils.createDirectories(DIRECTORY, DIRECTORIES);
    assertTrue(result.toFile().exists());

    cleanUpDirectories(result);
    assertFalse(result.toFile().exists());
  }
}