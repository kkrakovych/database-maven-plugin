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
prompt === Deploy schema ${schema.name}
prompt

connect &usr_${schema.name}/&pwd_${schema.name}@&tns_name

@./${serviceDirectory}/sqlplus_setup.sql
@./${serviceDirectory}/check_deploy_tables.sql
@./${serviceDirectory}/deploy_start.sql

<#list schema.objects as object>
@./${database.sourceDirectory}/${schema.sourceDirectory}/${object.sourceDirectory}/install_${object.index}_${object.type}.sql
</#list>

@./${serviceDirectory}/compile_schema.sql
@./${serviceDirectory}/deploy_finish.sql
