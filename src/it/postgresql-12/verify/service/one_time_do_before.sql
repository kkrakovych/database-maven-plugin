start transaction;
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
, to_timestamp('2018-12-09 22:44:15', 'yyyy-mm-dd hh24:mi:ss')
, :'script_directory'
, :'script_name'
, :'script_checksum'
, current_timestamp
, null
, 'NOT COMPLETED'
);
commit;