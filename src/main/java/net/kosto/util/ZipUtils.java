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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.zip.Deflater.BEST_COMPRESSION;

public class ZipUtils {

    private ZipUtils() {
    }

    public static void compress(String zipFile, String baseDirectory, List<String> files) throws MojoExecutionException {
        byte[] buffer = new byte[1024];

        try (
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            zos.setLevel(BEST_COMPRESSION);
            for (String file : files) {
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                try (
                    FileInputStream in = new FileInputStream(baseDirectory + File.separator + file)
                ) {
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }
            }
            zos.closeEntry();
        } catch (IOException x) {
            throw new MojoExecutionException("Failed to create zip file.", x);
        }
    }
}
