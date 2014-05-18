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
	public static final int STEP_MAX = 16;
	public static final int DEAD_ZONE_JOY = 30;
	public static final int OFFSET_ANGLE = 45;
	public static final int LONGUEUR_MAX = 100;
	public static final int VAL_UP_PATTE = 80;
	public static final int VAL_PHI_CIR = 60945;
	
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
	
	private char vitesseServomoteurs;

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
        originRobot();
        
        try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        System.out.println(" OK");
        
        // Definition de l'handle
        handle = this;

        periodTimer = 0;
        vitesseServomoteurs = 512;
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
    	
    	//System.out.println("val coord : " + w_x + ", " + w_y + ", " + w_z);

    	int moduleLeftJoy = module(w_x, w_y);
    	int angleLeftJoy = arcTanDeg(w_x, w_y);
    	
    	if(moduleLeftJoy <= 120)
    	{
    		w_periodTimer = 120;
    		vitesseServomoteurs = 128;
    	}
    	else if(moduleLeftJoy <= 200)
    	{
    		w_periodTimer = 60;
    		vitesseServomoteurs = 256;
    	}
    	else
    	{
    		w_periodTimer = 30;
    		vitesseServomoteurs = 670;
    	}
    		
    	if(w_z == 0)
    	{
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
    		if((w_x != 0) || (w_y != 0))
    		{
	    		int w_phiCIR;
	    		int w_signJoyLeft;
	    		
	    		if(w_z > 0)
	    		{
	    			w_phiCIR = -90;
	    			w_signJoyLeft = -1;
	    		}
	    		else
	    		{
	    			w_phiCIR = 90;
	    			w_signJoyLeft = 1;
	    		}
	    		
	    		float w_xCIR = Math.round( ( (Math.cos(Math.toRadians( angleLeftJoy + w_phiCIR )) * VAL_PHI_CIR ) / (w_signJoyLeft * w_z)) * 100) / 100;
	    		float w_yCIR = Math.round( ( (Math.sin(Math.toRadians( angleLeftJoy + w_phiCIR )) * VAL_PHI_CIR ) / (w_signJoyLeft * w_z)) * 100) / 100;
	    		
	    		float w_distCIR = module(w_xCIR, w_yCIR);
	    		
	    		// Calcul distances (toujour positif)
	    		float w_distFL = (float)Math.sqrt( ((158 + w_xCIR) * (158 + w_xCIR)) + ((218 - w_yCIR) * (218 - w_yCIR)) );
	    		float w_distFR = (float)Math.sqrt( ((158 - w_xCIR) * (158 - w_xCIR)) + ((218 - w_yCIR) * (218 - w_yCIR)) );
	    		float w_distML = (float)Math.sqrt( ((239 + w_xCIR) * (239 + w_xCIR)) + (w_yCIR * w_yCIR) );
	    		float w_distMR = (float)Math.sqrt( ((239 - w_xCIR) * (239 - w_xCIR)) + (w_yCIR * w_yCIR) );
	    		float w_distBL = (float)Math.sqrt( ((158 + w_xCIR) * (158 + w_xCIR)) + ((218 + w_yCIR) * (218 + w_yCIR)) );
	    		float w_distBR = (float)Math.sqrt( ((158 - w_xCIR) * (158 - w_xCIR)) + ((218 + w_yCIR) * (218 + w_yCIR)) );
	    		
	    		float w_distMax = getMax(w_distFL, w_distFR, w_distML, w_distMR, w_distBL, w_distBR); 
	    		
	    		angleFL		= (angleLeftJoy + (w_signJoyLeft * getAngleCIR(w_distCIR, w_distFL, (float)269.235956)) + OFFSET_ANGLE + 360) % 360;
				longueurFL	= getLongueurMovCIR(w_distFL, w_distMax);
				
				angleFR		= (angleLeftJoy + (w_signJoyLeft * getAngleCIR(w_distCIR, w_distFR, (float)269.235956)) - OFFSET_ANGLE + 180) % 360;
				longueurFR	= getLongueurMovCIR(w_distFR, w_distMax);
		        
				angleML		= (angleLeftJoy + (w_signJoyLeft * getAngleCIR(w_distCIR, w_distML, (float)239)) + 360) % 360;
				longueurML	= getLongueurMovCIR(w_distML, w_distMax);
		        
				angleMR		= (angleLeftJoy + (w_signJoyLeft * getAngleCIR(w_distCIR, w_distMR, (float)239)) + 180) % 360;
				longueurMR	= getLongueurMovCIR(w_distMR, w_distMax);
		        
				angleBL		= ((angleLeftJoy + (w_signJoyLeft * getAngleCIR(w_distCIR, w_distBL, (float)269.235956)) - OFFSET_ANGLE) + 360) % 360;
				longueurBL	= getLongueurMovCIR(w_distBL, w_distMax);
		        
				angleBR		= (angleLeftJoy + (w_signJoyLeft * getAngleCIR(w_distCIR, w_distBR, (float)269.235956)) + OFFSET_ANGLE + 180) % 360;
				longueurBR	= getLongueurMovCIR(w_distBR, w_distMax);
    		}
    		else
    		{	
    			int offsetAngleCIR = 0;
    			
    			if(w_z > 0)
    				offsetAngleCIR = 180;
    			
    			angleFL		= 225 - offsetAngleCIR;
				longueurFL	= LONGUEUR_MAX;
				
				angleFR		= 315 - offsetAngleCIR;
				longueurFR	= LONGUEUR_MAX;
		        
				angleML		= 270 - offsetAngleCIR;
				longueurML	= getLongueurMovCIR((float)239, (float)269.235956);
		        
				angleMR		= 270 - offsetAngleCIR;
				longueurMR	= getLongueurMovCIR((float)239, (float)269.235956);
		        
				angleBL		= 315 - offsetAngleCIR;
				longueurBL	= LONGUEUR_MAX;
		        
				angleBR		= 225 - offsetAngleCIR;
				longueurBR	= LONGUEUR_MAX;
				
				if((w_z > 180) || (w_z < -180))
				{
					w_periodTimer = 120;
					vitesseServomoteurs = 128;
				}
				else
				{
					w_periodTimer = 50;
	    			vitesseServomoteurs = 384;
				}
    		}
    	}
    	
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
		    	front_left.upDateDroiteMov(angleFL, longueurFL, vitesseServomoteurs);
				front_right.upDateDroiteMov(angleFR, longueurFR, vitesseServomoteurs);
		        middle_left.upDateDroiteMov(angleML, longueurML, vitesseServomoteurs);
		        middle_right.upDateDroiteMov(angleMR, longueurMR, vitesseServomoteurs);
		        back_left.upDateDroiteMov(angleBL, longueurBL, vitesseServomoteurs);
		        back_right.upDateDroiteMov(angleBR, longueurBR, vitesseServomoteurs);
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
	 * Methode permettant de mettre le robot en position de reference
	 */
    public void originRobot() {
    	StructPatte w_middle = Patte.getPointMiddle();
    	
    	front_left.resetStep();
		front_right.resetStep();
        middle_left.resetStep();
        middle_right.resetStep();
        back_left.resetStep();
        back_right.resetStep();
    	
        try {
			front_left.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia(), (char)256);
			front_right.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia(), (char)256);
	        middle_left.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia(), (char)256);
	        middle_right.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia(), (char)256);
	        back_left.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia(), (char)256);
	        back_right.setPosAll(w_middle.getAngleCoxa(), w_middle.getAngleFemur(), w_middle.getAngleTibia(), (char)256);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * Methode donnant le maximum des valeurs passe en parametres
	 * 
	 * @param val1
	 * 			Valeur 1 a tester
	 * 
	 * @param val2
	 * 			Valeur 2 a tester
	 * 
	 * @param val3
	 * 			Valeur 3 a tester
	 * 
	 * @param val4
	 * 			Valeur 4 a tester
	 * 
	 * @param val5
	 * 			Valeur 5 a tester
	 * 
	 * @param val6
	 * 			Valeur 6 a tester
	 * 
	 * @return la valeur la plus eleve des valeurs passe en parametres
	 */
    private static float getMax(float val1, float val2, float val3, float val4, float val5, float val6) {
    	float valeurMax;
    	
    	if(val1 > val2)
    		valeurMax = val1;
    	else
    		valeurMax = val2;
    	
    	if(valeurMax < val3)
    		valeurMax = val3;
    	
    	if(valeurMax < val4)
    		valeurMax = val4;
    	
    	if(valeurMax < val5)
    		valeurMax = val5;
    	
    	if(valeurMax < val6)
    		valeurMax = val6;
    	
    	return valeurMax;
    }
    
    /**
	 * Methode retourant la longueur de la droite de mouvement pour le CIR
	 * 
	 * @param dist
	 * 			Distance entre le CIR et la patte
	 * 
	 * @param distMax
	 * 			Distance entre le CIR et la patte la plus eloigne
	 * 
	 * @return la valeur de la droite de mouvement
	 */
    private static int getLongueurMovCIR(float dist, float distMax) {
    	return (int)((LONGUEUR_MAX * dist) / distMax);
    }
    
    /**
	 * Methode retourant l'angle de la droite de mouvement pour le CIR
	 * 
	 * @param distCIR
	 * 			Distance entre le CIR et l'origine du robot
	 * 
	 * @param distHypPatte
	 * 			Distance entre le CIR et la patte
	 * 
	 * @param distPatte
	 * 			Distance entre la patte et l'origine du robot
	 * 
	 * @return la valeur de l'angle de la droite de mouvement
	 */
    private static int getAngleCIR(float distCIR, float distHypPatte, float distPatte) {
    	return (int)Math.round(Math.acos( ((distHypPatte * distHypPatte) + (distCIR * distCIR) - (distPatte * distPatte)) / (2 * distHypPatte * distCIR) ));
    }
    
    /**
	 * Methode permettant de retourner un module a partir de deux coordonnees cartesiennes
	 * 
	 * @param x
	 * 			Valeur en x de la coordonnee cartesienne
	 * 
	 * @param y
	 * 			Valeur en y de la coordonnee cartesienne
	 * 
	 * @return module par rapport aux coordonnees
	 */
    private static float module(float x, float y)
    {
    	return (float)(Math.sqrt(x*x + y*y) );
    }
    
    /**
	 * Methode permettant de retourner un module a partir de deux coordonnees cartesiennes
	 * 
	 * @param x
	 * 			Valeur en x de la coordonnee cartesienne
	 * 
	 * @param y
	 * 			Valeur en y de la coordonnee cartesienne
	 * 
	 * @return module par rapport aux coordonnees
	 */
    private static int module(int x, int y)
    {
    	return (int)(Math.round( Math.sqrt(x*x + y*y) ));
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
    private static int arcTanDeg(int x, int y)
    {
    	return (int)((Math.round(Math.toDegrees(Math.atan2(y, x))) + 360) % 360);
    }
}
