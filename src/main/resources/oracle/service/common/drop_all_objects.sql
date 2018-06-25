prompt Drop all objects.

begin
   for rec in
   ( select case
               when o.object_type                = 'TYPE'
               then 'drop type \"' || o.object_name || '\" force'
               when o.object_type                in ( 'FUNCTION'
                                                    , 'PACKAGE'
                                                    , 'PACKAGE BODY'
                                                    , 'PROCEDURE'
                                                    , 'TRIGGER'
                                                    , 'TYPE BODY'
                                                    , 'VIEW'
                                                    )
               then 'drop ' || o.object_type || ' \"' || o.object_name || '\"'
            end                                  as cmd
       from user_objects                         o
      where o.object_type                        in ( 'FUNCTION'
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
               -- Orphaned SYS_PLSQL_% Objects Are Created When Using Data Pump Import or Original Import (Doc ID 757588.1)
               when o.object_name                like 'SYS_PLSQL%'
               then 0
               when o.object_type                like '% BODY'
               then 1
               else 2
            end
   )
   loop
      execute immediate rec.cmd;
   end loop;
end;
/
