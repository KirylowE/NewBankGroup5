package newbank.dbase;

import java.util.List;
import java.util.Map;

/**
 * IConnect interface defines methods that are required for a database connector
 */
public interface IConnect {

  void createConnection();

  boolean checkConnection();

  List<Map<String, Object>> getEntries(String tableName);

  Map<String, Object> getEntryById(String tableName, Integer primaryKey);

  void createEntry(String tableName);

  void createEntry(SqlQuery sqlQuery);

  void updateEntry(String tableName, Integer primaryKey);

  void updateEntry(SqlQuery sqlQuery);

}
