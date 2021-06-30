package streamsamp.pattern;


import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Data structure for representing an itemset (with occurrences).
 * 
 * @author 
 * 
 */
public class Itemset extends Pattern {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Itemset.class);
	
	private boolean isAdded = true;
	private ArrayList<Transaction> outliers = new ArrayList<Transaction>();

	/**
	 * The set of items.
	 */
	private ArrayList<Integer> items = new ArrayList<>();
	/**
	 * The transactions where this itemset appears (cover).
	 */
	private ArrayList<Integer> transactions = new ArrayList<>();
	
	/**
	 * Adds an item in the set of items.
	 * 
	 * @param i
	 */
	public void addItem(int i) {
		items.add(i);
	}

	/**
	 * Adds a transaction in the set of transactions (cover).
	 * 
	 * @param t
	 */
	public void addTransaction(int t) {
		transactions.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		for (Integer item : items)
			result += item.toString() + " ";
		/*result += ": ";
		for (Integer transaction : transactions)
			result += transaction.toString() + " ";*/
		result += ": " + this.getKey();
		//result += ": " + transactions.size();
		return result;
	}

	/**
	 * Returns the number of items.
	 * 
	 * @return item number
	 */
	public int size() {
		return items.size();
	}

	/**
	 * Returns the ith item.
	 * 
	 * @param i
	 * @return ith item
	 */
	public int getItem(int i) {
		return items.get(i);
	}

	/**
	 * Returns the set of items.
	 * 
	 * @return the set of items
	 */
	public ArrayList<Integer> getItems() {
		return items;
	}

	/**
	 * Returns the set of transactions where this itemset occurs.
	 * 
	 * @return the set of transaction.
	 */
	public ArrayList<Integer> getTransactions() {
		return transactions;
	}

	/**
	 * Returns its count/frequency i.e., the cardinality of the cover.
	 * 
	 * @return frequency
	 */
	public int getCount() {
		return transactions.size();
	}

	@Override
	public int hashCode() {
		return items.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Itemset)
			return items.equals(((Itemset)obj).items);
		return false;
	}

	@Override
	public void remove() {
		//logger.trace("remove " + outliers.size());
		for (Transaction t : outliers)
			t.decrement();
		outliers.clear();
	}
	
	public void removeOutlier(Transaction t) {
		outliers.remove(t);
	}

	public boolean isAdded() {
		return isAdded;
	}

	public void notAdded() {
		isAdded = false;
	}

	public void addOutlier(Transaction transaction) {
		outliers.add(transaction);
	}
	
	

}
