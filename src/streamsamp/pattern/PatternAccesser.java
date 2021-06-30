package streamsamp.pattern;

import java.math.BigInteger;

public interface PatternAccesser {
	public BigInteger getCardinality();
	public Pattern pick(BigInteger index);
	public int getTransactionIdentifier();
}
