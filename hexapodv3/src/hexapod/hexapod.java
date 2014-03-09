package hexapod;

import java.util.Scanner;

import jetty.WebSocketRobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import robot.*;

public class hexapod {
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.println("Choix execution :");
    	System.out.println("    - 1 : GPIO servomoteur raspberry pi");
    	System.out.println("    - 2 : Jetty websocket");
    	System.out.println("    - 3 : Lancement total");
    	
    	int choix = sc.nextInt();
    	
    	if(choix != 2) {
	    	// create gpio controller
	        final GpioController gpio = GpioFactory.getInstance();
	        
	        // Init du robot et attente instruction
	        Robot Hexapod = new Robot(gpio);
    	}
        
    	if(choix != 1) {
	        // Init serveur websocket
	        WebSocketRobot jetty = new WebSocketRobot() ;
	        jetty.start();
    	}
    }
}
