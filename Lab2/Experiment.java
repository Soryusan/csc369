import java.util.*;
import java.io.*;
import org.json.*;

public class Experiment {
	public static void main(String[] args) {
		KVCollection test = new KVCollection();
		JSONObject testObj;
		BufferedWriter writer;
		try {
			testObj = new JSONObject();
			testObj.put("derpie", 0);
			test.put(0, testObj);
			testObj = new JSONObject();
			testObj.put("blackie", 1);
			test.put(1, testObj);
			testObj = new JSONObject();
			testObj.put("snowy", 2);
			test.put(2, testObj);

			try {
				writer = new BufferedWriter(new FileWriter(new File("output.txt")));
				for(int i = 0; i < 3; i++) {
					writer.write(test.get(i).toString(3) + "\n");
				}
				writer.close();
				
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
		catch(JSONException e) {
			System.out.println(e);
		}

	}
}