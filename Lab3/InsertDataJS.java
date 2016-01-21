import java.util.*;
import java.io.*;

public class InsertDataJS {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Scanner reader;
		String inputFileName;
		String outputFileName
		PrintWriter writer;

		try {
			System.out.println("Enter input file name");
			inputFileName = scanner.next();
			System.out.println("Enter output file name");
			outputFileName = scanner.next();
			reader = new Scanner(new File(inputFileName));
			writer = new PrintWriter(new File(outputFileName));

			writer.print("conn = new Mongo();\n");
			writer.print("db = conn.getDB(\"jtang\");\n");
			writer.print("db = ");
			while(scanner.hasNextLine()) {
				writer.print(scanner.nextLine() + "\n");
			}

			writer.print("db.jtang.insert(doc);\n");
		}
		catch(Exception e) {
			System.out.println(e);
		}


	}
}