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
    public void setPositionExtrapol(char angle, float vitesse) {
        // TODO
    }
}
