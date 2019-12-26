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
create table if not exists deploy_version
( sys_timestamp           DateTime
, build_version           String
, build_timestamp         DateTime
, deploy_start_timestamp  DateTime
, deploy_finish_timestamp Nullable(DateTime)
, deploy_status           String
, is_current              FixedString(1)
)
engine = ReplacingMergeTree(sys_timestamp)
order by (build_version, build_timestamp, deploy_start_timestamp)
;
create table if not exists deploy_scripts
( sys_timestamp           DateTime
, build_version           String
, build_timestamp         DateTime
, script_directory        String
, script_name             String
, script_checksum         String
, script_start_timestamp  DateTime
, script_finish_timestamp DateTime
, deploy_status           String
)
engine = ReplacingMergeTree(sys_timestamp)
order by (build_version, build_timestamp, script_directory, script_name, script_checksum, script_start_timestamp)
;
