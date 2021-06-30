package streamsamp.sampler;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public class AResExpJWinDampingSampler extends ReservoirDampingSampler {
	

	private static Logger logger = Logger.getLogger(AResExpJWinDampingSampler.class);
	
	private static DecimalFormat formatter = new DecimalFormat("###");
	private BigInteger skip = null;

	private final double winSize;
	private int zeroNumber = 0;
	private ArrayList<Integer> insertions = new ArrayList<Integer>();

	
	public AResExpJWinDampingSampler(int k, Damping damping) {
		super(k, damping);
		if (damping instanceof SlidingWindow)
			this.winSize = ((SlidingWindow)damping).getSize();
		else {
			logger.warn("incorrect damping!");
			this.winSize = 0;
		}
	}

	public AResExpJWinDampingSampler(int k) {
		this(k,  null);
	}
	
	protected void damp(boolean active) {
		/*System.out.print(">> ");
		for (Integer insertions : insertions)
			System.out.print(insertions + " ");
		System.out.println();*/
		if (insertions.size() > winSize) {	
			zeroNumber += insertions.get(0); 
			insertions.remove(0);
			//logger.trace("z " + zeroNumber + " " + sample.size());
		}
		insertions.add(0);
		if (zeroNumber > 100000) { // bounded memory
			PriorityQueue<Pattern> ns = new PriorityQueue<Pattern>();
			for (Pattern pattern : sample) {
				if (getCurrentTime() - pattern.getTime() <= winSize) {
					ns.add(pattern);
				}
			}
			sample = ns;
			zeroNumber = 0;
		}
	}

	protected BigInteger fill(PatternAccesser accesser, BigInteger cardinality) {
		BigInteger current = BigInteger.ZERO;
		if (sample.size() < k) {
			current = new BigInteger(Integer.toString(k - sample.size()));
			current = current.min(cardinality);
			BigInteger i = BigInteger.ZERO;
			while (i.compareTo(current) < 0) {
				Pattern pattern = accesser.pick(i);
				addWinPattern(pattern);
				i = i.add(BigInteger.ONE);
			}
		}
		return current;
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
			addWinPattern(pattern);
			//logger.trace("add " + sample.size() + " " + zeroNumber + " -> " + (sample.size() - zeroNumber));
			/*System.out.print(">> ");
			for (Integer insertions : insertions)
				System.out.print(insertions + " ");
			System.out.println();*/
			double mk = minKey();
			if (mk == 1)
				mk = 0.9999999999999999;
			skip = new BigInteger(formatter.format(Math.floor(Math.log(Math.random()) / Math.log(mk)) + 1));
			//logger.trace("jump " + skip);
		}
		skip = current.add(skip).subtract(cardinality);
	}

	private void addWinPattern(Pattern pattern) {
		super.addPattern(pattern);
		int index = insertions.size() - 1;
		insertions.set(index, insertions.get(index) + 1);
	}

	protected double minKey() {
		PriorityQueue<Pattern> queue = (PriorityQueue<Pattern>) sample;
		if (sample.size() < k + zeroNumber)
			return 0;
		else
			return queue.peek().getKey();		
	}

	protected Pattern peekPattern() {
		PriorityQueue<Pattern> queue = (PriorityQueue<Pattern>) sample;
		Pattern pattern = queue.peek();
		//logger.trace("peek " + pattern);
		if (getCurrentTime() - pattern.getTime() > winSize) {
			queue.poll();
			zeroNumber--;
			return peekPattern();
		}
		else
			return pattern;
	}

	@Override
	protected void pollPattern() {
		PriorityQueue<Pattern> queue = (PriorityQueue<Pattern>) sample;
		Pattern pattern = queue.poll();
		while (getCurrentTime() - pattern.getTime() > winSize && queue.size() > 0) {
			zeroNumber--;
			pattern = queue.poll();
		}
		/*Pattern pattern = queue.poll();	// OLD
		if (getCurrentTime() - pattern.getTime() > winSize) {
			zeroNumber--;
			if (sample.size() > 0) {
				pollPattern();
			}
		}
		else*/  
			if (sample.size() - zeroNumber >= k - 1) {
			int index = (int) ((insertions.size() - 1) - (getCurrentTime() - pattern.getTime()));
			//logger.trace(insertions.size() + " " + pattern.getTime() + " " + getCurrentTime());
			insertions.set(index, insertions.get(index) - 1);
			/*if (pattern.getTime() == getCurrentTime()) {
				currentInsertions--;
			}
			else {
				int index = (int) (insertions.size() - (getCurrentTime() - pattern.getTime()));
//				logger.trace(insertions.size() + " " + pattern.getTime() + " " + getCurrentTime());
				insertions.set(index, insertions.get(index) - 1);
				if (insertions.get(index) < 0)
					logger.trace("ci " + index + " " +  insertions.get(index));
			}*/
		}
			else
				queue.add(pattern);
	}

	public void clean() {
		PriorityQueue<Pattern> ns = new PriorityQueue<Pattern>();
		for (Pattern s : sample)
			if (getCurrentTime() - s.getTime() <= winSize)
				ns.add(s);
		sample = ns;
		//logger.trace("final size: " + sample.size());
	}
	
	public void reset() {
		super.reset();
		skip = null;
		zeroNumber = 0;
		insertions.clear();
	}
	

}
