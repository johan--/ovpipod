package robot;
import servomoteur.*;
import stdrpi.SerialRPi;

/**
 *
 * @author jhergault
 */
public class Patte {
	private int step;
	
    private ServoMoteur servoCoxa;
    private ServoMoteur servoFemur;
    private ServoMoteur servoTibia;
    
    public Patte(SerialRPi liaison, int step, char IDCoxa, char IDFemur, char IDTibia) {
        try {
            servoCoxa = new AX12(liaison, IDCoxa);
            servoFemur = new AX12(liaison, IDFemur);
            servoTibia = new AX12(liaison, IDTibia);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void setPosAll(char PosCoxa, char PosFemur, char PosTibia) throws Exception {
    	setPosCoxa(PosCoxa);
    	setPosFemur(PosFemur);
    	setPosTibia(PosTibia);
    }
    
    public void upDateDroiteMov(int angle, int longueur) throws Exception {

    	char angleCoxa = 0;
    	char angleFemur = 0;
    	char angleTibia = 0;
    	
    	// TODO : droite avec step
    	
    	setPosCoxa(angleCoxa);
    	setPosFemur(angleFemur);
    	setPosTibia(angleTibia);
    	
    	/*servoCoxa.setPositionExtrapol(angleCoxa, vitesse);
    	servoFemur.setPositionExtrapol(angleFemur, vitesse);
    	servoTibia.setPositionExtrapol(angleTibia, vitesse);*/
    	step++;
    }
    
    public void setPosCoxa(char position) throws Exception {
    	servoCoxa.setPosition(position);
    }
    
    public void setPosFemur(char position) throws Exception {
    	servoFemur.setPosition(position);
    }
    
    public void setPosTibia(char position) throws Exception {
    	servoTibia.setPosition(position);
    }
    
    
}
