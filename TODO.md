1. Add description about circular dependencies.
2. Verify templates and rearrange them.
3. Java doc
4. Add checks for schema, scripts and etc indexes.
5. Add option to disable timing output.
6. Review and amend service scripts. 
7. Review .toString() calls - in scope of Paths actions.


Oracle levels     : database / schema / object
PostgreSQL levels : instance / database / schema / object
             real : database / schema / object - because `psql` does not support start without connection.

SET search_path TO ...
