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
start transaction;
insert
  into deploy$scripts
     ( build_version
     , build_timestamp
     , script_directory
     , script_name
     , script_checksum
     , script_start_timestamp
     , script_finish_timestamp
     , deploy_status
     )
values
     ( '${buildVersion}'
     , to_timestamp('${buildTimestamp}', 'yyyy-mm-dd hh24:mi:ss')
     , :'script_directory'
     , :'script_name'
     , :'script_checksum'
     , current_timestamp
     , null
     , 'NOT COMPLETED'
     );
commit;
