package com.cs506.t21.enigma;

/**
 * This class represents a reflector as seen in the Enigma machine used during World War II. An
 * application can use this class to swap letters according to a specified wiring table.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 * @version 2.0
 */
public class Reflector {
  private char[] mapping;
  private static final int MAPPING_LENGTH = 26;

  /**
   * Default constructor intantiate the load wiring table. Making each character maps to 
   * themselves.
   */
  public Reflector() {
    this.mapping = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  }

  /**
   * Constructor intantiate the load wiring table according to the provided wiring.
   *
   * @param wiring The wiring that needs to be loaded
   * @throws java.lang.IllegalArgumentException if the wiring is not in the correct Format
   * @throws java.lang.NullPointerException if the wiring is null
   */
  public Reflector(
      String wiring
  ) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
    this.mapping = new char[MAPPING_LENGTH];
    this.load(wiring);
  }

  /**
   * Load the reflector wiring table.
   *
   * @param wiring The wiring that needs to be loaded
   * @throws java.lang.IllegalArgumentException if the wiring is not in the correct Format
   * @throws java.lang.NullPointerException if the wiring is null
   */
  public void load(
      String wiring
  ) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
      
    if (wiring == null) {
      throw new java.lang.NullPointerException("The wiring provided was null");
    }

    if (wiring.length() != 26) {
      throw new java.lang.IllegalArgumentException("The length of the wiring should be 26");
    }
    
    char[] letters = wiring.toLowerCase().toCharArray();
    for (int i = 0; i < letters.length; i++) {
      this.mapping[i] = letters[i];
    }
  }

  /**
   * Swap two characters according to the wiring table.
   *
   * @param letter The character needs to be swapped
   * @return Swapped Character
   * @throws java.lang.IllegalArgumentException If the character provided is not a letter
   */
  public char swap(char letter) throws java.lang.IllegalArgumentException {
    if (!Character.isLetter(letter)) {
      throw new java.lang.IllegalArgumentException("Character must be a letter");
    }
    letter = Character.toLowerCase(letter);
    return this.mapping[letter - 'a'];
  }

  /**
   * Returns this reflector's wiring.
   *
   * @return The wiring used by this reflector
   */
  public String getWiring() {
    return new String(this.mapping);
  }
  
  /**
   * This method is used to print out the state of the mapping.
   */
  @Override
  public String toString() {
    return new String(this.mapping);
  }
}
