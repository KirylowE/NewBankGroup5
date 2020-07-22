package newbank.dbase;

public class ConnectAzureSql implements IConnect {

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

    System.out.println(this.dbUsername);
    System.out.println(this.dbPassword);

    String connectionString = "";

  }
}
