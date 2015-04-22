import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class is thread safe.
 */
public class Parser {

	/**
	 * added volatile to be sure all threads see same value.
	 * I do not do this member final because not know
	 * how you want to use this class further. But this could
	 * be good solution also.
	 */
	private volatile File file;

	/**
	 * 	This could be called when f save or read content,
	 * 	so read and save methods should be synchronized.
	 * 	May be creation of some object for locking purpose
	 * 	could be good solution in case of more sophisticated logic.
	 * 
	 */

	public synchronized void setFile(File f) {
		//Pass null as f value have no sense
		if (f!=null){
			file = f;
		}else{
			throw new NullPointerException("Attempt to pass reference to Null in setFile method");
		}
	}

	/**
	 * Not necessary to be synchronized, because file volatile
	 * this could be bit faster than synchronized
	 */
	public File getFile() {
		return file;
	}

	/**
	 * This should be synchronized to be thread safe
	 */
	public synchronized String getContent() throws IOException {
		String output = "";
		int data;
		//Try with resource for stream close		
		try (FileInputStream i = new FileInputStream(file);) {
			while ((data = i.read()) > 0) {
				output += (char) data;
			}
		}
		return output;
	}

	/**
	 * This should be synchronized to be thread safe
	 */
	public synchronized String getContentWithoutUnicode() throws IOException {
		String output = "";
		int data;
		//Try with resource for stream close
		try (FileInputStream i = new FileInputStream(file);) {
			while ((data = i.read()) > 0) {
				if (data < 0x80) {
					output += (char) data;
				}
			}
		}
		return output;
	}

	/**
	 * This should be synchronized to be thread safe
	 */
	public synchronized void saveContent(String content) throws IOException {
		//Try with resource for stream close
		try (FileOutputStream o = new FileOutputStream(file);){
			for (int i = 0; i < content.length(); i += 1) {
				o.write(content.charAt(i));
			}
		}
	}
}