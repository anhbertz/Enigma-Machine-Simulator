package com.cs506.t21.enigma;

import java.util.UUID;

/**
 * This class implements a rotor as seen in the Enigma machine used during world war II.
 * Using this class, an application will be able to take a letter and encrypt it according to a
 * substitution cypher.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 * @version 3.0
 */
public class Rotor {

  // The name used to identify this rotor.
  private String name;

  // The wiring table, where the letter 'a' is mapped to the character in index 0. This is the
  // forward mapping. The mapping of letters as seen from the input wheel to the reflector.
  private char[] forwardWiringTable;

  // Wiring table as seen from the reflector to the input wheel.
  private char[] backwardsWiringTable;

  // The letter that the ring marker is set to relative to the internal wiring.
  private char ringSetting;

  // The letter at which when the rotor rotates the notch will allow the next rotor to rotate.
  // i.e. If you see the turnOver letter on the window and then press another key, the notch will
  // cause the double step.
  private char turnOver;

  // Position of the rotor. This variable can be seen as the letter that can be seen from
  // the keyboard view of the enigma machine.
  private char currentPosition;

  /**
   * Default constructor that creates a new "unscrambled" rotor with a ring setting of 'a',
   * a turn over letter of 'z' and a current position of 'a'. 
   * 
   * <p>This means that the wiring will be the alphabet in order. i.e. Assuming the initial 
   * position of A and a ring setting of A, If the input will be equal to the output.</p>
   */
  public Rotor() {
    this.setName(UUID.randomUUID().toString());
    this.load("abcdefghijklmnopqrstuvwxyz", 'z');
    this.ringSetting = 'a';
    this.currentPosition = 'a';
  }

  /**
   * This contructor creates a new rotor with the specified wiring table, turn over character,
   * ring setting, and starting position.
   *
   * @param wiring The wiring table of this rotor. Should represents the mapping of every letter
   *         assuming the ring setting of 'a', and a current position of 'a' after rotation. 
   * @param turnOver The character when moved from, the notch from this rotor will align with the
   *         pawl of the rotor to its right.
   * @param ringSetting The relative position of the alphabet ring to the wiring. This represents
   *         the letter that the mark on the ring points to.
   * @param startingPosition The starting position as seen through the window of the enigma
   *         machine for this rotor.
   * @throws java.lang.IllegalArgumentException If any of the arguments are invalid
   * @throws java.lang.NullPointerException If the wiring table or name is null
   */
  public Rotor(
      String name, String wiring, char turnOver, char ringSetting, char startingPosition
  ) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
    this.setName(name);
    this.load(wiring, turnOver);
    this.setRingSetting(ringSetting);
    this.setCurrentPosition(startingPosition);
  }

  /**
   * Gets the name of this rotor.
   *
   * @return The name of this rotor.
   */
  public String getName() {
    return this.name;
  }


  /**
   * Assigns a new name to this rotor.
   *
   * @param name New name for this rotor
   * @throws java.lang.NullPointerException If the name is null
   */
  public void setName(String name) throws java.lang.NullPointerException {
    if (name == null) {
      throw new java.lang.NullPointerException("Name cannot be null");
    }

    this.name = name;
  }

  /**
   * Creates the mapping for the encryption when the signal comes from the left side of the rotor.
   * In other words, after the signal passes through the reflector and is going towards the
   * lampboard.
   */
  private void createBackwardMapping() {
    this.backwardsWiringTable = new char[26];

    // Swap values and indeces.
    // The backwards table is made with the values as the indeces shifted down by 'a' (97), and
    // the indeces as the values shifted up by 'a' ('97').
    for (int i = 0; i < this.forwardWiringTable.length; i++) {
      char forwardLetter = this.forwardWiringTable[i];
      this.backwardsWiringTable[forwardLetter - 'a'] = (char) ('a' + i);
    }
  }


  /**
   * Replaces the default wiring with the given wiring table and turnOver value.
   *
   * @param wiringTable The new wiring for this rotor. Format should be 26 mappings together
   *         without spaces, where the first value maps to pin A. E.g. ACHJSLROTP...
   * @param turnOver The new turnOver value for this rotor. Should be an Roman letter.
   * @throws java.lang.IllegalArgumentException If the wiring table or turnOver are not made of 
   *         only letters
   * @throws java.lang.NullPointerException If the wiring table is null.
   */
  public void load(
      String wiringTable, char turnOver
  ) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
    // Input checks

    if (wiringTable == null) {
      throw new java.lang.NullPointerException("Wiring table cannot be null");
    }
    
    if (wiringTable.length() != 26) {
      throw new java.lang.IllegalArgumentException("Wiring table must have 26 mappings");
    }

    if (!Character.isLetter(turnOver)) {
      throw new java.lang.IllegalArgumentException("Turn over must be a letter");
    }
    
    wiringTable = wiringTable.toLowerCase();
    char[] newWiring = wiringTable.toCharArray();
    for (char c : newWiring) {
      if (!Character.isLetter(c)) {
        throw new java.lang.IllegalArgumentException("Wiring table must have only letters");
      }
    }

    // Create the forwards and backwards mapping tables.
    this.turnOver = Character.toLowerCase(turnOver);
    this.forwardWiringTable = newWiring;
    this.createBackwardMapping();
  }

  /**
   * Returns the wiring table of this rotor.
   *
   * @return The wiring table of this rotor.
   */
  public String getWiring() {
    return new String(this.forwardWiringTable);
  }

  /**
   * Adjusts the ring setting for this rotor.
   *
   * @param ringSetting The new letter the ring mark is next to.
   * @throws java.lang.IllegalArgumentException If the ring setting is not an
   *                                            english letter.
   */
  public void setRingSetting(char ringSetting) throws java.lang.IllegalArgumentException {
    if (!Character.isLetter(ringSetting)) {
      throw new java.lang.IllegalArgumentException("Can only use letters as ring setting");
    }

    this.ringSetting = Character.toLowerCase(ringSetting);
  }

  /**
   * Fetches the current ring setting for this rotor.
   *
   * @return This rotor's ring setting.
   */
  public char getRingSetting() {
    return this.ringSetting;
  }

  /**
   * Adjust the starting position of the rotor, as seen through the window of an
   * Enigma machine.
   *
   * @param letter The desired letter on alphabet ring to be seen through the
   *               window
   * @throws java.lang.IllegalArgumentException If the letter is not an english
   *                                            letter.
   */
  public void setCurrentPosition(char letter) throws java.lang.IllegalArgumentException {
    if (!Character.isLetter(letter)) {
      throw new java.lang.IllegalArgumentException("Can only use letters as ring setting");
    }

    this.currentPosition = Character.toLowerCase(letter);
  }

  /**
   * Returns the current letter that could be seen through the window of the
   * enigma machine.
   *
   * @return The value of the alphabet ring as seen through the window of an
   *         enigma machine.
   */
  public char getCurrentPosition() {
    return this.currentPosition;
  }

  /**
   * Encrypts the given letter.
   *
   * <p>Precondition: Rotate function must be called before each call of this method.</p>
   *
   * @param letter   Letter to be encrypted '
   * @param backward If the encryption is happening from left to right (After the
   *                 signal passes through reflector)
   * @return The encrypted letter
   * @throws java.lang.IllegalArgumentException if provided character is not a
   *                                            letter.
   */
  public char encrypt(char letter, boolean backward) throws java.lang.IllegalArgumentException {
    // Check letter
    if (!Character.isLetter(letter)) {
      throw new java.lang.IllegalArgumentException("Only letter arguments accepted");
    }

    // Make letter lowercase
    letter = Character.toLowerCase(letter);

    char[] table;

    // Changes in input/output due to ring setting and current position
    int ringDelta = this.ringSetting - 'a';
    int positionDelta = this.currentPosition - 'a';

    // In the case that the signal is going back towards lampboard, then table is
    // backwards
    // I.e. Values are indeces and indeces are values. If A mapped to E in the
    // forward version,
    // then E maps to A in the backwards version.
    if (backward) {
      table = this.backwardsWiringTable;
    } else {
      table = this.forwardWiringTable;
    }

    // Shift input letter down due to ring setting
    letter = Rotor.shiftLetterByDelta(letter, -1 * ringDelta);

    // Shift input Letter up due to current position
    letter = Rotor.shiftLetterByDelta(letter, positionDelta);

    // Encrypt letter
    letter = table[letter - 'a'];

    // Shift output letter down due to ring setting
    letter = Rotor.shiftLetterByDelta(letter, ringDelta);

    // Shift output letter down due to current position
    letter = Rotor.shiftLetterByDelta(letter, -1 * positionDelta);

    return letter;
  }

  /**
   * Returns the letter + delta in the alphabet as lowercase. If the given letter is 'z' or 'Z' the 
   * and delta is 1 then 'a' is returned, on the otherhand, if delta is -1 'y' is returned.
   *
   * @param letter the letter to increment
   * @param delta the amount to shift by. Positive for increase in alphabet, negative
   *            for decrease.
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

  /**
   * Rotates the rotor up one position. E.g. If the current letter is A (letter seen through
   * window) then after this call it should be B.
   */
  public void rotate() {
    this.currentPosition = Rotor.shiftLetterByDelta(currentPosition, 1);
  }


  /**
   * Returns the Turn Over letter. I.e. If the turn Over letter is A, then when the current letter
   * changes from A to B, the double step should happen.
   *
   * @return The turn over letter of this rotor.
   */
  public char getTurnOver() {
    return this.turnOver;
  }

  /**
   * Returns the state of the rotor. Output is in the following format:
   *
   * <p>"Name: I
   * Wiring: abcd...
   * Turn Over Letter: Q
   * Ring Setting: A
   * Current Position: L" </p>
   *
   * @return The state of the rotor according to the format above
   */
  @Override
  public String toString() {
    return "Name: " + this.getName() + "Wiring: " + this.getWiring() + "\nTurn Over Letter: " 
        + this.turnOver + "\nRing Setting: " + this.ringSetting + "\nCurrent Position: " 
        + this.currentPosition;
  }

}
