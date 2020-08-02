package newbank.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientConsoleTest {

  @Test
  void matchAmountTest1() {
    // true
    Assertions.assertTrue(ClientConsole.matchAmount("0"));
    Assertions.assertTrue(ClientConsole.matchAmount("00"));
    Assertions.assertTrue(ClientConsole.matchAmount("000"));
    Assertions.assertTrue(ClientConsole.matchAmount("0000"));
    Assertions.assertTrue(ClientConsole.matchAmount("1"));
    Assertions.assertTrue(ClientConsole.matchAmount("11"));
    Assertions.assertTrue(ClientConsole.matchAmount("1."));
    Assertions.assertTrue(ClientConsole.matchAmount("1.0"));
    Assertions.assertTrue(ClientConsole.matchAmount("1.00"));
    Assertions.assertTrue(ClientConsole.matchAmount("11.00"));
    Assertions.assertTrue(ClientConsole.matchAmount(".00"));
    Assertions.assertTrue(ClientConsole.matchAmount(".1"));
    Assertions.assertTrue(ClientConsole.matchAmount(".11"));
    Assertions.assertTrue(ClientConsole.matchAmount("110"));
  }

  @Test
  void matchAmountTest2() {
    Assertions.assertFalse(ClientConsole.matchAmount(""));
    Assertions.assertFalse(ClientConsole.matchAmount("."));
    Assertions.assertFalse(ClientConsole.matchAmount("1c"));
    Assertions.assertFalse(ClientConsole.matchAmount("1.c"));
    Assertions.assertFalse(ClientConsole.matchAmount("1.c1"));
    Assertions.assertFalse(ClientConsole.matchAmount("1c1"));
    Assertions.assertFalse(ClientConsole.matchAmount("c1."));
    Assertions.assertFalse(ClientConsole.matchAmount("0c1t1."));
    Assertions.assertFalse(ClientConsole.matchAmount("9102.."));
    Assertions.assertFalse(ClientConsole.matchAmount("9102.1d"));
    Assertions.assertFalse(ClientConsole.matchAmount("0d"));
    Assertions.assertFalse(ClientConsole.matchAmount("213!"));
    Assertions.assertFalse(ClientConsole.matchAmount("ab"));
    Assertions.assertFalse(ClientConsole.matchAmount("ba"));
    Assertions.assertFalse(ClientConsole.matchAmount("d"));

  }

}
