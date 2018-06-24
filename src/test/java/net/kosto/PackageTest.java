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

package net.kosto;

import junit.framework.TestCase;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageTest extends TestCase {

    public void testOraclePackage() throws Exception {
        /*
         * Check in your dummy Maven project in /src/test/resources/...
         * The testdir is computed from the location of this file.
         */
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/oracle");

        Verifier verifier;

        /*
         * We must first make sure that any artifact created by this test has been removed from the local
         * repository. Failing to do this could cause unstable test results. Fortunately, the verifier
         * makes it easy to do this.
         */
        verifier = new Verifier(testDir.getAbsolutePath());
        verifier.deleteArtifact("net.test", "oracle-test", "version-test", "pom");

        /*
         * The Command Line Options (CLI) are passed to the verifier as a list. This is handy for things like
         * redefining the local repository if needed. In this case, we use the -N flag so that Maven won't
         * recurse. We are only installing the parent pom to the local repo here.
         */
        List<String> cliOptions = new ArrayList<>();
        cliOptions.add("-N");
        verifier.setCliOptions(cliOptions);
        verifier.executeGoal("net.kosto:database-maven-plugin:package");

        /*
         * This is the simplest way to check a build succeeded. It is also the simplest way to create
         * an IT test: make the build pass when the test should pass, and make the build fail when the
         * test should fail. There are other methods supported by the verifier. They can be seen here:
         * http://maven.apache.org/shared/maven-verifier/apidocs/index.html
         */
        verifier.verifyErrorFreeLog();

        /*
         * Reset the streams before executing the verifier again.
         */
        verifier.resetStreams();

        /*
         * The verifier also supports beanshell scripts for
         * verification of more complex scenarios. There are
         * plenty of examples in the core-it tests here:
         * https://svn.apache.org/repos/asf/maven/core-integration-testing/trunk
         */
    }
}
