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
prompt Finish deploy version.
declare
   c_yes       constant varchar2(1 char) := 'Y';
   c_completed constant varchar2(9 char) := 'COMPLETED';
begin
   update deploy$version            v
      set v.deploy_finish_timestamp = sysdate
        , v.deploy_status           = c_completed
    where v.is_current              = c_yes;
   commit;
end;
/
