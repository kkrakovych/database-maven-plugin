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

package oracle_a

import java.nio.charset.StandardCharsets
import java.nio.file.Files

def sep = File.separator
def target = "$basedir" + sep + "target" + sep
def verify = "$basedir" + sep + "verify" + sep
def target_service = target + ".service" + sep
def verify_service = verify + ".service" + sep
String file

static void checkFile(output, sample, diffCountLimit) {
    def outputFile = new File(output.toString())
    assert outputFile.exists(): output + " file not found"

    def sampleFile = new File(sample.toString())
    assert sampleFile.exists(): sample + " file not found"

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

file = "install_manual.sql"
checkFile(target + file, verify + file, 1)

file = "install_auto.sql"
checkFile(target + file, verify + file, 1)

file = "install_database_database.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_schema_1_schema_b.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_b_1_FUNCTION.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_b_2_PROCEDURE.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_b_3_VIEW.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_b_4_PACKAGE_SPEC.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_b_5_PACKAGE_BODY.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_schema_2_schema_a.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_2_schema_a_BEFORE_1.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_1_TYPE_SPEC.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_2_TYPE_BODY.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_3_VIEW.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_4_PROCEDURE.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_5_PACKAGE_SPEC.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_6_PACKAGE_BODY.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_2_schema_a_7_FUNCTION.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_2_schema_a_AFTER_1.sql"
checkFile(target_service + file, verify_service + file, 0)
