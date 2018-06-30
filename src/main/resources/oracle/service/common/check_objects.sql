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

prompt Check objects.

declare
   l_cnt pls_integer;
begin
   dbms_output.put('Number of invalid schema''s objects = ');
   select count(1)
     into l_cnt
     from user_objects o
    where o.status     <> 'VALID';
   dbms_output.put_line(l_cnt || '.');
   if l_cnt <> 0
   then
      raise_application_error(-20001, 'The schema contains invalid objects.');
   end if;
end;
/
