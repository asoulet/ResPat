package streamsamp.pattern;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public abstract class Pattern implements Comparable<Object> {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Pattern.class);
	private double key = Math.random();
	private double time;

	@Override
	public int compareTo(Object o) {
		if (o instanceof Pattern) {
			Pattern pattern = (Pattern) o;
			if (this.key > pattern.key)
				return 1;
			else
				if (this.key < pattern.key)
					return -1;
				else 
					return 0;
		}
		return 0;
	}

	public double getKey() {
		return key;
	}

	public void setKey(double key) {
		this.key = key;
	}

	
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public abstract int getCount();
	
	public abstract ArrayList<Integer> getTransactions();
	
	public abstract void remove();

}
