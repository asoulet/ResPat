package streamsamp.sampler;

public class NoDamping implements Damping {

	@Override
	public double getWeight(double t) {
		return 1;
	}

}
