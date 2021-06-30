package streamsamp.pattern;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class ItemStream implements Iterator<PatternAccesser> {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ItemStream.class);
	
	private Dataset dataset;
	private int currentTransaction = 0;

	public ItemStream(Dataset dataset) {
		this.dataset = dataset; 
	}
	
	@Override
	public boolean hasNext() {
		return (currentTransaction < dataset.getTransactionNumber());
	}

	@Override
	public PatternAccesser next() {
		return new ItemAccesser(dataset, currentTransaction++);
	}

}
