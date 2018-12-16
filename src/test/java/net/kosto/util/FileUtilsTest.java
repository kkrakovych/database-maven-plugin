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

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileUtilsTest {

  private static final Path DIRECTORY = Paths.get("unit-tests");
  private static final String[] DIRECTORIES = {"sub-directory-1", "sub-directory-2"};

  /**
   * Path to directory a test work with.
   */
  private Path path;
  /**
   * Path to file a test work with.
   */
  private Path file;

  /**
   * Clean ups directories after test execution.
   *
   * @throws IOException In case any {@link IOException} took place.
   */
  void cleanUpDirectories() throws IOException {
    Path index = path;
    while (index != null) {
      Files.delete(index);
      index = index.getParent();
    }
  }

  /**
   * Clean up file after test execution.
   * File will be deleted in case it exists.
   *
   * @throws IOException In case any {@link IOException} took place.
   */
  void cleanUpFile() throws IOException {
    if (file != null && !Files.isDirectory(file) && Files.exists(file)) {
      Files.delete(file);
    }
  }

  @Test
  @DisplayName("Create single directory.")
  void test01() throws MojoExecutionException, IOException {
    path = FileUtils.createDirectories(DIRECTORY);
    assertTrue(path.toFile().exists());

    cleanUpDirectories();
    assertFalse(path.toFile().exists());
  }

  @Test
  @DisplayName("Create multiply directories.")
  void test02() throws MojoExecutionException, IOException {
    path = FileUtils.createDirectories(DIRECTORY, DIRECTORIES);
    assertTrue(path.toFile().exists());

    cleanUpDirectories();
    assertFalse(path.toFile().exists());
  }

  @Test
  @DisplayName("File Checksum.")
  void test03() throws IOException, MojoExecutionException {
    List<String> data = Arrays.asList("Some text", "Additional text", "One more time");
    file = Paths.get("test-file-md5-checksum");
    Files.write(file, data, StandardCharsets.UTF_8, CREATE, WRITE, APPEND);

    String expected = "7B1407FE7D0C0070EC360953F0AE3BF1";
    String actual = FileUtils.getFileChecksum(file);
    Assertions.assertEquals(expected, actual);

    cleanUpFile();
  }
}
