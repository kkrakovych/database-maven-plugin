prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt Oracle database [database] version [test] created at [2018-07-01 23:15:02]
@./.service/input_parameters.sql
@./.service/sqlplus_setup.sql
@./.service/check_connections.sql
column dt new_value timestamp noprint
select to_char(sysdate, 'yyyymmddhh24miss') dt from dual;
spool install_manual_database_test_&timestamp..log
@./.service/deploy_information.sql
@./install_database_database.sql
prompt
spool off
exit