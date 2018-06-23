prompt Start deploy version.

update deploy$version                            v
   set v.is_current                              = 'N'
 where v.is_current                              = 'Y';

insert
  into deploy$version
     ( build_version
     , build_timestamp
     , deploy_start_timestamp
     , deploy_finish_timestamp
     , deploy_status
     , is_current
     )
values
     ( '${buildVersion}'
     , to_date( '${buildTimestamp}'
              , 'yyyy-mm-dd hh24:mi:ss'
              )
     , sysdate
     , null
     , 'NOT COMPLETED'
     , 'Y'
     );

commit;
