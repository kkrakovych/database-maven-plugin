# Database maven plugin

## Main idea of the plugin

1. All database objects, source code, data definition and delta scripts should be under version source control.
2. Any release is a set of previously mentioned objects packed in a single compressed zip file.
3. The release can be deployed into target database by a single script execution.

## How to add the plugin to database maven build

Add new plugin to build section.

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

| Tag Name           | Description |
| ------------------ | ----------- |
| `serviceDirectory` | Service directory name. By default set as `service`. |

Other depends on database type.

#### Oracle database configuration section

```xml
<configuration>
    <oracle>
        <name>DatabaseName</name>
        <sourceDirectory>DatabaseSourceDirectory</sourceDirectory>
        <ignoreDirectory>false</ignoreDirectory>
        <schemes>
            <schema>
                <objects>
                </objects>
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
| `index`           | Schema's index. Affects schema processing order. |
| `name`            | Schema name. |
| `sourceDirectory` | Source directory for all schema's objects. By default schema name is used as source directory. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `objects`         | List of schema's objects. |

###### `objects` Tag

| Tag Name          | Description |
| ----------------- | ----------- |
| `index`           | Objects' index. Affects objects processing order. |
| `type`            | Objects' type. Possible values are: `FUNCTION`, `PACKAGE_BODY`, `PACKAGE_SPEC`, `PROCEDURE`, `TRIGGER`, `TYPE_BODY`, `TYPE_SPEC`, `VIEW`.  |
| `sourceDirectory` | Source directory for all objects' type. By default objects' type in plural form is used as source directory. |
| `ignoreDirectory` | If `true` source directory will be ignored. By default set as `false`. |
| `fileMask`        | File mask for objects. By default set as `*.sql`. |

## How to make database build with the plugin

Execute next command and check output directory.
```
mvn database:package
```

## Appendix A - Full example of database-maven-plugin for Oracle

```xml
<build>
    <plugins>
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
                                    <index>2</index>
                                    <type>PROCEDURE</type>
                                </object>
                                <object>
                                    <index>1</index>
                                    <type>FUNCTION</type>
                                </object>
                            </objects>
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
                            </objects>
                        </schema>
                    </schemes>
                </oracle>
            </configuration>
        </plugin>
    </plugins>
</build>
```
