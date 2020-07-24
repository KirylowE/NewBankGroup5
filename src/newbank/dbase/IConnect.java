package newbank.dbase;

import java.util.List;
import java.util.Map;

/**
 * IConnect interface defines methods required for database connectors
 */
public interface IConnect {

  void createConnection();

  boolean checkConnection();

  List<Map<String, Object>> getEntries(String tableName);

  Map<String, Object> getEntryById(String tableName, String primaryKey);

  void createEntry(String tableName);

  void createEntry(SqlQuery sqlQuery);

  void updateEntry(String tableName, String primaryKey);

  void updateEntry(SqlQuery sqlQuery);

}
