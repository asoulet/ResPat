package streamsamp.pattern;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class ItemsetFileStream implements Iterator<PatternAccesser> {

	private static Logger logger = Logger.getLogger(ItemsetFileStream.class);
	private String filename;
	private long transactionNumber = 0;
	private FileReader in = null;
	private BufferedReader buffer = null;
	private String line = null;

	public ItemsetFileStream(String filename) {
		this.filename = filename;
		try {
			in = new FileReader(filename);
			buffer = new BufferedReader(in);
		} catch (FileNotFoundException e) {
			logger.error(e, e);
		}
	}
	
	public void close() {
		try {
			buffer.close();
			in.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
		buffer = null;
		in = null;
	}


	@Override
	public boolean hasNext() {
		if (buffer == null)
			return false;
		try {
			while ((line = buffer.readLine()) != null && line.substring(0, 1).equals("#")) ;
		} catch (IOException e) {
			logger.error(e, e);
		}
		if (line == null) {
			close();
			return false;
		}
		transactionNumber++;
		return true;
	}

	@Override
	public PatternAccesser next() {
		return new ItemsetFileAccesser(line, transactionNumber);
	}

	public String getFilename() {
		return filename;
	}
	
	

}
