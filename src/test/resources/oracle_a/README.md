# Test example for Oracle database `ORACLE_A`

Before to test install script for Oracle database `ORACLE_A` please do next steps below on test database.

1. Script below should be executed under schema with dba privileges.

```oraclesqlplus
create role role_oracle_a_test;
grant connect to role_oracle_a_test;
grant create table to role_oracle_a_test;
grant create procedure to role_oracle_a_test;
grant create view to role_oracle_a_test;
grant create type to role_oracle_a_test;

create user schema_a identified by schema_a;
grant role_oracle_a_test to schema_a;
grant unlimited tablespace to schema_a;

create user schema_b identified by schema_b;
grant role_oracle_a_test to schema_b;
grant unlimited tablespace to schema_b;
```

2. Script below should be executed under schema `SCHEMA_A`.

```oraclesqlplus
create table test_a
( id   number
, text varchar2(100 char)
)
/
```

After tests please use script below to get rid of all previously created objects. The script should be done under schema with dba privileges.

```oraclesqlplus
drop user schema_a cascade;
drop user schema_b cascade;
drop role role_oracle_a_test;
```
