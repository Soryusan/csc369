import java.util.*;
import org.json.*;
import java.io.*;

public class ExperimentShardBF {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int collectionSize;
		KeyValueStore store = new KeyValueStore();
		KVCollection curCollection;
		int storeSize = 0;
		String filename;
		System.out.println("Enter filename: ");
		filename = scanner.next();
		System.out.println("Enter collection sizes: ");
		collectionSize = scanner.nextInt();

		try {
			JSONTokener tok;
			JSONObject curObj;
			int j = 0;
			int i = 0;
			int shardSize = 
			store.addCollection("" + i);
			curCollection = store.getCollection("" + i++);
			try {
				tok = new JSONTokener(new FileReader(new File(filename)));
				while(tok.skipTo('{') != 0) {
					if(curCollection.size() == collectionSize) {
						store.addCollection("" + i);
						curCollection = store.getCollection("" + i++);
					}
					curObj = new JSONObject(tok);
					curCollection.put(j++, curObj);
				}
				storeSize = i;
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