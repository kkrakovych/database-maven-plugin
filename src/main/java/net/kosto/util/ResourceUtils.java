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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Contains support constants and methods to work with resource files.
 */
public final class ResourceUtils {

  private static final String FAILED_LIST_FILES = "Failed to get list of resource files.";

  private ResourceUtils() {
  }

  /**
   * Returns full paths to files in directory matched with file mask.
   *
   * @param fileMask      File mask for files look up.
   * @param baseDirectory Full path to base directory.
   * @param directories   Relative paths to additional subdirectories.
   * @return Full paths to files.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static List<Path> getFiles(final String fileMask, final String baseDirectory, final String... directories) throws MojoExecutionException {
    final List<Path> result = new ArrayList<>();

    final CodeSource source = ResourceUtils.class.getProtectionDomain().getCodeSource();

    if (source != null) {
      try (
          InputStream is = source.getLocation().openStream();
          ZipInputStream zip = new ZipInputStream(is)
      ) {
        final Path directory = Paths.get(baseDirectory, directories);
        final String pattern = fileMask
            .replace("?", ".?")
            .replace("*", ".*?");

        ZipEntry ze;
        while ((ze = zip.getNextEntry()) != null) {
          if (!ze.getName().contains(baseDirectory) || !ze.getName().matches(pattern)) {
            continue;
          }
          final Path path = Paths.get(ze.getName());
          final Path parent = path.getParent();
          final Path file = path.getFileName();
          if (file != null && parent != null && parent.equals(directory) && file.toString().matches(pattern)) {
            result.add(path);
          }
        }
      } catch (IOException x) {
        throw new MojoExecutionException(FAILED_LIST_FILES, x);
      }
    }

    return result;
  }
}
