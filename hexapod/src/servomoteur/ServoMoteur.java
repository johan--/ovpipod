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
 * Classe generalise de servomoteur controle par des trames serie
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public abstract class ServoMoteur {
    protected SerialRPi serialPi;
    protected char IDServo;
    
    /**
     * Constructeur de l'objet ServoMoteur
     * 
     * @param liaison
     * 				Reference de la liaison RS485
     * 
     * @param ID
     * 			ID du servomoteur
     */
    public ServoMoteur(SerialRPi liaison, char ID) throws Exception {
        if((ID >= 0) && (ID <= 253))
            IDServo = ID;
        else
            throw new Exception("Erreur ID Servo");
        
        serialPi = liaison;
    }
    
    /**
     * Methode de controle pour savoir si l'angle du servomoteur est correct
     * 
     * @param w_angle
     * 				angle du servomoteur
     * 
     * @return 1 si l'angle est correct, 0 sinon
     */
    protected static boolean testAngle(char w_angle) {
        return ((w_angle >= 0) && (w_angle <= 1023));
    }
    
    /**
     * Methode de controle pour savoir si la vitesse du servomoteur est correct
     * 
     * @param w_vitesse
     * 				vitesse du servomoteur
     * 
     * @return 1 si la vitesse est correct, 0 sinon
     */
    protected static boolean testVitesse(char w_vitesse) {
        return ((w_vitesse >= 0) && (w_vitesse <= 1023));
    }
    
    /**
     * Methode permettant de calculer le CRC d'une trame
     * 
     * @param data
     * 			Trame de data a envoyer au servomoteur
     * 
     * @return le CRC de la trame
     */
    protected static char CalcCRC(char[] data) {
        int len = data[3] + 2;
        byte crc = 0;
        for (int i = 2; i <= len; i++)
        {
            crc += data[i];
        }
        return (char)((0xFF - crc) & 0xFF);
    }
    
    /**
     * Methode de positionnement du servomoteur
     * 
     * @param angleFinal
     * 			angle du servomoteur
     */
    public void setPosition(char angleFinal) throws Exception {
        if(!testAngle(angleFinal))
            throw new Exception("Erreur angle servomoteur");
 
        // constitution de la trame RS232
        char data[] = new char[9];
        data[0] = 0xFF;									// trame init
        data[1] = 0xFF;									// trame init
        data[2] = IDServo;								// ID du servo a controler
        data[3] = 0x05;									// Longeur de la trame
        data[4] = 0x03;									// Execute une action
        data[5] = 0x1E;									// Command move
        data[6] = (char)(angleFinal & 0xFF);			// char mouvement poid faible
        data[7] = (char)((angleFinal & 0xFF00) >> 8);	// char mouvement poid fort
        data[8] = CalcCRC(data);						// CRC
        
        // envoi de la trame
        serialPi.send(data, 9);
    }
    
    /**
     * Methode de positionnement du servomoteur a une vitesse precise
     * 
     * @param angleFinal
     * 			angle du servomoteur
     * 
     * @param vitesse
     * 			vitesse du servomoteur
     */
    public abstract void setPositionExtrapol(char angle, char vitesse) throws Exception;
    
}
