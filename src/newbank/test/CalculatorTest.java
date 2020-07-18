package newbank.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

  @Test
  void adderTest1() {
    int expected = 4;
    int actual = Calculator.adder(2, 2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void adderTest2() {
    int expected = 5;
    int actual = Calculator.adder(2, 2);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void dividerTest1() {
    int expected = 2;
    int actual = Calculator.divider(10, 5);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void dividerTest2() {
    int expected = 0;
    int actual = Calculator.divider(10, 0);
    Assertions.assertEquals(expected, actual);
  }


}
