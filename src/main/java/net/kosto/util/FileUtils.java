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

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Contains support constants and methods to work with files.
 */
public final class FileUtils {

  private static final String FAILED_CREATE_DIRECTORY = "Failed to create a directory.";
  private static final String FAILED_LIST_FILES = "Failed to get list of files.";
  private static final String FAILED_CALCULATE_CHECKSUM = "Failed to calculate checksum for file.";
  private static final String FAILED_READ_FILE = "Failed to read file.";
  private static final String FAILED_WRITE_FILE = "Failed to write file.";

  /**
   * Not null empty string.
   */
  public static final String EMPTY_STRING = "";
  /**
   * {@code UTF-8 BOM} (Byte Order Mask) symbol.
   *
   * @see <a href="https://en.wikipedia.org/wiki/Byte_order_mark">Byte Order Mask</a>
   */
  public static final String UTF8_BOM = "\uFEFF";
  /**
   * Unix style directory separator.
   *
   * @see <a href="https://en.wikipedia.org/wiki/Path_(computing)">Path (Computing)</a>
   */
  public static final String UNIX_SEPARATOR = "/";
  /**
   * Default file mask for files with {@code SQL} (Structured Query Language).
   *
   * @see <a href="https://en.wikipedia.org/wiki/SQL">Structured Query Language</a>
   */
  public static final String FILE_MASK_SQL = "*.sql";
  /**
   * {@code MD5} message-digest algorithm.
   *
   * @see <a href="https://en.wikipedia.org/wiki/MD5">MD5</a>
   */
  public static final String MD5 = "MD5";

  private FileUtils() {
  }

  /**
   * Creates base directory with all additional subdirectories within
   * and returns full path to it.
   *
   * @param baseDirectory Full path to base directory.
   * @param directories   Relative paths to additional subdirectories.
   * @return Full path to created directory.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static Path createDirectories(final String baseDirectory, final String... directories) throws MojoExecutionException {
    final Path result = Paths.get(baseDirectory, directories);

    if (!result.toFile().exists()) {
      try {
        Files.createDirectories(result);
      } catch (IOException x) {
        throw new MojoExecutionException(FAILED_CREATE_DIRECTORY, x);
      }
    }

    return result;
  }

  /**
   * Returns full paths to files in base directory matched with file mask.
   *
   * @param baseDirectory Full path fo base directory for files look up.
   * @param fileMask      File mask for files look up.
   * @return Full paths to files.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static List<Path> getFiles(final Path baseDirectory, final String fileMask) throws MojoExecutionException {
    final List<Path> result = new ArrayList<>();

    if (baseDirectory.toFile().exists()) {
      try (
          DirectoryStream<Path> directoryStream = Files.newDirectoryStream(baseDirectory, fileMask)
      ) {
        for (final Path path : directoryStream) {
          result.add(path);
        }
      } catch (IOException x) {
        throw new MojoExecutionException(FAILED_LIST_FILES, x);
      }
    }

    return result;
  }

  /**
   * Returns full file names in base directory matched with file mask.
   *
   * @param baseDirectory Full path to base directory for files look up.
   * @param fileMask      File mask for files look up.
   * @return Full file names.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static List<String> getFileNames(final Path baseDirectory, final String fileMask) throws MojoExecutionException {
    final List<Path> files = getFiles(baseDirectory, fileMask);

    return files.stream().map(Path::getFileName).map(Path::toString).sorted(String::compareTo).collect(Collectors.toList());
  }

  /**
   * Returns {@code MD5} checksum for file.
   *
   * @param file Full file name.
   * @return {@code MD5} checksum.
   * @throws MojoExecutionException If expected exception occurs.
   * @see <a href="https://en.wikipedia.org/wiki/MD5">MD5</a>
   */
  private static String getFileChecksum(final Path file) throws MojoExecutionException {
    String result;

    try {
      final byte[] bytes = Files.readAllBytes(file);
      final byte[] hash = MessageDigest.getInstance(MD5).digest(bytes);
      result = DatatypeConverter.printHexBinary(hash);
    } catch (IOException | NoSuchAlgorithmException x) {
      throw new MojoExecutionException(FAILED_CALCULATE_CHECKSUM, x);
    }

    return result;
  }

  /**
   * Returns full file names and calculates their {@value MD5} checksums
   * in base directory matched with file mask.
   *
   * @param baseDirectory Full path to base directory for files look up.
   * @param fileMask      File mask for files look up.
   * @return Map where key is full path to file, and value - file checksum.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static Map<String, String> getFileNamesWithCheckSum(final Path baseDirectory, final String fileMask) throws MojoExecutionException {
    // TreeMap is in use to make map sorting possible.
    final Map<String, String> result = new TreeMap<>();

    final List<String> files = getFileNames(baseDirectory, fileMask);
    for (final String file : files) {
      result.put(file, getFileChecksum(baseDirectory.resolve(file)));
    }

    return result;
  }

  /**
   * Reads source code lines from file.
   *
   * @param file Full path to source file.
   * @return Source code lines.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static List<String> readFileSourceCode(final Path file) throws MojoExecutionException {
    List<String> result;

    try {
      result = Files.readAllLines(file);
    } catch (IOException x) {
      try {
        // Fallbacks from UTF-8 to ISO-8859-1 charset
        result = Files.readAllLines(file, ISO_8859_1);
      } catch (IOException xx) {
        throw (MojoExecutionException) new MojoExecutionException(FAILED_READ_FILE, xx).initCause(x);
      }
    }

    return result;
  }

  /**
   * Writes source code lines into file.
   *
   * @param file  Full path to target file.
   * @param lines Source code lines.
   * @throws MojoExecutionException If expected exception occurs.
   */
  public static void writeFileSourceCode(final Path file, final List<String> lines) throws MojoExecutionException {
    try (
        OutputStream os = Files.newOutputStream(file, CREATE, WRITE, APPEND);
        OutputStreamWriter osw = new OutputStreamWriter(os, UTF_8)
    ) {
      if (lines != null && !lines.isEmpty()) {
        for (int i = 0; i < lines.size(); i++) {
          osw.write(postProcessSourceCodeLine(lines.get(i), i == 0));
        }
      }
      osw.flush();
    } catch (IOException x) {
      throw new MojoExecutionException(FAILED_WRITE_FILE, x);
    }
  }

  /**
   * Performs source code post processing.
   * <li>
   * Removes {@code UTF-8 BOM} symbol from source code line
   * if {@code firstLine} condition is {@code true}.
   * </li>
   *
   * @param line      Source code line.
   * @param firstLine First line condition.
   * @return Source code line without UTF-8 BOM symbol.
   * @see <a href="https://en.wikipedia.org/wiki/Byte_order_mark">Byte Order Mask</a>
   */
  private static String postProcessSourceCodeLine(final String line, final boolean firstLine) {
    String result = line + lineSeparator();

    // Removes UTF-8 BOM symbol
    if (firstLine && result.contains(UTF8_BOM)) {
      result = result.replace(UTF8_BOM, EMPTY_STRING);
    }

    return result;
  }
}
