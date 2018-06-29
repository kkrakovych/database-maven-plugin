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

package oracle

import java.nio.charset.StandardCharsets
import java.nio.file.Files

def sep = File.separator

// Check service scripts existence
def outputFilePath = "$basedir" + sep + "target" + sep + "install_manual.sql"
def outputFile = new File(outputFilePath.toString())
assert outputFile.exists(): outputFilePath + " file not found"

// Check service script content
def sampleFilePath = "$basedir" + sep + "verify" + sep + "install_manual.sql"
def sampleFile = new File(sampleFilePath.toString())
assert sampleFile.exists(): sampleFilePath + " file not found"

List<String> outputLines = Files.readAllLines(outputFile.toPath(), StandardCharsets.UTF_8)
List<String> sampleLines = Files.readAllLines(sampleFile.toPath(), StandardCharsets.UTF_8)
assert (outputLines.size() == sampleLines.size()): outputFilePath + " file has wrong size"

def diffCount = 0
for (int i = 0; i < outputLines.size(); i++) {
    if (outputLines.get(i) != sampleLines.get(i)) {
        diffCount = diffCount + 1
    }
}
assert (diffCount < 2): outputFilePath + " file has wrong content"
