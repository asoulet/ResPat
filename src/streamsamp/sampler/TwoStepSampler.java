package streamsamp.sampler;

import java.util.AbstractCollection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import streamsamp.pattern.Dataset;
import streamsamp.pattern.Itemset;
import streamsamp.pattern.Pattern;

public class TwoStepSampler implements Sampler {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TwoStepSampler.class);

	protected int k = 10;
	protected AbstractCollection<Pattern> sample = new ArrayList<Pattern>();

	private Dataset dataset;
	private long totalTime = 0;
	private double weights [] = null;
	private double total = 0;
	
	public TwoStepSampler(Dataset dataset, int k) {
		this.dataset = dataset;
		this.k = k;
		totalTime = System.nanoTime();
		prepare();
		compute();
		totalTime = System.nanoTime() - totalTime;
	}

	public long getTotalTime() {
		return totalTime;
	}

	private void prepare() {
		weights = new double [dataset.getTransactionNumber()];
		for (int t = 0; t < dataset.getTransactionNumber(); t++) {
			total += Math.pow(2, dataset.getTransactionLength(t));
			weights[t] = total;
		}
	}

	private void compute() {
		for (int i = 0; i < k; i++)
			drawTransaction();
	}

	private void drawTransaction() {
		double value = Math.random() * total;
		int left = 0;
		int right = weights.length;
		int middle = (left + right) / 2;
		while ((middle > 0 && weights[middle - 1] >= value) || weights[middle] < value) {
			if (weights[middle] < value)
				left = middle + 1;
			else
				right = middle - 1;
			middle = (left + right) / 2;
		}
		drawPattern(middle);
	}

	private void drawPattern(int t) {
		boolean[][] matrix = dataset.getMatrix();
		Itemset itemset = new Itemset();
		for (int i = 1; i <= dataset.getItemNumber(); i++)
			if (matrix[t][i] && Math.random() < 0.5)
				itemset.addItem(i);
		sample.add(itemset);
		//logger.debug(t + " " + itemset);
	}

	@Override
	public int getK() {
		return k;
	}

	@Override
	public AbstractCollection<Pattern> getSample() {
		return sample;
	}

}
