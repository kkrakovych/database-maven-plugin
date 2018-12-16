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
update deploy$scripts            s
   set s.script_finish_timestamp = sysdate
     , s.deploy_status           = 'COMPLETED'
 where s.script_directory        = '&script_directory'
   and s.script_name             = '&script_name'
   and s.build_version           = '${buildVersion}'
   and s.build_timestamp         = to_date('${buildTimestamp}', 'yyyy-mm-dd hh24:mi:ss')
/
commit
/
prompt [SUCCESS] - Script &script_name was applied.
