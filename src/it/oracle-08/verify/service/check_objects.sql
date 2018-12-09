prompt Check objects.
declare
c_valid constant varchar2(1000 char) := 'VALID';
c_msg_info constant varchar2(1000 char) := 'Number of invalid schema''s objects = ';
c_msg_dot constant varchar2(1000 char) := '.';
c_msg_error constant varchar2(1000 char) := 'The schema contains invalid objects.';
l_cnt pls_integer;
begin
dbms_output.put(c_msg_info);
select count(1)
into l_cnt
from user_objects o
where o.status <> c_valid;
dbms_output.put_line(l_cnt || c_msg_dot);
if l_cnt <> 0
then
raise_application_error(-20001, c_msg_error);
end if;
end;
/