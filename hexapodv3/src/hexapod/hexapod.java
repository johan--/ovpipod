package hexapod;

import java.util.Scanner;

import jetty.WebSocketRobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import robot.*;

/**
 * Classe Maitre permettant de lancer le Serveur Websocket et/ou le controleur du robot
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public class hexapod {
	/**
	 * Main de la classe Maitre
	 *
	 * @param args
	 * 			Argument passe en parametre du programme
	 */
    public static void main(String[] args) {

    	int choix = 0;
    	
    	if(args.length == 1)
    	{
    		choix = Integer.parseInt(args[0]);
    		
    		if(args[0] == "-h")
    		{
    			// Affichage de l'aide
    			helpPrint();
    		}
    	}
    	
    	if(choix != 0)
    	{
	    	Scanner sc = new Scanner(System.in);
	    	helpPrint();
	    	choix = sc.nextInt();
	    	sc.close();
    	}
    	
    	if(choix == 7)
    	{
    		System.out.println("**** Tests unitaires du programme OVPIPOD V3 ****");
    		// TODO : routine de test a effectuer ici JP
    	}
    	else
    	{
	    	if(choix != 2) {
		    	// create gpio controller
		        final GpioController gpio = GpioFactory.getInstance();
		        
		        // Init du robot et attente instruction
		        @SuppressWarnings("unused")
				Robot Hexapod = new Robot(gpio);
	    	}
	        
	    	if(choix != 1) {
		        // Init serveur websocket
		        WebSocketRobot jetty = new WebSocketRobot();
		        jetty.start();
	    	}
    	}
    }
    
    /**
     * Methode affichant l'aide a l'ecran du programme
     */
    private static void helpPrint() {
    	System.out.println("Choix execution :");
    	System.out.println("    - 1 : GPIO servomoteur raspberry pi");
    	System.out.println("    - 2 : Jetty websocket");
    	System.out.println("    - 3 : Lancement total");
    	System.out.println("    - 7 : Routine de test du programme");
    }
}
