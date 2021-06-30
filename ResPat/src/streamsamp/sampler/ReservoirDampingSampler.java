package streamsamp.sampler;

import java.util.PriorityQueue;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public abstract class ReservoirDampingSampler extends ReservoirSampler {

	protected Damping damping = null;
	private double currentTime = 0;
	private double previousTime = -1;
	
	public ReservoirDampingSampler(int k, Damping damping) {
		this.k = k;
		this.damping = damping;
		sample = new PriorityQueue<Pattern>();
	}
	
	protected void damp(boolean active) {
		if (active && damping != null && !(damping instanceof NoDamping)) {
			PriorityQueue<Pattern> ns = new PriorityQueue<Pattern>();
			for (Pattern pattern : sample) {
				double previousDelay = previousTime - pattern.getTime();
				double currentDelay = currentTime - pattern.getTime();
				double currentW = damping.getWeight(currentDelay);
				double previousW = damping.getWeight(previousDelay);
				if (previousW > 0 && currentW > 0) {
					pattern.setKey(Math.pow(pattern.getKey(), previousW / currentW));
					ns.add(pattern);
				}
				else {
					pattern.setKey(0);
					ns.add(pattern);
				}
			}
			sample = ns;
		}
	}

	@Override
	final protected void addPattern(Pattern pattern) {
		pattern.setTime(currentTime);
		super.addPattern(pattern);
	}
		
	@Override
	final public void processDataObservation(PatternAccesser accesser) {
		currentTime = previousTime + 1;
		if (previousTime >= 0)
			damp(true);
		else
			damp(false);
		processDataObservationWithDamping(accesser);
		previousTime++;		
	}

	public abstract void processDataObservationWithDamping(PatternAccesser accesser);

	public double getCurrentTime() {
		return currentTime;
	}
	
	public void reset() {
		super.reset();
		currentTime = 0;
		previousTime = -1;
	}
	
}
