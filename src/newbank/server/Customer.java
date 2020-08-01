package newbank.server;

import newbank.dbase.Dispatcher;

import java.util.ArrayList;
import java.util.List;

public class Customer {

  private List<Account> accounts;

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
    this.accounts = new ArrayList<>();
    this.primaryKey = primaryKey;
    this.userName = userName;
    // Start the dispatcher before running database operations
    this.dispatcher = Dispatcher.getInstance();
    this.accounts = this.dispatcher.readAccounts(this);
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

  public List<Account> getAccounts() {
    return this.accounts;
  }

  public List<Account> loadAccounts() {
    this.accounts = this.dispatcher.readAccounts(this);
    return this.accounts;
  }

  public void updateAccounts() {
    this.dispatcher.updateAccounts(this);
  }

  public String printAccounts() {
    this.loadAccounts();
    StringBuilder sb = new StringBuilder();
    sb.append("\n\n");
    sb.append(String.format("%1s", "Id"));
    sb.append(String.format("%6s", "Name"));
    sb.append(String.format("%20s", "Balance"));
    sb.append("\n-----------------------------------\n");
    for (Account acc : this.accounts) {
      sb.append(String.format("%1s", acc.getPrimaryKey()));
      sb.append(String.format("%6s", acc.getAccountName()));
      sb.append(String.format("%20s", acc.getBalance()));
      sb.append("\n");
    }
    sb.append("-----------------------------------\n");
    return sb.toString();
  }

  public String accountsToString() {
    String s = "";
    for (Account a : accounts) {
      s += a.toString() + "\n";
    }
    return s;
  }

  public void addAccount(Account account) {
    accounts.add(account);
  }

  /**
   * addingMoneyToBalance allows each customer to request to add money to one of his/her acconts
   * @param typeAccount
   * @param amountToAdd
   * @return
   */

  public String addingMoneyToBalance(String typeAccount, double amountToAdd) {
    System.out.println("Adding " + amountToAdd + " to account " + typeAccount + " for user " + this.getUserName());
    for (Account a : this.accounts) {
        System.out.println(a);
        System.out.println(a.getAccountName());
      if (a.getAccountName().equals(typeAccount)) {
        a.addMoneyToBalance(amountToAdd);
        // save accounts to database
        this.dispatcher.updateAccounts(this);
        return "\nREQUEST ACCEPTED" + " - " + " DEPOSIT " + a.getAccountName() + ". NEW BALANCE = " + a.getBalance() + "\n";
      }
    }
    return "REQUEST DENIED" + " - " + " DEPOSIT " + typeAccount;
  }


  /**
   * withdrawingMoneyToBalance allows each customer to request to withdraw money from one of his/her acconts
   * @param typeAccount
   * @param amountToSubtract
   * @return
   */

  public String withdrawingMoneyToBalance(String typeAccount, double amountToSubtract) {
    for (Account a : accounts) {
      if (a.getAccountName().equals(typeAccount)) {
        if (amountToSubtract < a.getBalance()) {
          a.subtractMoneyToBalance(amountToSubtract);
          // save accounts to database
          this.dispatcher.updateAccounts(this);
          return "REQUEST ACCEPTED " + " - " + " WITHDRAW " + a.getAccountName();
        } else return "REQUEST DENIED " + " - " + " Not enough money for bank account " + a.getAccountName() + " withdrawal";
      }
    }
    return "REQUEST DENIED" + " - " + " WITHDRAW " + typeAccount;
  }


  /**
   * addNewCustomerAccount allows customers to add one account
   * @param name
   * @return
   */

  public Boolean addNewCustomerAccount(String name) {
    Boolean accFound = false;
    for (Account acc : accounts) {
      if (acc.getAccountName().equals(name)) {
        accFound = true;
      }
    }
    return accFound;
  }


  /**
   * pay allows each customer to give money to another customer
   * @param nameAccountSendsMoney
   * @param ownerAccountNameReceivesMoney
   * @param accountNameReceivesMoney
   * @param amountToTransfer
   * @return
   */

  public Boolean pay(String nameAccountSendsMoney, String ownerAccountNameReceivesMoney, String accountNameReceivesMoney, double amountToTransfer) {
    for (Account a : accounts) {
      if (a.getAccountName().equalsIgnoreCase(nameAccountSendsMoney)) {
        if (amountToTransfer <= a.getBalance()) {
          a.subtractMoneyToBalance(amountToTransfer);
          // save accounts to database
          this.dispatcher.updateAccounts(this);
          return true;
        }
      }
      return false;
    }
    return false; // if the flow does not enter in the for
  }


  /**
   * move allows each customer to transfer money from one account to another one
   * @param accountFrom
   * @param accountTo
   * @param amountToMove
   * @return
   */

  public String move(String accountFrom, String accountTo, double amountToMove) {

    for (Account a : accounts) {
      if (a.getAccountName().equals(accountFrom)) {
        if (amountToMove <= a.getBalance()) {
          a.subtractMoneyToBalance(amountToMove);
          for (Account b : accounts) {
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


}
