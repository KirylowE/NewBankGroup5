**Welcome to NewBank - Giving Back Control**
============================================

Version: 0.2.0 (Prototype for submission)
-----------------------------------------

This project aims to fill a gap in the retail banking market by providing a terminal interface for bank account management.

**JDK baseline required**
* Java 11 or later

**Dependencies:**
* mssql jdbc 8.2.2 or later.

The applications:

* NewBank
* Dispatcher
* ExampleClient
* NewBankServer

should be run with the following environment variables:

NB_DEV=false;NB_DB_USERNAME=[username];NB_DB_PASSWORD=[password]

Please contact a group 5 member for the username and password.

NOTE: NB_DEV variable should be changed to true to use the test accounts described in NewBank.java.  Switching to false uses Azure SQL database instead.

NewBank Azure SQL DB settings:
Host: new-bank.database.windows.net
Port: 1433
Database: NewBank

**Running the program:**
1. Run the application src\newbank\server\NewBankServer, it will listen for client connections.
2. Run src\newbank\client\ExampleClient and it should connect.

The menu:
1. SHOW MY ACCOUNTS  (display accounts for logged in user)
2. DEPOSIT MONEY   (add money to a specified account)
3. WITHDRAW MONEY  (remove money from a specified account)
4. CREATE NEW ACCOUNT  (create a new account owned by the current user)
5. MOVE MONEY TO ANOTHER ACCOUNT   (move money between accounts owned by the logged in user)
6. PAY SOMEONE (move money to another NewBank user's account)
7. BECOME A LENDER (register as a lender for NewBank's microloan service)
8. SHOW LENDER (display a list of available micro-lenders)
9. GET MICROLOAN   (borrow from a micro-lender)
10. SHOW LOANS (display currently outstanding loans for the logged in user)
11. MODIFY CUSTOMER DETAILS    (amend personal details for logged in user)
12. LOG OUT (log out current user - returns to login)

**Known issues:**
The Azure database is not on a production tier, so may go into a sleep mode if not accessed for a period of time.  Connection attempts should rectify this, please try again.

Contribute to the project: [NewBank] (https://github.com/KirylowE/NewBankGroup5):
* Please do not save credentials in plain text in new branches!

Authors:
Group 5