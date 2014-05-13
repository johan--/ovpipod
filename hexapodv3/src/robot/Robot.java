/**
 * This file is part of OVPiPod.
 *
 * OVPiPod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OVPiPod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OVPiPod.  If not, see <http://www.gnu.org/licenses/>.
 */

package robot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.lang.Math;
import java.util.Timer;
//import java.util.TimerTask;

import robot.PeriodicUpdateTask;
import stdrpi.SerialRPi;

/**
 * Classe Robot representant l'hexapod associant 6 objects Patte
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 */
public class Robot {
	public static final int STEP_MAX = 64;
	public static final int DEAD_ZONE_JOY = 20;
	public static final int OFFSET_ANGLE = 45;
	public static final int LONGUEUR_MAX = 100;
	public static final int VAL_UP_PATTE = 50;
	
	private int angleFL;
	private int angleFR;
	private int angleML;
	private int angleMR;
	private int angleBL;
	private int angleBR;
	
	private int longueurFL;
	private int longueurFR;
	private int longueurML;
	private int longueurMR;
	private int longueurBL;
	private int longueurBR;

	private Timer timer;
	private long periodTimer;
	
    //private int minHauteurPatte;				// Variable indiquant la hauteur des pattes lorsquelles sont pose
    //private int maxHauteurPatte;				// Variable indiquant la hauteur des pattes lorsquelles sont leve

    // Valeurs en INT de -255 a 255 des joysticks
    private int x_joy;
    private int y_joy;
    private int z_joy;
    
    private static Robot handle;
    
    @SuppressWarnings("unused")
	private final GpioPinDigitalOutput modeBasc;
    
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
        modeBasc = gpioRPi.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.HIGH);
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        // Creation des pattes de l'hexapod
        front_left = new Patte(liaisonRS232, (STEP_MAX/4), (char)1, (char)3, (char)5);
        front_right = new Patte(liaisonRS232, ((STEP_MAX/4)*3), (char)2, (char)4, (char)6);
        middle_left = new Patte(liaisonRS232, ((STEP_MAX/4)*3), (char)13, (char)15, (char)17);
        middle_right = new Patte(liaisonRS232, (STEP_MAX/4), (char)14, (char)16, (char)18);
        back_left = new Patte(liaisonRS232, (STEP_MAX/4), (char)7, (char)9, (char)11);
        back_right = new Patte(liaisonRS232, ((STEP_MAX/4)*3), (char)8, (char)10, (char)12);
        
        System.out.print("InitServo ... ");
        
        // Init Servomoteurs
        structPatte w_middle = Patte.getPointMiddle();
        try {
			front_left.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia());
			front_right.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia());
	        middle_left.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia());
	        middle_right.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia());
	        back_left.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia());
	        back_right.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        System.out.println(" OK");
        System.out.println("angleCoxa = " + (int)w_middle.getAngleCoxa() + " angleFemur = " + (int)w_middle.getAngleFemur() + " angleTibia = " + (int)w_middle.getAngleTibia());
        
        // Valeurs temporaires
        //minHauteurPatte = 10;   // Defini hauteur de la base hexapod
        //maxHauteurPatte = 14;   // Defini hauteur montee des pattes lors du mouvement
        
        // Definition de l'handle
        handle = this;

        periodTimer = 0;
        timer = new Timer();
        //timer.schedule(new TimerTask(sendDirectionsPattes()), 1000, periodTimer);
    }
    
    /**
     * Methode permettant de parametrer le timer par rapport au module des joysticks
     */
    public void processDirectionRobot() {
    	long w_periodTimer = 100;
    	// Set du timer appellant la methode sendDirectionsPattes cycliquement
    	
    	int w_x = x_joy;
    	int w_y = y_joy;
    	int w_z = z_joy;

    	int angleLeftJoy = ArcTanDeg(w_x, w_y);
    	
    	
    	// TODO calcul module

    	if(w_z == 0)
    	{
    		// TODO sans CIR
    		
    		if( (w_x != 0) || (w_y != 0) )
    		{
    			// Mouvement sans CIR
    			angleFL		= (angleLeftJoy + OFFSET_ANGLE) % 360;
    			longueurFL	= LONGUEUR_MAX;
    			
    			angleFR		= (angleLeftJoy - OFFSET_ANGLE + 180) % 360;
    			longueurFR	= LONGUEUR_MAX;
    	        
    			angleML		= angleLeftJoy;
    			longueurML	= LONGUEUR_MAX;
    	        
    			angleMR		= (angleLeftJoy + 180) % 360;
    			longueurMR	= LONGUEUR_MAX;
    	        
    			angleBL		= ((angleLeftJoy - OFFSET_ANGLE) + 360) % 360;
    			longueurBL	= LONGUEUR_MAX;
    	        
    			angleBR		= (angleLeftJoy + OFFSET_ANGLE + 180) % 360;
    			longueurBR	= LONGUEUR_MAX;
    		}
    		else
    			w_periodTimer = 0;
    	}
    	else
    	{
    		// TODO avec CIR
    	}
    	
    	System.out.println("angleJoy = " + angleLeftJoy + " w_timer = " + w_periodTimer);
    	
    	if(w_periodTimer != periodTimer)
    	{    		
    		if(periodTimer != 0)
    			timer.cancel();
    		
    		if(w_periodTimer != 0)
    		{
    			timer = new Timer();
    			timer.schedule(new PeriodicUpdateTask(), 0, w_periodTimer);
    		}
    		
    		periodTimer = w_periodTimer;
    	}
    }
    
    /**
     * Methode cyclique permettant de mettre a jour les pattes (step + 1)
     */
    public void sendDirectionsPattes() {
    	// Mise a jour des patte
    	if( (x_joy == 0) && (y_joy == 0) && (z_joy == 0) )
    	{
    		// baisser toute les pattes
    	}
    	else
    	{
	        try {
		    	front_left.upDateDroiteMov(angleFL, longueurFL);
				front_right.upDateDroiteMov(angleFR, longueurFR);
		        middle_left.upDateDroiteMov(angleML, longueurML);
		        middle_right.upDateDroiteMov(angleMR, longueurMR);
		        back_left.upDateDroiteMov(angleBL, longueurBL);
		        back_right.upDateDroiteMov(angleBR, longueurBR);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * Methode permettant d'obtenir une reference sur l'objet unique Robot
     */
    public static Robot getHandle() {
    	return handle;
    }
    
	/**
	 * Methode permettant de mettre a jour la valeur du joystick gauche sur l'axe horizontal
	 * 
	 * @param x_ws
	 * 			Valeur en int (-255 a 255) de la position du joystick gauche sur l'axe horizontal
	 */
    public void MouvementX(int x_ws) {
    	if( (x_ws > (-DEAD_ZONE_JOY)) && (x_ws < (DEAD_ZONE_JOY)) )
    		x_ws = 0;

    	if(x_joy != x_ws)
    	{
    		x_joy = x_ws;
    		processDirectionRobot();
    	}
    }
    
	/**
	 * Methode permettant de mettre a jour la valeur du joystick gauche sur l'axe vertical
	 * 
	 * @param y_ws
	 * 			Valeur en int (-255 a 255) de la position du joystick gauche sur l'axe vertical
	 */
    public void MouvementY(int y_ws) {
    	if( (y_ws > (-DEAD_ZONE_JOY)) && (y_ws < (DEAD_ZONE_JOY)) )
    		y_ws = 0;
    	
    	if(y_joy != y_ws)
    	{
    		y_joy = y_ws;
    		processDirectionRobot();
    	}
    }

	/**
	 * Methode permettant de mettre a jour la valeur du joystick droit sur l'axe horizontal
	 * 
	 * @param z_ws
	 * 			Valeur en int (-255 a 255) de la position du joystick droit sur l'axe horizontal
	 */
    public void MouvementZ(int z_ws) {
    	if( (z_ws > (-DEAD_ZONE_JOY)) && (z_ws < (DEAD_ZONE_JOY)) )
    		z_ws = 0;
    	
    	if(z_joy != z_ws)
    	{
    		z_joy = z_ws;
    		processDirectionRobot();
    	}
    }
    
    /**
	 * Methode permettant de retourner un angle a partir de deux coordonnees cartesiennes
	 * 
	 * @param x
	 * 			Valeur en x de la coordonnee cartesienne
	 * 
	 * @param y
	 * 			Valeur en y de la coordonnee cartesienne
	 * 
	 * @return Angle en degre par rapport aux coordonnees
	 */
    public static int ArcTanDeg(int x, int y)
    {
    	return (int)((Math.round(Math.toDegrees(Math.atan2(y, x))) + 360) % 360);
    }
}
