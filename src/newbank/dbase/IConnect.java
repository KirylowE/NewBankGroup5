package newbank.dbase;

import java.util.List;
import java.util.Map;

/**
 * IConnect interface defines methods required for database connectors.
 */
public interface IConnect {

  void createConnection();

  boolean checkConnection();

  List<Map<String, Object>> getEntries(String table);

  List<Map<String, Object>> getEntries(SqlQuery sqlQuery);

  Map<String, Object> getEntryById(String table, String pk);

  Map<String, Object> getEntryByProperty(SqlQuery sqlQuery);

  void createEntry(SqlQuery sqlQuery);

  void updateEntry(String table, String field, String value, String pk);

  void updateEntry(SqlQuery sqlQuery);

}
