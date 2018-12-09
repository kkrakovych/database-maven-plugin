prompt
accept tns_name char prompt 'Enter TNS name for database [database]: '
accept usr_schema char prompt 'Enter username for schema [schema]: ' default schema
accept pwd_schema char prompt 'Enter password for schema [&usr_schema]: ' hide default &usr_schema