prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt Oracle database [database] version [test] created at [2018-08-05 12:04:27]
@./service/input_parameters_manual.sql
@./service/sqlplus_setup.sql
@./service/check_connections.sql
column dt new_value start_timestamp noprint
select to_char(sysdate, 'yyyymmddhh24miss') dt from dual;
spool install_manual_database_test_&start_timestamp..log
@./service/deploy_information.sql
@./service/install_database_database.sql
prompt
column script_runtime new_value script_runtime noprint
column script_info new_value script_info noprint
select 'Script runtime was ' || (cast(sysdate as timestamp) - cast(to_date(&start_timestamp, 'yyyymmddhh24miss') as timestamp)) || '.' script_runtime
, 'Script started at ' || to_char(to_date(&start_timestamp, 'yyyymmddhh24miss'), 'yyyy-mm-dd hh24:mi:ss') || ' and finished at ' || to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss') script_info
from dual;
prompt &script_runtime
prompt &script_info
spool off
exit