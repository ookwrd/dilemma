package world;

/**
 * Object representing what an agent decided to do this turn
 * 
 * @author olaf
 * 
 */
public class Decision {
	double signalOut;
	double speed;
	boolean cooperate;

	public double getSignalOut() {
		return signalOut;
	}

	public void setSignalOut(double signalOut) {
		this.signalOut = signalOut;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public boolean cooperates() {
		return cooperate;
	}

	public void setCooperate(boolean cooperate) {
		this.cooperate = cooperate;
	}
}
