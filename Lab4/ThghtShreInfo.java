import java.util.Hashtable;
import java.util.ArrayList;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.*;

public class ThghtShreInfo {
	private Hashtable<String, Integer> recipients;
	private Hashtable<String, Boolean> users;
	private ArrayList<String> recIDs;

	private int newMessages;
	private long totalMessages;
	private int startNdx;

	public ThghtShreInfo() {
		users = new Hashtable<String, Boolean>();
		recipients = new Hashtable<String, Integer>();
		recIDs = new ArrayList<String>();
		newMessages = 0;
		totalMessages = 0;
		startNdx = 0;
	}

	public void set() {
		startNdx = 0;
	}

	//Incremental methods
	public void addTotalMessages() {
		totalMessages++;
	}


	public void addUser(String user) {
		if(!users.containsKey(user)) {
			users.put(user, true);
		}
	}

	public void increStart() {
		startNdx++;
	}

	public void increRecipient(String rec) {

		
		if(recipients.contains(rec)) {
			recipients.put(rec, recipients.get(rec)+1);
		}
		else {
			recipients.put(rec, 1);
			recIDs.add(rec);
		}
	}

	//Get methods
	public long getTotalMessages() {
		return totalMessages;
	}

	public int getUsers() {
		return users.size();
	}

	public int getStart() {
		return startNdx;
	}

	public ArrayList<String> recList() {
		return recIDs;
	}

	public int getRecMsgCount(String key) {
		return recipients.get(key);
	}

}