import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


//parameter: string
//output: ArrayList<String>
//purpose: takes in a file and takes each line as a string in an arrayList
public class FileReader {
	
	//same as before with simple capabiliteiss
	public static ArrayList<String> readFile(String fileName)  {

		ArrayList<String> fileLines = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(fileName);
				Scanner scan = new Scanner(fis)){
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				fileLines.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //auto-close
		return fileLines;
	}
}
