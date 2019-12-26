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
\qecho Finish deploy version.
start transaction;
do $$
declare
   c_yes       constant varchar(1) := 'Y';
   c_completed constant varchar(9) := 'COMPLETED';
begin
   update deploy$version
      set deploy_finish_timestamp = current_timestamp
        , deploy_status           = c_completed
    where is_current              = c_yes;
end$$;
commit;
