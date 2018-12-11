\qecho Start deploy version.
start transaction;
do $$
declare
   c_no            constant varchar(1)  := 'N';
   c_yes           constant varchar(1)  := 'Y';
   c_not_completed constant varchar(13) := 'NOT COMPLETED';
   c_date_format   constant varchar(21) := 'YYYY-MM-DD HH24:MI:SS';
begin
   update deploy$version
      set is_current = c_no
    where is_current = c_yes;
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
        ( 'test'
        , to_timestamp('YYYY-MM-DD HH:MI:SS', c_date_format)
        , current_timestamp
        , null
        , c_not_completed
        , c_yes
        );
end$$;
commit;
