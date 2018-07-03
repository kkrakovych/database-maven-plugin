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
| `schemes`         | List of database's schemes. |

###### `schemes` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `index`           | Schema's index (integer). Affects schema processing order. |
| `name`            | Schema name. |
| `sourceDirectory` | Source directory for all schema's objects. By default schema name is used as source directory. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `objects`         | List of schema's objects. |
| `scripts`         | List of scripts for the schema. |

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
```
mvn database:package
```

## Appendix A - A sample pom.xml for database-maven-plugin for Oracle

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
