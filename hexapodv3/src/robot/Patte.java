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
import robot.structPatte;
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
     * Methode Pour poser toutes les pattes au sol
     */
    public void setGroundAll()
    {
    	// TODO
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
    	
    	System.out.println("angleD = " + angle + " longD = " + longueur);
    	
    	structPatte position = getPointTop(angle, longueur);
    	
    	angleCoxa = position.getAngleCoxa();
    	angleFemur = position.getAngleFemur();
    	angleTibia = position.getAngleTibia();
    	
    	
    	setPosCoxa(angleCoxa);
    	setPosFemur(angleFemur);
    	setPosTibia(angleTibia);
    	/*servoCoxa.setPositionExtrapol(angleCoxa, vitesse);
    	servoFemur.setPositionExtrapol(angleFemur, vitesse);
    	servoTibia.setPositionExtrapol(angleTibia, vitesse);*/
    	//step++;
    }
    
    /**
     * Methode de definition de la position du servomoteur Coxa de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public void setPosCoxa(char position) throws Exception {
    	if((position < 410) || (position > 615))
    		throw new Exception("Erreur angle servomoteur Coxa : " + (int)position);
    	servoCoxa.setPosition(position);
    }

    /**
     * Methode de definition de la position du servomoteur Femur de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public void setPosFemur(char position) throws Exception {
    	if((position < 341) || (position > 683))
    		throw new Exception("Erreur angle servomoteur Femur : " + (int)position);

    	servoFemur.setPosition(position);
    }
    
    /**
     * Methode de definition de la position du servomoteur Tibia de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public void setPosTibia(char position) throws Exception {
    	if((position < 205) || (position > 717))
    		throw new Exception("Erreur angle servomoteur Tibia : " + (int)position);

    	servoTibia.setPosition(position);
    }
    
    /**
     * Methode renvoyant les angles des ser
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF
     */
    public static structPatte getPointMiddle() {
    	return new structPatte((char)512, (char)154, (char)590);
    }
    
    public static structPatte getPointTop(int angle, int longueur) {
    	int longueurDemiCarre = ((longueur / 2) * (longueur / 2));
    	double w_dist1 = Math.sqrt( longueurDemiCarre + 25921 - (longueur * 161 * Math.cos(Math.toRadians(angle))) );
    	double w_dist2 = Math.sqrt( ((w_dist1 - 53) * (w_dist1 - 53)) + 12100 );
    	
    	char angleCoxa;
    	if(angle > 180)
    		angleCoxa = (char)(512 + (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 25921 - longueurDemiCarre) / (2 * 161 * w_dist1)) ))) * 256) / 75));
    	else
    		angleCoxa = (char)(512 - (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 25921 - longueurDemiCarre) / (2 * 161 * w_dist1)) ))) * 256) / 75));
    		
    	char angleFemur = (char)(512 + ((((Math.toDegrees(Math.acos( ((w_dist2 * w_dist2) - 13600) / (132 * w_dist2) )) + Math.toDegrees(Math.acos(110 / w_dist2)) ) - 90) * 256) / 75));
    	char angleTibia = (char)(512 - (((135 - Math.toDegrees(Math.acos( (22312 - (w_dist2 * w_dist2)) / 17688 ))) * 256) / 75));    	
    	
    	return new structPatte(angleCoxa, angleFemur, angleTibia);
    }
    
    public static structPatte getPointBottom(int angle, int longueur) {
    	int longueurDemiCarre = ((longueur / 2) * (longueur / 2));
    	double w_dist1 = Math.sqrt( longueurDemiCarre + 25921 - (longueur * 161 * Math.cos(Math.toRadians(180 - angle))) );
    	double w_dist2 = Math.sqrt( ((w_dist1 - 53) * (w_dist1 - 53)) + 12100 );
    	
    	char angleCoxa;
    	if(angle > 180)
    		angleCoxa = (char)(512 - (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 25921 - longueurDemiCarre) / (2 * 161 * w_dist1)) ))) * 256) / 75));
    	else
    		angleCoxa = (char)(512 + (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 25921 - longueurDemiCarre) / (2 * 161 * w_dist1)) ))) * 256) / 75));
    	
    	char angleFemur = (char)(512 + ((((Math.toDegrees(Math.acos( ((w_dist2 * w_dist2) - 13600) / (132 * w_dist2) )) + Math.toDegrees(Math.acos(110 / w_dist2)) ) - 90) * 256) / 75));
    	char angleTibia = (char)(512 - (((135 - Math.toDegrees(Math.acos( (22312 - (w_dist2 * w_dist2)) / 17688 ))) * 256) / 75));
    	
    	return new structPatte(angleCoxa, angleFemur, angleTibia);
    }
    
    public void getMov() {
    	// TODO
    }
    
    
}
