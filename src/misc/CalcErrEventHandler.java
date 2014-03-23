package misc;

import org.apache.commons.math3.ode.events.EventHandler;

public class CalcErrEventHandler implements EventHandler {

	@Override
	public Action eventOccurred(double arg0, double[] arg1, boolean arg2) {
		return EventHandler.Action.RESET_STATE;
	}

	@Override
	public double g(double arg0, double[] arg1) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < arg1.length; i++) {
			double value = Math.round(arg1[i]*1e6)/1e6; // Added by Nat: sometimes, the precision is too high for the library.
			//just round to something it can handle.
			if (value < min)
				min = value;
		}
		
		return min;
	}

	@Override
	public void init(double arg0, double[] arg1, double arg2) {

	}

	@Override
	public void resetState(double t, double[] y) {
		for (int i = 0;i<y.length;i++){
			y[i] = Math.max(y[i], 0.0);
		}
	}

}
