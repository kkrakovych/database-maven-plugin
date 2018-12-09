column script_runtime new_value script_runtime noprint
column script_info new_value script_info noprint
select 'Script runtime was ' || (cast(sysdate as timestamp) - cast(to_date(&start_timestamp, 'yyyymmddhh24miss') as timestamp)) || '.' script_runtime
, 'Script started at ' || to_char(to_date(&start_timestamp, 'yyyymmddhh24miss'), 'yyyy-mm-dd hh24:mi:ss') || ' and finished at ' || to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss') || '.' script_info
from dual;
prompt &script_runtime
prompt &script_info