package robot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.lang.Math;

import stdrpi.SerialRPi;

/**
 *
 * @author jhergault
 */
public class Robot {
	public static final int STEPMAX = 128;
	
    private int minHauteurPatte;
    private int maxHauteurPatte;
    
    private int x;
    private int y;
    private int z;
    
    private int step;
    
    private static Robot handle;
    
    private final GpioPinDigitalOutput modeBasc;
    
    // Definition des pattes pour un Hexapod
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
        front_left = new Patte(liaisonRS232, (STEPMAX/4), (char)1, (char)3, (char)5);
        front_right = new Patte(liaisonRS232, ((STEPMAX/4)*3), (char)2, (char)4, (char)6);
        middle_left = new Patte(liaisonRS232, ((STEPMAX/4)*3), (char)13, (char)15, (char)17);
        middle_right = new Patte(liaisonRS232, (STEPMAX/4), (char)14, (char)16, (char)18);
        back_left = new Patte(liaisonRS232, (STEPMAX/4), (char)7, (char)9, (char)11);
        back_right = new Patte(liaisonRS232, ((STEPMAX/4)*3), (char)8, (char)10, (char)12);
        
        // Init Servomoteurs
        try {
			front_left.setPosAll((char)52, (char)-118, (char)97);
			front_right.setPosAll((char)52, (char)118, (char)97);
	        middle_left.setPosAll((char)0, (char)-118, (char)97);
	        middle_right.setPosAll((char)0, (char)118, (char)97);
	        back_left.setPosAll((char)-52, (char)-118, (char)97);
	        back_right.setPosAll((char)-52, (char)118, (char)97);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Valeurs temporaires
        minHauteurPatte = 10;   // Defini hauteur de la base hexapod
        maxHauteurPatte = 14;   // Defini hauteur montee des pattes lors du mouvement
        step = 0;
        
        // Definition de l'handle
        handle = this;
    }
    
    public void process() {
    	// TODO : methode appelle toute les ...ms (1step)
    	
    	sendDirectionsPattes(step);
    	
    	step++;
    	
    	if(step >= STEPMAX)
    		step = 0;
    }
    
    private void sendDirectionsPattes(int numStep) {
    	// Step ==> 0 - 127
    	
    	int w_x = x;
    	int w_y = y;
    	int w_z = z;
    	
    	int module = 0;
    	int angle = 0;
    	
    	// TODO : conversion coordonnee joy ==> angle
    	
    	if((w_x >= 10) || (w_y >= 10))
    	{
    		module = 0;
    		angle = 0;
    	}
    	else
    	{
    		// TODO : conversion
    		if (w_x == 0)
    		{
    			if(w_y > 0)
    			{
    				module = w_y;
    				angle = 90;
    			}
    			else
    			{
	    			module = -w_y;
	    			angle = 270;
    			}
    		}
    		else if	(w_y == 0)
    		{
    			if(w_x > 0)
    			{
    				module = w_x;
    				angle = 0;
    			}
    			else
    			{
	    			module = -w_x;
	    			angle = 180;
    			}
    		}
    		else
    		{
    			// TODO : retourne des radians : modifier
    			angle = (int)Math.atan2(w_y, w_x);
    		}
    	}
    		
    	
    	// TODO : conversion joy ==> directions pattes
    	
    	// Init Servomoteurs
        try {
	    	front_left.setPosAll((char)52, (char)-118, (char)97);
			front_right.setPosAll((char)52, (char)118, (char)97);
	        middle_left.setPosAll((char)0, (char)-118, (char)97);
	        middle_right.setPosAll((char)0, (char)118, (char)97);
	        back_left.setPosAll((char)-52, (char)-118, (char)97);
	        back_right.setPosAll((char)-52, (char)118, (char)97);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static Robot getHandle() {
    	return handle;
    }
    
    public void MouvementX(int x_ws) {
    	// X : axe vertical joystick gauche
    	x = x_ws;
    }
    
    public void MouvementY(int y_ws) {
    	// Y : axe horizontal joystick gauche
    	y = y_ws;
    }
    
    public void MouvementZ(int z_ws) {
    	// Z : axe horizontal joystick droit
    	z = z_ws;
    }    
    
}
