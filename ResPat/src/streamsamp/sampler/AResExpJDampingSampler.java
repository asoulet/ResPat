package streamsamp.sampler;
import java.math.BigInteger;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public class AResExpJDampingSampler extends ReservoirDampingSampler {
	

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AResExpJDampingSampler.class);
	
	private static DecimalFormat formatter = new DecimalFormat("###");
	private BigInteger skip = null;

	
	public AResExpJDampingSampler(int k, Damping damping) {
		super(k, damping);
	}

	public AResExpJDampingSampler(int k) {
		this(k,  null);
	}

	@Override
	public void processDataObservationWithDamping(PatternAccesser accesser) {
		BigInteger cardinality = accesser.getCardinality();
		BigInteger current = fill(accesser, cardinality);
		if (skip == null)
			skip = new BigInteger(formatter.format(Math.floor(Math.log(Math.random()) / Math.log(minKey())) + 1));
		while (current.add(skip).compareTo(cardinality) < 0) {
			double min = minKey();
			pollPattern();
			current = current.add(skip);
			Pattern pattern = accesser.pick(current);
			pattern.setKey(min + Math.random() * (1 - min));
			addPattern(pattern);
			double mk = minKey();
			if (mk == 1)
				mk = 0.9999999999999999;
			skip = new BigInteger(formatter.format(Math.floor(Math.log(Math.random()) / Math.log(mk)) + 1));
			//logger.trace(mk + " " + skip);
		}
		skip = current.add(skip).subtract(cardinality);
	}
	
	public void reset() {
		super.reset();
		skip = null;
	}
	
}
