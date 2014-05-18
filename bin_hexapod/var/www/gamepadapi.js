//var haveEvents = 'GamepadEvent' in window;
var controllers = {};
var etatJoyPad = 0;

function connecthandler(e) {
  addgamepad(e.gamepad);
}
function addgamepad(gamepad) {
  controllers[gamepad.index] = gamepad;
  etatJoyPad = 1;
}

function disconnecthandler(e) {
  removegamepad(e.gamepad);
}

function removegamepad(gamepad) {  
  delete controllers[gamepad.index];
  etatJoyPad = 0;
}

function scangamepads() {
	var gamepads = navigator.getGamepads ? navigator.getGamepads() : (navigator.webkitGetGamepads ? navigator.webkitGetGamepads() : []);
	for (var i = 0; i < gamepads.length; i++) {
		if (gamepads[i]) {
			if (!(gamepads[i].index in controllers)) {
				addgamepad(gamepads[i]);
			} else {
				controllers[gamepads[i].index] = gamepads[i];
    		}
    	}
	}
}
