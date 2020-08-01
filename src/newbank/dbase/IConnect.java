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

  List<Map<String, Object>> getEntries(SqlQuery sqlQuery);

  Map<String, Object> getEntryById(String tableName, String primaryKey);

  Map<String, Object> getEntryByProperty(SqlQuery sqlQuery);

  void createEntry(String tableName);

  void createEntry(SqlQuery sqlQuery);

  void updateEntry(String tableName, String fieldName, String fieldValue, String primaryKey);

  void updateEntry(SqlQuery sqlQuery);

}
