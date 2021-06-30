package streamsamp.sampler;
import java.util.AbstractCollection;
import org.apache.log4j.Logger;

import streamsamp.pattern.Pattern;
import streamsamp.pattern.PatternAccesser;

public abstract class StreamSampler implements Sampler {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(StreamSampler.class);

	protected int k = 10;
	protected AbstractCollection<Pattern> sample;
	private static long totalTime = 0;
	private static long lastTime = 0;
	private double outputSpace = 0;
	
	public void show() {
		for (Pattern pattern : sample)
			System.out.println(pattern);
	}

	public int getK() {
		return k;
	}

	public AbstractCollection<Pattern> getSample() {
		return sample;
	}

	public final void addDataObservation(PatternAccesser accesser) {
		long currentTime = System.nanoTime();
		processDataObservation(accesser);
		lastTime = System.nanoTime() - currentTime;
		totalTime += lastTime;
		outputSpace += accesser.getCardinality().doubleValue();
	}

	public static long getTotalTime() {
		return totalTime;
	}

	public static long getLastTime() {
		return lastTime;
	}
	
	

	public double getOutputSpace() {
		return outputSpace;
	}
	
	public void reset() {
		sample.clear();
		totalTime = 0;
		lastTime = 0;
		outputSpace = 0;
	}

	protected abstract void processDataObservation(PatternAccesser accesser);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [k=" + k + "]";
	}
	
}
