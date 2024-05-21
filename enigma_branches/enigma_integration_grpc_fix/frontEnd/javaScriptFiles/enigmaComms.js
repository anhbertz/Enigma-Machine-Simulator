module.exports = {
  getClient: (IP, PORT) => {

    IP = process.env.IP || IP;
    PORT = process.env.PORT || PORT;

    const grpc = require('@grpc/grpc-js');
    const protoLoader = require('@grpc/proto-loader');
    const PROTO_PATH = __dirname + '/proto/enigma_interface.proto';

    const packageDefinition = protoLoader.loadSync(
      PROTO_PATH, {
      keepCase: true,
      longs: String,
      enums: String,
      defaults: true,
      oneofs: true,
    });

    const enigmaEmulator = grpc.loadPackageDefinition(packageDefinition);
    const target = `${IP}:${PORT}`;
    console.log("Connecting to", target);
    const client = new enigmaEmulator.EnigmaEmulator(
      target,
      grpc.credentials.createInsecure()
    );

    return client;
  },

  EnigmaRequestBuilder: class {
    constructor() {
      this.left_rotor;
      this.middle_rotor;
      this.right_rotor;

      this.plugboard;
      this.reflector;

      this.message;
    }


    setLeftRotor(type, startPosition, ringSetting) {
      this.left_rotor = {
        type: type,
        start_position: startPosition,
        ring_setting: ringSetting
      }

      return this;
    }

    setRightRotor(type, startPosition, ringSetting) {
      this.right_rotor = {
        type: type,
        start_position: startPosition,
        ring_setting: ringSetting
      }

      return this;
    }

    setMiddleRotor(type, startPosition, ringSetting) {
      this.middle_rotor = {
        type: type,
        start_position: startPosition,
        ring_setting: ringSetting
      }

      return this;
    }

    setPlugboard(mappings) {
      this.plugboard = {
        mappings: mappings
      }

      return this;
    }

    setReflector(type) {
      this.reflector = {
        type: type
      }

      return this;
    }


    setMessage(message) {
      this.message = message;
      return this;
    }
  }
}


