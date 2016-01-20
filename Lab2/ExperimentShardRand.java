import java.util.*;
import org.json.*;
import java.io.*;

public class ExperimentShardRand {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int collectionSize;
		KeyValueStore store = new KeyValueStore();
		KVCollection curCollection;
		int storeSize = 0;
		String filename;
		System.out.println("Enter filename: ");
		filename = scanner.next();
		System.out.println("Enter number of collections to generate: ");
		storeSize = scanner.nextInt();
		for(int i = 0; i < storeSize; i++) {
			store.addCollection("" + i);
		}

		try {
			JSONTokener tok;
			JSONObject curObj;
			Random random = new Random();
			int j = 0;
			int collectionKey;
			try {
				tok = new JSONTokener(new FileReader(new File(filename)));
				while(tok.skipTo('{') != 0) {
					collectionKey = random.nextInt(storeSize);
					curCollection = store.getCollection("" + collectionKey);
					curObj = new JSONObject(tok);
					curCollection.put(j++, curObj);
				}
			}
			catch(FileNotFoundException e) {
				System.out.println(e);
			}
		}
		catch(JSONException e) {
			System.out.println(e);
		}
		int key;
		long startTime;
		long estTime;
		System.out.println("Enter key to find: ");
		key = scanner.nextInt();
		startTime = System.nanoTime();
		for(int i = 0; i < storeSize; i++) {
			curCollection = store.getCollection("" + i);
			if(curCollection.get(key) != null) {
				break;
			}
		}
		estTime = System.nanoTime() - startTime;
		System.out.println("Time to find object in " + storeSize + " collections: " + estTime);
	}
}