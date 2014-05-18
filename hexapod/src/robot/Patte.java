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
import robot.StructPatte;
import servomoteur.*;
import stdrpi.SerialRPi;

/**
 * Classe definissant une patte d'un robot contennant 3 servomoteurs (Coxa, Femur, Tibia)
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public class Patte {
	private int initStep;
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
    public Patte(SerialRPi liaison, int Step, char IDCoxa, char IDFemur, char IDTibia) {
    	initStep = Step;
    	step = Step;
    	
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
     * Test si l'angle de la droite de mouvement est correct
     * 
     * @param angle
     * 				angle de la droite de mouvement
     * 
     * @return true si l'angle est correct, flase sinon
     */
    private boolean testAngle(int angle) {
    	return ( (angle >= 0) && (angle < 360) );
    }
    
    /**
     * Test si la longueur de la droite de mouvement est correcte
     * 
     * @param longueur
     * 				longueur de la droite de mouvement
     * 
     * @return true si la longueur est correct, flase sinon
     */
    private boolean testLongueur(double longueur) {
    	return ( (longueur >= 0) && (longueur <= Robot.LONGUEUR_MAX) );
    }
    
    /**
     * Methode de definition des positions des servomoteurs
     * 
     * @param PosCoxa
     * 			Position du servomoteur Coxa de la patte de 0 a 0FFF (0 a 1024)
     * 
     * @param PosFemur
     * 			Position du servomoteur Femur de la patte de 0 a 0FFF (0 a 1024)
     * 
     * @param PosTibia
     * 			Position du servomoteur Tibia de la patte de 0 a 0FFF (0 a 1024)
     */
    public void setPosAll(char PosCoxa, char PosFemur, char PosTibia) throws Exception {
    	setPosCoxa(PosCoxa);
    	setPosFemur(PosFemur);
    	setPosTibia(PosTibia);
    }
    
    /**
     * Methode de definition des positions des servomoteurs en indiquant la vitesse
     * 
     * @param PosCoxa
     * 			Position du servomoteur Coxa de la patte de 0 a 0FFF (0 a 1024)
     * 
     * @param PosFemur
     * 			Position du servomoteur Femur de la patte de 0 a 0FFF (0 a 1024)
     * 
     * @param PosTibia
     * 			Position du servomoteur Tibia de la patte de 0 a 0FFF (0 a 1024)
     * 
     * @param vitesse
     * 			Vitesse des servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosAll(char PosCoxa, char PosFemur, char PosTibia, char vitesse) throws Exception {
    	setPosCoxa(PosCoxa, vitesse);
    	setPosFemur(PosFemur, vitesse);
    	setPosTibia(PosTibia, vitesse);
    }
    
    /**
     * Methode Pour poser toutes les pattes au sol
     */
    public void setGroundAll() {
    	if(step > (Robot.STEP_MAX/2))
    		;// TODO
    }
    
    /**
     * Methode de continuation du mouvement
     * 
     * @param angle
     * 			Angle de la droite
     * @param longueur
     * 			Longueur de la droite de mouvement
     * 
     * @param vitesse
     * 			Vitesse des servomoteurs
     */
    public void upDateDroiteMov(int angle, double longueur, char vitesse) throws Exception {
    	if(!testAngle(angle))
    		throw new Exception("Erreur angle droite mouvement : " + angle);
    	if(!testLongueur(longueur))
    		throw new Exception("Erreur longueur droite mouvement : " + longueur);

    	StructPatte w_position;
    	
    	if(longueur != 0)
    		w_position = getPoint(angle, longueur);
    	else
    		w_position = getPointMiddle();

    	servoCoxa.setPositionExtrapol(w_position.getAngleCoxa(), vitesse);
    	servoFemur.setPositionExtrapol(w_position.getAngleFemur(), vitesse);
    	servoTibia.setPositionExtrapol(w_position.getAngleTibia(), vitesse);
    	
    	step++;
    	if(step > Robot.STEP_MAX)
    		step = 0;
    }
    
    /**
     * Methode de definition de la position du servomoteur Femur de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosCoxa(char position) throws Exception {
    	if((position < 405) || (position > 620))
    		throw new Exception("Erreur angle servomoteur Coxa : " + (int)position);

    	servoCoxa.setPosition(position);
    }
    
    /**
     * Methode de definition de la position du servomoteur Coxa de la patte en indiquant la vitesse
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF (0 a 1024)
     * 
     * @param vitesse
     * 			Vitesse du servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosCoxa(char position, char vitesse) throws Exception {
    	if((position < 405) || (position > 620))
    		throw new Exception("Erreur angle servomoteur Coxa : " + (int)position);

    	servoCoxa.setPositionExtrapol(position, vitesse);
    }

    /**
     * Methode de definition de la position du servomoteur Femur de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosFemur(char position) throws Exception {
    	if((position < 300) || (position > 700))
    		throw new Exception("Erreur angle servomoteur Femur : " + (int)position);

    	servoFemur.setPosition(position);
    }
    
    /**
     * Methode de definition de la position du servomoteur Femur de la patte en indiquant la vitesse
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF (0 a 1024)
     * 
     * @param vitesse
     * 			Vitesse du servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosFemur(char position, char vitesse) throws Exception {
    	if((position < 300) || (position > 700))
    		throw new Exception("Erreur angle servomoteur Coxa : " + (int)position);

    	servoFemur.setPositionExtrapol(position, vitesse);
    }
    
    /**
     * Methode de definition de la position du servomoteur Tibia de la patte
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosTibia(char position) throws Exception {
    	if((position < 220) || (position > 710))
    		throw new Exception("Erreur angle servomoteur Tibia : " + (int)position);

    	servoTibia.setPosition(position);
    }
    
    /**
     * Methode de definition de la position du servomoteur Coxa de la patte en indiquant la vitesse
     * 
     * @param position
     * 			Angle du servomoteur de 0 a 0FFF (0 a 1024)
     * 
     * @param vitesse
     * 			Vitesse du servomoteur de 0 a 0FFF (0 a 1024)
     */
    public void setPosTibia(char position, char vitesse) throws Exception {
    	if((position < 220) || (position > 710))
    		throw new Exception("Erreur angle servomoteur Coxa : " + (int)position);

    	servoTibia.setPositionExtrapol(position, vitesse);
    }
    
    /**
     * Reset de la step courante par celle initiale
     */
    public void resetStep() {
    	step = initStep;
    }
    
    /**
     * Methode renvoyant le mouvement de la patte en l'air
     * 
     * @param position
     * 			Position des servomoteurs
     * 
     * @return objet structPatte contenant les angles pour la patte en l'air
     */
    private static StructPatte upPatte(StructPatte position) {
    	position.setAngleFemur((char)(position.getAngleFemur() - Robot.VAL_UP_PATTE));
    	//position.setAngleTibia((char)(position.getAngleTibia() - Robot.VAL_UP_PATTE));
    	return position;
    }
    
    /**
     * Methode renvoyant les angles des servomoteurs pour la position middle
     */
    public static StructPatte getPointMiddle() {
    	return new StructPatte((char)512, (char)575, (char)362);
    }
    
    /**
     * Methode renvoyant les angles des servomoteurs pour la position top
     * 
     * @param angle
     * 			Angle de la droite du mouvement
     * 
     * @param longueur
     * 			Longueur de la droite du mouvement
     * 
     * @return objet structPatte contenant les angles du mouvement pour la position extreme top
     */
    public static StructPatte getPointTop(int angle, double longueur) {
    	double longueurDemiCarre = ((longueur / 2) * (longueur / 2));
    	double w_dist1 = Math.sqrt( longueurDemiCarre + 19321 - (longueur * 139 * Math.cos(Math.toRadians(angle))) );
    	double w_dist2 = Math.sqrt( ((w_dist1 - 52) * (w_dist1 - 52)) + 13225 );
    	
    	char angleCoxa;
    	if(angle > 180)
    		angleCoxa = (char)(512 + (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 19321 - longueurDemiCarre) / (2 * 139 * w_dist1)) ))) * 256) / 75));
    	else
    		angleCoxa = (char)(512 - (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 19321 - longueurDemiCarre) / (2 * 139 * w_dist1)) ))) * 256) / 75));
    		
    	char angleFemur = (char)(556 + ((((Math.toDegrees(Math.acos( ((w_dist2 * w_dist2) - 13200) / (134 * w_dist2) )) + Math.toDegrees(Math.acos(115 / w_dist2)) ) - 90) * 256) / 75));
    	char angleTibia = (char)(512 - (((138 - Math.toDegrees(Math.acos( (22178 - (w_dist2 * w_dist2)) / 17822 ))) * 256) / 75)); 
    	
    	return new StructPatte(angleCoxa, angleFemur, angleTibia);
    }
    
    /**
     * Methode renvoyant les angles des servomoteurs pour la position top
     * 
     * @param angle
     * 			Angle de la droite du mouvement
     * 
     * @param longueur
     * 			Longueur de la droite du mouvement
     * 
     * @return objet structPatte contenant les angles du mouvement pour la position extreme top
     */
    private static StructPatte getPointBottom(int angle, double longueur) {
    	double longueurDemiCarre = ((longueur / 2) * (longueur / 2));
    	double w_dist1 = Math.sqrt( longueurDemiCarre + 19321 - (longueur * 139 * Math.cos(Math.toRadians(180-angle))) );
    	double w_dist2 = Math.sqrt( ((w_dist1 - 52) * (w_dist1 - 52)) + 13225 );
    	
    	char angleCoxa;
    	if(angle > 180)
    		angleCoxa = (char)(512 - (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 19321 - longueurDemiCarre) / (2 * 139 * w_dist1)) ))) * 256) / 75));
    	else
    		angleCoxa = (char)(512 + (((Math.toDegrees(Math.acos( (((w_dist1 * w_dist1) + 19321 - longueurDemiCarre) / (2 * 139 * w_dist1)) ))) * 256) / 75));
    		
    	char angleFemur = (char)(556 + ((((Math.toDegrees(Math.acos( ((w_dist2 * w_dist2) - 13200) / (134 * w_dist2) )) + Math.toDegrees(Math.acos(115 / w_dist2)) ) - 90) * 256) / 75));
    	char angleTibia = (char)(512 - (((138 - Math.toDegrees(Math.acos( (22178 - (w_dist2 * w_dist2)) / 17822 ))) * 256) / 75));  
    	
    	return new StructPatte(angleCoxa, angleFemur, angleTibia);
    }
    
    /**
     * Methode renvoyant le mouvement a effectuer pour la step courante
     * 
     * @param angle
     * 			Angle de la droite du mouvement
     * 
     * @param longueur
     * 			Longueur de la droite du mouvement
     * 
     * @return objet structPatte contenant les angles du mouvement de la step courante
     */
    public StructPatte getPoint(int angle, double longueur) {
    	final int demiStep = (Robot.STEP_MAX/2);
    	final int quartStep = (Robot.STEP_MAX/4);
    	int w_step = Math.abs(step - demiStep);
    	StructPatte w_middle = getPointMiddle();
    	StructPatte w_extrem;

    	if(w_step < quartStep)
    	{
    		w_extrem = getPointTop(angle, longueur);
    	}
    	else if(w_step > quartStep)
    	{
    		w_step = demiStep - w_step;
    		w_extrem = getPointBottom(angle, longueur);
    	}
    	else
    	{
    		if((step > demiStep) && (step < Robot.STEP_MAX))
    			return upPatte(w_middle);
    		else
    			return w_middle;
    	}
    	
    	StructPatte w_position = new StructPatte((char)( (((quartStep - w_step) * w_extrem.getAngleCoxa()) + (w_step * w_middle.getAngleCoxa())) / quartStep ),
    			(char)( (((quartStep - w_step) * w_extrem.getAngleFemur()) + (w_step * w_middle.getAngleFemur())) / quartStep ),
    			(char)( (((quartStep - w_step) * w_extrem.getAngleTibia()) + (w_step * w_middle.getAngleTibia())) / quartStep ));
    	
    	// On leve la patte pour pisser
    	if((step > demiStep) && (step < Robot.STEP_MAX))
    		w_position = upPatte(w_position);
    	
    	return w_position;
    }   
}
