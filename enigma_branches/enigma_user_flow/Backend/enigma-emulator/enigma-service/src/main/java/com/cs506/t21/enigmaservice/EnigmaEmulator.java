package com.cs506.t21.enigmaservice;

import com.cs506.t21.enigma.EnigmaM3;
import com.cs506.t21.enigma.Plugboard;
import com.cs506.t21.enigma.Reflector;
import com.cs506.t21.enigma.Rotor;
import com.cs506.t21.enigma_service_interface.EnigmaEmulatorGrpc;
import com.cs506.t21.enigma_service_interface.EnigmaRequest;
import com.cs506.t21.enigma_service_interface.EnigmaResponse;
import com.cs506.t21.enigma_service_interface.PlugboardSettings;
import com.cs506.t21.enigma_service_interface.ReflectorSettings;
import com.cs506.t21.enigma_service_interface.RotorSettings;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * This class serves as a gRPC interface for utilizing the enigma class. All grpc requests to the
 * enigma machine is handled by this class.
 *
 * @author Juan Carlos Dorrejo
 * @version 1.1
 */
@GrpcService
public class EnigmaEmulator extends EnigmaEmulatorGrpc.EnigmaEmulatorImplBase {

  /**
   * Encryption gRPC call. This method creates an enigma machine with the specified settings and
   * encrypts the provided message using the specified settings.
   *
   * @param request Request parameters.
   * @param responseObserver Object through which response will be sent.
   */
  @Override
  public void encrypt(EnigmaRequest request, StreamObserver<EnigmaResponse> responseObserver) {
    // Plugboard details
    PlugboardSettings plugboardDetails = request.getPlugboard();

    // Rotor details
    RotorSettings rightRotorDetails = request.getRightRotor();
    RotorSettings middleRotorDetails = request.getMiddleRotor();
    RotorSettings leftRotorDetails = request.getLeftRotor();

    // Reflector details 
    ReflectorSettings reflectorDetails = request.getReflector();


    Plugboard plugboard = new Plugboard(plugboardDetails.getMappings());
    Reflector reflector = createReflector(reflectorDetails);
    
    System.out.println("Starting object creation...");

    // Create right rotor
    Rotor rightRotor = createRotor(rightRotorDetails);

    // Create middle rotor
    Rotor middleRotor = createRotor(middleRotorDetails);

    // Create left rotor
    Rotor leftRotor = createRotor(leftRotorDetails);

    System.out.println("Rotors Complete!");

    // Build enigma machine
    EnigmaM3 enigma = new EnigmaM3(
        plugboard,
        reflector,
        leftRotor,
        middleRotor,
        rightRotor
    );
    
    System.out.println("Enigma Created!");
    // Encrypt message
    StringBuilder result = new StringBuilder();
    for (char c : request.getMessage().toCharArray()) {
      result.append(enigma.translate(c));
    }

    System.out.println("Message encrypted");

    ArrayList<Integer> positions = new ArrayList<Integer>(3);
    for (int i = 0; i < 3; i++) {
      positions.add((int) (enigma.getRotor(i).getCurrentPosition() - 'a') + 1);
    }

    System.out.println("Positions have been calculated");

    // Build response
    EnigmaResponse response = EnigmaResponse.newBuilder()
        .setError("")
        .setEncryptedMessage(result.toString())
        .addAllRotorsCurrentPosition(positions)
        .build();

    System.out.println("Response built");

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }

  /**
   * Method that acts as a mock database and selects the turnover and wiring for a certain rotor
   * type. This method also builds a rotor with the given setting information.
   *
   * @param settings The rotor details to be used to build the rotor
   * @return A rotor with the specified settings
   */
  private Rotor createRotor(RotorSettings settings) {
    char turnOver;
    String wiring;
    switch (settings.getType()) {
      case ROTOR_I:
        turnOver = 'Q';
        wiring = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
        break;
        
      case ROTOR_II:
        turnOver = 'E';
        wiring = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
        break;
        
      case ROTOR_III:
        turnOver = 'V';
        wiring = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
        break;

      default:
        turnOver = 'A';
        wiring = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        break;
    }

    Rotor rotor = new Rotor(
        settings.getType().toString(),
        wiring,
        turnOver,
        (char) (settings.getRingSetting() - 1 + 'a'),
        (char) (settings.getStartPosition() - 1 + 'a')
    );

    return rotor;
  }

  /**
   * Method that acts as a mock database and selects the wiring for a certain reflector type. 
   * This method also builds a reflector with the given setting information.
   *
   * @param settings The reflector details to be used to build the reflector
   * @return A reflector with the specified settings
   */
  private Reflector createReflector(ReflectorSettings settings) {
    String wiring;
    switch (settings.getType()) {
      case REFLECTOR_UKW_B:
        wiring = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
        break;
        
      case REFLECTOR_UKW_C:
        wiring = "FVPJIAOYEDRZXWGCTKUQSBNMHL";
        break;
        
      default:
        wiring = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        break;
    }

    Reflector reflector = new Reflector(wiring);

    return reflector;
  }

}
