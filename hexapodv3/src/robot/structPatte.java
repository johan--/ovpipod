package robot;

public class structPatte {
	private char angleCoxa;
	private char angleFemur;
	private char angleTibia;
	
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

}
