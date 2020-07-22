package newbank.dbase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectAzureSql implements IConnect {

  private Connection dbConnection;
  private final String dbUsername;
  private final String dbPassword;
  private final String className = IConnect.class.getName();
  private static IConnect SingleInstance;

  // private constructor prevents from instantiating this class
  private ConnectAzureSql(String dbUsername, String dbPassword) {
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
  }

  // single entry point to create only one instance of this class
  public static IConnect getInstance(String dbUsername, String dbPassword) {
    if (SingleInstance == null) {
      SingleInstance = new ConnectAzureSql(dbUsername, dbPassword);
    }
    return SingleInstance;
  }

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

  public List<Map<String, Object>> getEntries(String tableName) {
    try {
      String sqlQuery = "SELECT * FROM " + tableName + ";";
      Statement statement = dbConnection.createStatement();
      ResultSet resultSet = statement.executeQuery(sqlQuery);
      ResultSetMetaData metaData = resultSet.getMetaData();
      List<Map<String, Object>> results = new ArrayList<>();
      while (resultSet.next()) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          row.put(metaData.getColumnLabel(i).toUpperCase(), resultSet.getObject(i));
        }
        results.add(row);
      }
      return results;
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to retrieve entries.");
    }
    return null;
  }

  public Map<String, Object> getEntryById(String tableName, Integer primaryKey) {
    try {
      String sqlQuery = "SELECT * FROM " + tableName + " WHERE Id=" + primaryKey + ";";
      Statement statement = dbConnection.createStatement();
      ResultSet resultSet = statement.executeQuery(sqlQuery);
      ResultSetMetaData metaData = resultSet.getMetaData();
      resultSet.next();
      Map<String, Object> row = new HashMap<>();
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        row.put(metaData.getColumnLabel(i).toUpperCase(), resultSet.getObject(i));
      }
      return row;
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to retrieve entry.");
    }
    return null;
  }

  public void createEntry(String tableName) {
    try {
      String sqlQuery = "INSERT INTO " + tableName + "(FirstName, LastName) VALUES ('Mark', 'Fieldman');";
      Statement statement = dbConnection.createStatement();
      statement.executeUpdate(sqlQuery);
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to create entry.", e);
    }
  }

  public void updateEntry(String tableName, Integer primaryKey) {
    try {
      String sqlQuery = "UPDATE " + tableName + " SET FirstName='Sabina' WHERE Id=" + primaryKey + ";";
      Statement statement = dbConnection.createStatement();
      statement.executeUpdate(sqlQuery);
    } catch (SQLException e) {
      Logger.getLogger(this.className).log(Level.SEVERE, "Unable to update entry.", e);
    }
  }


}
