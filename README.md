# Database maven plugin

## Main idea of the plugin

1. All database objects, source code, data definition and delta scripts should be under version source control.
2. Any release is a set of previously mentioned objects packed in a single compressed zip file.
3. The release can be deployed into target database by a single script execution.

## How to add the plugin to database maven build

Add new plugin to build section and set up configuration.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>net.kosto</groupId>
            <artifactId>database-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                ...
            </configuration>
        </plugin>
    </plugins>
</build>
```

Snapshot versions of the plugin available at [Sonatype Central Repository](https://central.sonatype.org).
If you want to use them, add plugin repository section to configuration as below.

```xml
<pluginRepositories>
    <pluginRepository>
        <id>sonatype-plugin-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </pluginRepository>
</pluginRepositories>
```

#### Main configuration section

```xml
<plugin>
    ...
    <configuration>
        <serviceDirectory>[service directory name]</serviceDirectory>
        ...
    </configuration>
</plugin>
```

| Tag Name           | Description |
| ------------------ | ----------- |
| `serviceDirectory` | Service directory name. By default set as `service`. Plugin creates the directory and put all generated service scripts to it. You can set another name for the directory to avert name coincidence. As an example, `.service`. |

Other depends on database type.

#### Oracle database configuration section

```xml
<configuration>
    ...
    <oracle>
        <name>[database name]</name>
        <sourceDirectory>[database source code directory]</sourceDirectory>
        <ignoreDirectory>false</ignoreDirectory>
        <schemes>
            <schema>
                <index>1</index>
                <name>[schema name]</name>
                <sourceDirectory>[schema source directory]</sourceDirectory>
                <ignoreDirectory>false</ignoreDirectory>
                <objects>
                    <object>
                        <index>1</index>
                        <type>[object type]</type>
                        <sourceDirectory>[object type source directory]</sourceDirectory>
                        <ignoreDirectory>false</ignoreDirectory>
                        <fileMask>*.sql</fileMask>
                    </object>
                    ...
                </objects>
                <scripts>
                    <script>
                        <type>ONE_TIME</type>
                        <condition>BEFORE</condition>
                        <index>1</index>
                        <sourceDirectory>[script source directory]</sourceDirectory>
                        <ignoreDirectory>false</ignoreDirectory>
                        <fileMask>*.sql</fileMask>
                    </script>
                `   ...
                </scripts>
            </schema>
            ...
        </schemes>
    </oracle>
</configuration>
```

###### `oracle` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `name`            | Database name. |
| `sourceDirectory` | Source directory for all database's objects. By default database name is used as source directory. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `schemes`         | List of database's schemes for deploy. |

###### `schemes` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `index`           | Schema's index (integer). Affects schema processing order. |
| `name`            | Schema name. |
| `sourceDirectory` | Source directory for all schema's objects. By default schema name is used as source directory. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `objects`         | List of schema's objects for deploy. |
| `scripts`         | List of schema's scripts for deploy. |

###### `objects` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `index`           | Objects' index (integer). Affects objects processing order. |
| `type`            | Objects' type. Possible values are: `FUNCTION`, `PACKAGE_BODY`, `PACKAGE_SPEC`, `PROCEDURE`, `TRIGGER`, `TYPE_BODY`, `TYPE_SPEC`, `VIEW`.  |
| `sourceDirectory` | Source directory for all objects' type. By default objects' types have next associated directories: `FUNCTION` - `functions`, `PACKAGE_BODY` - `package_bodies`, `PACKAGE_SPEC` - `package_specs`, `PROCEDURE` - `procedures`, `TRIGGER` - `triggers`, `TYPE_BODY` - `type_bodies`, `TYPE_SPEC` - `type_specs`, and `VIEW` - `views`. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `fileMask`        | File mask for objects. By default set as `*.sql`. |

###### `scripts` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `type`            | Scripts' type. Possible values are: `ONE_TIME` and `REUSABLE`. Affects how scripts will be used, one time only or every time during deploy. |
| `condition`       | Scripts' condition. Possible values are: `BEFORE` and `AFTER`. Affects when scripts will be executed, before or after source code deploy. |
| `index`           | Scripts' index (integer). Affects scripts processing order. |
| `sourceDirectory` | Source directory for all scripts. By default scripts' type have next associated directories: `ONE_TIME` - `script_one_time` and `REUSABLE` - `script_reusable`. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `fileMask`        | File mask for objects. By default set as `*.sql`. |

## How to make database build with the plugin

Execute next command and check output directory.
```bash
mvn database:package
```

In output directory you will find next things:
1. Script install_manual.sql - Main SQL script for manual deploy.
2. Service directory - All service scripts required for deploy.
The directory name by default is `service`.
The name can be changed with `serviceDirectory` parameter in `configuration` section of the plugin.
3. Database directory and sub directories - All database source code and scripts required for deploy.
Content of the directory may vary dependent on configuration of the plugin.

## How to deploy result script

Please take into account before the script execution you will need to install and setup:
1. [Oracle Instant Client](http://www.oracle.com/technetwork/database/database-technologies/instant-client/overview/index.html) with Oracle SQL*Plus;
2. `tnsnames.ora` [configuration file](https://docs.oracle.com/database/121/NETRF/tnsnames.htm#NETRF007);
3. Both `ORACLE_HOME` and `TNS_ADMIN` system variables should be set properly;
4. Oracle SQL*Plus should be available for execution via `PATH` system variable.

The main script for manual deploy can be executed with Oracle SQL*Plus.
```bash
sqlplus /nolog @install_manual.sql
```

At the beginning the script requests next parameters to be set:
1. TNS name for database to deploy the script.
2. For every schema in database: schema name and corresponding password.
Please take into account if no input on schema name request - schema name will be as is.
The same is true for password.

Please take into account - no waiting or pause after all mentioned parameters set before deploy.

We strongly recommend to turn off all connections to database before deploy to avert session blocking and errors.

We strongly recommend to test the script on prod-like environment before going live on production.

## What does script execute

1. Script shows brief information about itself: database name, build version and when it was created.
2. Script requests all parameters required for deploy.
3. Script checks all connections to schemas required for deploy.
4. Script starts to spool all activities into log file with name `install_manual_${database_name}_${build_version}_${build_timestamp}.log`.
5. Script prints detailed information about upcoming deploy.
6. For every schema in database the script does next:
   * Connects to the schema
   * Checks service tables `deploy$version` and `deploy$scripts`. If they are missing than they will be created.
   * Writes details about the deploy into `deploy$version` table.
   * Deploys scripts before source code drop if set.
   * Drops source code. Tables are not affected for sure.
   * Deploys source code according to configuration of the plugin.
   * Compiles invalid objects if any.
   * Checks for invalid objects.
   * Deploys scripts after source code deploy if set.

The script uses fail fast strategy. In case of any error deploy will be terminated. 

## Appendix A - Sample pom.xml for database-maven-plugin for Oracle

You may find sample projects in test directory.
One example is database called [oracle_a](https://github.com/kkrakovych/database-maven-plugin/blob/develop/src/test/resources/oracle_a/pom.xml).
Below you may see part of the project's pom.xml file.

```xml
<plugin>
    <groupId>net.kosto</groupId>
    <artifactId>database-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <serviceDirectory>.service</serviceDirectory>
        <oracle>
            <name>database</name>
            <schemes>
                <schema>
                    <index>2</index>
                    <name>schema_a</name>
                    <objects>
                        <object>
                            <index>1</index>
                            <type>TYPE_SPEC</type>
                        </object>
                        <object>
                            <index>2</index>
                            <type>TYPE_BODY</type>
                        </object>
                        <object>
                            <index>3</index>
                            <type>VIEW</type>
                        </object>
                        <object>
                            <index>4</index>
                            <type>PROCEDURE</type>
                        </object>
                        <object>
                            <index>3</index>
                            <type>FUNCTION</type>
                        </object>
                        <object>
                            <index>5</index>
                            <type>PACKAGE_SPEC</type>
                        </object>
                        <object>
                            <index>6</index>
                            <type>PACKAGE_BODY</type>
                        </object>
                    </objects>
                    <scripts>
                        <script>
                            <type>ONE_TIME</type>
                            <condition>BEFORE</condition>
                            <index>1</index>
                            <sourceDirectory>scripts/delta</sourceDirectory>
                            <fileMask>*.sql</fileMask>
                        </script>
                        <script>
                            <type>REUSABLE</type>
                            <condition>AFTER</condition>
                            <index>1</index>
                            <sourceDirectory>scripts/dictionaries</sourceDirectory>
                            <fileMask>*.sql</fileMask>
                        </script>
                    </scripts>
                </schema>
                <schema>
                    <index>1</index>
                    <name>schema_b</name>
                    <objects>
                        <object>
                            <index>1</index>
                            <type>FUNCTION</type>
                            <sourceDirectory>fncs</sourceDirectory>
                            <fileMask>*.fnc</fileMask>
                        </object>
                        <object>
                            <index>2</index>
                            <type>PROCEDURE</type>
                            <sourceDirectory>prcs</sourceDirectory>
                            <fileMask>*.prc</fileMask>
                        </object>
                        <object>
                            <index>3</index>
                            <type>VIEW</type>
                            <sourceDirectory>vws</sourceDirectory>
                            <fileMask>*.vw</fileMask>
                        </object>
                        <object>
                            <index>4</index>
                            <type>PACKAGE_SPEC</type>
                            <sourceDirectory>pkgs</sourceDirectory>
                            <fileMask>*.pks</fileMask>
                        </object>
                        <object>
                            <index>5</index>
                            <type>PACKAGE_BODY</type>
                            <sourceDirectory>pkgs</sourceDirectory>
                            <fileMask>*.pkb</fileMask>
                        </object>
                    </objects>
                </schema>
            </schemes>
        </oracle>
    </configuration>
</plugin>
```

## Appendix B - Sample output for install_manual.sql script for Oracle

```
$ sqlplus /nolog @install_manual.sql

SQL*Plus: Release 12.2.0.1.0 Production on Sat Jul 7 23:12:41 2018

Copyright (c) 1982, 2017, Oracle.  All rights reserved.


=== DATABASE-MAVEN-PLUGIN
Oracle database [database] version [test] created at [2018-07-07 23:11:14]

Enter TNS name for database [database]: ORACLE_A
Enter username for schema [schema_b]:
Enter password for schema [schema_b]:
Enter username for schema [schema_a]:
Enter password for schema [schema_a]:

=== Check Connections

schema_b
Connected.
schema_a
Connected.


Elapsed: 00:00:00.02
=== Deploy Information

Database database
Build version: test
Build timestamp: 2018-07-07 23:11:14
Database TNS name: ORACLE_A
List of schemas:
* schema_b -> schema_b
* schema_a -> schema_a

=== Deploy Database [database]

=== Deploy Schema [schema_b]

Connected.
Check deploy tables.
Service table deploy$version... already exists.
Service table deploy$scripts... already exists.
Elapsed: 00:00:00.03
Start deploy version.
Elapsed: 00:00:00.03
Drop source code.
Elapsed: 00:00:00.08
Deploy source code.
Execute /database/schema_b/fncs/fnc_test_c.fnc
Elapsed: 00:00:00.02
Execute /database/schema_b/fncs/fnc_test_d.fnc
Elapsed: 00:00:00.02
Execute /database/schema_b/prcs/prc_test_c.prc
Elapsed: 00:00:00.04
Execute /database/schema_b/prcs/prc_test_d.prc
Elapsed: 00:00:00.02
Execute /database/schema_b/vws/vw_test_c.vw
Elapsed: 00:00:00.03
Execute /database/schema_b/vws/vw_test_d.vw
Elapsed: 00:00:00.03
Elapsed: 00:00:00.03
Execute /database/schema_b/pkgs/pkg_test_c.pks
Elapsed: 00:00:00.03
Execute /database/schema_b/pkgs/pkg_test_c.pkb
Elapsed: 00:00:00.03
Compile schema objects.
Elapsed: 00:00:00.65
Check objects.
Number of invalid schema's objects = 0.
Elapsed: 00:00:00.02
Finish deploy version.
Elapsed: 00:00:00.01

=== Deploy Schema [schema_a]

Connected.
Check deploy tables.
Service table deploy$version... already exists.
Service table deploy$scripts... already exists.
Elapsed: 00:00:00.02
Start deploy version.
Elapsed: 00:00:00.03
Execute ONE_TIME scripts with BEFORE condition.
Execute /database/schema_a/scripts/delta/v1_20180701232000_delta_test_a.sql
[SUCCESS] - Script v1_20180701232000_delta_test_a.sql was already applied.
Drop source code.
Elapsed: 00:00:00.10
Deploy source code.
Execute /database/schema_a/type_specs/ot_test_a.sql
Elapsed: 00:00:00.14
Execute /database/schema_a/type_specs/ot_test_b.sql
Elapsed: 00:00:00.03
Execute /database/schema_a/type_specs/tt_test_a.sql
Elapsed: 00:00:00.04
Execute /database/schema_a/type_bodies/ot_test_b.sql
Elapsed: 00:00:00.02
Execute /database/schema_a/functions/fnc_test_a.sql
Elapsed: 00:00:00.03
Execute /database/schema_a/functions/fnc_test_b.sql
Elapsed: 00:00:00.03
Execute /database/schema_a/views/vw_test_a.sql
Elapsed: 00:00:00.02
Execute /database/schema_a/views/vw_test_b.sql
Elapsed: 00:00:00.05
Execute /database/schema_a/procedures/prc_test_a.sql
Elapsed: 00:00:00.02
Execute /database/schema_a/procedures/prc_test_b.sql
Elapsed: 00:00:00.03
Execute /database/schema_a/package_specs/pkg_test_a.sql
Elapsed: 00:00:00.03
Execute /database/schema_a/package_bodies/pkg_test_a.sql
Elapsed: 00:00:00.02
Compile schema objects.
Elapsed: 00:00:00.44
Check objects.
Number of invalid schema's objects = 0.
Elapsed: 00:00:00.02
Execute REUSABLE scripts with AFTER condition.
Execute /database/schema_a/scripts/dictionaries/test_b.sql
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.00
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Execute /database/schema_a/scripts/dictionaries/test_a.sql
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Elapsed: 00:00:00.01
Finish deploy version.
Elapsed: 00:00:00.02

Disconnected from Oracle Database 11g Enterprise Edition Release 11.2.0.4.0 - 64bit Production
With the Partitioning, OLAP, Data Mining and Real Application Testing options
```