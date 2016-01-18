import java.util.*;
import org.json.*;

public class KVCollection {
	HashMap<Integer, JSONObject> collection;

	public KVCollection() {
		collection = new HashMap<Integer, JSONObject>();
	}

	public boolean clear() {
		collection.clear();
		return true;
	}

	public boolean containsKey(int key) {
		return collection.containsKey(key);
	}

	public JSONObject get(int key) {
		return collection.get(key);
	}

	public int put(int key, JSONObject value) {
		if(collection.containsKey(key)) {
			return -1;
		}
		else {
			collection.put(key, value);
			return 1;
		}
	}

	public int remove(int key) {
		if(containsKey(key)) {
			collection.remove(key);
			return 1;
		}
		else {
			return -1;
		}
	}

	public int replace(int key, JSONObject value) {
		if(containsKey(key)) {
			collection.put(key, value);
			return 1;
		}
		else {
			return -1;
		}
	}

	public int size() {
		return collection.size();
	}

	public int upsert(int key, JSONObject value) {
		if(containsKey(key)) {
			replace(key, value);
			return 2;
		}
		else {
			put(key, value);
			return 1;
		}
	}

	public JSONArray find(String jsonFieldName, Object jsonFieldValue) {
		ArrayList<JSONObject> objects = new ArrayList<JSONObject>(collection.values());
		ArrayList<Integer> keys = new ArrayList<Integer>(collection.keySet());
		JSONArray foundObjs = new JSONArray();
		JSONObject curObj;
		JSONObject found;
		try {
			for(int i = 0; i < objects.size(); i++) {
				curObj = objects.get(i);
				if(curObj.has(jsonFieldName)) {
					if(jsonFieldValue.equals(curObj.get(jsonFieldName))) {
						found = new JSONObject();
						found.put("key", keys.get(i));
						found.put("value", curObj);
						foundObjs.put(curObj);
					}
				}
			}
		}
		catch(JSONException e) {
			System.out.println(e);
		}
		return foundObjs;
	}
}