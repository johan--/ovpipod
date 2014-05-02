package servomoteur;
import stdrpi.SerialRPi;

/**
 *
 * @author jhergault
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
            throw new Exception("Erreur angle servomoteur");
 
        // constitution de la trame RS232
        char data[] = new char[9];
        data[0] = 0xFF;									// trame init
        data[1] = 0xFF;									// trame init
        data[2] = IDServo;								// ID du servo a controler
        data[3] = 0x05;									// Execute une action
        data[4] = 0x03;									// Longeur de la trame
        data[5] = 0x1D;									// Command Slope
        data[6] = (char)(angle & 0xFF);					// char mouvement poid faible
        data[7] = (char)((angle & 0xFF00) >> 8);		// char mouvement poid fort
        data[8] = CalcCRC(data);						// CRC
        
        // TODO : verifier
        
        // envoi de la trame
        serialPi.send(data, 9);
    }
}
