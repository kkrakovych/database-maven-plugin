prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt [${database.name}] oracle database version [${buildVersion}] created at [${buildTimestamp}]

@./${serviceDirectory}/input_parameters.sql
@./${serviceDirectory}/sqlplus_setup.sql
@./${serviceDirectory}/check_connections.sql

column dt new_value sysdate noprint
select to_char(sysdate, 'YYYYMMDD-HH24MISS') dt from dual;
spool install.${database.name}.${buildVersion}.&sysdate..log

@./${serviceDirectory}/information.sql

<#list database.schemes as schema>
@./${schema.sourceDirectory}/install_schema.sql
</#list>

spool off

exit