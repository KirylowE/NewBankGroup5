package newbank.dbase;

import java.util.List;
import java.util.Map;

public class Dispatcher {
  public static void main(String[] args) {

    String dbUsername = EnvironmentVariable.getVariableValue("DB_USERNAME");
    String dbPassword = EnvironmentVariable.getVariableValue("DB_PASSWORD");

    if (dbUsername == null || dbPassword == null) {
      throw new Error("Please set the environment variables DB_USERNAME and DB_PASSWORD");
    }

    IConnect dbase = ConnectAzureSql.getInstance(dbUsername, dbPassword);
    dbase.createConnection();
    List<Map<String, Object>> data = dbase.getEntries("Customer");
    System.out.println(data);

  }
}
