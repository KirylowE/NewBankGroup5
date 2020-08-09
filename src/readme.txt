Welcome to NewBank Group 5

Dependencies:
Java 11 or later
mssql jdbc 8.2.2 or later.

The applications

NewBank
Dispatcher
ExampleClient
NewBankServer

should be run with the following environment variables:

NB_DEV=false;NB_DB_USERNAME=[username];NB_DB_PASSWORD=[password]

Please contact a group member for the username and password

NOTE: NB_DEV variable should be changed to true to use the test accounts described in NewBank.java.  Switching to false uses Azure SQL database instead.

NewBank Azure SQL DB settings:
Host: new-bank.database.windows.net
Port: 1433
Database: NewBank

