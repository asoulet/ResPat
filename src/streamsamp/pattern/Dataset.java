package streamsamp.pattern;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigInteger;

/**
 * Data structure for representing a transactional dataset.
 * 
 * @author 
 * 
 */
public class Dataset {
	
	/**
	 * The maximal number of classes in a dataset.
	 */
	private static final int MAXIMAL_CLASS_NUMBER = 10;
	/**
	 * The filename
	 */
	private final String filename;
	/**
	 * The binary matrix encoding the dataset
	 */
	private boolean[][] matrix;
	/**
	 * The number of transactions
	 */
	private int transactionNumber = 0;
	/**
	 * The number of items
	 */
	private int itemNumber = 0;
	
	private int [] transactionLengths;

	/**
	 * Constructs a dataset corresponding to the given bin file.
	 * 
	 * @param filename
	 * @param firstItem 
	 */
	public Dataset(String filename) {
		this.filename = filename;
		prepare();
		load();
	}

	/**
	 * Loads the bin file into the binary matrix.
	 */
	private void load() {
		try {
			Reader in = new FileReader(filename);
			BufferedReader buffer = new BufferedReader(in);
			String line = null;
			int transaction = 0;
			while ((line = buffer.readLine()) != null) {
				if (!line.substring(0, 1).equals("#")) {
					String[] items = line.split(" ");
					int length = 0;
					for (String item : items) {
						length++;
						int value = Integer.parseInt(item);
							matrix[transaction][value] = true;
					}
					transactionLengths[transaction] = length; 
					transaction++;
				}
			}
			buffer.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prepares the binary matrix by reading the bin file for counting the
	 * number of transactions and the number of items.
	 */
	private void prepare() {
		try {
			Reader in = new FileReader(filename);
			BufferedReader buffer = new BufferedReader(in);
			String line = null;
			while ((line = buffer.readLine()) != null) {
				if (!line.substring(0, 1).equals("#")) {
					transactionNumber++;
					String[] items = line.split(" ");
					for (String item : items) {
						int value = Integer.parseInt(item);
						if (value > itemNumber)
							itemNumber = value;
					}
				}
			}
			buffer.close();
			in.close();
			matrix = new boolean[transactionNumber][itemNumber + 1];
			transactionLengths = new int [transactionNumber];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the number of transactions.
	 * 
	 * @return the number of transactions
	 */
	public int getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * Returns the number of items.
	 * 
	 * @return the number of items
	 */
	public int getItemNumber() {
		return itemNumber;
	}

	/**
	 * Checks if the transaction t has the item i.
	 * 
	 * @param t
	 * @param i
	 * @return true if the item is supported, false otherwise
	 */
	public boolean isSupported(int t, int i) {
		return matrix[t][i];
	}

	/**
	 * Gets the length of the transaction t.
	 * 
	 * @param t
	 * @return transaction length
	 */
	public int getTransactionLength(int t) {
		/*int l = 0;
		for (int i = 0; i <= itemNumber; i++)
			if (isSupported(t, i))
				l++;
		return l;*/
		return transactionLengths[t];
	}

	/**
	 * Computes the support of the itemset within the dataset.
	 * 
	 * @param itemset
	 */
	public void support(Itemset itemset) {
		for (int t = 0; t < transactionNumber; t++) {
			int k = 0;
			while (k < itemset.size() && isSupported(t, itemset.getItem(k)))
				k++;
			if (k == itemset.size())
				itemset.addTransaction(t);
		}
	}

	public boolean isSupported(int t, Pattern pattern) {
		if (pattern instanceof Itemset) {
			Itemset itemset = (Itemset)pattern;
			int k = 0;
			while (k < itemset.size() && isSupported(t, itemset.getItem(k)))
				k++;
			if (k == itemset.size())
				return true;
		}
		return false;
	}

	/**
	 * Computes the minority class (item class has to be at the beginning of the
	 * transaction and return its value.
	 * 
	 * @return the minority class
	 */
	public int getMinorityClass() {
		int[] classNumbers = new int[MAXIMAL_CLASS_NUMBER];
		for (int t = 0; t < transactionNumber; t++) {
			int i = 0;
			while (!matrix[t][i] && i < classNumbers.length)
				i++;
			if (i < classNumbers.length)
				classNumbers[i]++;
		}
		int min = 1;
		for (int c = 2; c < classNumbers.length; c++)
			if (classNumbers[c] < classNumbers[min] && classNumbers[c] > 0)
				min = c;
		return min;
	}

	public void show() {
		for (int t = 0; t < transactionNumber; t++) {
			for (int i = 1; i <= itemNumber; i++)
				if (matrix[t][i])
					System.out.print(i + " ");
			System.out.println();
		}
	}
	
	public Itemset pick(int t, BigInteger id) {
		Itemset itemset = new Itemset();
		int k = 0;
		while (id.compareTo(BigInteger.ZERO) > 0) {
			while (!matrix[t][k])
				k++;
			if (id.and(BigInteger.ONE).compareTo(BigInteger.ONE) == 0) {
				itemset.addItem(k);
			}
			id = id.shiftRight(1);
			k++;
		}
		return itemset;
	}

	public boolean[][] getMatrix() {
		return matrix;
	}

	public void support(Pattern pattern) {
		if (pattern instanceof Itemset)
			support((Itemset) pattern);
	}

	public int intersect(int t, int u) {
		int common = 1;
		for (int i = 1; i <= itemNumber; i++)
			if (matrix[t][i] && matrix[u][i])
				common *= 2;
		return common;
	}

	@Override
	public String toString() {
		return filename;
	}
	
	
	
}
