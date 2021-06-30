package streamsamp.sampler;
import java.math.BigInteger;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public class AResExpJExpDampingSampler extends ReservoirDampingSampler {
	

	private static Logger logger = Logger.getLogger(AResExpJExpDampingSampler.class);
	
	private static DecimalFormat formatter = new DecimalFormat("###");
	private BigInteger skip = null;

	private final double alpha;
	private final double factor;
	private double df = 1;
	private double idf = 1;
	
	private int dampingTimes = 0;

	
	public AResExpJExpDampingSampler(int k, Damping damping) {
		super(k, damping);
		if (damping instanceof DampedWindow)
			this.alpha = ((DampedWindow)damping).getAlpha();
		else {
			logger.warn("incorrect damping!");
			alpha = 0;
		}
		factor = Math.exp(alpha);
	}
	
/*	private double dampingFactor() {
		//return Math.exp(alpha * dampingTimes);
		return df;
	}

	private double inverseDampingFactor() {
		//return 1/Math.exp(alpha * dampingTimes);
		return idf;
	}*/

	public AResExpJExpDampingSampler(int k) {
		this(k,  null);
	}
	
	

	@Override
	protected void damp(boolean active) {
		if (active) {
			dampingTimes++;
			df = df * factor;
			idf = idf * (1/factor);
			if (dampingTimes == 10000) {
				for (Pattern pattern : sample) {
					pattern.setKey(Math.pow(pattern.getKey(), df));
				}
				dampingTimes = 0;
				df = 1;
				idf = 1;
			}
		}
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
			double key = min + Math.random() * (1 - min);
			//logger.trace("add " + key + " " + Math.pow(key, idf));
			key = Math.pow(key, idf);
			pattern.setKey(key);
			addPattern(pattern);
			//logger.trace("jump " + key + " " + minKey());
			double mk = minKey();
			if (mk == 1)
				mk = 0.9999999999999999;
			skip = new BigInteger(formatter.format(Math.floor(Math.log(Math.random()) / Math.log(mk)) + 1));
		}
		skip = current.add(skip).subtract(cardinality);
	}

	@Override
	protected double minKey() {
		double key = super.minKey();
		//logger.trace("minKey " + key + " " + Math.pow(key, df));
		key = Math.pow(key, df);
		return key;
	}
	
	public void reset() {
		super.reset();
		skip = null;
		df = 1;
		idf = 1;
		dampingTimes = 0;
	}
	

	
	
}
