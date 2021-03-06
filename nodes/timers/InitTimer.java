package projects.dm.nodes.timers;

import sinalgo.nodes.timers.Timer;
import projects.dm.nodes.nodeImplementations.DMNode;

public class InitTimer extends Timer {
	static public int timerRefresh = 100;

	private DMNode initiator;
	
	public InitTimer(DMNode initiator) {
			this.initiator = initiator;
	}
		
	/* the function "fire" is called when the timer is over */
	public void fire() {
		initiator.initiate();
		//this.startRelative(timerRefresh, initiator); // recursive restart of the timer (sends several walkers)
	}
}

