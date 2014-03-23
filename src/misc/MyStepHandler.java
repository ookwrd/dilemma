package misc;

import java.util.ArrayList;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

public class MyStepHandler implements StepHandler {

	protected static int maxnumberofstep = 1000000;
	
	protected double[][] timeSerie;
	protected int time = 0;
	protected int startedStable = 0;
	protected int sizeSimpleSeq;
	
	protected long stepCount = 0;
	public ArrayList<Integer> reporters = new ArrayList<Integer>();

	public MyStepHandler(int sizeSimpleSeq) {
		this.sizeSimpleSeq = sizeSimpleSeq;
		
	}

	

	@Override
	public void handleStep(StepInterpolator step, boolean isLastStep) {
		if (stepCount++ > maxnumberofstep) {
			throw new MaxCountExceededException(stepCount);
		}
		StepInterpolator localCopy = step.copy();
		if (step.getCurrentTime() >= time + 1
				&& time <  Constants.internalSteps - 1) {
			time++;
			localCopy.setInterpolatedTime(time);
			double[] y0 = localCopy.getInterpolatedState();
			int offset = 0;
			int dim = Math.min(sizeSimpleSeq, y0.length);
			
				for (int i = 0; i < dim; i++) {
					this.timeSerie[i + offset][time] = y0[i];
				}
				for( int i = 0; i< reporters.size(); i++){
					this.timeSerie[dim + offset + i][time] = y0[reporters.get(i)];
				}
				offset += dim+ reporters.size();
			
			
		}

	}

	@Override
	public void init(double t0, double[] y0, double t) {
		// Be careful that this is only valid if the init is done for time t=0
		this.timeSerie = new double[Math.min(sizeSimpleSeq + reporters.size(), y0.length)][Constants.internalSteps];
		// System.out.println(Math.min(sizeSimpleSeq,y0.length));

		int offset = 0;
		int dim = Math.min(sizeSimpleSeq, y0.length);
		
			for (int i = 0; i < dim; i++) {
				this.timeSerie[i + offset][0] = y0[i ];
			}
			for( int i = 0; i< reporters.size(); i++){
				this.timeSerie[dim + offset + i][0] = y0[reporters.get(i)];
			}
			offset += dim + reporters.size();
		
	}

	public double[][] getTimeSerie() {
		return this.timeSerie.clone();
	}
}
