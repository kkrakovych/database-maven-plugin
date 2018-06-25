prompt
prompt === Check connections
prompt

<#list database.schemes as schema>
prompt &usr_${schema.name}
connect &usr_${schema.name}/&pwd_${schema.name}@&tns_name
</#list>
