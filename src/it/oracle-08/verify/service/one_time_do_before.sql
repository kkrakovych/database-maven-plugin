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
     , to_date('YYYY-MM-DD HH:MI:SS', 'yyyy-mm-dd hh24:mi:ss')
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
