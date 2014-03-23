package misc;

import java.util.HashMap;

import org.apache.commons.math3.ode.events.EventHandler;


public class MyODEEventHandler implements EventHandler {

	public int maxValue = 1000;


	// @Override
	public Action eventOccurred(double arg0, double[] arg1, boolean arg2) {
	
			return EventHandler.Action.STOP;
		
	}

	// @Override
	public double g(double t, double[] y) {
		double ret = 0.0;
		for(int i = 0; i < y.length; i++){
			if(ret < y[i])
				ret = y[i];
		}
		return ret-maxValue;
	}

	// @Override
	public void init(double arg0, double[] arg1, double arg2) {

	}

	//@Override
	public void resetState(double arg0, double[] arg1) {
		// TODO Auto-generated method stub
		
	}

	

}
