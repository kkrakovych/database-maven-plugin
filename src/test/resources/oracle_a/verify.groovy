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
def service = ".service" + sep
String output
String sample

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

output = target + "install_manual.sql"
sample = verify + "install_manual.sql"
checkFile(output, sample, 1)

output = target + "install_auto.sql"
sample = verify + "install_auto.sql"
checkFile(output, sample, 1)

output = target + service + "install_database_database.sql"
sample = verify + service + "install_database_database.sql"
checkFile(output, sample, 0)

output = target + service + "install_schema_2_schema_a.sql"
sample = verify + service + "install_schema_2_schema_a.sql"
checkFile(output, sample, 0)

output = target + service + "install_schema_1_schema_b.sql"
sample = verify + service + "install_schema_1_schema_b.sql"
checkFile(output, sample, 0)
