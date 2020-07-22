package newbank.dbase;

public class SqlQuery {

  private final String sqlQuery;

  public SqlQuery(String sqlQuery) {
    this.sqlQuery = sqlQuery;
  }

  @Override
  public String toString() {
    return sqlQuery;
  }
}
