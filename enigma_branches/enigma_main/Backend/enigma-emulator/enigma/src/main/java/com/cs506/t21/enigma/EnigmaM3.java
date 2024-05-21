package com.cs506.t21.enigma;


/**
 * This class implements the enigma machine used by the Nazi army during World War II. An
 * application can use this class to encrypt and decrypt messages. This machine uses a
 * polyalphabetic substitution cypher, everytime a key is pressed the algorithm changes.
 * 
 * <p>
 * EnigmaM3 uses 3 rotors. Each rotor can have a different wiring, ring setting, and starting letter
 * positon. The order of the rotors can be placed in any order. Furthermore, the plugboard can be
 * customized to swap any two letters for an extra layer of encryption.
 * </p>
 *
 * @author Juan Carlos Dorrejo Paulino
 * @author Yuxin Zhang
 * @version 1.0
 */
public class EnigmaM3 {

  private Rotor[] rotors;
  private Reflector reflector;
  private Plugboard plugboard;

  // An an array containing the input letter after passing through each component.
  private char[] encryptionStagesResults;

  // Stages:
  // 0 Plugboard
  // 1 Rightmost rotor
  // 2 Middle rotor
  // 3 Leftmost rotor
  // 4 Reflector
  // 5 Rightmost rotor (backwards)
  // 6 Middle rotor (backwards)
  // 7 Leftmost rotor (backwards)
  // 8 Plugboard
  private static final int ENCRYPTION_STAGES = 9;


  /**
   * Overloaded constructor makes an enigma machine using the specified rotors, reflector and
   * plugboard.
   * 
   * <p>
   * NOTE: The last rotor in the array is the rotor to first encrypt the input letter to this
   * machine. This the order of the rotors passed should reflect the order of rotors as seen on a
   * real enigma machine, hence the rightmost rotor is the first to encrypt the message.
   * </p>
   *
   * @param plugboard The plugboard to be used on this enigma machine
   * @param reflector The reflector to be used on the enigma machine
   * @param rotors The rotors to be used on the enigma machine. Must be exactly 3 rotors, any others
   *        Will be ignored.
   * @throws java.lang.IllegalArgumentException If less than 3 rotors are provided.
   * @throws java.lang.NullPointerException If any of the parameters is null.
   */
  public EnigmaM3(Plugboard plugboard, Reflector reflector, Rotor... rotors)
      throws java.lang.NullPointerException {

    if (rotors.length < 3) {
      throw new java.lang.IllegalArgumentException("Three rotors must be provided");
    }

    // Assign rotors
    this.rotors = new Rotor[3];
    for (int i = 0; i < 3; i++) {
      EnigmaM3.checkNotNull(rotors, "Provided rotor is null");
      this.rotors[i] = rotors[i];
    }
    EnigmaM3.checkNotNull(reflector, "Privded reflector is null");
    this.reflector = reflector;

    EnigmaM3.checkNotNull(plugboard, "Provided plugboard is null");
    this.plugboard = plugboard;

    this.encryptionStagesResults = new char[ENCRYPTION_STAGES];
  }

  /**
   * Checks that a given rotor position is valid. Rotor positions can only be between 0 and 2.
   *
   * @param position The position to be validated
   * @throws java.lang.IllegalArgumentException If the provided position is invalid
   */
  private static void checkRotorPosition(int position) throws java.lang.IllegalArgumentException {
    if (position < 0 || position > 2) {
      throw new java.lang.IllegalArgumentException("Provided position is invalid");
    }
  }

  /**
   * Checks that the provided character is a letter. The method {@link Character#isLetter(char)
   * isLetter} is used to check whether the provided character is a letter or not.
   *
   * @param character The character to be validated
   * @throws java.lang.IllegalArgumentException If the provided character is not a letter
   */
  private static void checkLetter(char character) throws java.lang.IllegalArgumentException {
    if (!Character.isLetter(character)) {
      throw new java.lang.IllegalArgumentException("Provided character must be a letter.");
    }
  }

  /**
   * Checks that the provided object is not null.
   *
   * @param obj Object to be validated.
   * @param message Message to be thrown with exception in the case that object is null.
   * @throws java.lang.NullPointerException If the object provided is null.
   */
  private static void checkNotNull(Object obj, String message)
      throws java.lang.NullPointerException {
    if (obj == null) {
      throw new java.lang.NullPointerException(message);
    }
  }


  /**
   * Sets the rotor to be used by this enigma machine at the specified position.
   *
   * @param position The position used for the specified rotor. See the
   *        {@link EnigmaM3#EnigmaM3(Rotor[], Reflector, Plugboard) EnigmaM3} constructor
   *        description for format detals.
   * @param rotor The rotor object to be used by this machine. Cannot be null.
   * @throws java.lang.IllegalArgumentException If any of the arguments are invalid.
   * @throws java.lang.NullPointerException If the rotor is null.
   */
  public void setRotor(int position, Rotor rotor) throws java.lang.IllegalArgumentException {
    EnigmaM3.checkRotorPosition(position);
    EnigmaM3.checkNotNull(rotor, "Rotor cannot be null");
    this.rotors[position] = rotor;
  }

  /**
   * Returns the specified rotor object. You can see the {@link src.Rotor Rotor} class for the
   * methods that can be called on this object.
   *
   * @param position The placement of the rotor. Possible values are 0, 1, 2, where position 2
   *        represents the rightmost rotor (i.e. the last rotor the signal passes through).
   * @return the rotor at the specified position
   */
  public Rotor getRotor(int position) {
    EnigmaM3.checkRotorPosition(position);

    return this.rotors[position];
  }

  /**
   * Sets the plugboard to be used by this enigma machine.
   *
   * @param plugboard The plugboard object used by this machine. Cannot be null.
   * @throws java.lang.NullPointerException if the plugboard is null
   */
  public void setPlugboard(Plugboard plugboard) throws java.lang.NullPointerException {
    EnigmaM3.checkNotNull(plugboard, "Plugboard cannot be null");
    this.plugboard = plugboard;
  }

  /**
   * Returns the plugboard object being used by this enigma machine. You can see the
   * {@link src.Plugboard Plugboard} class for the methods that can be called.
   *
   * @return the {@link String} representing the plugboard.
   */
  public Plugboard getPlugboard() {
    return this.plugboard;
  }

  /**
   * Sets the reflector to be used by this enigma machine.
   *
   * @param reflector The reflector object used by this machine. Cannot be null.
   * @throws java.lang.NullPointerException If reflector is null
   */
  public void setReflector(Reflector reflector) throws java.lang.NullPointerException {
    EnigmaM3.checkNotNull(reflector, "Reflector cannot be null");
    this.reflector = reflector;
  }

  /**
   * Returns the reflector object being used by this enigma machine. You can see the
   * {@link src.Reflector Reflector} class for the methods that can be called.
   *
   * @return the {@link String} representing the plugboard.
   */
  public Reflector getReflector() {
    return this.reflector;
  }

  /**
   * Encrypts the letter using the polyalphabetic substitution cipher with a random key sequence
   * used by the Enigma machine during World War II.
   *
   * <p>
   * This method can also be used to decrypt a letter by reconfiguring the machine to its starting
   * state and using the encrypted message as the input.
   * </p>
   *
   * @param letter The letter to be encrypted.
   * @return The encrypted message.
   */
  public char translate(char letter) {
    EnigmaM3.checkLetter(letter);
    // ------------------------------------Step rotors----------------------------------------
    char rightRotorCurrentPosition = this.rotors[2].getCurrentPosition();
    char rightRotorTurnOver = this.rotors[2].getTurnOver();
    
    // First rotor always rotates.
    this.rotors[2].rotate();
    
    // Middle rotor rotates if the right rotor was on the turn-over letter before rotation.
    if (rightRotorCurrentPosition == rightRotorTurnOver) {
      
      char leftRotorCurrentPosition = this.rotors[1].getCurrentPosition();
      char leftRotorTurnOver = this.rotors[1].getTurnOver();
      
      this.rotors[1].rotate();

      // Rotate Left rotor if and only if the second rotor is on its turn-over letter right
      // before it rotates.
      if (leftRotorCurrentPosition == leftRotorTurnOver) {
        this.rotors[0].rotate();
      }
    }


    // ----------------------------------Encryption-------------------------------------------
    char encryptedLetter = this.plugboard.swap(letter);
    this.encryptionStagesResults[0] = encryptedLetter;

    // Forwards through rotors.
    for (int i = this.rotors.length - 1; i > -1; i--) {
      encryptedLetter = this.rotors[i].encrypt(encryptedLetter, false);
      this.encryptionStagesResults[3 - i] = encryptedLetter;
    }

    encryptedLetter = this.reflector.swap(encryptedLetter);
    this.encryptionStagesResults[4] = encryptedLetter;

    // Backwards through rotors.
    for (int i = 0; i < this.rotors.length; i++) {
      encryptedLetter = this.rotors[i].encrypt(encryptedLetter, true);
      this.encryptionStagesResults[i + 5] = encryptedLetter;
    }

    encryptedLetter = this.plugboard.swap(encryptedLetter);
    this.encryptionStagesResults[8] = encryptedLetter;

    return encryptedLetter;
  }

  /**
   * Returns the result of passing the message through each component in succession. Stages: 0
   * Plugboard 1 Rightmost rotor 2 Middle rotor 3 Leftmost rotor 4 Reflector 5 Rightmost rotor
   * (backwards) 6 Middle rotor (backwards) 7 Leftmost rotor (backwards) 8 Plugboard
   *
   * @return The result for each encryption stage
   */
  public char[] getEncryptionStagesResults() {
    return this.encryptionStagesResults;
  }

  /**
   * Returns a string with the current machine configuration. In the following format:
   * 
   * <p>
   * "Rotor Order: I, II, III Rotor Position: Y, H, A Ring Setting: A, H, Q Reflector wiring:
   * abcdefghijklmnopqrstuvwxyz Plugboard Configuration: ab cz op lg"
   * </p>
   *
   * @return The String representation of the current state of the machine.
   */
  @Override
  public String toString() {
    return String.format(
        "RotorOrder: %s, %s, %s\nRotor Position: %c, %c, %c\nRing Setting: %c, %c, %c\n"
            + "Plugboard Configuration: %s",
        this.rotors[0].getName(), this.rotors[1].getName(), this.rotors[2].getName(),
        this.rotors[0].getCurrentPosition(), this.rotors[1].getCurrentPosition(),
        this.rotors[2].getCurrentPosition(), this.rotors[0].getRingSetting(),
        this.rotors[1].getRingSetting(), this.rotors[2].getRingSetting(),
        this.reflector.getWiring(), this.plugboard.toString());
  }


}
