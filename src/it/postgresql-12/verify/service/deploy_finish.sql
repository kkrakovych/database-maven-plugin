\qecho Finish deploy version.
start transaction;
do $$
declare
   c_yes       constant varchar(1) := 'Y';
   c_completed constant varchar(9) := 'COMPLETED';
begin
   update deploy$version
      set deploy_finish_timestamp = current_timestamp
        , deploy_status           = c_completed
    where is_current              = c_yes;
end$$;
commit;
