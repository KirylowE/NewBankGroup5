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
   * Loads the account list from database and updates the local this.account field
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
   * Note it also performs the database query to make sure the latest state is displayed
   *
   * @return formatted table
   */
  public String printAccounts() {
    this.loadAccounts();
    StringBuilder sb = new StringBuilder();
    sb.append("\n\nNo");
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


}
