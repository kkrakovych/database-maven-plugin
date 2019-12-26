<#--
  -- Copyright 2019 Kostyantyn Krakovych
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
update deploy$scripts
   set script_finish_timestamp = current_timestamp
     , deploy_status           = 'COMPLETED'
 where script_directory        = :'script_directory'
   and script_name             = :'script_name'
   and build_version           = '${buildVersion}'
   and build_timestamp         = to_timestamp('${buildTimestamp}', 'yyyy-mm-dd hh24:mi:ss');
commit;
\qecho [SUCCESS] - Script :script_name was applied.
