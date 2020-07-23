package newbank.dbase;

/**
 * A type that contains the SQL query string.
 * <p>
 * The IConnect methods such as Create, Read, Update query
 * accept this type rather than a String.
 * <p>
 * Why do we need this type? It makes the API implementation more clear,
 * we can make two methods that use the same name e.g. updateEntry():
 * 1st method accepts an SQL query string, you'll can build and pass any query
 * 2nd method accepts the field name, content and primary key of item to update,
 * which can assists with some common, repeated tasks of updating a single field
 * This is a language feature called method overloading.
 * <p>
 * We could use a String instead of SQL type, but when we use the latter, it's clear
 * that it's not just any string but a query. Currently, it's a minimalistic implementation but it's easily extendable.
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
