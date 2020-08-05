package newbank.dbase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newbank.server.Account;
import newbank.server.Customer;

/**
 * This class manages all actions related to the data persistence,
 * a bridge between the database read and write operations and the business logic.
 * Methods in this class retrieve and process the data. Other classes can request data
 * and expect Java objects returned rather than a string/text format
 * It uses a basic singleton pattern to ensure that it can only be instantiated once.
 */
public final class Dispatcher {

  /**
   * This class also instantiates and exposes the IConnect as 'dbase' object, which gives
   * direct access to the Create, Read and Update methods using the following syntax:
   * ```
   * Dispatcher dispatcher = Dispatcher.getInstance();
   * dispatcher.dbase.getEntries("Customer");
   * ```
   * Most actions should be started through the methods included in this class,
   * the access to 'dbase' is given for additional flexibility
   **/
  public IConnect dbase;

  private static Dispatcher SingleInstance;

  // private constructor prevents from instantiating this class
  private Dispatcher() {
    this.createDbaseConnection();
  }

  /**
   * Single entry point to create only one instance of this class.
   *
   * @return this class instance
   */
  public static Dispatcher getInstance() {
    if (SingleInstance == null) {
      SingleInstance = new Dispatcher();
    }
    return SingleInstance;
  }


  /**
   * Creates a database connection using the values from the env variables.
   */
  private void createDbaseConnection() {
    System.out.print("Creating a database connection...");

    String dbUsername = EnvironmentVariable.getVariableValue("NB_DB_USERNAME");
    String dbPassword = EnvironmentVariable.getVariableValue("NB_DB_PASSWORD");

    if (dbUsername == null || dbPassword == null) {
      throw new Error("Please set the environment variables NB_DB_USERNAME and NB_DB_PASSWORD");
    }

    this.dbase = ConnectAzureSql.getInstance(dbUsername, dbPassword);
    this.dbase.createConnection();
    System.out.println(this.getStatus());
  }

  /**
   * Prints status of the database connection.
   *
   * @return "OK" if connection is live
   */
  private String getStatus() {
    return this.dbase.checkConnection() ? "OK" : "Failed";
  }


  /**
   * Get customer by user name.
   *
   * @param userNameEntered user name
   * @return Customer object
   */
  public Customer getCustomerByUserName(String userNameEntered) {
    // TODO: create central mapping for the table names
    String query = "SELECT * FROM Customer WHERE UserName='" + userNameEntered + "';";
    SqlQuery sqlQuery = new SqlQuery(query);
    Map<String, Object> entry = this.dbase.getEntryByProperty(sqlQuery);
    if (entry != null) {
      // entity must be created with the primary key  and user name provided
      String primaryKey = entry.get("Id").toString();
      String userName = entry.get("UserName").toString();
      Customer customer = new Customer(primaryKey, userName);
      // data fields are saved using field setters
      customer.setFirstName(entry.get("FirstName").toString());
      customer.setLastName(entry.get("LastName").toString());
      return customer;
    }
    return null;
  }

  /**
   * Get all Customers mapped to the correct Java data structure.
   *
   * @return Collection of Customers
   */
  public HashMap<String, Customer> getCustomers() {
    // TODO: create central mapping for the table names

    HashMap<String, Customer> output = new HashMap<>();
    List<Map<String, Object>> entries = this.dbase.getEntries("Customer");

    for (Map<String, Object> entry : entries) {
      // entity must be created with the primary key  and user name provided
      String primaryKey = entry.get("Id").toString();
      String userName = entry.get("UserName").toString();
      Customer customer = new Customer(primaryKey, userName);
      // data fields are saved using field setters
      customer.setFirstName(entry.get("FirstName").toString());
      customer.setLastName(entry.get("LastName").toString());
      // add customer object to the collection
      output.put(entry.get("UserName").toString(), customer);
    }
    return output;
  }

  /**
   * Get the customer Accounts mapped to the correct Java data structure.
   *
   * @param customer Customer object
   * @return Collection of accounts for the customer
   */
  public List<Account> readAccounts(Customer customer) {
    /*
     *  SELECT Accounts.Id, Accounts.CustomerID, Accounts.Balance, AccountTypes.AccountType
     *  FROM Accounts
     *  INNER JOIN AccountTypes ON AccountTypes.Id = Accounts.AccountTypeID
     *  WHERE CustomerID = 1;
     */
    List<Account> output = new ArrayList<>();
    SqlQuery sqlQuery = new SqlQuery(
        "SELECT Accounts.Id, Accounts.CustomerID, Accounts.Balance, AccountTypes.AccountType "
            + "FROM Accounts "
            + "INNER JOIN AccountTypes ON AccountTypes.Id = Accounts.AccountTypeID "
            + "WHERE CustomerID=" + customer.getPrimaryKey() + ";");
    List<Map<String, Object>> entries = this.dbase.getEntries(sqlQuery);
    int index = 1;
    for (Map<String, Object> entry : entries) {
      // entity must be created with the primary key provided
      String primaryKey = entry.get("Id").toString();
      double balance = Double.parseDouble(entry.get("Balance").toString());
      String accountType = entry.get("AccountType").toString();
      Account account = new Account(String.valueOf(index), primaryKey, accountType, balance);
      output.add(account);
      index++;
    }
    return output;
  }

  /**
   * Get the accounts that can be offered to Customers
   * mapped to the correct Java data structure.
   *
   * @param customer Customer object
   * @return Collection of accounts available to open
   */
  public List<Account> readNewAccounts(Customer customer) {
    /*
     * SELECT AccountTypes.Id, AccountTypes.AccountType, AccountTypes.InterestRate
     * FROM AccountTypes
     * WHERE AccountTypes.Id NOT IN
     * (SELECT Accounts.AccountTypeID FROM Accounts WHERE CustomerID = 1);
     */
    List<Account> output = new ArrayList<>();
    SqlQuery sqlQuery = new SqlQuery(
        "SELECT AccountTypes.Id, AccountTypes.AccountType, AccountTypes.InterestRate "
            + "FROM AccountTypes "
            + "WHERE AccountTypes.Id NOT IN "
            + "(SELECT Accounts.AccountTypeID FROM Accounts "
            + "WHERE CustomerID=" + customer.getPrimaryKey() + ");");
    List<Map<String, Object>> entries = this.dbase.getEntries(sqlQuery);
    int index = 1;
    for (Map<String, Object> entry : entries) {
      // entity must be created with the primary key provided
      String primaryKey = entry.get("Id").toString();
      double balance = 0;
      String accountType = entry.get("AccountType").toString();
      Account account = new Account(String.valueOf(index), primaryKey, accountType, balance);
      output.add(account);
      index++;
    }
    return output;
  }


  /**
   * Update accounts for the customer.
   *
   * @param customer Customer object
   */
  public void updateAccounts(Customer customer) {
    for (Account account : customer.accounts.getAccounts()) {
      String balance = String.valueOf(account.getBalance());
      String primaryKey = account.getPrimaryKey();
      this.dbase.updateEntry("Accounts", "Balance", balance, primaryKey);
    }
  }


  /**
   * Example usages of the 'dbase' object which is publicly available through Dispatcher.
   * Inside the dispatcher class
   * (main method is an exception and requires an instantiation of dispatcher):
   * ----------------------------
   * this.dbase.createEntry(
   * new SqlQuery("INSERT INTO Customer (FirstName, LastName) VALUES ('Ray', 'Meyer');")
   * );
   * Everywhere else (including this main method), after instantiating a Dispatcher object:
   * ---------------------------------------------------------
   * Dispatcher dispatcher = Dispatcher.getInstance();
   * dispatcher.dbase.createEntry(
   * new SqlQuery("INSERT INTO Customer (FirstName, LastName) VALUES ('Ray', 'Meyer');")
   * );
   **/

  public static void main(String[] args) {
    Dispatcher dispatcher = new Dispatcher();
    dispatcher.dbase.createConnection();
    List<Map<String, Object>> results = dispatcher.dbase.getEntries("Customer");
    System.out.println(results);
    System.out.println("-----------");
    Map<String, Object> result = dispatcher.dbase.getEntryById("Customer", "2");
    System.out.println(result);
    System.out.println("-----------");
  }
}
