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

  }
}
