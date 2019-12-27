create table if not exists deploy_version
(
    sys_timestamp           DateTime,
    build_version           String,
    build_timestamp         DateTime,
    deploy_start_timestamp  DateTime,
    deploy_finish_timestamp Nullable(DateTime),
    deploy_status           String,
    is_current              FixedString(1)
) engine = ReplacingMergeTree(sys_timestamp)
      order by (build_version, build_timestamp, deploy_start_timestamp);

create table if not exists deploy_scripts
(
    sys_timestamp           DateTime,
    build_version           String,
    build_timestamp         DateTime,
    script_directory        String,
    script_name             String,
    script_checksum         String,
    script_start_timestamp  DateTime,
    script_finish_timestamp Nullable(DateTime),
    deploy_status           String
) engine = ReplacingMergeTree(sys_timestamp)
      order by (script_directory, script_name, build_version, build_timestamp, script_start_timestamp);
