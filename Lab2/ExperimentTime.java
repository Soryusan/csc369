import java.util.*;
import java.io.*;
import org.json.*;

public class ExperimentTime {
	public static void main(String[] args) {
		KeyValueStore store = new KeyValueStore();
		Scanner scanner = new Scanner(System.in);
		String key;
		String filename;
		int numCollections;
		int size = 0;
		long startTime;
		long estTime;
		System.out.println("Number of collections to generate: ");
		numCollections = scanner.nextInt();
		startTime = System.nanoTime();
		for(int i = 0; i < numCollections; i++) {
			store.addCollection("" + i);
		}
		estTime = System.nanoTime() - startTime;
		System.out.println("Time to create " + numCollections + " collections: " + estTime);

		System.out.println("Enter string for collection: ");
		key = scanner.next();
		System.out.println("Enter filename: ");
		filename = scanner.next();

		try {
			JSONTokener tok;
			KVCollection curCollection;
			JSONObject curObj;
			int j = 0;
			store.addCollection(key);
			curCollection = store.getCollection(key);
			try {
				tok = new JSONTokener(new FileReader(new File(filename)));
				while(tok.skipTo('{') != 0) {
					curObj = new JSONObject(tok);
					curCollection.put(j++, curObj);
				}
				size = curCollection.size();
			}
			catch(FileNotFoundException e) {

			}
		}
		catch(JSONException e) {
			System.out.println(e);
		}
		startTime = System.nanoTime();
		store.getCollection(key).get(size - 1);
		estTime = System.nanoTime() - startTime;
		System.out.println("Time to get object in collection of " + numCollections + " collections: " + estTime);

		System.out.println("Name of field to get: ");
		String field;
		int type;
		field = scanner.next();
		JSONArray objArray;
		KVCollection curCollection = store.getCollection(key);
		System.out.println("Enter 0 for BeFuddled data, 1 for ThghtShre data: ");
		type = scanner.nextInt();
		startTime = System.nanoTime();
		if(type == 0)
			curCollection.find(field, new Integer(10));
		else
			curCollection.find(field, "public");
		estTime = System.nanoTime() - startTime;

		System.out.println("Time to get objects with field " + field + ": " + estTime);
	}
}