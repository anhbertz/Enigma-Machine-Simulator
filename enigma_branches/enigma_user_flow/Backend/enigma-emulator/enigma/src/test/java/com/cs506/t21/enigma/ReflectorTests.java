package com.cs506.t21.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests the Reflector class and checks that its behavior matches that of the reflector used by the
 * Enigma machine during World War II.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 */
public class ReflectorTests {
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

  @Test
  public void testConstructorCreatesReflectorWithLettersMappingThemselves() {

    Reflector r = new Reflector();
    assertEquals(ReflectorTests.ALPHABET, r.getWiring(), "Default constructor has an error!");
  }

  @Test
  public void testLoadOverwriteWiring() {
    Reflector r = new Reflector();
    r.load("acbdegfhijklmnopqrstuvwxyz");
    assertEquals("acbdegfhijklmnopqrstuvwxyz", r.getWiring(), "Wiring is not updated");
  }

  @Test
  public void testLoadFailsIfWiringLessThan26() {
    Reflector r = new Reflector();
    String wiring = "aaaaaaaaaaaaaaaaaaaaaaaaa"; // 25 characters
    Exception e = assertThrows(java.lang.IllegalArgumentException.class, () -> r.load(wiring),
        "Load should throw an IllegalArgumentException if wiring is incorrect length");

    assertEquals("The length of the wiring should be 26", e.getMessage(),
        "Message thrown when loading wiring with incorrect length is incorrect");
    assertEquals(ReflectorTests.ALPHABET, r.getWiring(),
        "Wiring should not be changed if wiring is less that 26 characters");
  }

  @Test
  public void testLoadFailsIfWiringMoreThan26() {
    Reflector r = new Reflector();
    String wiring = "aaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 27 characters
    Exception e = assertThrows(java.lang.IllegalArgumentException.class, () -> r.load(wiring),
        "Load should throw an IllegalArgumentException if wiring is incorrect length");

    assertEquals("The length of the wiring should be 26", e.getMessage(),
        "Message thrown when loading wiring with incorrect length is incorrect");
    assertEquals(ReflectorTests.ALPHABET, r.getWiring(),
        "Wiring should not be changed if wiring is less that 26 characters");
  }

  @Test
  public void testLoadFailsIfWiringIsNull() {
    Reflector r = new Reflector();
    String wiring = null;
    Exception e = assertThrows(java.lang.NullPointerException.class, () -> r.load(wiring),
        "Load should throw an NullPointerException if wiring is incorrect length");

    assertEquals("The wiring provided was null", e.getMessage(),
        "Message thrown when loading wiring with incorrect length is incorrect");
  }

  @Test
  public void testSwapMethodSwitchesLettersAccordingToWiring() {
    Reflector r = new Reflector();
    r.load("acbdegfhijkLmnopQrstuvwXyz");
    assertEquals('c', r.swap('b'), "B should be swapped with C");
    assertEquals('b', r.swap('c'), "C should be swapped with B");
    assertEquals('g', r.swap('f'), "F should be swapped with G");
    assertEquals('f', r.swap('g'), "G should be swapped with F");
  }

  @Test
  public void testSwapMethodIsCaseInsensitive() {
    Reflector r = new Reflector("acbdegfhijklmnopqrstuvwxyz");
    assertEquals('c', r.swap('B'), "B should be swapped with C");
    assertEquals('b', r.swap('C'), "C should be swapped with B");
    assertEquals('g', r.swap('F'), "F should be swapped with G");
    assertEquals('f', r.swap('G'), "G should be swapped with F");
  }

  @Test
  public void testswapMethodShouldThrowIllegalArgumentExceptionIfCharacterIsNotLetter() {

    Reflector r = new Reflector();
    Exception e = assertThrows(java.lang.IllegalArgumentException.class, () -> r.swap('1'),
        "Load should throw an IllegalArgumentException if wiring is incorrect length");

    assertEquals("Character must be a letter", e.getMessage(),
        "Message thrown when swapping a character that is not a letter is incorrect");
  }

}
