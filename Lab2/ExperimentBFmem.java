import java.util.*;
import java.io.*;
import org.json.*;

public class ExperimentBFmem {
	public static void main(String[] args) {
		KeyValueStore store = new KeyValueStore();
		Scanner scanner = new Scanner(System.in);
		int numFiles;
		int key = 0;
		int memCount = 0;
		ArrayList<String> filenames = new ArrayList<String>();
		System.out.println("Enter number of files to scan: ");
		numFiles = scanner.nextInt();
		System.out.println("Enter file names to scan");
		for(int i = 0; i < numFiles; i++) {
			filenames.add(scanner.next());
		}
		try {
			JSONTokener tok;
			JSONObject obj;
			KVCollection curCollection;
			int j = 0;
			for(int i = 0; i < filenames.size(); i++) {
				try {
					try {
						tok = new JSONTokener(new FileReader(new File(filenames.get(i))));
						store.addCollection("" + i);
						curCollection = store.getCollection("" + i);
						while(tok.skipTo('{') != 0) {
							obj = new JSONObject(tok);
							curCollection.put(key++, obj);
							memCount++;
						}
					}
					catch(OutOfMemoryError e) {
						System.out.println("Objects made: " + memCount);
					}
				}
				catch(FileNotFoundException e) {
					System.out.println(e);
				}
			}
			System.out.println("Collection size: " + store.getCollection("0").size());
		}
		catch(JSONException e) {
			System.out.println(e);
		}


	}
}