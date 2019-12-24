\qecho Execute ONE_TIME scripts with AFTER condition.
\qecho Execute /database/schema/script_one_time_after/dummy_s1_one_time_after_a.sql
\set script_directory '/database/schema/script_one_time_after/'
\set script_name      'dummy_s1_one_time_after_a.sql'
\set script_name_full './database/schema/script_one_time_after/dummy_s1_one_time_after_a.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
\qecho Execute /database/schema/script_one_time_after/dummy_s1_one_time_after_b.sql
\set script_directory '/database/schema/script_one_time_after/'
\set script_name      'dummy_s1_one_time_after_b.sql'
\set script_name_full './database/schema/script_one_time_after/dummy_s1_one_time_after_b.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
\qecho Execute /database/schema/script_one_time_after/dummy_s1_one_time_after_c.sql
\set script_directory '/database/schema/script_one_time_after/'
\set script_name      'dummy_s1_one_time_after_c.sql'
\set script_name_full './database/schema/script_one_time_after/dummy_s1_one_time_after_c.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
