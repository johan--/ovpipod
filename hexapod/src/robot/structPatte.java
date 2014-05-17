package robot;

public class structPatte {
	private char angleCoxa;
	private char angleFemur;
	private char angleTibia;
	
	public structPatte() {
		structPatte temp = Patte.getPointMiddle();
		angleCoxa = temp.getAngleCoxa();
		angleFemur = temp.getAngleFemur();
		angleTibia = temp.getAngleTibia();
	}
	
	public structPatte(structPatte copy) {
		angleCoxa = copy.getAngleCoxa();
		angleFemur = copy.getAngleFemur();
		angleTibia = copy.getAngleTibia();
	}
	
	public structPatte(char AngleCoxa, char AngleFemur, char AngleTibia) {
		angleCoxa = AngleCoxa;
		angleFemur = AngleFemur;
		angleTibia = AngleTibia;
	}
	
	public char getAngleCoxa() {
		return angleCoxa;
	}
	
	public char getAngleFemur() {
		return angleFemur;
	}
	
	public char getAngleTibia() {
		return angleTibia;
	}
	
	public void setAngleCoxa(char AngleCoxa) {
		angleCoxa = AngleCoxa;
	}
	
	public void setAngleFemur(char AngleFemur) {
		angleFemur = AngleFemur;
	}
	
	public void setAngleTibia(char AngleTibia) {
		angleTibia = AngleTibia;
	}
}
