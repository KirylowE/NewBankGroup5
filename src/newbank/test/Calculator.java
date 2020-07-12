package newbank.test;

public class Calculator {

  public static int adder(int arg1, int arg2) {
    return arg1 + arg2;
  }

  public static Integer divider(int arg1, int arg2) {
    if (arg2 != 0) {
      return arg1 / arg2;
    }
    System.out.println("Cannot divide by zero.");
    return null;
  }

}
