prompt Execute ONE_TIME scripts with AFTER condition.
prompt Execute /database/schema/script_one_time_after/dummy_a.sql
define script_directory = '/database/schema/script_one_time_after/'
define script_name = 'dummy_a.sql'
define script_name_full = '@./database/schema/script_one_time_after/dummy_a.sql'
define script_checksum = '04371E333E486157DD0A288728259764'
@./service/one_time_control.sql
prompt Execute /database/schema/script_one_time_after/dummy_b.sql
define script_directory = '/database/schema/script_one_time_after/'
define script_name = 'dummy_b.sql'
define script_name_full = '@./database/schema/script_one_time_after/dummy_b.sql'
define script_checksum = '04371E333E486157DD0A288728259764'
@./service/one_time_control.sql
prompt Execute /database/schema/script_one_time_after/dummy_c.sql
define script_directory = '/database/schema/script_one_time_after/'
define script_name = 'dummy_c.sql'
define script_name_full = '@./database/schema/script_one_time_after/dummy_c.sql'
define script_checksum = '04371E333E486157DD0A288728259764'
@./service/one_time_control.sql