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

import java.nio.charset.StandardCharsets
import java.nio.file.Files

def sep = File.separator
def target = "$basedir" + sep + "target" + sep
def verify = "$basedir" + sep + "verify" + sep
def target_service = target + "service" + sep
def verify_service = verify + "service" + sep
String directory
String file

// Checks whether the specified file exists.
static File checkExist(filePath) {
    def file = new File(filePath.toString())
    assert file.exists(): filePath + " file not found"
    return file
}

// Checks whether the specified files exist and are the same.
static void checkFile(output, sample, diffCountLimit) {
    def outputFile = checkExist(output.toString())
    def sampleFile = checkExist(sample.toString())

    List<String> outputLines = Files.readAllLines(outputFile.toPath(), StandardCharsets.UTF_8)
    List<String> sampleLines = Files.readAllLines(sampleFile.toPath(), StandardCharsets.UTF_8)
    assert (outputLines.size() == sampleLines.size()): output + " file has wrong size"

    def diffCount = 0
    for (int i = 0; i < outputLines.size(); i++) {
        if (outputLines.get(i) != sampleLines.get(i)) {
            diffCount = diffCount + 1
        }
    }
    assert (diffCount <= diffCountLimit): output + " file has wrong content"
}

file = "install_object_0_schema_first_0_FUNCTION.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_0_schema_first_1_PROCEDURE.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_second_AFTER_1.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_second_BEFORE_0.sql"
checkFile(target_service + file, verify_service + file, 0)

assert true
