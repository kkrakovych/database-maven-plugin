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
\qecho Check service tables.
start transaction;
create table if not exists deploy$version
( build_version           varchar(100)
, build_timestamp         timestamp
, deploy_start_timestamp  timestamp
, deploy_finish_timestamp timestamp
, deploy_status           varchar(100)
, is_current              char
);
create table if not exists deploy$scripts
( build_version           varchar(100)
, build_timestamp         timestamp
, script_directory        varchar(100)
, script_name             varchar(100)
, script_checksum         varchar(100)
, script_start_timestamp  timestamp
, script_finish_timestamp timestamp
, deploy_status           varchar(100)
);
create unique index if not exists deploy$scripts_unique_key on deploy$scripts
( build_version
, build_timestamp
, script_directory
, script_name
);
commit;
