package com.cs506.t21.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests the Plugboard class and checks that its behavior matches that of the plugboard used by the
 * Enigma machine during World War II.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 * @version 1.5
 */
public class PlugboardTests {
  private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

  @Test
  public void noMappingsOnEmptyPlugboard() {
    Plugboard p = new Plugboard();

    for (char letter : PlugboardTests.alphabet) {
      assertEquals(letter, p.swap(letter),
          "Letter " + letter + " Should map to itself if the plugboard is empty");
    }
  }

  @Test
  public void parameterizedConstructorShouldSetCorrectMappings() {
    String mappings = "ab";
    Plugboard p = new Plugboard(mappings);
    assertEquals(mappings, p.toString(),
        "Parameterized constructor did not set correct letter mappings");
  }

  @Test
  public void atoB_MappingShouldSwapThoseLettersOnly() {
    Plugboard p = new Plugboard("ab");
    assertEquals('b', p.swap('a'), "A should map to B");
    assertEquals('a', p.swap('b'), "B should map to A");

    for (char letter : PlugboardTests.alphabet) {
      if (letter == 'a' || letter == 'b') {
        continue;
      }
      assertEquals(letter, letter, "Only A and B should be swapped");
    }
  }

  @Test
  public void clearShouldRemoveAllMappings() {
    Plugboard p = new Plugboard("Ab Cd Ef kd op ls hg");
    p.clear();

    for (char letter : PlugboardTests.alphabet) {
      assertEquals(letter, p.swap(letter),
          "Letter " + letter + " Should map to itself if the plugboard is empty");
    }
  }

  @Test
  public void toStringShouldOnlyReturnNonReflectiveMappings() {
    String mappings = "ab Cd eF gh Jk lS op";
    Plugboard p = new Plugboard(mappings);
    assertEquals(mappings.toLowerCase(), p.toString(), "To String Does not reflect given mappings");
  }

  @Test
  public void setMappingsShouldCorrectlyChangeLetterMappings() {
    Plugboard p = new Plugboard();
    String mappings = "aB Gh";
    p.setMappings(mappings);
    assertEquals(mappings.toLowerCase(), p.toString(),
        "setMappings did not update toString return value");

    assertEquals('b', p.swap('a'), "A should be swapped with B");
    assertEquals('a', p.swap('b'), "B should be swapped with A");
    assertEquals('h', p.swap('g'), "H should be swapped with G");
    assertEquals('g', p.swap('h'), "G should be swapped with H");
  }

  @Test
  public void removeMappingsShouldCorrectlyResetSpecifiedLetterMappings() {
    Plugboard p = new Plugboard();
    String mappings = "aB Gh";
    p.setMappings(mappings);
    p.removeMappings(new char[] {'a'});
    assertEquals("gh", p.toString(), "removeMappings did not update toString return value");

    assertEquals('a', p.swap('a'), "A should not be swapped");
    assertEquals('b', p.swap('b'), "B should not be swapped");
  }


  @Test
  public void removeMappingsThrowAnExceptionIfMappingsAreNull() {
    Plugboard p = new Plugboard();
    char[] mappings = null;
    Exception e =
        assertThrows(java.lang.NullPointerException.class, () -> p.removeMappings(mappings),
            "setMappings should throw an exception if mapping is null");
    assertEquals("Provided a null array", e.getMessage(),
        "Message thrown for null mappings difers from expected message");
  }

  @Test
  public void removeMappingsThrowAnIllegalArgumentExceptionIfLettersAreInvalid() {
    Plugboard p = new Plugboard();
    char[] mappings = {'a', 'b', '1'};
    Exception e =
        assertThrows(java.lang.IllegalArgumentException.class, () -> p.removeMappings(mappings),
            "setMappings should throw an exception if mapping is null");
    assertEquals("Provided invalid letter", e.getMessage(),
        "Message thrown for null mappings difers from expected message");
    assertEquals('a', p.swap('a'), "A should not be swaped");
    assertEquals('b', p.swap('b'), "B should not be swaped");
  }



  @Test
  public void setMappingsThrowsAnIllegalArgumentExceptionForIncorrectMappingFormat() {
    Plugboard p = new Plugboard();
    String mappings = "ab c d";
    Exception e =
        assertThrows(java.lang.IllegalArgumentException.class, () -> p.setMappings(mappings),
            "setMappings should throw an exception if mapping format is incorrect");
    assertEquals("Invalid mappings format", e.getMessage(),
        "Message thrown for multiple letter maps difers from the expected message");
    assertEquals('a', p.swap('a'), "A should not be swaped");
    assertEquals('b', p.swap('b'), "B should not be swaped");
    assertEquals('c', p.swap('c'), "C should not be swaped");
    assertEquals('d', p.swap('d'), "D should not be swaped");
  }

  @Test
  public void setMappingsThrowAnExceptionIfMappingIsNull() {
    Plugboard p = new Plugboard();
    String mappings = null;
    Exception e = assertThrows(java.lang.NullPointerException.class, () -> p.setMappings(mappings),
        "setMappings should throw an exception if mapping is null");
    assertEquals("Mappings cannot be null", e.getMessage(),
        "Message thrown for null mappings difers from expected message");
  }

  @Test
  public void setMappingsShouldIgnoreMultipleMappingsToTheSameLetter() {
    Plugboard p = new Plugboard("ab bc");

    assertEquals('b', p.swap('a'), "A should map to B");
    assertEquals('a', p.swap('b'), "B should map to A");
    assertEquals('c', p.swap('c'), "C should not be swaped");
  }
}
