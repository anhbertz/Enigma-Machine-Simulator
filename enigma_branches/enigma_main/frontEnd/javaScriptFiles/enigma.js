/**
 * JS file for enigma front-end functions
 * Authors: Gabriel Friederichs, Nic Leighty
 * Version: 3.0 (Sprint 3)
 */

// Rotor selection stuff
const ROTOR_POSITIONS_NUMBERS = new Map([
  ["leftRotor", 0],
  ["middleRotor", 0],
  ["rightRotor", 0],
]); /* User selected mappping of rotor position options to rotor numbersby [id, 0] 
       where 0 will become a number [1,3] corresponding to rotor options 1 through 3
       (0 means a rotor hasn't been selected for this position yet) */
const MIN_ROTOR_NUMBER = 1;
const MAX_ROTOR_NUMBER = 3;
// const NUM_ROTORS = 3; // Number of rotors present on the front end. Used for rotor selection and state display.

// (Inclusive) bounds for the rotor numbering
const ROTOR_LOWER_BOUND = 1;
const ROTOR_UPPER_BOUND = 26;

// Dial settings for rotors 1 and 2 (rightmost and second-rightmost). When the dial setting number is displayed
// on the corresponding rotor, the next rotation that the rotor performs will increment the next rotor by one
const DIAL_1 = 5;
const DIAL_2 = 5;

// Inline function definition for helping format rotor numbers
let formatRotorNum = (val) => (val < 10 ? "0" + val : "" + val); // Format to rotor numbers to proper string representation ("01", "02" ... "10" ... "26")

// plug1 and plug2 are strings for storing the plugboard configuration.
// Example:
// plug1 = "ABCD"
// plug2 = "EFGH"
// (connects AE, BF, CG, DH)
let plug1 = "";
let plug2 = "";

/**
 * Execute initialization code when the page loads.
 */
window.addEventListener("load", () => {
  init();
});

/**
 * Validate the front-end environment loads correctly.
 */
function init() {
  try {
    // Conduct user input for settings (that will be sent to backend statelessly upon user confirmation)
    handleRingSettings(); // Attempt ring setting selection

    // Add an onclick event listener to all .plugboard-letter elements for handling plugboard configuration.
    $("#plugboard td").on("click", function plugboardSelect() {
      plugboardSelectHelper(this);
    });
  } catch (error) {
    // Alert dialog box to inform the end-user that something went wrong and log the error to the console
    alert("Something went wrong initializing the page.");
    console.log(error);
  }
}

/**
 * Helper function to keep init() clean. This function figures out what the user is attempting to
 * do when they click on a plugboard box and acts accordingly based on the state of the machine.
 * Option 1: Select an unselected letter
 * Option 2: Select another unselect letter to create a mapping
 * Option 3: Unselect a selected letter
 * Option 4: Clear a mapping
 *
 * @param {HTMLElement} element HTML element clicked by the user.
 */
function plugboardSelectHelper(element) {
  // Get the letter from the clicked element
  let letter = element.innerHTML;

  // Check if the letter is in plug1 or plug2.
  // If not, add it to plug1 & illuminate the letter (user new unmapped letter selection)
  // If it is && it's in plug1 and plug1.length > plug2.length, remove it & dim the letter (user unselect w/o mapping)
  // Else, remove it and the letter with the same index from the other string & uncolor the letter pairing (user unselect a mapping)
  // Else if plug1.length > plug2.length and the letter != the most recent plug1 entry, add the letter to plug2 & randomly color the letter pairing (user create new mapping)
  if (plug1.includes(letter) || plug2.includes(letter)) {
    // Clicked letter already exists in one of the two lists, so the user
    // is looking to do some form of unselecting
    if (plug1.includes(letter) && plug1.length > plug2.length) {
      // unselect the letter (user unselect w/o mapping)
      console.log("user unselect w/o mapping");
    } else {
      // remove the letter pairing from both strings & dim the elements (user unselect a mapping)
      console.log("user unselect a mapping");
    }
    // remove plugboard letter or map
    plugboardUnselect(letter);
  } else {
    // Letter is not mapped or selected
    if (plug1.length > plug2.length) {
      // (user create new mapping!)
      console.log("user create new mapping");
      plug2 += letter;

      // Randomly color the newly selected letter and its corresponding mapped letter
      colorPlugboardMapping(letter, plug1[plug2.indexOf(letter)]);
    } else {
      // (user new unmapped letter selection)
      console.log("user new unmapped letter selection");
      plug1 += letter;

      // Set element background to white to show that the plugboard letter is "selected"
      $(element).css("background-color", "white");
    }
  }
  console.log("plug1: ", plug1);
  console.log("plug2: ", plug2);
}

/**
 * This helper function finds and dims a target letterbox on the plugboard.
 *
 * @param {char} letter The target letter to "unselect" on the plugboard.
 */
function plugboardUnselect(letter) {
  // Get the index in both strings where the map exists
  let mapIndex = -1;
  if (plug1.includes(letter)) {
    mapIndex = plug1.indexOf(letter);
  } else {
    // plug2 has the selected letter
    mapIndex = plug2.indexOf(letter);
  }

  // dim the boxes
  dimPlugboard(plug1[mapIndex]);
  dimPlugboard(plug2[mapIndex]);

  // Remove the mapping from both strings
  plug1 = plug1.replace(plug1[mapIndex], "");
  plug2 = plug2.replace(plug2[mapIndex], "");
}

/**
 * Helper function that generates a random hex value and then colors two mapped plugboard boxes.
 *
 * @param {char} letter1 First letter in a plugboard mapping
 * @param {char} letter2 Second letter in a plugboard mapping
 */
function colorPlugboardMapping(letter1, letter2) {
  // Choose a random hex value
  const letters = "0123456789ABCDEF";
  let hexValue = "#";
  for (let i = 0; i < 6; i++) {
    // appends a random hex character to hexValue 6 times
    hexValue += letters[Math.floor(Math.random() * 16)];
  }

  // Color the plugboard elements
  $("#plugboard td:contains(" + letter1 + ")").css(
    "background-color",
    hexValue
  );
  $("#plugboard td:contains(" + letter2 + ")").css(
    "background-color",
    hexValue
  );
}

/**
 * Helper function for dimming plugboard letterboxes.
 *
 * @param {char} letter The letter in the target letterbox to dim.
 */
function dimPlugboard(letter) {
  let element = $("#plugboard td:contains(" + letter + ")");
  element.css("background-color", "black");
}

/**
 * Specifies what should happen when a rotor is dragged (for selection).
 * Transfers the rotor id data for rotorDrop() to use.
 *
 * @param {DragEvent} ev The event caused by dragging the element.
 */
function rotorDrag(ev) {
  ev.dataTransfer.setData("text", ev.target.id);
}

/**
 * Specifies what should happen when a rotor a dropped (for selection).
 * Sets the rotor in preparation for saving config settings to the backend.
 *
 * @param {DragEvent} ev The event
 * 
 * @todo Some questions to consider: do nothing as all 3 rotors are selected? Or have availability to unselect/reset rotors? 
         If we add additional rotor options (5 total as in an actual military enigma),
         we will need to handle the user picking a fourth rotor when the enigma machine only uses 3 at a time.
 */
function rotorDrop(ev) {
  ev.preventDefault(); // Prevent browser default of opening as link on drop
  const rotorId = ev.dataTransfer.getData("text");
  const targetRotorPositionId = ev.target.id;
  const rotorNum = parseInt(rotorId.charAt(rotorId.length - 1)); // Get the rotor number from the rotor id, as this is the last character in its id

  ev.target.removeAttribute("ondrop"); // Make the user selected rotor positon undroppable since it's filled with a rotor now
  document.getElementById(rotorId).style.display = "none"; // Hide the rotor so it can't be reused
  ROTOR_POSITIONS_NUMBERS.set(targetRotorPositionId, rotorNum);

  alert("Rotor " + rotorNum + " added as " + targetRotorPositionId);
}

/**
 * Dispatch function for handling the selection of ring settings via clicking the arrow icons in the Ring Settings Area;
 * Up arrow -> increment ring setting for the given rotor
 * Down arrow -> decrement ring setting for the given rotor
 *
 * @see handleSpecificRingAdjuster(arrowClicked)
 */
function handleRingSettings() {
  const leftRingSettingIncrement = $("#leftRingSettingIncrement");
  const leftRingSettingDecrement = $("#leftRingSettingDecrement");
  const middleRingSettingIncrement = $("#middleRingSettingIncrement");
  const middleRingSettingDecrement = $("#middleRingSettingDecrement");
  const rightRingSettingIncrement = $("#rightRingSettingIncrement");
  const rightRingSettingDecrement = $("#rightRingSettingDecrement");

  leftRingSettingIncrement.on("click", () => {
    handleSpecificRingAdjuster(
      leftRingSettingIncrement,
      $("#leftRingSettingValue")
    );
  });

  leftRingSettingDecrement.on("click", () => {
    handleSpecificRingAdjuster(
      leftRingSettingDecrement,
      $("#leftRingSettingValue")
    );
  });

  middleRingSettingIncrement.on("click", () => {
    handleSpecificRingAdjuster(
      middleRingSettingIncrement,
      $("#middleRingSettingValue")
    );
  });

  middleRingSettingDecrement.on("click", () => {
    handleSpecificRingAdjuster(
      middleRingSettingDecrement,
      $("#middleRingSettingValue")
    );
  });

  rightRingSettingIncrement.on("click", () => {
    handleSpecificRingAdjuster(
      rightRingSettingIncrement,
      $("#rightRingSettingValue")
    );
  });

  rightRingSettingDecrement.on("click", () => {
    handleSpecificRingAdjuster(
      rightRingSettingDecrement,
      $("#rightRingSettingValue")
    );
  });
}

/**
 * Helper function to help @function handleRingSettings() visually adjusting ring setting values
 *
 * Will cycle when trying to increment/decrement when an upper/lower bound has been reached
 * @example If the setting displayed is 26 and the user hits the up arrow for that rotor, that ring setting will return to 01.
 * @param {JQuery} arrowClicked
 * @param {JQuery} ringSettingVal
 * @see @function handleRingSettings()
 */
function handleSpecificRingAdjuster(arrowClicked, ringSettingVal) {
  // Find the value corresponding to the arrow (middle ring or right ring)
  try {
    /* Now that we can know which ring setting needs to be adjusted based off ringSettingVal...
     Figure out if it was an increment or decrement arrow, using the Google Fonts API text for it
    */

    // Case 1:
    if (arrowClicked.text().trim() === "arrow_upward") {
      // Case 1a: As long as the current ring setting value is not 26 already, increment by 1...
      if (parseInt(ringSettingVal.text()) != ROTOR_UPPER_BOUND)
        ringSettingVal.text(
          formatRotorNum(parseInt(ringSettingVal.text()) + 1)
        );
      // Case 1b: Current ring setting value is already 26, so cycle to 01
      else ringSettingVal.text(formatRotorNum(ROTOR_LOWER_BOUND));
    }

    // Case 2:
    else if (arrowClicked.text().trim() === "arrow_downward") {
      // Case 2a: As long as the current ring setting value is not 01 already, decrement by 1...
      if (parseInt(ringSettingVal.text()) != ROTOR_LOWER_BOUND)
        ringSettingVal.text(
          formatRotorNum(parseInt(ringSettingVal.text()) - 1)
        );
      // Case 2b: Current ring setting value is already 01, so cycle to 26
      else
        ringSettingVal.text(
          ROTOR_UPPER_BOUND
        ); /** @note No additional formatting needed, per an actual enigma rotor, needed since we know it's 26 */
    } else throw "Issue with identifying arrow";
  } catch (e) {
    alert(e);
    console.log(e);
  }
}

function confirmConfig() {
  if (isValidConfig()) {
    // Add overlay over the left pane
    $("#leftOverlay").css("display", "flex");

    // Hide overlay over the middle pane
    $("#middleOverlay").css("display", "none");
  } else alert("Invalid config"); // Make the user fix their config before moving on
}

/**
 * For validating the config settings before sending to backend
 *
 * @returns {boolean} false unless config is confirmed to be good, then it returns true
 */
function isValidConfig() {
  // Make sure there's no unfinished business in plugboard settings
  if (plug1.length == plug2.length) {
    return true;
  } // Currently , everything else in the config should technically be dummy proof

  return false;
}

/**
 * Callback function for when a key is pressed while in the input box. Calls helper functions to enact visual response of enigma machine doing work.
 *
 * @note Restricts input to only letters and spaces, autoformatting output letters to uppercase
 */
async function keydown(e) {
  let keynum = e.which;
  let letter;

  // Prevent "backspace" from being typed
  if (e.which == 8) {
    e.preventDefault();
    alert(
      'There was no "Backspace" on an Enigma Machine...\nKeep typing or reset the machine.'
    );
  }

  // Gets the letter from the keypress and stores it to "letter" as the uppercase version of itself.
  letter = String.fromCharCode(keynum).toUpperCase(); // Non-alphabet input does not error, since toUpperCase ignores non-alphabet chars

  // If the letter is not A-Z or a space, prevent the character from being typed without warning and quit
  if (!/^[A-Z\s]+$/.test(letter)) {
    e.preventDefault();
    return;
  }

  // Encrypt the letter
  /* TODO: Backend integration to output actual ciphertext and dynamically
           update rotor visual
   */
  letter = encrypt(letter);

  // Light up the encrypted letter on the lightboard
  await light(letter);

  // Append the encrypted letter to the OUTPUT box
  $("#outText").append(letter);
}

/**
 * Given a letter A-Z (capitalized), briefly lights up the corresponding lampboard element
 *
 * @param {char} letter Character A-Z, capitalized
 */
async function light(letter) {
  // Get the lampboard td element where the displayed letter matches the letter passed to this function
  let lamp = $("#lampboard td:contains(" + letter + ")");

  // Set the lamp background color to #FFFF8F (Canary Yellow) for 0.175 seconds, then return to black
  lamp
    .css("background-color", "#FFFF8F")
    .delay(175) // 0.175 second delay
    .queue(function (next) {
      $(this).css("background-color", "black"); // Reset background color
      next();
    });
}

/**
 * Called when the user submits a username and password in the login form.
 *
 * @todo Add security for plaintext usernames and passwords
 */
function submitLogin() {
  // Get the PLAINTEXT!! username and password from the input elements
  // TODO (in a future sprint): use secure hashing
  let username = $("#username").val();
  let password = $("#password").val();
  let invalidLogin = false;

  // If username is empty or just whitespace, red outline the box by adding the "outline" class
  if (username.trim().length === 0) {
    $("#username").addClass("outline");
    invalidLogin = true;
  } else {
    $("#username").removeClass("outline");
  }
  // If password is empty or just whitespace, red outline the box by adding the "outline" class
  if (password.trim().length === 0) {
    $("#password").addClass("outline");
    invalidLogin = true;
  } else {
    $("#password").removeClass("outline");
  }
  // Quit if the login was invalid
  if (invalidLogin) return;

  console.log("Username: " + username + "\nPassword: " + password);

  // Hash the password before making network request to DB.

  // Create or retrieve user in DB. Bounce the request if wrong password for existing user.

  // Hide the login form on successful login
  transferOverlay();

  /* TODO: (with backend integration) Load user's saved messages & configs into the right sidebar
     AND Add an onclick event listener to all saved messages (for loading the configuration when clicked)
    */
}

/**
 * Transfers overlay from the enigma message entry/lamboard area
 * to the enigma config. Used when the user confirms their configuaration.
 */
function transferOverlay() {
  // Hide the login overlay
  $("#overlay").css("display", "none");

  // Add overlay over the middle pane
  $("#middleOverlay").css("display", "flex");
}

/**
 * Rotates the rotors based of keypress and machine state.
 *
 * @todo Integrate with actual backend machine state. With the updated
 *       frontend layout, this function does not actual accomplish anything
 *       currently.
 */
async function rotate() {
  // Cache the rotor elements into local variables
  let rotor1 = $("#rightRotor");
  let rotor2 = $("#middleRotor");
  let rotor3 = $("#leftRotor");

  // Get the currently displayed values of the rotors. These values will be determine if rotors 2 and/or 3 should rotate, based on dial settings
  let r1val = parseInt(rotor1.text());
  let r2val = parseInt(rotor2.text());
  let r3val = parseInt(rotor3.text());

  // The variables determine which rotors to rotate (rotor 1 will ALWAYS rotate, but rotors 2 and 3 are conditional on the dial settings)
  // These comparisons are cached to prevent "rotor 1 rotates first" from affecting the other rotors
  let rotate2 = r1val == DIAL_1;
  let rotate3 = r2val == DIAL_2;

  // Increment the proper rotors
  // Rotor 1 always rotates
  // Rotor 2 rotates if rotor 1 displays its dial setting number before rotor 1 rotates
  // Rotor 3 rotates if rotor 2 displays its dial setting number before rotor 2 rotates (so both rotor 1 and rotor 2 rotate when rotor 3 does)
  rotor1.text(rotorInc(r1val)); // always rotate rotor 1
  if (rotate2) {
    rotor2.text(rotorInc(r2val)); // rotate rotor 2 if it should
    if (rotate3) {
      rotor3.text(rotorInc(r3val)); // rotate rotor 3 if it should
    }
  }
}

/**
 * Function for incrementing the rotors when the corresponding "+" button is pressed
 *
 * @param {HTMLElement} button A "+" td element from the rotor configuration window
 */
function plusRotor(button) {
  let index = button.cellIndex; // index the button is in the parent row <tr>[td0][td1][td2]</tr>
  let row = $(button).closest("tr"); // get parent row of the button
  let rotorRow = row.next("tr")[0];
  let targetRotor = rotorRow.cells[index];

  $(targetRotor)
    .find("tr")
    .each(function () {
      console.log($(this));
      $(this).find("td")[0].innerHTML = rotorInc(
        parseInt($(this).find("td")[0].innerHTML)
      );
    });
}

/**
 * Function for decrementing the rotors when the corresponding "-" button is pressed
 *
 * @param {*} button
 */
function minusRotor(button) {
  let index = button.cellIndex;
  let row = $(button).closest("tr");
  let rotorRow = row.prev("tr")[0];
  let targetRotor = rotorRow.cells[index];

  $(targetRotor)
    .find("tr")
    .each(function () {
      $(this).find("td")[0].innerHTML = rotorDec(
        parseInt($(this).find("td")[0].innerHTML)
      );
    });
}

/**
 * Function used for rotating the rotors when new data is received from the backend
 *
 * @param {*} element
 * @param {*} value
 */
function setRotor(element, value) {}

/**
 * Helper function to keep rotor values between "01" and "26"
 *
 * @param {integer} val integer value to be "rotated" from
 * @returns The next value the rotor window will display after incrementing
 */
function rotorInc(val) {
  // Limit the integer value between 1 and 26
  val = (val + 1) % 27; // modulate between 0 and 26
  if (val == 0) val = 1; // set to 1 if value is 0

  return formatRotorNum(val);
}

/**
 * Helper function to keep rotor values between "01" and "26"
 *
 * @param {integer} val integer value to be "rotated" from
 * @returns The next value the rotor window will display after decrementing
 */
function rotorDec(val) {
  // Limit the integer value between 1 and 26
  val = (val - 1) % 27; // modulate between 0 and 26
  if (val == 0) val = 26; // set to 26 if value is 0

  return formatRotorNum(val);
}

/**
 * Calls to the backend for converting the plaintext letter to its cipher counterpart
 *
 * @param {string} letter
 * @todo call the backend
 * @returns The cipher letter
 */
function encrypt(letter) {
  return letter;
}

/**
 * Resets the engima machine.
 *
 * @todo handle this without logging the user out
 * @todo use JQuery
 */
function reset() {
  if (!confirm("Are you sure you want to reset the Enigma Machine?")) return;

  // Clear input and output textareas
  $("#inText").val("");
  $("#outText").text("");

  // Reset plugboard config
  $("#plugboard td").css("background-color", "black");
  plug1 = "";
  plug2 = "";

  // TODO: reset rotor config

  // Hide the left pane overlay and re-show the middle pane overlay
  $("#leftOverlay").css("display", "none");
  $("#middleOverlay").css("display", "flex");

  console.log("!!! MACHINE RESET !!!");
}
