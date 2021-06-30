package streamsamp.pattern;

import java.math.BigInteger;

public class ItemsetFileAccesser implements PatternAccesser {
	
	public int [] transaction = null;
	private int transactionIdentifier;
	
	public ItemsetFileAccesser(String line, long transactionIdentifier) {
		String[] items = line.split(" ");
		transaction = new int [items.length];
		for (int i = 0; i < items.length; i++)
			transaction[i] = Integer.parseInt(items[i]);
		this.transactionIdentifier = (int) transactionIdentifier;
	}

	@Override
	public BigInteger getCardinality() {
		return (new BigInteger("2")).pow(transaction.length);
	}

	@Override
	public Pattern pick(BigInteger index) {
		Itemset itemset = new Itemset();
		int k = 0;
		while (index.compareTo(BigInteger.ZERO) > 0) {
			if (index.and(BigInteger.ONE).compareTo(BigInteger.ONE) == 0) {
				itemset.addItem(transaction[k]);
			}
			index = index.shiftRight(1);
			k++;
		}
		return itemset;
	}

	@Override
	public int getTransactionIdentifier() {
		return transactionIdentifier;
	}

	@Override
	public String toString() {
		String t = transactionIdentifier + " : ";
		for (int i = 0; i < transaction.length; i++)
			t += transaction[i] + " ";
		return t;
	}
	
	

}
