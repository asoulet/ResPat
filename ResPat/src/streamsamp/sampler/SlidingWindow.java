package streamsamp.sampler;

public class SlidingWindow implements Damping {
	
	private double size = 0;

	public SlidingWindow(double size) {
		this.size = size;
	}

	@Override
	public double getWeight(double t) {
		if (t <= size)
			return 1;
		else
			return 0;
	}

	public double getSize() {
		return size;
	}

}
