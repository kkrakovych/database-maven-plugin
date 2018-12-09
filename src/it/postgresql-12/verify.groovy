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

file = "install.sql"
checkFile(target + file, verify + file, 0)

file = "check_deploy_tables.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "deploy_finish.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "deploy_information.sql"
checkFile(target_service + file, verify_service + file, 1)

file = "deploy_start.sql"
checkFile(target_service + file, verify_service + file, 1)

file = "drop_source_code.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_database_database.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_0_public_0_FUNCTION.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_0_public_1_TRIGGER.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_0_public_2_VIEW.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_0_FUNCTION.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_1_TRIGGER.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_2_VIEW.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_schema_0_public.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_schema_1_schema.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_AFTER_0.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_AFTER_1.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_BEFORE_2.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_BEFORE_3.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_AFTER_0.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_AFTER_1.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_BEFORE_2.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_BEFORE_3.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "log_finish.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "log_start.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_control.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_do_after.sql"
checkFile(target_service + file, verify_service + file, 1)

file = "one_time_do_before.sql"
checkFile(target_service + file, verify_service + file, 1)

file = "one_time_do_checksum.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_do_nothing.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_do_success.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "psql_setup.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "script_information.sql"
checkFile(target_service + file, verify_service + file, 1)

directory = target + "database" + sep + "functions" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "script_one_time_after" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "script_one_time_before" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "script_reusable_after" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "script_reusable_before" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "triggers" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "views" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "functions" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_one_time_after" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_one_time_before" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_reusable_after" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_reusable_before" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "triggers" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

directory = target + "database" + sep + "schema" + sep + "views" + sep
checkExist(directory + "dummy_a.sql")
checkExist(directory + "dummy_b.sql")
checkExist(directory + "dummy_c.sql")

assert true
