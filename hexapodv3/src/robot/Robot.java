package robot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.lang.Math;

import stdrpi.SerialRPi;

/**
 * Classe Robot representant l'hexapod associant 6 objects Patte
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 */
public class Robot {
	public static final int STEPMAX = 128;
	
    private int minHauteurPatte;				// Variable indiquant la hauteur des pattes lorsquelles sont pose
    private int maxHauteurPatte;				// Variable indiquant la hauteur des pattes lorsquelles sont leve
    
    private int x_joy;
    private int y_joy;
    private int z_joy;
    
    private static Robot handle;
    
    //private final GpioPinDigitalOutput modeBasc;
    
    // Definition des pattes pour un Hexapod
    private Patte front_left;
    private Patte front_right;
    private Patte middle_left;
    private Patte middle_right;
    private Patte back_left;
    private Patte back_right;
    
    /**
     * Constructeur de l'objet robot representant un ensemble de pattes
     * 
     * @param gpioRPi
     * 				Object permettant la manipulation du GPIO du Raspberry Pi
     */
    public Robot(GpioController gpioRPi) {
        // Ouverture COM serie avant creation des pattes
        SerialRPi liaisonRS232 = new SerialRPi();
        
        // Positionnement bascule mode emission
        /*modeBasc = */gpioRPi.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.HIGH);
        
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
			e.printStackTrace();
		}
        
        // Valeurs temporaires
        minHauteurPatte = 10;   // Defini hauteur de la base hexapod
        maxHauteurPatte = 14;   // Defini hauteur montee des pattes lors du mouvement
        
        // Definition de l'handle
        handle = this;
    }
    
    /**
     * Methode cyclique permettant de mettre a jour les pattes (step + 1)
     */
    public void process() {
    	// TODO : methode appelle toute les ...ms (1step)
    	
    	sendDirectionsPattes();
    }
    
    /**
     * Methode cyclique permettant de mettre a jour les pattes (step + 1)
     */
    private void sendDirectionsPattes() {
    	// Step ==> 0 - 127
    	
    	// Copy pour evite la concurence d'acces
    	int w_x = x_joy;
    	int w_y = y_joy;
    	int w_z = z_joy;
    	
    	// Variable de module et d'angle du joystick de gauche
    	int moduleJoyLeft = 0;
    	int angleJoyLeft = 0;
    	
    	// TODO : conversion coordonnee joystick ==> angle
    	
    	if((w_x >= 10) || (w_y >= 10))
    	{
    		moduleJoyLeft = 0;
    		angleJoyLeft = 0;
    	}
    	else
    	{
    		// TODO : conversion
    		if (w_x == 0)
    		{
    			if(w_y > 0)
    			{
    				moduleJoyLeft = w_y;
    				angleJoyLeft = 90;
    			}
    			else
    			{
	    			moduleJoyLeft = -w_y;
	    			angleJoyLeft = 270;
    			}
    		}
    		else if	(w_y == 0)
    		{
    			if(w_x > 0)
    			{
    				moduleJoyLeft = w_x;
    				angleJoyLeft = 0;
    			}
    			else
    			{
	    			moduleJoyLeft = -w_x;
	    			angleJoyLeft = 180;
    			}
    		}
    		else
    		{
    			// TODO : retourne des radians : modifier
    			angleJoyLeft = (int)Math.atan2(w_y, w_x);
    		}
    	}
    		
    	
    	// TODO : conversion joy ==> directions pattes
    	
    	// Init Servomoteurs
        try {
	    	/*front_left.upDateDroiteMov(int angle, int longueur);
			front_right.upDateDroiteMov(int angle, int longueur);
	        middle_left.upDateDroiteMov(int angle, int longueur);
	        middle_right.upDateDroiteMov(int angle, int longueur);
	        back_left.upDateDroiteMov(int angle, int longueur);
	        back_right.upDateDroiteMov(int angle, int longueur);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static Robot getHandle() {
    	return handle;
    }
    
	/**
	 * Methode permettant de mettre a jour la valeur du joystick gauche sur l'axe horizontal
	 * 
	 * @param x_ws
	 * 			Valeur en int de la position du joystick gauche sur l'axe horizontal
	 */
    public void MouvementX(int x_ws) {
    	x_joy = x_ws;
    }
    
	/**
	 * Methode permettant de mettre a jour la valeur du joystick gauche sur l'axe vertical
	 * 
	 * @param y_ws
	 * 			Valeur en int de la position du joystick gauche sur l'axe vertical
	 */
    public void MouvementY(int y_ws) {
    	y_joy = y_ws;
    }

	/**
	 * Methode permettant de mettre a jour la valeur du joystick droit sur l'axe horizontal
	 * 
	 * @param z_ws
	 * 			Valeur en int de la position du joystick droit sur l'axe horizontal
	 */
    public void MouvementZ(int z_ws) {
    	z_joy = z_ws;
    }    
    
}
