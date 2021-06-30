package streamsamp.pattern;

import java.util.AbstractCollection;

public class Transaction {
	
	private Dataset dataset;
	private int tid;
	private int fpof = 0;

	public Transaction(Dataset dataset, int tid) {
		this.dataset = dataset;
		this.tid = tid;
	}
	
	public void computeFPOF() {
		for (int u = 0; u < dataset.getTransactionNumber(); u++)
			fpof += dataset.intersect(tid, u);
	}

	public void approximateFPOF(AbstractCollection<Pattern> sample) {
		fpof = 0;
		for (Pattern pattern : sample)
			if (dataset.isSupported(tid, pattern))
				fpof++;
	}

	public void show() {
		System.out.println(this);
	}

	@Override
	public String toString() {
		return "Transaction [tid=" + tid + ", fpof=" + fpof + "]";
	}

	public int getFPOF() {
		return fpof;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(tid);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transaction) {
			Transaction t = (Transaction) obj;
			return t.tid == tid;
		}
		return false;
	}

	public void decrement() {
		fpof--;
	}
	
	public void support(Itemset itemset) {
		if (dataset.isSupported(tid, itemset)) {
			fpof++;
			itemset.addOutlier(this);
		}
	}
	
	

}
