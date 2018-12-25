\qecho Execute ONE_TIME scripts with BEFORE condition.
\qecho Execute /database/schema/script_one_time_before/dummy_s1_one_time_before_a.sql
\set script_directory '/database/schema/script_one_time_before/'
\set script_name      'dummy_s1_one_time_before_a.sql'
\set script_name_full './database/schema/script_one_time_before/dummy_s1_one_time_before_a.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
\qecho Execute /database/schema/script_one_time_before/dummy_s1_one_time_before_b.sql
\set script_directory '/database/schema/script_one_time_before/'
\set script_name      'dummy_s1_one_time_before_b.sql'
\set script_name_full './database/schema/script_one_time_before/dummy_s1_one_time_before_b.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
\qecho Execute /database/schema/script_one_time_before/dummy_s1_one_time_before_c.sql
\set script_directory '/database/schema/script_one_time_before/'
\set script_name      'dummy_s1_one_time_before_c.sql'
\set script_name_full './database/schema/script_one_time_before/dummy_s1_one_time_before_c.sql'
\set script_checksum  '04371E333E486157DD0A288728259764'
\include ./service/one_time_control.sql
