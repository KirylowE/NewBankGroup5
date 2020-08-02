package newbank.server;

public class ClientConsole {

  /**
   * Validates the user input to confirm if a amount has a correct format
   *
   * @param input String containing money amount
   * @return true if input format is correct
   */
  public static boolean matchAmount(String input) {
    if (input.equals("") || input.equals(".")) {
      return false;
    }
    if (!input.replaceAll("[0-9]", "").replaceAll("\\.", "").equals("")) {
      return false;
    }
    return input.matches("^[0-9]*.?[0-9]*$");
  }
}
