package streamsamp.sampler;

import java.math.BigInteger;
import java.util.PriorityQueue;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public abstract class ReservoirSampler extends StreamSampler {
	
	private long insertionNumber = 0;
	
	public enum Algorithms {
		AResDampingNo,
		AResDampingExp,
		AResDampingWin,
		AResExpJDampingNo,
		AResExpJDampingExp,
		AResExpJDampingWin,
		AResExpJExpDampingExp,
		AResExpJWinDampingWin
	};
	
	protected BigInteger fill(PatternAccesser accesser, BigInteger cardinality) {
		BigInteger current = BigInteger.ZERO;
		if (sample.size() < k) {
			current = new BigInteger(Integer.toString(k - sample.size()));
			current = current.min(cardinality);
			BigInteger i = BigInteger.ZERO;
			while (i.compareTo(current) < 0) {
				Pattern pattern = accesser.pick(i);
				addPattern(pattern);
				i = i.add(BigInteger.ONE);
			}
		}
		return current;
	}

	protected void addPattern(Pattern pattern) {
		insertionNumber++;
		sample.add(pattern);
	}
		
	protected double minKey() {
		PriorityQueue<Pattern> queue = (PriorityQueue<Pattern>) sample;
		if (sample.size() < k)
			return 0;
		else
			return queue.peek().getKey();				
	}

	protected Pattern peekPattern() {
		PriorityQueue<Pattern> queue = (PriorityQueue<Pattern>) sample;
		return queue.peek();
	}

	protected void pollPattern() {
		PriorityQueue<Pattern> queue = (PriorityQueue<Pattern>) sample;
		Pattern pattern = queue.poll();
		pattern.remove();
	}

	public long getInsertionNumber() {
		return insertionNumber;
	}
	
	public void reset() {
		super.reset();
		insertionNumber = 0;
	}

}
