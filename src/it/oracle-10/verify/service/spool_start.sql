column dt new_value start_timestamp noprint
select to_char(sysdate, 'yyyymmddhh24miss') dt from dual;
spool install_auto_database_test_&start_timestamp..log