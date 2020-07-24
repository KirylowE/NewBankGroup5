package newbank.dbase;

import java.util.Map;

/**
 * Utility class to manage environment variables which are used to
 * store secrets such as database password.
 */
public class EnvironmentVariable {

  /**
   * Returns the value of the environment variable.
   *
   * @param name the variable name
   * @return the variable value
   */
  public static String getVariableValue(String name) {
    Map<String, String> env = System.getenv();
    for (String envName : env.keySet()) {
      if (envName.equals(name)) {
        return env.get(envName);
      }
    }
    return null;
  }
}
