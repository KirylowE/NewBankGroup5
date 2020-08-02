package newbank.server;

import newbank.dbase.Dispatcher;

public class Customer {

  public AccountList accounts;

  private String primaryKey;
  private String firstName;
  private String lastName;
  private String userName;

  private Dispatcher dispatcher;

  /**
   * @param primaryKey
   * @param userName
   */

  public Customer(String primaryKey, String userName) {
    this.primaryKey = primaryKey;
    this.userName = userName;
    this.accounts = new AccountList(this);
    // Start the dispatcher before running database operations
    this.dispatcher = Dispatcher.getInstance();
  }

  // setters are disabled for the primary key and user name, these must be set during object creation!
  public String getPrimaryKey() {
    return primaryKey;
  }

  public String getUserName() {
    return userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFullName() {
    if (this.firstName != null && this.lastName != null) {
      return this.firstName + " " + this.lastName;
    }
    return null;
  }

  /**
   * pay allows each customer to give money to another customer
   *
   * @param nameAccountSendsMoney
   * @param ownerAccountNameReceivesMoney
   * @param accountNameReceivesMoney
   * @param amountToTransfer
   * @return
   */
  public boolean pay(String nameAccountSendsMoney, double amountToTransfer) {
    for (Account a : accounts) {
      if (a.getAccountName().equals(nameAccountSendsMoney)) {

        if (amountToTransfer <= a.getBalance()) {
          a.subtractMoneyToBalance(amountToTransfer);
          // save accounts to database
          this.accounts.updateAccounts();
          return true;
        }
      }
    }
    return false; // if the flow does not enter in the for
  }


  /**
   * move allows each customer to transfer money from one account to another one
   *
   * @param accountFrom
   * @param accountTo
   * @param amountToMove
   * @return
   */

  public String move(String accountFrom, String accountTo, double amountToMove) {

    for (Account a : accounts.getAccounts()) {
      if (a.getAccountName().equals(accountFrom)) {
        if (amountToMove <= a.getBalance()) {
          a.subtractMoneyToBalance(amountToMove);
          for (Account b : accounts.getAccounts()) {
            if (b.getAccountName().equals(accountTo)) {
              b.addMoneyToBalance(amountToMove);
              return "SUCCESS";
            }
          }
        }
      }
    }

    return "FAIL";
  }


  public boolean validationDataLender(String accountName, Double amountToLend, Double interestRate) {
    for (Account a : accounts){
      if(a.getAccountName().equals(accountName)){
        if(amountToLend <= a.getBalance()) {
          return true;
        }
      }
    }
    return false;
  }


}
