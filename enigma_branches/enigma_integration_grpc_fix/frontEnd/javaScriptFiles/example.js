// Enigma module will contain all the functions needed.
const enigma = require("./enigmaComms.js");
const IP = "127.0.0.1";
const PORT = 9090;

// Do not use this! There is a builder on the enigma.js module
// to be able to create a request object
function buildRequest() {
  let leftRotorState = {
    type: 0,
    start_position: 1,
    ring_setting: 1,
  };
  let middleRotorState = {
    type: 1,
    start_position: 1,
    ring_setting: 1,
  };
  let rightRotorState = {
    type: 2,
    start_position: 1,
    ring_setting: 1,
  };
  let plugboardSettings = {
    mappings: "AB CD",
  };
  let reflectorSettings = {
    type: 0,
  };

  let userMessage = "hello";

  let EnigmaRequest = {
    left_rotor: leftRotorState,
    middle_rotor: middleRotorState,
    right_rotor: rightRotorState,
    plugboard: plugboardSettings,
    reflector: reflectorSettings,
    message: userMessage,
  };

  return EnigmaRequest;
}

function main() {
  // Using the Builder to construct a request
  const EnigmaRequest = new enigma.EnigmaRequestBuilder()
    .setLeftRotor(0, 1, 1)
    .setMiddleRotor(1, 1, 1)
    .setRightRotor(2, 1, 1)
    .setPlugboard("ab cd")
    .setReflector(0)
    .setMessage("hello");

  console.log("Payload:", EnigmaRequest);

  // Using the getClient function to connect to server
  const client = enigma.getClient(IP, PORT);

  // Using the encrypt function on the server through gRPC
  client.encrypt(EnigmaRequest, (err, response) => {
    if (err) {
      console.log("Error", err);
    } else {
      console.log("Data:", response);
    }
  });
}

main();
