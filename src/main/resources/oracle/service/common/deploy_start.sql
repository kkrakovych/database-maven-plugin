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

prompt Start deploy version.

declare
   c_no            constant varchar2(1 char)  := 'N';
   c_yes           constant varchar2(1 char)  := 'Y';
   c_not_completed constant varchar2(13 char) := 'NOT COMPLETED';
   c_date_format   constant varchar2(21 char) := 'YYYY-MM-DD HH24:MI:SS';
begin
   update deploy$version v
      set v.is_current   = c_no
    where v.is_current   = c_yes;
   insert
     into deploy$version
        ( build_version
        , build_timestamp
        , deploy_start_timestamp
        , deploy_finish_timestamp
        , deploy_status
        , is_current
        )
   values
        ( '${buildVersion}'
        , to_date('${buildTimestamp}', c_date_format)
        , sysdate
        , null
        , c_not_completed
        , c_yes
        );
   commit;
end;
/

</#compress>
