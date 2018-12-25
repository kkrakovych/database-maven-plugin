# Database Maven Plugin

[![Build Status](https://travis-ci.com/kkrakovych/database-maven-plugin.svg?branch=develop)](https://travis-ci.com/kkrakovych/database-maven-plugin)

## Main Idea of the Plugin

The plugin creates database migration scripts to update databases from version to version.

We believe database migration scripts should be automated.
We believe all database objects, source code, and data dictionaries should be under version source control.
Thus creation of database migration scripts should be performed based on files under version source control only.
Further deploy of these scripts to databases should be easy and simple to automate but still allow manual deploy as well.

## Strategies for Database Migration Scripts

At the moment plugin supports one strategy - 'Full Source Code Drop and Create'.

The strategy contains next steps:
- deploy scripts before source code drop;
- drop existent source code;
- deploy actual source code;
- deploy scripts after source code validation.

Configuration in pom.xml defines:
- database
- schemes if any
- groups of database objects
  - type of objects within a group
  - location of the group in repository
  
- location of database objects within repository;
- location of DDL (Data Definition Language) and DML (Data Manipulation Language) scripts;
- order for execution of groups of scripts;

#### Oracle

0. Fail fast in case of any issue
1. Execute scripts before source code processing
2. Execute source code processing
3. Execute scripts after source code processing

#### PostgreSQL

N.B. Support for PostgreSQL is very limited at the moment.

0. Fail fast in case of any issue
1. Execute scripts processing

## How to add the plugin to database maven build

Add new plugin to build section and set up configuration.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>net.kosto</groupId>
            <artifactId>database-maven-plugin</artifactId>
            <version>1.3-SNAPSHOT</version>
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
        <logFileName>[log-file-name]</logFileName>
        <serviceDirectory>[service-directory-name]</serviceDirectory>
        ...
    </configuration>
</plugin>
```

| Tag Name           | Description |
| ------------------ | ----------- |
| `logFileName`      | Log file name. By default the name will be generated automatically as `install_[database-name]_[build-version]_[start-timestamp].log`. Any valid file name can be set to get stable log file name. As an example, `install.log`. |
| `serviceDirectory` | Service directory name. By default set as `service`. Plugin creates the directory and puts all generated service scripts to it. Any valid directory name can be set to avert name coincidence. As an example, `.service`. |

Other depends on database type.

#### Oracle database configuration section

```xml
<configuration>
    ...
    <oracle>
        <name>[database name]</name>
        <sourceDirectory>[database source code directory]</sourceDirectory>
        <ignoreDirectory>false</ignoreDirectory>
        <defineSymbol>[symbol]</defineSymbol>
        <ignoreDefine>false</ignoreDefine>
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
                    ...
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
| `name`            | Database name. By default set as `database`. |
| `sourceDirectory` | Source directory for all database's objects. By default database name is used as source directory. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `defineSymbol`    | Define symbol for variable substitution. By default set as `&`. The option affects all sub objects recursively. |
| `ignoreDefine`    | If `true` variable substitution will be disabled. By default set as `true`. The option affects all sub objects recursively. |
| `schemes`         | List of database's schemes for deploy. |

###### `schemes` Tag

| Tag Name              | Description |
| --------------------- | ----------- |
| `ignoreServiceTables` | If `true` service tables will be ignored for the schema only. By default set as `false`. It makes sense to use the option for _proxy_ schemes or schemes without `CREATE TABLE` privilege. However if service tables are ignored there no way to run `ONE_TIME` scripts. |
| `index`               | Schema's index (integer). Affects schema processing order. It should be either set for every `schema` and unique or missing. If it is missing for every `schema`, natural order of schemes in configuration will be used. |
| `name`                | Schema name. By default set as `schema`. |
| `sourceDirectory`     | Source directory for all schema's objects. By default schema name is used as source directory. |
| `ignoreDirectory`     | If `true` source directory will be ignored. By default set as `false`. |
| `defineSymbol`        | Define symbol for variable substitution. By default takes value set for database. |
| `ignoreDefine`        | If `true` variable substitution will be disabled. By default takes value set for database. |
| `objects`             | List of schema's objects for deploy if any. |
| `scripts`             | List of schema's scripts for deploy if any. |

###### `objects` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `index`           | Objects' index (integer). Affects objects processing order. It should be either set for every `object` and unique or missing. If it is missing for every `object`, natural order of objects in configuration will be used. |
| `type`            | Objects' type. Possible values are: `FUNCTION`, `PACKAGE_BODY`, `PACKAGE_SPEC`, `PROCEDURE`, `TRIGGER`, `TYPE_BODY`, `TYPE_SPEC`, `VIEW`.  |
| `sourceDirectory` | Source directory for all objects' type. By default objects' types have next associated directories: `FUNCTION` - `functions`, `PACKAGE_BODY` - `package_bodies`, `PACKAGE_SPEC` - `package_specs`, `PROCEDURE` - `procedures`, `TRIGGER` - `triggers`, `TYPE_BODY` - `type_bodies`, `TYPE_SPEC` - `type_specs`, and `VIEW` - `views`. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `defineSymbol`    | Define symbol for variable substitution. By default takes value set for schema. |
| `ignoreDefine`    | If `true` variable substitution will be disabled. By default takes value set for schema. |
| `fileMask`        | File mask for objects. By default set as `*.sql`. |

###### `scripts` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `type`            | Scripts' type. Possible values are: `ONE_TIME` and `REUSABLE`. Affects how scripts will be used, one time only or every time during deploy. |
| `condition`       | Scripts' condition. Possible values are: `BEFORE` and `AFTER`. Affects when scripts will be executed, before or after source code deploy. |
| `index`           | Scripts' index (integer). Affects scripts processing order. It should be either set for every `script` and unique within script `type` or missing. If it is missing for every `script`, natural order of scripts in configuration will be used. |
| `sourceDirectory` | Source directory for all scripts. By default scripts' type have next associated directories: `ONE_TIME` - `script_one_time` and `REUSABLE` - `script_reusable`. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `defineSymbol`    | The option is not supported for scripts. |
| `ignoreDefine`    | The option is not supported for scripts. |
| `fileMask`        | File mask for objects. By default set as `*.sql`. |

#### PostgreSQL database configuration section

```xml
<configuration>
    ...
    <postgresql>
        <name>[database name]</name>
        <sourceDirectory>[database source code directory]</sourceDirectory>
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
            ...
        </scripts>
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
    </postgresql>
</configuration>
```

## How to make database build with the plugin

Execute next command and check output directory.
```bash
mvn database:package
```

In output directory you will find next things:
1. Script install_auto.sql - Main SQL script for deploy via script.
2. Script install_manual.sql - Main SQL script for deploy with manual input.
3. Service directory - All service scripts required for deploy.
The directory name by default is `service`.
The name can be changed with `serviceDirectory` parameter in `configuration` section of the plugin.
4. Database directory and sub directories - All database source code and scripts required for deploy.
Content of the directory may vary dependent on configuration of the plugin.

## How to deploy result script

Please take into account before the script execution you will need to install and setup:
1. [Oracle Instant Client](http://www.oracle.com/technetwork/database/database-technologies/instant-client/overview/index.html) with Oracle SQL*Plus;
2. `tnsnames.ora` [configuration file](https://docs.oracle.com/database/121/NETRF/tnsnames.htm#NETRF007);
3. `ORACLE_HOME`, `TNS_ADMIN` and `NLS_LANG` system variables should be set properly;
4. Oracle SQL*Plus should be available for execution via `PATH` system variable.

#### Automatically

###### Oracle

The main script for automatic deploy can be executed with Oracle SQL*Plus.
```bash
sqlplus /nolog @install_auto.sql [tns-name] [user-schema-1] [user-password-1] [user-schema-2] [user-password-2] ...
```

###### PostgreSQL

The main script for automatic deploy can be executed with PostgreSQL interactive terminal.
```bash
psql -h [host] -p [port] -U [user-name] -W [user-password] -d [database-name] -f install.sql
```

If SSH tunnel is required, first session to create it
```bash
ssh -L [port]:localhost:[port-remote] [user-remote]@[host-remote]
```
second session to connect database
```bash
psql -h localhost -p [port] -U [user-name] -W [user-password] -d [database-name] -f install.sql
```

#### Manually

The main script for deploy with manual input can be executed with Oracle SQL*Plus.
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
3. Script checks all connections to schemes required for deploy.
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
    <version>1.3-SNAPSHOT</version>
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
List of schemes:
* schema_b -> schema_b
* schema_a -> schema_a

=== Deploy Database [database]

=== Deploy Schema [schema_b]

Connected.
Check service tables.
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
Check service tables.
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
