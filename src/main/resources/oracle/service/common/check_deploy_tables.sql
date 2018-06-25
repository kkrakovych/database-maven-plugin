prompt Check deploy tables.

declare

   function table_exists
   ( in_table_name varchar2
   )
   return boolean
   is
      l_check pls_integer                        := 0;
   begin
      select count(1)
        into l_check
        from user_tables                         t
       where t.table_name                        = upper(in_table_name);

      return case
                when l_check                     = 0
                then false
                else true
             end;
   end table_exists;

begin
   if not table_exists( 'deploy$version' )
   then
      execute immediate
      'create table deploy$version' ||
      '( build_version           varchar2(100 char)' ||
      ', build_timestamp         date' ||
      ', deploy_start_timestamp  date' ||
      ', deploy_finish_timestamp date' ||
      ', deploy_status           varchar2(100 char)' ||
      ', is_current              char' ||
      ')';
      dbms_output.put_line
      ( 'Service table deploy$version was created.'
      );
   else
      dbms_output.put_line
      ( 'Service table deploy$version already exists.'
      );
   end if;

   if not table_exists( 'deploy$scripts' )
   then
      execute immediate
      'create table deploy$scripts' ||
      '( build_version           varchar2(100 char)' ||
      ', build_timestamp         date' ||
      ', script_directory        varchar2(100 char)' ||
      ', script_name             varchar2(100 char)' ||
      ', script_checksum         varchar2(100 char)' ||
      ', script_start_timestamp  date' ||
      ', script_finish_timestamp date' ||
      ', deploy_status           varchar2(100 char)' ||
      ')';
      execute immediate
      'create unique index deploy$scripts_unique_key on deploy$scripts' ||
      '( build_version' ||
      ', build_timestamp' ||
      ', script_directory' ||
      ', script_name' ||
      ')';
      dbms_output.put_line
      ( 'Service table deploy$script was created.'
      );
   else
      dbms_output.put_line
      ( 'Service table deploy$script already exists.'
      );
   end if;
end;
/
