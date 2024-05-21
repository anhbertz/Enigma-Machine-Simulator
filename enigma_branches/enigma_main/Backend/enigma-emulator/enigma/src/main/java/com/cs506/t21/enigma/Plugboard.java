package com.cs506.t21.enigma;


/**
 * This class implements a Plugboard as seen in the Enigma machine used during World War II. A user
 * or application may use this class to swap any two letters based on the given letter mappings.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 * @version 1.0
 */
public class Plugboard {

  // Maps the input charcters to a different letter
  private char[] mappings;

  /**
   * Default constructor. Creates an "empty" plugboard. Meaning no letters are swapped
   */
  public Plugboard() {
    this.clear();
  }

  /**
   * Creates a plugboard with predefined mappings. The format of the mappings are as follow:
   * "ab cf rd qr"
   * The example above will swap letters a and b, c and f, etc.
   *
   * @param mappings The letters to be swapped.
   */
  public Plugboard(String mappings) {
    this();
    setMappings(mappings);
  }


  /**
   * Checks if a string of letter mappings is composed of pairs of letters and a space in between 
   * each pair. Format of mappings should be like the following string: "ab kd ls fk"
   *
   * @param mappings Letter mappings to be checked.
   * @return {@code true} if the mappings are valid, {@code false} otherwise
   */
  private boolean checkMappings(String mappings) {
    String[] pairs = mappings.split(" ");
    
    for (String pair : pairs) {
      if (pair.length() != 2) {
        return false;
      }

      char[] characters = pair.toCharArray();
      for (char character : characters) {
        if (!Character.isLetter(character)) {
          return false;
        }
      }
    }

    return true;
  }


  /**
   * Checks if the given letter has a mapping.
   *
   * @param letter Letter to check for a mapping.
   * @return {@code true} if a mapping exists, {@code false} otherwise.
   */
  private boolean mappingExists(char letter) {
    return this.mappings[letter - 'a'] != letter;
  }

  /**
   * Replaces the old letter mappings with new letter mappings. If a letter mapping was not
   * spcified. Then the old mapping will prevail.
   * 
   * <p>You can only create a mapping for a letter that does not currently have one. If tried,
   * method will skip the mapping and return false as not all mappings were successfully 
   * updated.</p>
   *
   * @param newMappings The cables (mappings) to be added to the plugboard.
   * @throws java.lang.IllegalArgumentException If the new mappings are invalid
   * @throws java.lang.NullPointerException If the mappings are null.
   */
  public void setMappings(
      String newMappings
  ) throws java.lang.IllegalArgumentException, java.lang.NullPointerException {
    if (newMappings == null) {
      throw new java.lang.NullPointerException("Mappings cannot be null");
    }
    
    if (!checkMappings(newMappings)) {
      throw new java.lang.IllegalArgumentException("Invalid mappings format");
    }

    String[] pairs = newMappings.toLowerCase().split(" ");
    for (String pair : pairs) {
      char[] letters = pair.toCharArray();

      // If a mapping for a letter is already defined, The new mapping will not be set
      if (mappingExists(letters[0]) || mappingExists(letters[1])) {
        continue;
      }
      
      this.mappings[letters[0] - 'a'] = letters[1];
      this.mappings[letters[1] - 'a'] = letters[0];
    }
  }

  /**
   * Removes the mapping connected to the specified character. This means that if a mapping is
   * "ab", after executing this method on the input ['a'] or ['b'], a and b will match to 
   * themselves rather than eachother. Multiple mappings may be removed at the same time.
   * 
   * <p>If a character that is not a letter is provided, then an exception will be thrown and the
   * action will be cancelled. No data will be updated.</p>
   *
   * @param letters Array of letters whose mapping will be reset.
   * @throws java.lang.IllegalArgumentException If any of the provided characters is not a letter.
   * @throws java.lang.NullPointerException If the mappings are null.
   */
  public void removeMappings(char[] letters) throws java.lang.NullPointerException {
    if (letters == null) {
      throw new java.lang.NullPointerException("Provided a null array");
    }
    
    for (char letter : letters) {
      if (!Character.isLetter(letter)) {
        throw new java.lang.IllegalArgumentException("Provided invalid letter");
      }
    }

    for (char letter : letters) {
      char lowercaseChar = Character.toLowerCase(letter);
      char oldMapping = this.mappings[lowercaseChar - 'a'];
      this.mappings[lowercaseChar - 'a'] = lowercaseChar;
      this.mappings[oldMapping - 'a'] = oldMapping;
    }

  }


  /**
   * Removes all letter mappings from the plugboard. Resulting in the letter passed being 
   * returned.
   */
  public void clear() {
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    this.mappings = alphabet.toCharArray();
  }

  /**
   * Swaps two characters according to the mapping information provided.
   *
   * @param character The character to be swapped
   * @return Swapped character
   * @throws java.lang.IllegalArgumentException if the character provided is not a letter
   */
  public char swap(char character) throws java.lang.IllegalArgumentException {
    if (!Character.isLetter(character)) {
      throw new java.lang.IllegalArgumentException(
          "Character must be in the english alphabet"
      );
    }
    character = Character.toLowerCase(character);
    return this.mappings[character - 'a'];
  }


  /**
   * Returs a string with the letter mappings that are not to reflective. For example:
   * "ab uf oe" if these are the only letters that are swapped with each other.
   *
   * @return A string representing the plugboard's non-reflective mappings information.
   */
  @Override
  public String toString() {

    // Use StringBuilder for faster performance
    StringBuilder plugboardSettings = new StringBuilder();
    for (int i = 0; i < 26; i++) {

      // Avoid reflexive mappings
      if (((char) i + 'a') == mappings[i]) {
        continue;
      }
      if (plugboardSettings.indexOf(Character.toString(mappings[i])) != -1) {
        continue;
      }

      plugboardSettings.append((char) ((char) i + 'a'));
      plugboardSettings.append(mappings[i]);
      plugboardSettings.append(' ');
    }

    // Remove trailing space
    plugboardSettings.deleteCharAt(plugboardSettings.length() - 1);
    
    return plugboardSettings.toString();
  }
} 
