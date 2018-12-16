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
with
  script_info as
( select count(1)                      executed
       , coalesce
         ( sum
           ( case
                when s.script_checksum = :'script_checksum'
                then 1
                else 0
             end
           )
         , 0
         )                             checksum
  from deploy$scripts                  s
 where s.script_directory              = :'script_directory'
   and s.script_name                   = :'script_name'
   and s.deploy_status                 = 'COMPLETED'
)
select case
          when executed                > 0
          then './${serviceDirectory}/one_time_do_nothing.sql'
          when executed                = 0
          then './${serviceDirectory}/one_time_do_before.sql'
       end                             execute_before
     , case
          when executed                > 0
           and checksum                > 0
          then './${serviceDirectory}/one_time_do_success.sql'
          when executed                > 0
           and checksum                = 0
          then './${serviceDirectory}/one_time_do_checksum.sql'
          when executed                = 0
           and checksum                = 0
          then :'script_name_full'
       end                             execute_script
     , case
          when executed                > 0
          then './${serviceDirectory}/one_time_do_nothing.sql'
          when executed                = 0
          then './${serviceDirectory}/one_time_do_after.sql'
       end                             execute_after
  from script_info
\gset
\include :execute_before
\include :execute_script
\include :execute_after
