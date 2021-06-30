package streamsamp.sampler;

import java.util.AbstractCollection;

import streamsamp.pattern.Pattern;

public interface Sampler {

	public int getK();

	public AbstractCollection<Pattern> getSample();
	
	

}
