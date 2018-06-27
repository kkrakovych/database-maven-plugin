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
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceUtils {

    private ResourceUtils() {
    }

    public static List<Path> getFiles(String fileMask, String firstDirectoryPart, String... moreDirectoryParts) throws MojoExecutionException {
        List<Path> result = new ArrayList<>();

        CodeSource source = ResourceUtils.class.getProtectionDomain().getCodeSource();

        if (source != null) {
            URL location = source.getLocation();

            try (
                ZipInputStream zip = new ZipInputStream(location.openStream())
            ) {
                String pattern = fileMask
                    .replace("?", ".?")
                    .replace("*", ".*?");

                Path directory = Paths.get(firstDirectoryPart, moreDirectoryParts);
                ZipEntry ze;
                while ((ze = zip.getNextEntry()) != null) {
                    if (!ze.getName().contains(firstDirectoryPart) || !ze.getName().matches(pattern))
                        continue;
                    Path path = Paths.get(ze.getName());
                    Path parent = path.getParent();
                    Path file = path.getFileName();
                    if (file != null && parent != null && parent.equals(directory) && file.toString().matches(pattern)) {
                        result.add(path);
                    }
                }
            } catch (IOException x) {
                throw new MojoExecutionException("Failed to get resource files.", x);
            }
        }

        return result;
    }
}
