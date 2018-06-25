prompt FAILURE - Script &script_name was already applied with different checksum.

begin
   raise_application_error
   ( -20001
   , 'One time script checksum mismatch.'
   );
end;
/
