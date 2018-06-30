<#--
  -- Copyright 2018 Kostyantyn Krakovych
  --
  -- Licensed under the Apache License, Version 2.0 (the "License");
  -- you may not use this file except in compliance with the License.
  -- You may obtain a copy of the License at
  --
  --     http://www.apache.org/licenses/LICENSE-2.0
  --
  -- Unless required by applicable law or agreed to in writing, software
  -- distributed under the License is distributed on an "AS IS" BASIS,
  -- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  -- See the License for the specific language governing permissions and
  -- limitations under the License.
  -->

prompt Check deploy tables

declare
   c_msg_service_table  constant varchar2(1000 char) := 'Service table ';
   c_tbl_deploy_version constant varchar2(1000 char) := 'deploy$version';
   c_tbl_deploy_scripts constant varchar2(1000 char) := 'deploy$scripts';
   c_msg_was_created    constant varchar2(1000 char) := 'was created.';
   c_msg_already_exists constant varchar2(1000 char) := 'already exists.';
   function table_exists(in_table_name in varchar2)
      return pls_integer
   is
      l_result pls_integer := 0;
   begin
      select count(1)
        into l_result
        from user_tables  t
       where t.table_name = upper(in_table_name);
      return l_result;
   end table_exists;
begin
   dbms_output.put(c_msg_service_table || c_tbl_deploy_version || '... ');
   if table_exists(c_tbl_deploy_version) = 0
   then
      execute immediate
      'create table deploy$version'                  ||
      '( build_version           varchar2(100 char)' ||
      ', build_timestamp         date'               ||
      ', deploy_start_timestamp  date'               ||
      ', deploy_finish_timestamp date'               ||
      ', deploy_status           varchar2(100 char)' ||
      ', is_current              char'               ||
      ')';
      dbms_output.put_line(c_msg_was_created);
   else
      dbms_output.put_line(c_msg_already_exists);
   end if;
   dbms_output.put(c_msg_service_table || c_tbl_deploy_scripts || '... ');
   if table_exists(c_tbl_deploy_scripts) = 0
   then
      execute immediate
      'create table deploy$scripts'                  ||
      '( build_version           varchar2(100 char)' ||
      ', build_timestamp         date'               ||
      ', script_directory        varchar2(100 char)' ||
      ', script_name             varchar2(100 char)' ||
      ', script_checksum         varchar2(100 char)' ||
      ', script_start_timestamp  date'               ||
      ', script_finish_timestamp date'               ||
      ', deploy_status           varchar2(100 char)' ||
      ')';
      execute immediate
      'create unique index deploy$scripts_unique_key on deploy$scripts' ||
      '( build_version'                                                 ||
      ', build_timestamp'                                               ||
      ', script_directory'                                              ||
      ', script_name'                                                   ||
      ')';
      dbms_output.put_line(c_msg_was_created);
   else
      dbms_output.put_line(c_msg_already_exists);
   end if;
end;
/
