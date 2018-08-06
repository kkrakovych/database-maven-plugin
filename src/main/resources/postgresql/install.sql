<#--
  -- Copyright 2018 Kostyantyn Krakovych
  --
  -- Licensed under the Apache License, Version 2.0 (the "License");
  -- you may not use this file except in compliance with the License.
  -- You may obtain a copy of the License at
  --
  --     http://www.apache.org/licenses/LICENSE-2.0
  --
  -- Unless required by applicable law or agreed to in writing, software
  -- distributed under the License is distributed on an "AS IS" BASIS,
  -- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  -- See the License for the specific language governing permissions and
  -- limitations under the License.
  -->
<#compress>

\include ./${serviceDirectory}/script_information.sql
\include ./${serviceDirectory}/psql_setup.sql
\include ./${serviceDirectory}/log_start.sql
\include ./${serviceDirectory}/deploy_information.sql
\include ./${serviceDirectory}/install_database_${database.name}.sql
\include ./${serviceDirectory}/log_finish.sql
\quit

</#compress>
