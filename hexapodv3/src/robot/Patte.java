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

package robot;
import servomoteur.*;
import stdrpi.SerialRPi;

/**
 * Classe definissant une patte d'un robot contennant 3 servomoteurs (Coxa, Femur, Tibia)
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public class Patte {
	private int step;
	
    private ServoMoteur servoCoxa;
    private ServoMoteur servoFemur;
    private ServoMoteur servoTibia;
    
    /**
     * Constructeur d'un object patte
     * 
     * @param liaison
     * 				reference sur la lisaison serie
     * @param step
     * 			valeur du step au depart
     * @param IDCoxa
     * 			ID du servomoteur Coxa de la patte
     * @param IDFemur
     * 			ID du servomoteur Femur de la patte
     * @param IDTibia
     * 			ID du servomoteur Tibia de la patte
     */
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
    
    /**
     * Methode de definition des positions des servomoteurs
     * 
     * @param PosCoxa
     * 			Position du servomoteur Coxa de la patte
     * @param PosFemur
     * 			Position du servomoteur Femur de la patte
     * @param PosTibia
     * 			Position du servomoteur Tibia de la patte
     */
    public void setPosAll(char PosCoxa, char PosFemur, char PosTibia) throws Exception {
    	setPosCoxa(PosCoxa);
    	setPosFemur(PosFemur);
    	setPosTibia(PosTibia);
    }
    
    /**
     * Methode de continuation du mouvement
     * 
     * @param angle
     * 			Angle de la droite
     * @param longueur
     * 			Longueur de la droite de mouvement
     */
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
    
    /**
     * Methode de definition de la position du servomoteur Coxa de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public void setPosCoxa(char position) throws Exception {
    	servoCoxa.setPosition(position);
    }

    /**
     * Methode de definition de la position du servomoteur Femur de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public void setPosFemur(char position) throws Exception {
    	servoFemur.setPosition(position);
    }
    
    /**
     * Methode de definition de la position du servomoteur Tibia de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public void setPosTibia(char position) throws Exception {
    	servoTibia.setPosition(position);
    }
    
    
}
