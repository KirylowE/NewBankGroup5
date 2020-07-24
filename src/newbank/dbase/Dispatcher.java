package newbank.dbase;

import newbank.server.Account;
import newbank.server.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages all actions related to the data persistence,
 * a bridge between the database read and write operations and the business logic.
 * Methods in this class retrieve and process the data. Other classes can request data
 * and expect Java objects returned rather than a string/text format
 * <p>
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

  public static Dispatcher getInstance() {
    if (SingleInstance == null) {
      SingleInstance = new Dispatcher();
    }
    return SingleInstance;
  }


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

  private String getStatus() {
    return dbase.checkConnection() ? "OK" : "Failed";
  }

  public HashMap<String, Customer> getCustomers() {
    // TODO: create central mapping for the table names

    HashMap<String, Customer> output = new HashMap<>();
    List<Map<String, Object>> entries = dbase.getEntries("Customer");

    for (Map<String, Object> entry : entries) {
      // entity must be created with the primary key provided
      String primaryKey = entry.get("Id").toString();
      Customer customer = new Customer(primaryKey);
      // data fields are saved using field setters
      customer.setFirstName(entry.get("FirstName").toString());
      customer.setLastName(entry.get("LastName").toString());
      // add customer object to the collection
      output.put(entry.get("FirstName").toString(), customer);
    }
    return output;
  }

  public HashMap<String, Account> getAccounts() {
    // TODO: create central mapping for the table names

    HashMap<String, Account> output = new HashMap<>();
    List<Map<String, Object>> entries = dbase.getEntries("Accounts");

    for (Map<String, Object> entry : entries) {
      // entity must be created with the primary key provided
      String primaryKey = entry.get("Id").toString();
      Account account = new Account(primaryKey, entry.get("AccountTypeID").toString(), Double.parseDouble(entry.get("Balance").toString()));
      // data fields are saved using field setters
      // ...
      // add entity to the collection
      output.put(entry.get("Id").toString(), account);
    }
    return output;
  }


  public HashMap<String, Customer> getCustomersAccounts() {
    // TODO: create central mapping for the table names

    HashMap<String, Customer> customers = new HashMap<>();
    HashMap<String, Customer> tbCustomer = this.getCustomers();
    HashMap<String, Account> tbAccounts = this.getAccounts();

    return customers;
  }


  /**
   * Example usages of the 'dbase' object which is publicly available through Dispatcher.
   *
   * Inside the dispatcher class:
   * ----------------------------
   * dbase.createEntry(new SqlQuery("INSERT INTO Customer (FirstName, LastName) VALUES ('Ray', 'Meyer');"));
   *
   * Everywhere else, after instantiating a Dispatcher object:
   * ---------------------------------------------------------
   * Dispatcher dispatcher = Dispatcher.getInstance();
   * dispatcher.dbase.createEntry(new SqlQuery("INSERT INTO Customer (FirstName, LastName) VALUES ('Ray', 'Meyer');"));
   *
   **/

  public static void main(String[] args) {

    System.out.println("creating");

    String dbUsername = EnvironmentVariable.getVariableValue("NB_DB_USERNAME");
    String dbPassword = EnvironmentVariable.getVariableValue("NB_DB_PASSWORD");
    if (dbUsername == null || dbPassword == null) {
      throw new Error("Please set the environment variables NB_DB_USERNAME and NB_DB_PASSWORD");
    }
    IConnect dbase = ConnectAzureSql.getInstance(dbUsername, dbPassword);
    dbase.createConnection();
    List<Map<String, Object>> results = dbase.getEntries("Customer");
    System.out.println(results);
    System.out.println("-----------");
    Map<String, Object> result = dbase.getEntryById("Customer", "2");
    System.out.println(result);
    System.out.println("-----------");
    dbase.updateEntry("Customer", "2");
    dbase.updateEntry(new SqlQuery("UPDATE Customer SET FirstName='Sabrina' WHERE 'Id'='2';"));
    Map<String, Object> updatedResult = dbase.getEntryById("Customer", "2");
    System.out.println(updatedResult);
    System.out.println("-----------");
    dbase.createEntry("Customer");
    dbase.createEntry(new SqlQuery("INSERT INTO Customer (FirstName, LastName) VALUES ('Ray', 'Meyer');"));

  }
}
