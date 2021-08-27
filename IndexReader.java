import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class IndexReader {
	//uses Java.net package.
	//Each time I call a method I need to establish a new URLConnection or else the data returned will be the same for each method
	//Establishing this connection can take a while which is why it takes 2 minutes to create and initialize data
	private URL url;
	private URLConnection urlConn;
	private InputStreamReader inStream;
	private BufferedReader buff;
	private String txt;
	
	IndexReader(String txt) throws IOException{
		this.txt = txt;
//		url = new URL("https://finance.yahoo.com/quote/" + SYM);
//		urlConn = url.openConnection();
//		inStream = new InputStreamReader(urlConn.getInputStream());
//		buff = new BufferedReader(inStream);
	}
		
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for market price and return that value as a double
	public double getPrice() throws IOException{
		String price = null;
		url = new URL(txt);
		//creates a new URL
		urlConn = url.openConnection();
		//Opens a new connection online
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		//reads in a json/html type meta data information 
		String line = buff.readLine();
		while(line != null) {
//			System.out.println(line);
			//This is our way of fact checking to see if the stock exits. If the price is -9999 then the ticker is invalid 
			//There is no way to throw an error for this because it will still spit out a number even if the ticker is invalid
			//also for whavtever reason "asdf" as a ticker throws an error different from the other types
			if(line.contains("currentPageName\":\"lookup\"")) {
				throw new InvalidSecurityException("Invalid ticker");
			}
			//finds the first instance of this (if the lookup is there that means the ticker doesn't exist
			if(line.contains("regularMarketPrice\":{")) {
				int target = line.indexOf("regularMarketPrice\":{");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				price = line.substring(start + 2, deci + 3);
				if(price.contains(",")) {//sometimes the input contains extra info which we want to get rid of depnding on how long the double data is
					price = price.substring(0, price.length()-1);
				}
				double roundOff = Double.parseDouble(price);
				return roundOff;
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	//This is essentially the same for all web scraping info, but we are just looking for diferent key valeus
	//for stock
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for closing price and return that value as a double
	public double getClose() throws IOException{
		String prevClose = null;
		url = new URL(txt);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		String line = buff.readLine();
		while(line != null) {
			if(line.contains("regularMarketPreviousClose\":{\"raw\":")) {
				int target = line.indexOf("regularMarketPreviousClose\":{\"raw\":");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				prevClose = line.substring(start + 2, deci + 3);	
				if(prevClose.contains(",")) {
					prevClose = prevClose.substring(0, prevClose.length()-2);
				}
				double roundOff = Double.parseDouble(prevClose);
				return roundOff;
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for open price and return that value as a double
	public double getOpen() throws IOException{
		String open = null;
		url = new URL(txt);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		String line = buff.readLine();
		while(line != null) {
			if(line.contains("regularMarketOpen\":{\"raw\":")) {
				int target = line.indexOf("regularMarketOpen\":{\"raw\":");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				open = line.substring(start + 2, deci + 3);	
				if(open.contains(",")) {
					open = open.substring(0, open.length()-2);
				}
				double roundOff = Double.parseDouble(open);
				return roundOff;
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	
//	public static void main(String[] args){
//		try {
//			
//			IndexReader test = new IndexReader("https://finance.yahoo.com/quote/%5EIXIC?p=^IXIC&.tsrc=fin-srch");
//			double price = test.getPrice();
//			System.out.println(price);
//			double close = test.getClose();
//			System.out.println(close);
//		}
//		catch (IOException e) {
//			System.out.println(e);
//		}
//	}
	
}
