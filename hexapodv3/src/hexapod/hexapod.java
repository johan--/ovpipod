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
    	
    	if(choix == 0)
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
    		
    		/*System.out.println("** Tests droite Patte **");
    		for(int i = 0; i < Robot.STEP_MAX; i++)
    		{
    			System.out.print(i + "|");
    			structPatte test2 = Patte.getPoint(90, 120, i);
    			System.out.println((int)test2.getAngleCoxa() + "|" + (int)test2.getAngleFemur() + "|" + (int)test2.getAngleTibia() );
    		}*/
    		double w_xCIR;
    		double w_yCIR;
    		
    		w_xCIR = Math.round( ( (Math.cos(Math.toRadians( 90 + 90 )) * 35700 ) / 20) * 100) / 100;
    		w_yCIR = Math.round( ( (Math.sin(Math.toRadians( 90 + 90 )) * 35700 ) / 20) * 100) / 100;
    		
    		System.out.println("x CIR = " + w_xCIR + " y CIR = " + w_yCIR);
    		
    		//System.out.println(Robot.getLongueurMovCIR( (float)239, (float)269.235956));
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
    	System.out.println("\t- 1 : GPIO servomoteur raspberry pi");
    	System.out.println("\t- 2 : Jetty websocket");
    	System.out.println("\t- 3 : Lancement total");
    	System.out.println("\t- 7 : Routine de test du programme");
    }
}
