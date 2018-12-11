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
select 'Script runtime was ' || current_timestamp - to_timestamp(:'start_timestamp', 'yyyymmddhh24miss') script_runtime
     , 'Script started at ' || to_timestamp(:'start_timestamp', 'yyyymmddhh24miss') || ' and finished at ' || current_timestamp script_info
\gset
\qecho
\qecho :script_runtime
\qecho :script_info
