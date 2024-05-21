package com.cs506.t21.enigmaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class represents the entry point for the backend server of the enigma application. This is
 * where the Springboot Application starts from, so that the server can start accepting requests.
 *
 * @author Juan Carlos Dorrejo Paulino
 * @version 0.0.1
 */
@SpringBootApplication
public class EnigmaEmulatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnigmaEmulatorApplication.class, args);
  }

}
