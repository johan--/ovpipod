package servomoteur;
import stdrpi.SerialRPi;

/**
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 */
public abstract class ServoMoteur {
    protected SerialRPi serialPi;
    protected char IDServo;
    
    public ServoMoteur(SerialRPi liaison, char ID) throws Exception {
        if((ID >= 0) && (ID <= 253))
            IDServo = ID;
        else
            throw new Exception("Erreur ID Servo");
        
        serialPi = liaison;
    }
    
    protected boolean testAngle(char w_angle) {
        return (w_angle >= 0) && (w_angle <= 1023);
    }
    
    protected char CalcCRC(char[] data) {
        int len = data[4] + 4;
        byte crc = 0;
        for (int i = 2; i <= len; i++)
        {
            crc += data[i];
        }
        return (char)((0xFF - crc) & 0xFF);
    }
    
    public void setPosition(char angleFinal) throws Exception {
        if(!testAngle(angleFinal))
            throw new Exception("Erreur angle servomoteur");
 
        // constitution de la trame RS232
        char data[] = new char[9];
        data[0] = 0xFF;									// trame init
        data[1] = 0xFF;									// trame init
        data[2] = IDServo;								// ID du servo a controler
        data[3] = 0x05;									// Execute une action
        data[4] = 0x03;									// Longeur de la trame
        data[5] = 0x1E;									// Command move
        data[6] = (char)(angleFinal & 0xFF);			// char mouvement poid faible
        data[7] = (char)((angleFinal & 0xFF00) >> 8);	// char mouvement poid fort
        data[8] = CalcCRC(data);						// CRC
        
        // envoi de la trame
        serialPi.send(data, 9);
    }
    
    public abstract void setPositionExtrapol(char angle, char vitesse) throws Exception;
    
}
