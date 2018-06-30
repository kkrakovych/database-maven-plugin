/*
 * Copyright 2018 Kostyantyn Krakovych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt [database] oracle database version [test] created at [2018-06-28 21:15:31]

@./.service/input_parameters.sql
@./.service/sqlplus_setup.sql
@./.service/check_connections.sql

column dt new_value timestamp noprint
select to_char(sysdate, 'YYYYMMDDHH24MISS') dt from dual;
spool install_manual_database_test_&timestamp..log

@./.service/deploy_information.sql

@./database/schema_b/install.sql
@./database/schema_a/install.sql

prompt

spool off

exit
