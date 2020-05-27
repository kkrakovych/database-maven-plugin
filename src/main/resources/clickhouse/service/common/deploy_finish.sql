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
insert into deploy_version
     ( sys_timestamp
     , build_version
     , build_timestamp
     , deploy_start_timestamp
     , deploy_finish_timestamp
     , deploy_status
     , is_current
     )
select now()
     , build_version
     , build_timestamp
     , deploy_start_timestamp
     , now()
     , 'COMPLETED'
     , is_current
  from deploy_version final
 where is_current = 'Y';
