prompt EXECUTE - Script &script_name.

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
     ( '${buildVersion}'
     , to_date( '${buildTimestamp}'
              , 'yyyy-mm-dd hh24:mi:ss'
              )
     , '&script_directory'
     , '&script_name'
     , '&script_checksum'
     , sysdate
     , null
     , 'NOT COMPLETED'
     );

commit;
