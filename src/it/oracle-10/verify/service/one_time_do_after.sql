update deploy$scripts s
set s.script_finish_timestamp = sysdate
, s.deploy_status = 'COMPLETED'
where s.script_directory = '&script_directory'
and s.script_name = '&script_name'
and s.build_version = 'test'
and s.build_timestamp = to_date('2018-12-09 19:49:56', 'yyyy-mm-dd hh24:mi:ss')
/
commit
/
prompt [SUCCESS] - Script &script_name was applied.