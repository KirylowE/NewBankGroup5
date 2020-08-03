package newbank.dbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConnectAzureSql implements IConnect {

  private Connection dbConnection;
  private final String dbUsername;
  private final String dbPassword;
  private final String className = IConnect.class.getName();
  private static IConnect SingleInstance;

  /**
   * Passing in the parameters (username and password) for the database.
   *
   * @param dbUsername username
   * @param dbPassword password
   */
  // private constructor prevents from instantiating this class
  private ConnectAzureSql(String dbUsername, String dbPassword) {
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
  }

  /**
   * Single entry point to create only one instance of this class.
   *
   * @param dbUsername username
   * @param dbPassword password
   * @return this class instance
   */
  public static IConnect getInstance(String dbUsername, String dbPassword) {
    if (SingleInstance == null) {
      SingleInstance = new ConnectAzureSql(dbUsername, dbPassword);
    }
    return SingleInstance;
  }


  /**
   * Creation of the database connection.
   */
  public void createConnection() {
    String host = "jdbc:sqlserver://new-bank.database.windows.net:1433;database=newbank;";
    String cred = "user=" + this.dbUsername + ";password=" + this.dbPassword + ";";
    String arg1 = "encrypt=true;trustServerCertificate=false;";
    String arg2 = "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    String connectionString = host + cred + arg1 + arg2;
    try {
      dbConnection = DriverManager.getConnection(connectionString);
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Database connection error.");
      int errorCode = e.getErrorCode();
      if (errorCode == 40613) {
        System.out.println("Retry...");
        createConnection();
      }
    }
  }

  /**
   * Validates the database connection.
   *
   * @return true if connection is valid
   */
  public boolean checkConnection() {
    try {
      return dbConnection.isValid(10);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Gets all entries. Performs the SELECT query to get all entries from a table.
   *
   * @param table database table name
   * @return data from the table
   */
  public List<Map<String, Object>> getEntries(String table) {
    SqlQuery sqlQuery = new SqlQuery("SELECT * FROM " + table + ";");
    return this.getEntries(sqlQuery);
  }

  /**
   * Get all entries. Performs the SELECT query and maps columns
   *
   * @param sqlQuery query to be executed
   * @return data from the table
   */
  public List<Map<String, Object>> getEntries(SqlQuery sqlQuery) {
    try {
      Statement statement = dbConnection.createStatement();
      ResultSet resultSet = statement.executeQuery(sqlQuery.toString());
      ResultSetMetaData metaData = resultSet.getMetaData();
      List<Map<String, Object>> results = new ArrayList<>();
      while (resultSet.next()) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          row.put(metaData.getColumnLabel(i).trim(), resultSet.getObject(i));
        }
        results.add(row);
      }
      return results;
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to retrieve entries.");
    }
    return null;
  }

  /**
   * Gets entry by id. Performs the SELECT query and returns a single item by id then maps columns.
   *
   * @param table  database table
   * @param pk primary key
   * @return a record from the table
   */
  public Map<String, Object> getEntryById(String table, String pk) {
    String queryStr = String.format("SELECT * FROM %s WHERE Id=%s;", table, pk);
    SqlQuery sqlQuery = new SqlQuery(queryStr);
    return this.getEntryByProperty(sqlQuery);
  }


  /**
   * Gets entry by property. Performs the SELECT query and maps columns.
   *
   * @param sqlQuery query to be executed
   * @return data from the table
   */
  public Map<String, Object> getEntryByProperty(SqlQuery sqlQuery) {
    try {
      Statement statement = dbConnection.createStatement();
      ResultSet resultSet = statement.executeQuery(sqlQuery.toString());
      ResultSetMetaData metaData = resultSet.getMetaData();
      resultSet.next();
      Map<String, Object> row = new HashMap<>();
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        row.put(metaData.getColumnLabel(i).trim(), resultSet.getObject(i));
      }
      return row;
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.INFO, "Unable to retrieve entry.");
    }
    return null;
  }

  /**
   * Creates a new entry using the prepared SQL query.
   *
   * @param sqlQuery query to be executed
   */
  public void createEntry(SqlQuery sqlQuery) {
    try {
      Statement statement = dbConnection.createStatement();
      statement.executeUpdate(sqlQuery.toString());
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to create entry.", e);
    }
  }

  /**
   * Updates entry using parameters.
   *
   * @param table  database table
   * @param pk primary key
   */
  public void updateEntry(String table, String field, String value, String pk) {
    String query = "UPDATE " + table + " SET " + field + "='" + value + "' WHERE Id=" + pk + ";";
    SqlQuery sqlQuery = new SqlQuery(query);
    this.updateEntry(sqlQuery);
  }

  /**
   * Updates entry using the prepared SQL query.
   *
   * @param sqlQuery query to be executed
   */
  public void updateEntry(SqlQuery sqlQuery) {
    try {
      Statement statement = dbConnection.createStatement();
      statement.executeUpdate(sqlQuery.toString());
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to update entry.", e);
    }
  }
}
