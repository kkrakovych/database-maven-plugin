prompt Compile schema objects.
begin
   dbms_utility.compile_schema
   ( schema         => user
   , compile_all    => false
   , reuse_settings => false
   );
end;
/
