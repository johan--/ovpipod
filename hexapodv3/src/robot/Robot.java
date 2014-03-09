package robot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import stdrpi.SerialRPi;

/**
 *
 * @author jhergault
 */
public class Robot {
    private int minHauteurPatte;
    private int maxHauteurPatte;
    
    private int x;
    private int y;
    private int z;
    
    private static Robot handle;
    
    private final GpioPinDigitalOutput modeBasc;
    
    // Définition des pattes pour un Hexapod
    private Patte front_left;
    private Patte front_right;
    private Patte middle_left;
    private Patte middle_right;
    private Patte back_left;
    private Patte back_right;
    
    public Robot(GpioController gpioRPi) {
        // Ouverture COM serie avant creation des pattes
        SerialRPi liaisonRS232 = new SerialRPi();
        
        // Positionnement bascule mode emission
        modeBasc = gpioRPi.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.HIGH);
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Creation des pattes de l'hexapod
        front_left = new Patte(liaisonRS232, (char)1, (char)3, (char)5);
        front_right = new Patte(liaisonRS232, (char)2, (char)4, (char)6);
        middle_left = new Patte(liaisonRS232, (char)13, (char)15, (char)17);
        middle_right = new Patte(liaisonRS232, (char)14, (char)16, (char)18);
        back_left = new Patte(liaisonRS232, (char)7, (char)9, (char)11);
        back_right = new Patte(liaisonRS232, (char)8, (char)10, (char)12);
        
        // Valeurs temporaires
        minHauteurPatte = 10;   // Defini hauteur de la base hexapod
        maxHauteurPatte = 14;   // Defini hauteur montee des pattes lors du mouvement
        
        // Definition de l'handle
        handle = this;
    }
    
    public static Robot getHandle() {
    	return handle;
    }
    
    public void Mouvement(int x_ws, int y_ws, int z_ws) {
        // X : axe vertical joystick gauche
        // Y : axe horizontal joystick gauche
        // Z : axe horizontal joystick droit
    	
    	x = x_ws;
    	y = y_ws;
    	z = z_ws;
    }

    
    
}
