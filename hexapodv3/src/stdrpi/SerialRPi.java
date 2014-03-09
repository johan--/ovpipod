package stdrpi;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

// Objet SerialRPi a partir de la lib pi4j
// http://pi4j.com/example/serial.html


/**
 *
 * @author jhergault
 */
public class SerialRPi {
    private final Serial serialPi;
    
    public SerialRPi() {
        // Creation de l'instance
        serialPi = SerialFactory.createInstance();

        /*serialPi.addListener(new SerialDataListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                // affiche les donnees recu des servomoteurs
                System.out.print(event.getData());
            }            
        });*/
        
        System.out.print("OPEN_COM_PORT ... ");
        
        try {
	        // Ouverture port serie
	        serialPi.open(Serial.DEFAULT_COM_PORT, 1000000);
        }
	    catch(SerialPortException ex) {
	        System.out.println("SERIAL SETUP FAILED : " + ex.getMessage());
	        return;
	    }
        
        if(serialPi.isOpen())
        	System.out.println("OK.");
        else
        	System.out.println("KO.");
    }
    
    // Fonction pour envoyer un tableau de char
    public void send(char data[], int lenght) {
    	if(serialPi.isOpen()) {
    		for(int i = 0; i < lenght; i++) {
	    		serialPi.write(data[i]);
    		}
    	}
    	else {
    		System.out.println("Serie non ouvert");
    	}
    	
    	// Attente avant de passer au moteur suivant
        /*try {
			Thread.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    

    
    
    
}
