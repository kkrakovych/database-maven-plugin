prompt Start deploy version.
declare
   c_no            constant varchar2(1 char)  := 'N';
   c_yes           constant varchar2(1 char)  := 'Y';
   c_not_completed constant varchar2(13 char) := 'NOT COMPLETED';
   c_date_format   constant varchar2(21 char) := 'YYYY-MM-DD HH24:MI:SS';
begin
   update deploy$version v
      set v.is_current   = c_no
    where v.is_current   = c_yes;
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
        , to_date('YYYY-MM-DD HH:MI:SS', c_date_format)
        , sysdate
        , null
        , c_not_completed
        , c_yes
        );
   commit;
end;
/
