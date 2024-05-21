package com.cs506.t21.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test suit to check the functionality of the Rotor class. Test suit ensures that the Rotor
 * objects are able to accurately replicate the behavior of the Rotors used by the Enigma M3
 * during World War II.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 */
public class RotorTests {

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";


  @Test
  public void constructorUsesAlphabetAsWiringTable() {
    Rotor r = new Rotor();


    assertEquals(RotorTests.ALPHABET, r.getWiring(), "Wiring table should be the alphabet.");
  }

  @Test
  public void constructorSetsDefaultRingSettingToA() {
    Rotor r = new Rotor();

    assertEquals('a', r.getRingSetting(), "Wiring table should be the alphabet");
  }

  @Test
  public void constructorSetsDefaultTurnOverLetterToZ() {
    Rotor r = new Rotor();

    assertEquals('z', r.getTurnOver(), "Wiring table should be the alphabet");
  }

  @Test
  public void constructorSetsDefaultCurrentPositionToA() {
    Rotor r = new Rotor();

    assertEquals('a', r.getCurrentPosition(), "Wiring table should be the alphabet");
  }

  @Test
  public void setNameThrowsNullPointerExceptionIfNameIsNull() {
    Rotor r = new Rotor();

    Exception e = assertThrows(java.lang.NullPointerException.class, () -> r.setName(null),
        "setName should throw a NullPointerException if name provided is null");

    assertEquals("Name cannot be null", e.getMessage(),
        "NullPointerException thrown by setName returned an unexpected message");
  }

  @Test
  public void loadThrowsNullPointerExceptionIfWiringIsNull() {
    Rotor r = new Rotor();

    Exception e = assertThrows(java.lang.NullPointerException.class, () -> r.load(null, 'a'),
        "Load should throw a NullPointerException if the wiring provided is null");

    assertEquals("Wiring table cannot be null", e.getMessage(),
        "NullPointerException thrown by load returned an unexpected message");
  }


  @Test
  public void loadShouldThrowIllegalArgumentExceptionIfWiringMoreThan26Chars() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwxyjl";
    char expectedTurnOver = 'L';
    Exception e = assertThrows(java.lang.IllegalArgumentException.class,
        () -> r.load(expectedWiring, expectedTurnOver),
        "IllegalArgumentException should be thrown if invalid wiring");

    assertEquals("Wiring table must have 26 mappings", e.getMessage(),
        "Message from error is not as expected");
  }


  @Test
  public void loadShouldThrowIllegalArgumentExceptionIfWiringLessThan26Chars() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwxy";
    char expectedTurnOver = 'L';
    Exception e = assertThrows(java.lang.IllegalArgumentException.class,
        () -> r.load(expectedWiring, expectedTurnOver),
        "IllegalArgumentException should be thrown if invalid wiring");

    assertEquals("Wiring table must have 26 mappings", e.getMessage(),
        "Message from error is not as expected");
  }

  @Test
  public void loadShouldThrowIllegalArgumentExceptionIfWiringContainsNonLetterCharacters() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwx1l";
    char expectedTurnOver = 'L';
    Exception e = assertThrows(java.lang.IllegalArgumentException.class,
        () -> r.load(expectedWiring, expectedTurnOver),
        "IllegalArgumentException should be thrown if invalid wiring");

    assertEquals("Wiring table must have only letters", e.getMessage(),
        "Message from error is not as expected");
  }


  @Test
  public void loadShouldThrowIllegalArgumentExceptionIfTurnOverLetterThatIsNotValid() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwxyj";
    char expectedTurnOver = (char) 64;
    Exception e = assertThrows(java.lang.IllegalArgumentException.class,
        () -> r.load(expectedWiring, expectedTurnOver),
        "IllegalArgumentException should be thrown if invalid wiring");

    assertEquals("Turn over must be a letter", e.getMessage(),
        "Message from error is not as expected");
  }

  @Test
  public void loadShouldNotChangeWiringOrTurnOverLetterIfInvalidTurnOverIsProvided() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwxyj";
    char expectedTurnOver = (char) 64;

    Exception expected = assertThrows(
        java.lang.IllegalArgumentException.class,
        () -> r.load(expectedWiring, expectedTurnOver), 
        expectedWiring
    );

    assertEquals(
        "Turn over must be a letter", 
        expected.getMessage(), 
        "Message thrown does not match expected message"
    );
    assertEquals(RotorTests.ALPHABET, r.getWiring(), "Wiring should not be updated");
    assertEquals('z', r.getTurnOver(), "Wiring should not be updated");
  }

  @Test
  public void loadShouldNotChangeWiringOrTurnOverLetterIfInvalidWiringIsProvided() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwxyjl";
    char expectedTurnOver = 'L';

    Exception expected = assertThrows(
        java.lang.IllegalArgumentException.class,
        () -> r.load(expectedWiring, expectedTurnOver), 
        expectedWiring
    );

    assertEquals(
        "Wiring table must have 26 mappings", 
        expected.getMessage(), 
        "Message thrown does not match expected message"
    );

    assertEquals(RotorTests.ALPHABET, r.getWiring(), "Wiring should not be updated");
    assertEquals('z', r.getTurnOver(), "Wiring should not be updated");
  }


  @Test
  public void loadShouldSaveTheGivenWiringTableAndTurnOverLetterIfInputsAreValid() {
    Rotor r = new Rotor();

    String expectedWiring = "dbhaefucizklmnopqtsrgvwxyj";
    char expectedTurnOver = 'L';
    r.load(expectedWiring, expectedTurnOver);


    assertEquals(expectedWiring, r.getWiring(), "Wiring should be overriden by call to load");
    assertEquals(Character.toLowerCase(expectedTurnOver), r.getTurnOver(),
        "Turn over letter should be overriden by call to load");
  }

  @Test
  public void getTurnOverLetterReturnsTheCorrectValueAfterCallingLoad() {
    Rotor r = new Rotor();
    r.load(RotorTests.ALPHABET, 'H');

    assertEquals('h', r.getTurnOver(),
        "getTurnOver should return H after load is passed H as the turnOver letter");
  }

  @Test
  public void setRingSettingShouldUpdateTheRingSettingValueOfThisObject() {
    Rotor r = new Rotor();

    char expectedRingSetting = 'J';
    r.setRingSetting(expectedRingSetting);

    assertEquals(Character.toLowerCase(expectedRingSetting), r.getRingSetting(),
        "Ring Setting was not properly set");
  }

  @Test
  public void setCurrentPositionShouldChangeThePositionOfTheRotor() {
    Rotor r = new Rotor();

    char expectedPosition = 'O';
    r.setCurrentPosition(expectedPosition);
    assertEquals(Character.toLowerCase(expectedPosition), r.getCurrentPosition(),
        "Rotor position should be updated by the setCurrentPosition method");
  }


  /**
   * Returns the letter + delta in the alphabet as lowercase. If the given letter is 'z' or 'Z' the
   * and delta is 1 then 'a' is returned, on the otherhand, if delta is -1 'y' is returned.
   *
   * @param letter letter to increment
   * @param delta amount to shift by. Positive for increase in alphabet, negative for decrease.
   * @return the next letter in the alphabet as lowercase.
   */
  public static char shiftLetterByDelta(char letter, int delta) {
    char offset = 0;
    if (Character.isUpperCase(letter)) {
      offset = 'A';
    } else {
      offset = 'a';
    }

    int newPositionInAlphabet = letter - offset + delta;
    while (newPositionInAlphabet < 0) {
      newPositionInAlphabet = 26 + newPositionInAlphabet;
    }

    return (char) ((newPositionInAlphabet % 26) + 'a');
  }

  @Test
  public void rotateShouldMoveTheCurrentLetterToTheNextLetterInTheAlphabet() {
    Rotor r = new Rotor();

    char providedPosition = 'O';

    r.setCurrentPosition('O');
    r.rotate();
    assertEquals(RotorTests.shiftLetterByDelta(providedPosition, 1), r.getCurrentPosition(),
        "Rotor position should up to one position to P");
  }

  @Test
  public void inputA_PositionA_RingSettingA_ShouldResultIn_OutputE_WithRotorI_FromEnigmaI() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('a');
    r.setCurrentPosition('a');

    assertEquals('e', r.encrypt('a', false), "A should encrypt as E.");
  }

  @Test
  public void inputA_PositionB_RingSettingA_ShouldResultIn_OutputJ_WithRotorI_FromEnigmaI() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('a');
    r.setCurrentPosition('b');

    assertEquals('j', r.encrypt('a', false), "A should encrypt as J.");
  }

  @Test
  public void inputA_PositionA_RingSettingB_ShouldResultIn_OutputK_WithRotorI_FromEnigmaI() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('b');
    r.setCurrentPosition('a');

    assertEquals('k', r.encrypt('a', false), "A should encrypt as K.");
  }

  @Test
  public void inputA_PositionY_RingSettingF_ShouldResultIn_OutputW_WithRotorI_FromEnigmaI() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('F');
    r.setCurrentPosition('Y');

    assertEquals('w', r.encrypt('a', false), "A should encrypt as W.");
  }


  @Test
  public void encryptShouldThrowIllegalArgumentExceptionIfArgumentPassedIsNotaLetter() {
    Rotor r = new Rotor();

    Exception e =
        assertThrows(java.lang.IllegalArgumentException.class, () -> r.encrypt('7', false),
            "IllegalArgumentException should be thrown by encrypt if invalid argument");

    assertEquals("Only letter arguments accepted", e.getMessage(),
        "Message from error is not as expected");
  }

  // Test Backward encryption

  @Test
  public void inputE_PositionA_RingSettingA_ShouldResultIn_OutputA_WithRotorI_Backward() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('a');
    r.setCurrentPosition('a');

    assertEquals('a', r.encrypt('e', true), "A should encrypt as E.");
  }

  @Test
  public void inputJ_PositionB_RingSettingA_ShouldResultIn_OutputA_WithRotorI_Backward() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('a');
    r.setCurrentPosition('b');

    assertEquals('a', r.encrypt('j', true), "A should encrypt as J.");
  }

  @Test
  public void inputK_PositionA_RingSettingB_ShouldResultIn_OutputA_WithRotorI_Backward() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('b');
    r.setCurrentPosition('a');

    assertEquals('a', r.encrypt('k', true), "A should encrypt as K.");
  }

  @Test
  public void inputW_PositionY_RingSettingF_ShouldResultIn_OutputA_WithRotorI_Backward() {
    Rotor r = new Rotor();
    r.load("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
    r.setRingSetting('F');
    r.setCurrentPosition('Y');

    assertEquals('a', r.encrypt('w', true), "A should encrypt as W.");
  }
}

