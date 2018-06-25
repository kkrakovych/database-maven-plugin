prompt Finish deploy version.

update deploy$version                            v
   set v.deploy_finish_timestamp                 = sysdate
     , v.deploy_status                           = 'COMPLETED'
 where v.is_current                              = 'Y';

commit;
