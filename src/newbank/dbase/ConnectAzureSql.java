package newbank.dbase;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectAzureSql implements IConnect {

  private Connection dbConnection;
  private final String dbUsername;
  private final String dbPassword;
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

  public void connect() {
    try {
      String host = "jdbc:sqlserver://new-bank.database.windows.net:1433;database=newbank;";
      String cred = "user=" + this.dbUsername + ";password=" + this.dbPassword + ";";
      String args = "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
      String connectionString = host + cred + args;
      dbConnection = DriverManager.getConnection(connectionString);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
