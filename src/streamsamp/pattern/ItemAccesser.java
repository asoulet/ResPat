package streamsamp.pattern;

import java.math.BigInteger;

public class ItemAccesser implements PatternAccesser {
	
	private Dataset dataset;
	private int currentTransaction;

	public ItemAccesser(Dataset dataset, int t) {
		this.dataset = dataset;
		this.currentTransaction = t;
	}

	@Override
	public BigInteger getCardinality() {
		int length = dataset.getTransactionLength(currentTransaction);
		BigInteger cardinality = new BigInteger(Integer.toString(length));
		return cardinality;
	}

	@Override
	public Pattern pick(BigInteger index) {
		boolean[][] matrix = dataset.getMatrix();
		Itemset itemset = new Itemset();
		int k = 0;
		do {
			k++;
			while (!matrix[currentTransaction][k])
				k++;
			index = index.subtract(BigInteger.ONE);
		} while (index.compareTo(BigInteger.ZERO) >= 0);
		itemset.addItem(k);
		return itemset;
	}

	@Override
	public String toString() {
		return "ItemAccesser [currentTransaction=" + currentTransaction + ", cardinality=" + getCardinality() + "]";
	}

	@Override
	public int getTransactionIdentifier() {
		return currentTransaction;
	}	
	

}
