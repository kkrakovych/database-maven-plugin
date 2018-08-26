/*
 * Copyright 2018 Kostyantyn Krakovych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

create or replace force view vw_test_c
as
select 1           as id
     , 'something' as text
  from dual
 union all
select 2           as id
     , 'in'        as text
  from dual
 union all
select 3           as id
     , 'the'       as text
  from dual
 union all
select 3           as id
     , 'air'       as text
  from dual
/
