package newbank.dbase;

import java.sql.*;
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
   * Passing in the parameters (username and password) for the database
   * @param dbUsername
   * @param dbPassword
   */
  // private constructor prevents from instantiating this class
  private ConnectAzureSql(String dbUsername, String dbPassword) {
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
  }

  /**
   * @param dbUsername
   * @param dbPassword
   * @return
   */
  // single entry point to create only one instance of this class
  public static IConnect getInstance(String dbUsername, String dbPassword) {
    if (SingleInstance == null) {
      SingleInstance = new ConnectAzureSql(dbUsername, dbPassword);
    }
    return SingleInstance;
  }


  /**
   * creation of the database connection
   */
  public void createConnection() {
    try {
      String host = "jdbc:sqlserver://new-bank.database.windows.net:1433;database=newbank;";
      String cred = "user=" + this.dbUsername + ";password=" + this.dbPassword + ";";
      String args = "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
      String connectionString = host + cred + args;
      dbConnection = DriverManager.getConnection(connectionString);
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Database connection error.");
    }
  }

  public boolean checkConnection() {
    try {
      return dbConnection.isValid(10);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Query of the selected table
   * @param tableName
   * @return
   */
  public List<Map<String, Object>> getEntries(String tableName) {
    SqlQuery sqlQuery = new SqlQuery("SELECT * FROM " + tableName + ";");
    return this.getEntries(sqlQuery);
  }

  /**
   * A query is given and is executed by the database. Then, there's an output of results.
   * @param sqlQuery
   * @return
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
   * @param tableName
   * @param primaryKey
   * @return
   */
  public Map<String, Object> getEntryById(String tableName, String primaryKey) {
    SqlQuery sqlQuery = new SqlQuery(String.format("SELECT * FROM %s WHERE Id=%s;", tableName, primaryKey));
    return this.getEntryByProperty(sqlQuery);
  }


  /**
   * @param sqlQuery
   * @return
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
   * @param tableName
   */
  public void createEntry(String tableName) {
    SqlQuery sqlQuery = new SqlQuery("INSERT INTO " + tableName + "(FirstName, LastName) VALUES ('Mark', 'Fieldman');");
    this.createEntry(sqlQuery);
  }

  /**
   * @param sqlQuery
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
   * @param tableName
   * @param primaryKey
   */
  public void updateEntry(String tableName, String primaryKey) {
    SqlQuery sqlQuery = new SqlQuery("UPDATE " + tableName + " SET FirstName='Sabina' WHERE Id=" + primaryKey + ";");
    this.updateEntry(sqlQuery);
  }

  /**
   * @param sqlQuery
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
