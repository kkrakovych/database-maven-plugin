prompt Input parameters.

accept tns_name char prompt 'Enter TNS name for database [${database.name}]: '
<#list database.schemes as schema>
accept usr_${schema.name} char prompt 'Enter username for schema [${schema.name}]: ' default ${schema.name}
accept pwd_${schema.name} char prompt 'Enter password for schema [&usr_${schema.name}]: ' hide default &usr_${schema.name}
</#list>
