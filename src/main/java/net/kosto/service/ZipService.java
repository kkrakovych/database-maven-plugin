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

package net.kosto.service;

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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Controls zip file creation.
 */
public class ZipService {

  private static final String FAILED_CREATE_FILE = "Failed to create zip file.";

  /**
   * Default buffer size for input / output operations with data.
   */
  private static final int BUFFER_SIZE = 1024;

  /**
   * Zip file name.
   */
  private final String name;
  /**
   * Full path to base directory with files for compressing.
   */
  private final Path directory;
  /**
   * Relative paths to files for compression.
   */
  private final List<Path> files;

  /**
   * Constructs instance and sets default values.
   *
   * @param name      Zip file name.
   * @param directory Full path to base directory with files for compressing.
   */
  public ZipService(final String name, final Path directory) {
    super();
    this.name = name;
    this.directory = directory;
    this.files = new ArrayList<>();
  }

  @Override
  public String toString() {
    return "ZipService{" +
        "name='" + name + '\'' +
        ", directory=" + directory +
        ", files=" + files +
        '}';
  }

  /**
   * Adds file to list for further zip compression.
   *
   * @param file Full path to file.
   */
  public void add(final Path file) {
    files.add(directory.relativize(file));
  }

  /**
   * Compresses files into output zip file.
   *
   * @throws MojoExecutionException If expected exception occurs.
   */
  public void compress() throws MojoExecutionException {
    try (
        OutputStream os = Files.newOutputStream(directory.resolve(name), CREATE, WRITE, APPEND);
        ZipOutputStream zos = new ZipOutputStream(os)
    ) {
      final byte[] buffer = new byte[BUFFER_SIZE];
      zos.setLevel(BEST_COMPRESSION);
      for (final Path file : files) {
        final ZipEntry ze = new ZipEntry(file.toString());
        zos.putNextEntry(ze);
        try (
            InputStream is = Files.newInputStream(directory.resolve(file), READ)
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
