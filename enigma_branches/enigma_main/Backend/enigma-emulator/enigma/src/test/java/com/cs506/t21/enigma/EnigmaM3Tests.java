package com.cs506.t21.enigma;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests the EnigmaM3 class and checks that its behaviour mimicks that of the Enigma M3 used in
 * World War II.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class EnigmaM3Tests {

  @Mock private Rotor leftRotor;
  @Mock private Rotor middleRotor;
  @Mock private Rotor rightRotor;
  @Mock private Reflector reflector;
  @Mock private Plugboard plugboard;

  private EnigmaM3 enigma;

  @BeforeEach
  public void setUp() {
    this.enigma = new EnigmaM3(plugboard, reflector, leftRotor, middleRotor, rightRotor);
  }

  @Test
  public void constructorAssignsTheRotorsOnTheCorrectOrder() {

    assertSame(this.rightRotor, this.enigma.getRotor(2),
        "Enigma constructor did not correctly assign the rightmost rotor");

    assertSame(this.middleRotor, this.enigma.getRotor(1),
        "Enigma constructor did not correctly assign the leftmost rotor");

    assertSame(this.leftRotor, this.enigma.getRotor(0),
        "Enigma constructor did not correctly assign the middle rotor");
  }

  @Test
  public void constructorAssignsTheReflector() {

    assertSame(this.reflector, this.enigma.getReflector(),
        "Enigma constructor did not correctly assign the reflector");
  }

  @Test
  public void constructorAssignsThePlugboard() {

    assertSame(this.plugboard, this.enigma.getPlugboard(),
        "Enigma constructor did not correctly assign the plugboard");
  }

  @Test
  public void setRotorOnlyReplacesTheSpecifiedRotor() {

    Rotor newRotor = mock();
    this.enigma.setRotor(0, newRotor);

    assertSame(newRotor, this.enigma.getRotor(0),
        "setRotor did not correctly assign the leftmost rotor");
    assertSame(this.middleRotor, this.enigma.getRotor(1),
        "setRotor changed the middle rotor when trying to change the leftmost rotor");
    assertSame(this.rightRotor, this.enigma.getRotor(2),
        "setRotor changed the middle rotor when trying to change the rightmost rotor");
  }

  @Test
  public void setPlugboardCorrectlyReplacesThePlugboard() {

    Plugboard newPlugboard = mock();
    this.enigma.setPlugboard(newPlugboard);

    assertSame(newPlugboard, this.enigma.getPlugboard(),
        "setRotor did not correctly assign the leftmost rotor");
  }

  @Test
  public void setReflectorCorrectlyReplacesTheReflector() {

    Reflector newReflector = mock();
    this.enigma.setReflector(newReflector);

    assertSame(newReflector, this.enigma.getReflector(),
        "setRotor did not correctly assign the leftmost rotor");
  }


  // ------------------------------------Encryption Tests------------------------------------------
  @Test
  public void translateStepsRightRotorBeforeEncryptingOnSingleStepRotation() {
    // Setup rotors position and turnOver
    when(rightRotor.getCurrentPosition()).thenReturn('g');
    when(rightRotor.getTurnOver()).thenReturn('d');

    // Log order of method calls to right rotor (only one that rotates)
    final InOrder rightRotationOrder = inOrder(rightRotor);

    // Run simulation
    this.enigma.translate('a');

    // Check rotation comes before encryption on right rotor.
    rightRotationOrder.verify(rightRotor).rotate();
    rightRotationOrder.verify(rightRotor, times(2)).encrypt(anyChar(), anyBoolean());

    // Check that position and turn Over where checked at least once
    verify(rightRotor, atLeastOnce()).getCurrentPosition();
    verify(rightRotor, atLeastOnce()).getTurnOver();

    // Only right rotor rotated
    verify(rightRotor, times(1)).rotate();
    verify(middleRotor, never()).rotate();
    verify(leftRotor, never()).rotate();
  }


  @Test
  public void translateStepsRightAndMiddleRotorsBeforeEncryptingOnDoubleStepRotation() {

    // Setup rotors position and turnOver
    when(rightRotor.getCurrentPosition()).thenReturn('g');
    when(rightRotor.getTurnOver()).thenReturn('g');
    
    when(middleRotor.getCurrentPosition()).thenReturn('r');
    when(middleRotor.getTurnOver()).thenReturn('q');

    // Log order of method calls to each rotating rotor
    final InOrder rightRotationOrder = inOrder(rightRotor);
    final InOrder middleRotationOrder = inOrder(middleRotor);

    // Run simulation
    this.enigma.translate('a');
    
    // Check rotation came before encryption on respective rotors
    rightRotationOrder.verify(rightRotor).rotate();
    rightRotationOrder.verify(rightRotor, times(2)).encrypt(anyChar(), anyBoolean());
    
    middleRotationOrder.verify(middleRotor).rotate();
    middleRotationOrder.verify(middleRotor, times(2)).encrypt(anyChar(), anyBoolean());
    

    // Check that position and turn Over where checked at least once for right and middle rotor
    verify(rightRotor, atLeastOnce()).getCurrentPosition();
    verify(rightRotor, atLeastOnce()).getTurnOver();

    verify(middleRotor, atLeastOnce()).getCurrentPosition();
    verify(middleRotor, atLeastOnce()).getTurnOver();

    // One rotation happened per rotor (Except leftmost)
    verify(rightRotor, times(1)).rotate();
    verify(middleRotor, times(1)).rotate();
    verify(leftRotor, never()).rotate();
  }

  @Test
  public void translateStepsAllRotorsBeforeEncryptingOnTripleStepRotation() {
    // Setup rotors position and turnOver
    when(rightRotor.getCurrentPosition()).thenReturn('g');
    when(rightRotor.getTurnOver()).thenReturn('g');
    
    when(middleRotor.getCurrentPosition()).thenReturn('r');
    when(middleRotor.getTurnOver()).thenReturn('r');

    // Log order of method calls to each rotor
    final InOrder rightRotationOrder = inOrder(rightRotor);
    final InOrder middleRotationOrder = inOrder(middleRotor);
    final InOrder leftRotationOrder = inOrder(leftRotor);

    // Run simulation
    this.enigma.translate('a');
    
    // Check rotation came before encryption on respective rotors
    rightRotationOrder.verify(rightRotor).rotate();
    rightRotationOrder.verify(rightRotor, times(2)).encrypt(anyChar(), anyBoolean());
    
    middleRotationOrder.verify(middleRotor).rotate();
    middleRotationOrder.verify(middleRotor, times(2)).encrypt(anyChar(), anyBoolean());
    
    leftRotationOrder.verify(leftRotor).rotate();
    leftRotationOrder.verify(leftRotor, times(2)).encrypt(anyChar(), anyBoolean());

    // Check that position and turn Over where checked at least once for right and middle rotor
    verify(rightRotor, atLeastOnce()).getCurrentPosition();
    verify(rightRotor, atLeastOnce()).getTurnOver();

    verify(middleRotor, atLeastOnce()).getCurrentPosition();
    verify(middleRotor, atLeastOnce()).getTurnOver();

    // Check at most a single rotation happened on each rotor
    verify(rightRotor, times(1)).rotate();
    verify(middleRotor, times(1)).rotate();
    verify(leftRotor, times(1)).rotate();
  }

  @Test
  public void translatePassesInputThroughComponentsInCorrectOrderSingleStep() {
    // Setup rotors position and turnOver (Single step scenario)
    when(rightRotor.getCurrentPosition()).thenReturn('g');
    when(rightRotor.getTurnOver()).thenReturn('r');

    // Setup encryption calls
    final char plugboardForwardReturn = 'b';
    final char rightRotorForwardReturn = 'g';
    final char rightRotorBackwardReturn = 'v';
    final char middleRotorForwardReturn = 'k';
    final char middleRotorBackwardReturn = 'd';
    final char leftRotorForwardReturn = 'p';
    final char leftRotorBackwardReturn = 'c';
    final char reflectorReturn = 'j';


    when(plugboard.swap(anyChar())).thenReturn(plugboardForwardReturn).thenReturn('c');
    when(rightRotor.encrypt(anyChar(), anyBoolean()))
        .thenReturn(rightRotorForwardReturn)
        .thenReturn(rightRotorBackwardReturn);
    when(middleRotor.encrypt(anyChar(), anyBoolean()))
        .thenReturn(middleRotorForwardReturn)
        .thenReturn(middleRotorBackwardReturn);
    when(leftRotor.encrypt(anyChar(), anyBoolean()))
        .thenReturn(leftRotorForwardReturn)
        .thenReturn(leftRotorBackwardReturn);
    
    when(reflector.swap(anyChar())).thenReturn(reflectorReturn);
    

    // log order of method calls for each component
    final InOrder componentOrder = 
        inOrder(plugboard, reflector, leftRotor, middleRotor, rightRotor);
    
    // Start simulation
    this.enigma.translate('a');

    // Check order of encryption calls and arguments given
    componentOrder.verify(plugboard).swap('a');
    
    componentOrder.verify(rightRotor).encrypt(plugboardForwardReturn, false);
    componentOrder.verify(middleRotor).encrypt(rightRotorForwardReturn, false);
    componentOrder.verify(leftRotor).encrypt(middleRotorForwardReturn, false);
  
    componentOrder.verify(reflector).swap(leftRotorForwardReturn);
    
    componentOrder.verify(leftRotor).encrypt(reflectorReturn, true);
    componentOrder.verify(middleRotor).encrypt(leftRotorBackwardReturn, true);
    componentOrder.verify(rightRotor).encrypt(middleRotorBackwardReturn, true);
    
    componentOrder.verify(plugboard).swap(rightRotorBackwardReturn);

    // Plugboard, reflector, and left rotor, only participate in the encryption
    // (On single step case.)
    verifyNoMoreInteractions(plugboard, reflector, leftRotor);
  }

}
