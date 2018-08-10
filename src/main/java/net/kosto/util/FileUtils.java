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

import org.apache.maven.plugin.MojoExecutionException;

import javax.xml.bind.DatatypeConverter;
import java.io.FileOutputStream;
import java.io.IOException;
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

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtils {

    public static final String EMPTY = "";
    public static final String UTF8_BOM = "\uFEFF";
    public static final String UNIX_SEPARATOR = "/";
    public static final String FILE_MASK_SQL = "*.sql";
    public static final String MD5 = "MD5";

    private FileUtils() {
    }

    public static Path createDirectories(String firstDirectoryPart, String... moreDirectoryParts) throws MojoExecutionException {
        Path result = Paths.get(firstDirectoryPart, moreDirectoryParts);
        if (!result.toFile().exists()) {
            try {
                Files.createDirectories(result);
            } catch (IOException x) {
                throw new MojoExecutionException("Failed to create a directory.", x);
            }
        }
        return result;
    }

    public static List<Path> getFiles(Path sourceDirectory, String fileExtension) throws MojoExecutionException {
        List<Path> files = new ArrayList<>();
        if (sourceDirectory.toFile().exists()) {
            try (
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDirectory, fileExtension)
            ) {
                for (Path path : directoryStream) {
                    files.add(path);
                }
            } catch (IOException x) {
                throw new MojoExecutionException("Failed to get list of files.", x);
            }
        }
        return files;
    }

    public static List<String> getFileNames(Path sourceDirectory, String fileExtension) throws MojoExecutionException {
        List<Path> files = getFiles(sourceDirectory, fileExtension);
        return files.stream().map(Path::getFileName).map(Path::toString).sorted(String::compareTo).collect(Collectors.toList());
    }

    public static String getFileChecksum(Path file) throws MojoExecutionException {
        try {
            byte[] bytes = Files.readAllBytes(file);
            byte[] hash = MessageDigest.getInstance(MD5).digest(bytes);
            return DatatypeConverter.printHexBinary(hash);
        } catch (IOException | NoSuchAlgorithmException x) {
            throw new MojoExecutionException("Failed to calculate checksum for file.", x);
        }
    }

    public static Map<String, String> getFileNamesWithCheckSum(Path sourceDirectory, String fileExtension) throws MojoExecutionException {
        Map<String, String> result = new TreeMap<>();
        List<String> files = getFileNames(sourceDirectory, fileExtension);
        for (String file : files) {
            result.put(file, getFileChecksum(sourceDirectory.resolve(file)));
        }
        return result;
    }

    public static List<String> readFileSourceCode(Path file) throws MojoExecutionException {
        try {
            return Files.readAllLines(file);
        } catch (IOException x) {
            try {
                // fallback from UTF-8 to ISO-8859-1 charset
                return Files.readAllLines(file, ISO_8859_1);
            } catch (IOException xx) {
                throw new MojoExecutionException("Failed to read file.", xx);
            }
        }
    }

    public static void writeFileSourceCode(Path file, List<String> lines) throws MojoExecutionException {
        try (
            FileOutputStream fos = new FileOutputStream(file.toString(), true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, UTF_8)
        ) {
            if (lines != null && !lines.isEmpty())
                for (int i = 0; i < lines.size(); i++)
                    osw.write(postProcessSourceCodeLine(lines.get(i), (i == 0)));
            osw.flush();
        } catch (IOException x) {
            throw new MojoExecutionException("Failed to write file.", x);
        }
    }

    private static String postProcessSourceCodeLine(String line, boolean firstLine) {
        String result = line + lineSeparator();
        // remove UTF-8 BOM symbol at first line if any
        if (firstLine && result.contains(UTF8_BOM)) {
            result = result.replace(UTF8_BOM, EMPTY);
        }
        return result;
    }
}
