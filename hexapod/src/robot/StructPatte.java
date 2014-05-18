package robot;


/**
 * Classe StructPatte representant les angles des 3 servomoteurs d'une patte
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 */
public class StructPatte {
	private char angleCoxa;
	private char angleFemur;
	private char angleTibia;
	
	/**
     * Constructeur vide de l'objet StructPatte representant les 3 angles des servomoteur de la patte
     * Ce constructeur initialise le mouvement au point milieu
     */
	public StructPatte() {
		StructPatte temp = Patte.getPointMiddle();
		angleCoxa = temp.getAngleCoxa();
		angleFemur = temp.getAngleFemur();
		angleTibia = temp.getAngleTibia();
	}
	
	/**
     * Constructeur copy de l'objet StructPatte representant les 3 angles des servomoteur de la patte
     * 
     * @param copy
     * 			Structure de l'element a copier
     */
	public StructPatte(StructPatte copy) {
		angleCoxa = copy.getAngleCoxa();
		angleFemur = copy.getAngleFemur();
		angleTibia = copy.getAngleTibia();
	}
	
	/**
     * Constructeur normal de l'objet StructPatte representant les 3 angles des servomoteur de la patte
     * 
     * @param AngleCoxa
     * 			Angle du servomoteur Coxa (0 a 1024)
     * 
     * @param AngleFemur
     * 			Angle du servomoteur Femur (0 a 1024)
     * 
     * @param AngleTibia
     * 			Angle du servomoteur Tibia (0 a 1024)
     */
	public StructPatte(char AngleCoxa, char AngleFemur, char AngleTibia) {
		angleCoxa = AngleCoxa;
		angleFemur = AngleFemur;
		angleTibia = AngleTibia;
	}
	
	/**
     * Getteur angle Coxa de la structure
     * 
     * @return angle Coxa
     */
	public char getAngleCoxa() {
		return angleCoxa;
	}
	
	/**
     * Getteur angle Femur de la structure
     * 
     * @return angle Femur
     */
	public char getAngleFemur() {
		return angleFemur;
	}
	
	/**
     * Getteur angle Tibia de la structure
     * 
     * @return angle Tibia
     */
	public char getAngleTibia() {
		return angleTibia;
	}
	
	/**
     * Setteur angle Coxa de la structure
     * 
     * @param AngleCoxa
     * 			Angle du servomoteur Coxa
     */
	public void setAngleCoxa(char AngleCoxa) {
		angleCoxa = AngleCoxa;
	}
	
	/**
     * Setteur angle Femur de la structure
     * 
     * @param AngleFemur
     * 			Angle du servomoteur Femur
     */
	public void setAngleFemur(char AngleFemur) {
		angleFemur = AngleFemur;
	}
	
	/**
     * Setteur angle Tibia de la structure
     * 
     * @param AngleTibia
     * 			Angle du servomoteur Tibia
     */
	public void setAngleTibia(char AngleTibia) {
		angleTibia = AngleTibia;
	}
}
