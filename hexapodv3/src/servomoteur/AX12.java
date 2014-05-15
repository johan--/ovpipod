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

package servomoteur;
import stdrpi.SerialRPi;

/**
 * Classe specifique du servomoteur AX12 (heritage avec ServoMoteur)
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public class AX12 extends ServoMoteur {
    
    public AX12(SerialRPi liaison, char ID) throws Exception {
        super(liaison, ID);
    }
    
    /**
     * Methode changement d'angle selon une vitesse
     * @param angle
     * @param vitesse
     */
    @Override
    public void setPositionExtrapol(char angle, char vitesse) throws Exception {
        if(!testAngle(angle))
            throw new Exception("Erreur angle servomoteur : " + angle);
        if(!testVitesse(vitesse))
            throw new Exception("Erreur vitesse servomoteur : " + vitesse);
        
        
        // Position the output of a Dynamixel actuator with an ID of 0 to 180° with an angular velocity of 57 RPM
        //
        // Set address 0x1E (goal position) to 0x200 and Address 0x20 (moving speed) to 0x200
        // Instruction = WRITE_DATA, Address = 0x1E, DATA = 0x00, 0x02, 0x00, 0x02 
        //
        //FF FF 00 07 03 1E 00 02 00 02 D3 (LEN:011)
        
        // constitution de la trame RS232
        char data[] = new char[11];
        data[0] = 0xFF;									// trame init
        data[1] = 0xFF;									// trame init
        data[2] = IDServo;								// ID du servo a controler
        data[3] = 0x07;									// Longueur de la trame
        data[4] = 0x03;									// Execute une action
        data[5] = 0x1E;									// Command move
        data[6] = (char)(angle & 0xFF);					// char mouvement poid faible
        data[7] = (char)((angle & 0xFF00) >> 8);		// char mouvement poid fort
        data[8] = (char)(vitesse & 0xFF);				// char vitesse poid faible
        data[9] = (char)((vitesse & 0xFF00) >> 8);		// char vitesse poid fort
        data[10] = CalcCRC(data);						// CRC
        
        // TODO : verifier la trame pour la vitesse progressive
        
        // envoi de la trame
        serialPi.send(data, 11);
    }
}
