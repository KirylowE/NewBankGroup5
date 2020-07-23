package newbank.dbase;

/**
 * A type that contains the SQL query string.
 *
 * The IConnect methods such as Create, Read, Update query
 * are accepting this type rather than a String.
 * <p>
 * Currently, it's a minimalistic implementation but it's easily extendable
 */
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
