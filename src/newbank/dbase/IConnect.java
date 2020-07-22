package newbank.dbase;

import java.util.List;
import java.util.Map;

public interface IConnect {

  void createConnection();

  boolean checkConnection();

  List<Map<String, Object>> getEntries(String tableName);

}
