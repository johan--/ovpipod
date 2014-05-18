package robot;

import java.util.TimerTask;

/**
 * Classe Decrivant la tache periodique a executer pour la mise a jour des servomoteurs
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 */
public class PeriodicUpdateTask extends TimerTask {
	@Override
    public void run() {
	    Robot.getHandle().sendDirectionsPattes();
    }
}
