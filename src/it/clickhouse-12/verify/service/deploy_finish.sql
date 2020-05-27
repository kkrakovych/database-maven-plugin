insert into deploy_version
     ( sys_timestamp
     , build_version
     , build_timestamp
     , deploy_start_timestamp
     , deploy_finish_timestamp
     , deploy_status
     , is_current
     )
select now()
     , build_version
     , build_timestamp
     , deploy_start_timestamp
     , now()
     , 'COMPLETED'
     , is_current
  from deploy_version final
 where is_current = 'Y';
