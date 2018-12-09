select to_char(current_timestamp, 'yyyymmddhh24miss') start_timestamp
\gset
\out install_database_test_:start_timestamp.log