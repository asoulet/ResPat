package streamsamp.sampler;
import java.math.BigInteger;
import org.apache.log4j.Logger;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public class AResDampingSampler extends ReservoirDampingSampler {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AResDampingSampler.class);
	
	public AResDampingSampler(int k, Damping damping) {
		super(k, damping);
	}
	
	public AResDampingSampler(int k) {
		this(k, null);
	}

	@Override
	public void processDataObservationWithDamping(PatternAccesser accesser) {
		BigInteger cardinality = accesser.getCardinality();
		BigInteger current = fill(accesser, cardinality);
		while (current.compareTo(cardinality) < 0) {
			double r = Math.random();
			if (minKey() < r) {
				pollPattern();
				Pattern pattern = accesser.pick(current);
				pattern.setKey(r);
				addPattern(pattern);
			}
			current = current.add(BigInteger.ONE);
		}
	}
	
}
