prompt Finish deploy version.
declare
   c_yes       constant varchar2(1 char) := 'Y';
   c_completed constant varchar2(9 char) := 'COMPLETED';
begin
   update deploy$version            v
      set v.deploy_finish_timestamp = sysdate
        , v.deploy_status           = c_completed
    where v.is_current              = c_yes;
   commit;
end;
/
