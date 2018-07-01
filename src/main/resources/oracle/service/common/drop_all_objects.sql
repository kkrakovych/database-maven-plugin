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

prompt Drop all objects.

begin
   for rec in
   ( select case
               when o.object_type = 'TYPE'
               then 'drop type "' || o.object_name || '" force'
               when o.object_type in ( 'FUNCTION'
                                     , 'PACKAGE'
                                     , 'PACKAGE BODY'
                                     , 'PROCEDURE'
                                     , 'TRIGGER'
                                     , 'TYPE BODY'
                                     , 'VIEW'
                                     )
               then 'drop ' || o.object_type || ' "' || o.object_name || '"'
            end                   as cmd
       from user_objects          o
      where o.object_type         in ( 'FUNCTION'
                                     , 'PACKAGE'
                                     , 'PACKAGE BODY'
                                     , 'PROCEDURE'
                                     , 'TRIGGER'
                                     , 'TYPE'
                                     , 'TYPE BODY'
                                     , 'VIEW'
                                     )
      order by
            case
               /* Orphaned SYS_PLSQL_% Objects Are Created When Using Data Pump Import or Original Import (Doc ID 757588.1) */
               when o.object_name like 'SYS_PLSQL%'
               then 0
               when o.object_type like '% BODY'
               then 1
               else 2
            end
   )
   loop
      execute immediate rec.cmd;
   end loop;
end;
/

</#compress>
