column execute_before new_value v_execute_before noprint
column execute_script new_value v_execute_script noprint
column execute_after new_value v_execute_after noprint
set termout off
with
script_info as
( select count(1) executed
, coalesce
( sum
( case
when s.script_checksum = '&script_checksum'
then 1
else 0
end
)
, 0
) checksum
from deploy$scripts s
where s.script_directory = '&script_directory'
and s.script_name = '&script_name'
and s.deploy_status = 'COMPLETED'
)
select case
when executed > 0
then './service/one_time_do_nothing.sql'
when executed = 0
then './service/one_time_do_before.sql'
end execute_before
, case
when executed > 0
and checksum > 0
then './service/one_time_do_success.sql'
when executed > 0
and checksum = 0
then './service/one_time_do_checksum.sql'
when executed = 0
and checksum = 0
then '&script_name_full'
end execute_script
, case
when executed > 0
then './service/one_time_do_nothing.sql'
when executed = 0
then './service/one_time_do_after.sql'
end execute_after
from script_info;
set termout on
@&v_execute_before
@&v_execute_script
@&v_execute_after