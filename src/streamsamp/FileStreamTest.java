package streamsamp;


import java.util.Iterator;

import org.apache.log4j.Logger;

import streamsamp.pattern.ItemsetFileStream;
import streamsamp.pattern.PatternAccesser;
import streamsamp.sampler.AResExpJDampingSampler;
import streamsamp.sampler.AResExpJExpDampingSampler;
import streamsamp.sampler.AResExpJWinDampingSampler;
import streamsamp.sampler.DampedWindow;
import streamsamp.sampler.Damping;
import streamsamp.sampler.NoDamping;
import streamsamp.sampler.ReservoirSampler;
import streamsamp.sampler.SlidingWindow;
import streamsamp.sampler.StreamSampler;

/**
 * A toy example.
 * 
 * @author 
 *
 */
public class FileStreamTest {
	
	private static Logger logger = Logger.getLogger(FileStreamTest.class);

	private static final String FILENAME = "data/abalone.bin";
	private static final int k = 1000;

	public static void main(String[] args) {
		// selection of the damping		
		Damping damping = null;
		//damping = new NoDamping(); // no damping / equivalent to null
		//damping = new SlidingWindow(1000); // sliding window
		damping = new DampedWindow(0.0003); // exponential bias
		
		// selection of the right ResPat variant
		StreamSampler sampler = null;
		if (damping == null || damping instanceof NoDamping)
			sampler = new AResExpJDampingSampler(k, damping);
		if (damping instanceof SlidingWindow)
			sampler = new AResExpJWinDampingSampler(k, damping);
		if (damping instanceof DampedWindow)
			sampler = new AResExpJExpDampingSampler(k, damping);
		
		long n = 0;
		Iterator<PatternAccesser> stream = new ItemsetFileStream(FILENAME);
		while (stream.hasNext()) {
			PatternAccesser accesser = stream.next();
			//logger.trace(accesser + " " + sampler.getLastTime() + " " + sampler.getTotalTime() / (1000. * 1000. * 1000.));
			sampler.addDataObservation(accesser);
			n++;
		}
		
		// statistics
		logger.info("insertion_nb= " + (((ReservoirSampler)sampler).getInsertionNumber() - k));
		double output = ((ReservoirSampler)sampler).getOutputSpace();
		long k = sampler.getK();
		logger.info("output_space= " + output);
		if (damping == null || damping instanceof NoDamping) {
			double complexityNo = sampler.getK() * Math.log(output) / Math.log(sampler.getK());
			logger.info("insertion_estimate_no= " + complexityNo);
		}
		if (damping instanceof SlidingWindow) {
			double complexityWin = sampler.getK() * n / 1000.;
			logger.info("insertion_estimate_win= " + complexityWin);
		}
		if (damping instanceof DampedWindow) {
			double complexityExp = k * n / (1./0.0003);
			logger.info("insertion_estimate_exp= " + complexityExp);
		}
		logger.info("total_time= " + StreamSampler.getTotalTime() / (1000. * 1000. * 1000.));
	}

}
