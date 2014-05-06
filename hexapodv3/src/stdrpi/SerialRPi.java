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

package stdrpi;

import com.pi4j.io.serial.Serial;
//import com.pi4j.io.serial.SerialDataEvent;
//import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

// Objet SerialRPi a partir de la lib pi4j
// http://pi4j.com/example/serial.html


/**
 * Classe SerialRPi genere a partir de la lib pi4j
 * http://pi4j.com/example/serial.html
 * Librairie modifie pour accepter une liaison serie de 1Mbit/s
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public class SerialRPi {
    private final Serial serialPi;
    
    /**
     * Constructeur liaison serie 1 Mbits/s
     */
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

    /**
     * Methode permettant d'envoyer un tableau de char
     *
     * @param data
     * 			Tableau de char
     * @param lenght
     * 			Nombre d'octets a envoyer (taille du tableau de char)
     */
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
