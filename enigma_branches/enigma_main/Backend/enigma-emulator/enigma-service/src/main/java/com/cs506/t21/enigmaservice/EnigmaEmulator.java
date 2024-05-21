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
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * This class serves as a gRPC interface for utilizing the enigma class. All
 * grpc requests to the
 * enigma machine is handled by this class.
 *
 * @author Juan Carlos Dorrejo
 * @version 1.1
 */
@GrpcService
public class EnigmaEmulator extends EnigmaEmulatorGrpc.EnigmaEmulatorImplBase {

  /**
   * Encryption gRPC call. This method creates an enigma machine with the
   * specified settings and
   * encrypts the provided message using the specified settings.
   *
   * @param request          Request parameters.
   * @param responseObserver Object through which response will be sent.
   */
  @Override
  public void encrypt(EnigmaRequest request, StreamObserver<EnigmaResponse> responseObserver) {
    try {
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
          rightRotor);

      System.out.println("Enigma Created!");
      
      // Encrypt message
      StringBuilder result = new StringBuilder();
      for (String word : request.getMessage().split(" ")) {
        for (char c : word.toCharArray()) {
          result.append(enigma.translate(c));
        }
        result.append(" ");
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
    } catch (StatusRuntimeException e) {
      // Handle gRPC status exceptions
      Status status = Status.fromThrowable(e);

      String errorMessage = "";

      switch (status.getCode()) {
        case CANCELLED:
          // Handle CANCELLED error (status code 1)
          errorMessage = "CANCELLED error";
          responseObserver.onError(
              Status.CANCELLED
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        case INVALID_ARGUMENT:
          // Handle INVALID_ARGUMENT error (status code 3)
          errorMessage = "INVALID_ARGUMENT error";
          responseObserver.onError(
              Status.INVALID_ARGUMENT
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        case NOT_FOUND:
          // Handle NOT_FOUND error (status code 5)
          errorMessage = "NOT_FOUND error";
          responseObserver.onError(
              Status.NOT_FOUND
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        case ALREADY_EXISTS:
          // Handle ALREADY_EXISTS error (status code 6)
          errorMessage = "ALREADY_EXISTS error";
          responseObserver.onError(
              Status.ALREADY_EXISTS
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        case PERMISSION_DENIED:
          // Handle PERMISSION_DENIED error (status code 7)
          errorMessage = "PERMISSION_DENIED error";
          responseObserver.onError(
              Status.PERMISSION_DENIED
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        case RESOURCE_EXHAUSTED:
          // Handle RESOURCE_EXHAUSTED error (status code 8)
          errorMessage = "RESOURCE_EXHAUSTED error";
          responseObserver.onError(
              Status.RESOURCE_EXHAUSTED
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        case UNKNOWN:
          // Handle UNKNOWN error (status code 2)
          errorMessage = "UNKNOWN error";
          responseObserver.onError(
              Status.UNKNOWN
                .withDescription(errorMessage)
                .asRuntimeException()
          );
          break;
        default:
          // Handle other gRPC status codes
          errorMessage = "DEFAULT error";
          EnigmaResponse errorResponse = EnigmaResponse.newBuilder()
              .setError(errorMessage)
              .build();
          responseObserver.onNext(errorResponse);
          break;
      }
      responseObserver.onCompleted();
    }

  }

  /**
   * Method that acts as a mock database and selects the turnover and wiring for a
   * certain rotor
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
        (char) (settings.getStartPosition() - 1 + 'a'));

    return rotor;
  }

  /**
   * Method that acts as a mock database and selects the wiring for a certain
   * reflector type.
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
