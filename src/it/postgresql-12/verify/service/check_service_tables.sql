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
