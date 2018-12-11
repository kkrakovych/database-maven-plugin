prompt [FAILURE] - Script &script_name was already applied with different checksum.
declare
   c_msg_error constant varchar2(1000 char) := 'One time script checksum mismatch.';
begin
   raise_application_error(-20001, c_msg_error);
end;
/
