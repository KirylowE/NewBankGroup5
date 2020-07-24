package newbank.dbase;

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
    System.out.println("Creating a database connection...");

    String dbUsername = EnvironmentVariable.getVariableValue("NB_DB_USERNAME");
    String dbPassword = EnvironmentVariable.getVariableValue("NB_DB_PASSWORD");

    if (dbUsername == null || dbPassword == null) {
      throw new Error("Please set the environment variables NB_DB_USERNAME and NB_DB_PASSWORD");
    }

    this.dbase = ConnectAzureSql.getInstance(dbUsername, dbPassword);
    this.dbase.createConnection();
  }

  //public List<Map<String, Customer>> getCustomers() {
  public void getCustomers() {
    System.out.println("Getting Customers list...");
    System.out.println(dbase.checkConnection());
    String tableName = "Customer";
    List<Map<String, Object>> customers = dbase.getEntries(tableName);
    for (Object x : customers) {
      System.out.println(x);
    }
  }


  public static void main(String[] args) {

    String dbUsername = EnvironmentVariable.getVariableValue("NB_DB_USERNAME");
    String dbPassword = EnvironmentVariable.getVariableValue("NB_DB_PASSWORD");

    if (dbUsername == null || dbPassword == null) {
      throw new Error("Please set the environment variables NB_DB_USERNAME and NB_DB_PASSWORD");
    }

    /*
    IConnect dbase = ConnectAzureSql.getInstance(dbUsername, dbPassword);
    dbase.createConnection();
    List<Map<String, Object>> results = dbase.getEntries("Customer");
    System.out.println(results);
    System.out.println("-----------");
    Map<String, Object> result = dbase.getEntryById("Customer", 2);
    System.out.println(result);
    System.out.println("-----------");
    dbase.updateEntry("Customer", 2);
    dbase.updateEntry(new SqlQuery("UPDATE Customer SET FirstName='Sabrina' WHERE Id=2;"));
    Map<String, Object> updatedResult = dbase.getEntryById("Customer", 2);
    System.out.println(updatedResult);
    System.out.println("-----------");
    dbase.createEntry("Customer");
    dbase.createEntry(new SqlQuery("INSERT INTO Customer (FirstName, LastName) VALUES ('Ray', 'Meyer');"));

     */

  }
}
