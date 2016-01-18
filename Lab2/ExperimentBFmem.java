import java.util.*;
import java.io.*;

public class ExperimentBFmem {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int numFiles;
		ArrayList<String> filenames = new ArrayList<String>();
		System.out.println("Enter number of files to scan: ");
		numfiles = scanner.nextInt();
		System.out.println("Enter file names to scan");
		for(int i = 0; i < numFiles; i++) {
			filenames.add(Scanner.next());
		}
	}
}