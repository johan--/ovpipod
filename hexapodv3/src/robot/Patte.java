package robot;
import servomoteur.*;
import stdrpi.SerialRPi;

/**
 *
 * @author jhergault
 */
public class Patte {
    private ServoMoteur servoCoxa;
    private ServoMoteur servoFemur;
    private ServoMoteur servoTibia;
    
    public Patte(SerialRPi liaison, char IDCoxa, char IDFemur, char IDTibia) {
        try {
            servoCoxa = new AX12(liaison, IDCoxa);
            servoFemur = new AX12(liaison, IDFemur);
            servoTibia = new AX12(liaison, IDTibia);
            
            // Init servomoteurs
            servoCoxa.setPosition((char)512);
            servoFemur.setPosition((char)512);
            servoTibia.setPosition((char)512);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
}
