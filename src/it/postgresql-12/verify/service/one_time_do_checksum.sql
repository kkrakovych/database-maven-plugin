\qecho [FAILURE] - Script :script_name was already applied with different checksum.
do $$
begin
   raise exception 'One time script checksum mismatch.';
end$$;
