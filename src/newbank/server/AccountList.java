package newbank.server;

import newbank.dbase.Dispatcher;

import java.util.List;

public class AccountList {

  public List<Account> getAccounts() {
    return accounts;
  }

  private Customer customer;
  private List<Account> accounts;
  private Dispatcher dispatcher;

  public AccountList(Customer customer) {
    this.customer = customer;
    // Start the dispatcher before running database operations
    this.dispatcher = Dispatcher.getInstance();
    this.accounts = this.dispatcher.readAccounts(customer);
  }

  public List<Account> loadAccounts() {
    this.accounts = this.dispatcher.readAccounts(this.customer);
    return this.accounts;
  }

  public void updateAccounts() {
    this.dispatcher.updateAccounts(this.customer);
  }

  public void addAccount(Account account) {
    this.accounts.add(account);
  }

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
