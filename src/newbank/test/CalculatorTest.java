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

}
