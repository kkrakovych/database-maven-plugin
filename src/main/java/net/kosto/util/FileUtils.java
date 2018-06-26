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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;

public class FileUtils {

    public static final String FILE_MASK_SQL = "*.sql";

    private FileUtils() {
    }

    public static Path createDirectories(String... directories) throws MojoExecutionException {
        Path result = Paths.get(separator, directories);
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
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDirectory, fileExtension)) {
                for (Path path : directoryStream) {
                    files.add(path);
                }
            } catch (IOException x) {
                throw new MojoExecutionException("Failed to get list of files.", x);
            }
        }
        return files;
    }
}
