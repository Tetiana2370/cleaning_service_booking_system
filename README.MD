### PREREQUISITES

1. JDK 11
2. PostgreSQL

##### How to run the server?
Before running the application, create user **csms** and the database with same name. <br />
`$ alter user csms createdb createuser password 'qwe123'` <br />

Then, make sure this database is accessible for other applications - check out your pg_hba.conf. <br />
If you don't know on which port your postgresql server is running, execute the following query: <br />
`SELECT name, setting  FROM pg_settings WHERE name = 'port';` <br />

**Note**: if this value is different from the one set at the application.yml - change the value in the file. <br />

##### Credentials
*login*: admin<br />
*password*: admin<br />
