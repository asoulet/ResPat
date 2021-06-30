package streamsamp.pattern;

import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

public class ItemsetStream implements Iterator<PatternAccesser> {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ItemsetStream.class);
	
	private Dataset dataset;
	private int currentTransaction = 0;
	private int [] permutation;
	private Random random;

	public ItemsetStream(Dataset dataset, int seed) {
		this.dataset = dataset;
		random = new Random(seed);
		permutation = new int [dataset.getTransactionNumber()];
		for (int i = 0; i < permutation.length; i++)
			permutation[i] = i;
		if (seed != 0)
			for (int i = 0; i < permutation.length; i++)
				permutation[i] = permutation[random.nextInt(permutation.length)];
	}
	
	public ItemsetStream(Dataset dataset) {
		this(dataset,0);
	}

	
	@Override
	public boolean hasNext() {
		return (currentTransaction < dataset.getTransactionNumber());
	}

	@Override
	public PatternAccesser next() {
		return new ItemsetAccesser(dataset, permutation[currentTransaction++]);
	}

}
