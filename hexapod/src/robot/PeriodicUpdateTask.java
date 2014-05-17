package robot;

import java.util.TimerTask;

public class PeriodicUpdateTask extends TimerTask {
	@Override
    public void run() {
	    Robot.getHandle().sendDirectionsPattes();
    }

}
