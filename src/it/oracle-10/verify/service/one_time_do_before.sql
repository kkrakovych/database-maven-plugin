insert
into deploy$scripts
( build_version
, build_timestamp
, script_directory
, script_name
, script_checksum
, script_start_timestamp
, script_finish_timestamp
, deploy_status
)
values
( 'test'
, to_date('2018-12-09 19:49:56', 'yyyy-mm-dd hh24:mi:ss')
, '&script_directory'
, '&script_name'
, '&script_checksum'
, sysdate
, null
, 'NOT COMPLETED'
)
/
commit
/