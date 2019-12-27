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
     , deploy_finish_timestamp
     , deploy_status
     , 'N'
  from deploy_version final
 where is_current = 'Y';
insert into deploy_version
     ( sys_timestamp
     , build_version
     , build_timestamp
     , deploy_start_timestamp
     , deploy_finish_timestamp
     , deploy_status
     , is_current
     )
values (now(), 'test', toDateTime('2019-12-27 18:40:26'), now(), null, 'NOT COMPLETED', 'Y');
