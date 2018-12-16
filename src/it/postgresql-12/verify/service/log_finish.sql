select 'Script runtime was ' || current_timestamp - to_timestamp(:'start_timestamp', 'yyyymmddhh24miss') script_runtime
     , 'Script started at ' || to_timestamp(:'start_timestamp', 'yyyymmddhh24miss') || ' and finished at ' || current_timestamp script_info
\gset
\qecho
\qecho :script_runtime
\qecho :script_info
