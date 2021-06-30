package streamsamp.sampler;

public class DampedWindow implements Damping {

	private double alpha;
	
	public DampedWindow(double alpha) {
		this.alpha = alpha;
	}

	@Override
	public double getWeight(double t) {
		return Math.exp(- alpha * t);
	}

	public double getAlpha() {
		return alpha;
	}

}
