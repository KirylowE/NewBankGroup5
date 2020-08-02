package newbank.server;

import java.util.List;
import newbank.dbase.Dispatcher;

public class AccountList {

  private Customer customer;
  private List<Account> accounts;
  private Dispatcher dispatcher;

  /**
   * Constructor.
   *
   * @param customer Customer object
   */
  public AccountList(Customer customer) {
    this.customer = customer;
    // Start the dispatcher before running database operations
    this.dispatcher = Dispatcher.getInstance();
    this.accounts = this.dispatcher.readAccounts(customer);
  }

  /**
   * Loads the account list from database and updates the local this.accounts field.
   */
  public void loadAccounts() {
    this.accounts = this.dispatcher.readAccounts(this.customer);
  }

  /**
   * Get the account list from this.account field without querying the database.
   *
   * @return list of accounts
   */
  public List<Account> getAccounts() {
    return accounts;
  }

  /**
   * Performs the database update.
   */
  public void updateAccounts() {
    this.dispatcher.updateAccounts(this.customer);
  }

  /**
   * Creates a new account for the customer.
   *
   * @param account customer account to be added
   */
  public void addAccount(Account account) {
    this.accounts.add(account);
  }

  /**
   * Prints the list of the customers' accounts in a tabular form in the console.
   * Note it also performs the database query to make sure the latest state is displayed.
   *
   * @return formatted table
   */
  public String printAccounts() {
    this.loadAccounts();
    StringBuilder sb = new StringBuilder();
    sb.append("\nNo");
    sb.append(" ".repeat(3));
    sb.append(String.format("%6s", "Name"));
    sb.append(String.format("%28s", "Balance"));
    sb.append("\n-------------------------------------------\n");
    for (Account acc : this.getAccounts()) {
      int index = Integer.parseInt(acc.getIndex());
      String name = acc.getAccountName();
      sb.append(index);
      sb.append(" ".repeat(7 - acc.getIndex().length())).append(acc.getAccountName());
      sb.append(" ".repeat(25 - name.length())).append(acc.getBalance());
      sb.append("\n");
    }
    sb.append("-------------------------------------------\n");
    return sb.toString();
  }

  /**
   * Allows each customer to request to add money to one of his/her acconts.
   *
   * @param typeAccount account type
   * @param amountToAdd amount to add
   * @return message string
   */

  public String addingMoneyToBalance(String typeAccount, double amountToAdd) {
    String user = this.customer.getUserName();
    System.out.println("Adding " + amountToAdd + " to account " + typeAccount + " for " + user);
    for (Account a : this.accounts) {
      System.out.println(a);
      System.out.println(a.getAccountName());
      if (a.getAccountName().equals(typeAccount)) {
        a.addMoneyToBalance(amountToAdd);
        // save accounts to database
        this.updateAccounts();
        return "\nREQUEST ACCEPTED"
            + " - "
            + " DEPOSIT " + a.getAccountName()
            + ". NEW BALANCE = " + a.getBalance()
            + "\n";
      }
    }
    return "REQUEST DENIED" + " - " + " DEPOSIT " + typeAccount;
  }

  /**
   * Allows each customer to request to withdraw money from one of his/her acconts.
   *
   * @param typeAccount      account type
   * @param amountToSubtract amount to substract
   * @return message string
   */
  public String withdrawingMoneyToBalance(String typeAccount, double amountToSubtract) {
    for (Account a : this.accounts) {
      if (a.getAccountName().equals(typeAccount)) {
        if (amountToSubtract < a.getBalance()) {
          a.subtractMoneyToBalance(amountToSubtract);
          // save accounts to database
          this.updateAccounts();
          return "REQUEST ACCEPTED " + " - " + " WITHDRAW " + a.getAccountName();
        } else {
          return "REQUEST DENIED "
              + " - "
              + " Not enough money in the account "
              + a.getAccountName()
              + " withdrawal";
        }
      }
    }
    return "REQUEST DENIED" + " - " + " WITHDRAW " + typeAccount;
  }


  /**
   * Method to add a new account for a customer. Returns True or False.
   *
   * @param name - Account Name
   * @return - returns a boolean value true if account already exists.
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


}
