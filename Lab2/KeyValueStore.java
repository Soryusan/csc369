import java.util.*;

public class KeyValueStore {
	HashMap<String, KVCollection> store;
	boolean checkLimit;
	boolean premade;
	int limit;

	public KeyValueStore() {
		store = new HashMap<String, KVCollection>();
		checkLimit = false;
		premade = false;
		limit = -1;
	}

	public KeyValueStore(int checkLimit) {
		this.checkLimit = true;
		premade = false;
		limit = checkLimit;
	}

	public KeyValueStore(Set<String> collection) {
		store = new HashMap<String, KVCollection>();
		premade = true;
		checkLimit = false;
		limit = -1;
		//add all of the strings into the hashmap with empty collections
		ArrayList<String> values = new ArrayList<String>(collection);
		for(int i = 0; i < value.size(); i++) {
			store.put(value.get(i), new KVCollection());
		}
	}

	public void clear() {
		store.clear();
	}

	public int addCollection(String name) {
		if(!premade) {
			if(checkLimit) {
				if(store.size() < limit) {
					if(store.containsKey(name)) {
						store.put(name, new KVCollection());
						return 1;
					}
					else {
						return -1;
					}
				}
				else {
					return -2;
				}
			}
			else {
				if(store.containsKey(name)) {
					store.put(name, new KVCollection());
					return 1;
				}
				else {
					return -1;
				}
			}
		}
		else {
			return 0;
		}
	}

	public KVCollection getCollection(String name) {
		if(store.containsKey(name)) {
			return store.get(name);
		}
		else {
			return null;
		}
	}

	public Set<String> list() {
		return store.keySet();
	}

	public boolean isEmpty() {
		return store.isEmpty;
	}

	public int size() {
		return store.size();
	}

	public int getNumObjects() {
		ArrayList<KVCollection> collections = new ArrayList<KVCollection>(store.values);
		int numObjs = 0;
		for(int i = 0; i < collections.size(); i++) {
			numObjs += collections.get(i).size();
		}
		return numObjs;
	}
	public int getLimit() {
		return limit;
	}
}