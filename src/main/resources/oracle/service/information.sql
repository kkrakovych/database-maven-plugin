prompt
prompt === Deploy Information
prompt

prompt Database           ${database.name}
prompt Build version:     ${buildVersion}
prompt Build timestamp:   ${buildTimestamp}
prompt Database TNS name: &tns_name
prompt List of schemas:
<#list database.schemes as schema>
prompt * ${schema.name} -> &usr_${schema.name}
</#list>

prompt
