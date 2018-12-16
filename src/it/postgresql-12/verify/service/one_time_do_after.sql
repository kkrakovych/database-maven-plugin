start transaction;
update deploy$scripts
   set script_finish_timestamp = current_timestamp
     , deploy_status           = 'COMPLETED'
 where script_directory        = :'script_directory'
   and script_name             = :'script_name'
   and build_version           = 'test'
   and build_timestamp         = to_timestamp('YYYY-MM-DD HH:MI:SS', 'yyyy-mm-dd hh24:mi:ss');
commit;
\qecho [SUCCESS] - Script :script_name was applied.
