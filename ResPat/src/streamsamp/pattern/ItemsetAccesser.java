package streamsamp.pattern;

import java.math.BigInteger;

public class ItemsetAccesser implements PatternAccesser {
	
	private Dataset dataset;
	private int currentTransaction;

	public ItemsetAccesser(Dataset dataset, int t) {
		this.dataset = dataset;
		this.currentTransaction = t;
	}

	@Override
	public BigInteger getCardinality() {
		int length = dataset.getTransactionLength(currentTransaction);
		BigInteger cardinality = (new BigInteger("2")).pow(length);
		return cardinality;
	}

	@Override
	public Pattern pick(BigInteger index) {
		//return new Itemset();
		boolean[][] matrix = dataset.getMatrix();
		Itemset itemset = new Itemset();
		int k = 0;
		while (index.compareTo(BigInteger.ZERO) > 0) {
			while (!matrix[currentTransaction][k])
				k++;
			if (index.and(BigInteger.ONE).compareTo(BigInteger.ONE) == 0) {
				itemset.addItem(k);
			}
			index = index.shiftRight(1);
			k++;
		}
		return itemset;
	}

	@Override
	public String toString() {
		return "ItemsetAccesser [currentTransaction=" + currentTransaction + ", cardinality=" + getCardinality() + "]";
	}
	
	@Override
	public int getTransactionIdentifier() {
		return currentTransaction;
	}	
	


}
