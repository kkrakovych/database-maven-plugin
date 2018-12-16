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
start transaction;
do $$
declare
  item record;
  items_for_drop cursor for
    select distinct
           'drop trigger if exists ' || t.trigger_name || ' on ' || t.event_object_schema || '.' || t.event_object_table || ' cascade;' as txt
      from information_schema.triggers t
     where t.trigger_schema            not in ( 'pg_catalog'
                                              , 'information_schema'
                                              )
     union all
    select 'drop function if exists ' || r.routine_schema || '.' || r.routine_name || ' cascade;' as txt
      from information_schema.routines r
     where r.routine_schema            not in ( 'pg_catalog'
                                              , 'information_schema'
                                              )
       and r.routine_type              = 'FUNCTION'
     union all
    select 'drop view if exists ' || v.table_name || ' cascade;' as txt
      from information_schema.views    v
     where v.table_schema              not in ( 'pg_catalog'
                                              , 'information_schema'
                                              )
       and v.table_name                !~ '^pg_';
begin
  open items_for_drop;
  loop
    fetch items_for_drop
     into item;
    exit when not found;
    execute item.txt;
  end loop;
  close items_for_drop;
end$$;
commit;
