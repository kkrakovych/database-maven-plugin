\qecho Execute ONE_TIME scripts with AFTER condition.
\qecho Execute /database/script_one_time_after/dummy_sp_one_time_after_a.sql
\set script_directory '/database/script_one_time_after/'
\set script_name      'dummy_sp_one_time_after_a.sql'
\set script_name_full './database/script_one_time_after/dummy_sp_one_time_after_a.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
\qecho Execute /database/script_one_time_after/dummy_sp_one_time_after_b.sql
\set script_directory '/database/script_one_time_after/'
\set script_name      'dummy_sp_one_time_after_b.sql'
\set script_name_full './database/script_one_time_after/dummy_sp_one_time_after_b.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
\qecho Execute /database/script_one_time_after/dummy_sp_one_time_after_c.sql
\set script_directory '/database/script_one_time_after/'
\set script_name      'dummy_sp_one_time_after_c.sql'
\set script_name_full './database/script_one_time_after/dummy_sp_one_time_after_c.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
