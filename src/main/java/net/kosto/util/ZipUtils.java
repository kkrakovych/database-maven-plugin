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
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.zip.Deflater.BEST_COMPRESSION;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Contains support constants and methods to work with zip files.
 */
public final class ZipUtils {

  private static final String FAILED_CREATE_FILE = "Failed to create zip file.";

  /**
   * Default buffer size for input / output operations with data.
   */
  private static final int BUFFER_SIZE = 1024;

  private ZipUtils() {
  }

  /**
   * Compresses files located in directory into output zip file.
   *
   * @param zipFile       Full path to output zip file.
   * @param baseDirectory Full path to base directory with files for compressing.
   * @param files         Relative file paths to files for compressing.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static void compress(final Path zipFile, final Path baseDirectory, final List<String> files) throws MojoExecutionException {
    try (
        OutputStream os = Files.newOutputStream(zipFile, CREATE, WRITE, APPEND);
        ZipOutputStream zos = new ZipOutputStream(os)
    ) {
      final byte[] buffer = new byte[BUFFER_SIZE];
      zos.setLevel(BEST_COMPRESSION);
      for (final String file : files) {
        final ZipEntry ze = new ZipEntry(file);
        zos.putNextEntry(ze);
        try (
            InputStream is = Files.newInputStream(baseDirectory.resolve(file), READ)
        ) {
          int length;
          while ((length = is.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
          }
        }
      }
      zos.closeEntry();
    } catch (IOException x) {
      throw new MojoExecutionException(FAILED_CREATE_FILE, x);
    }
  }
}
