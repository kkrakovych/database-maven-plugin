/*
 * Copyright 2019 Kostyantyn Krakovych
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

file = "install_auto.sh"
checkFile(target + file, verify + file, 0)

file = "install_manual.sh"
checkFile(target + file, verify + file, 0)

file = "check_service_tables.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "deploy_finish.sql"
checkFile(target_service + file, verify_service + file, 0)

file = "deploy_information.sh"
checkFile(target_service + file, verify_service + file, 1)

file = "deploy_start.sql"
checkFile(target_service + file, verify_service + file, 1)

file = "input_parameters_auto.sh"
checkFile(target_service + file, verify_service + file, 1)

file = "input_parameters_manual.sh"
checkFile(target_service + file, verify_service + file, 1)

file = "install_database_database.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_0_public_0_MATERIALIZED_VIEW.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_0_public_1_VIEW.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_0_MATERIALIZED_VIEW.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_object_1_schema_1_VIEW.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_schema_0_public.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_schema_1_schema.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_AFTER_0.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_AFTER_1.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_BEFORE_2.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_0_public_BEFORE_3.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_AFTER_0.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_AFTER_1.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_BEFORE_2.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "install_script_1_schema_BEFORE_3.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "log_finish.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "log_start.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_control.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_do_after.sh"
checkFile(target_service + file, verify_service + file, 1)

file = "one_time_do_before.sh"
checkFile(target_service + file, verify_service + file, 1)

file = "one_time_do_checksum.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_do_nothing.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "one_time_do_success.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "run_file.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "run_query.sh"
checkFile(target_service + file, verify_service + file, 0)

file = "script_information.sh"
checkFile(target_service + file, verify_service + file, 1)

file = "source.sh"
checkFile(target_service + file, verify_service + file, 1)

directory = target + "database" + sep + "materialized_views" + sep
checkExist(directory + "dummy_sp_materialized_view_a.sql")
checkExist(directory + "dummy_sp_materialized_view_b.sql")
checkExist(directory + "dummy_sp_materialized_view_c.sql")

directory = target + "database" + sep + "script_one_time_after" + sep
checkExist(directory + "dummy_sp_one_time_after_a.sql")
checkExist(directory + "dummy_sp_one_time_after_b.sql")
checkExist(directory + "dummy_sp_one_time_after_c.sql")

directory = target + "database" + sep + "script_one_time_before" + sep
checkExist(directory + "dummy_sp_one_time_before_a.sql")
checkExist(directory + "dummy_sp_one_time_before_b.sql")
checkExist(directory + "dummy_sp_one_time_before_c.sql")

directory = target + "database" + sep + "script_reusable_after" + sep
checkExist(directory + "dummy_sp_reusable_after_a.sql")
checkExist(directory + "dummy_sp_reusable_after_b.sql")
checkExist(directory + "dummy_sp_reusable_after_c.sql")

directory = target + "database" + sep + "script_reusable_before" + sep
checkExist(directory + "dummy_sp_reusable_before_a.sql")
checkExist(directory + "dummy_sp_reusable_before_b.sql")
checkExist(directory + "dummy_sp_reusable_before_c.sql")

directory = target + "database" + sep + "views" + sep
checkExist(directory + "dummy_sp_view_a.sql")
checkExist(directory + "dummy_sp_view_b.sql")
checkExist(directory + "dummy_sp_view_c.sql")

directory = target + "database" + sep + "schema" + sep + "materialized_views" + sep
checkExist(directory + "dummy_s1_materialized_view_a.sql")
checkExist(directory + "dummy_s1_materialized_view_b.sql")
checkExist(directory + "dummy_s1_materialized_view_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_one_time_after" + sep
checkExist(directory + "dummy_s1_one_time_after_a.sql")
checkExist(directory + "dummy_s1_one_time_after_b.sql")
checkExist(directory + "dummy_s1_one_time_after_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_one_time_before" + sep
checkExist(directory + "dummy_s1_one_time_before_a.sql")
checkExist(directory + "dummy_s1_one_time_before_b.sql")
checkExist(directory + "dummy_s1_one_time_before_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_reusable_after" + sep
checkExist(directory + "dummy_s1_reusable_after_a.sql")
checkExist(directory + "dummy_s1_reusable_after_b.sql")
checkExist(directory + "dummy_s1_reusable_after_c.sql")

directory = target + "database" + sep + "schema" + sep + "script_reusable_before" + sep
checkExist(directory + "dummy_s1_reusable_before_a.sql")
checkExist(directory + "dummy_s1_reusable_before_b.sql")
checkExist(directory + "dummy_s1_reusable_before_c.sql")

directory = target + "database" + sep + "schema" + sep + "views" + sep
checkExist(directory + "dummy_s1_view_a.sql")
checkExist(directory + "dummy_s1_view_b.sql")
checkExist(directory + "dummy_s1_view_c.sql")

assert true
