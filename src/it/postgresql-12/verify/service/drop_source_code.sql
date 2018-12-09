start transaction;
do $$
declare
item record;
items_for_drop cursor for
select distinct
'drop trigger if exists ' || t.trigger_name || ' on ' || t.event_object_schema || '.' || t.event_object_table || ' cascade;' as txt
from information_schema.triggers t
where t.trigger_schema not in ('pg_catalog', 'information_schema')
union all
select 'drop function if exists ' || r.routine_schema || '.' || r.routine_name || ' cascade;' as txt
from information_schema.routines r
where r.routine_schema not in ('pg_catalog', 'information_schema')
and r.routine_type = 'FUNCTION'
union all
select 'drop view if exists ' || v.table_name || ' cascade;' as txt
from information_schema.views v
where v.table_schema not in ('pg_catalog', 'information_schema')
and v.table_name !~ '^pg_';
begin
open items_for_drop;
loop
fetch items_for_drop into item;
exit when not found;
execute item.txt;
end loop;
close items_for_drop;
end$$;
commit;