import java.io.*;
import java.net.URL;
import java.net.URLConnection;

//most commments exact same to the indexReader so read through that
public class WebReader {
	private String SYM;
	private URL url;
	private URLConnection urlConn;
	private InputStreamReader inStream;
	private BufferedReader buff;
	
	WebReader(String symbol) throws IOException{
		this.SYM = symbol;
//		url = new URL("https://finance.yahoo.com/quote/" + SYM);
//		urlConn = url.openConnection();
//		inStream = new InputStreamReader(urlConn.getInputStream());
//		buff = new BufferedReader(inStream);
	}
		
	//for all
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for market price and return that value as a double
	public double getPrice() throws IOException{
		String price = null;
		url = new URL("https://finance.yahoo.com/quote/" + SYM);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		String line = buff.readLine();
		while(line != null) {
			//This is our way of fact checking to see if the stock exits. If the price is -9999 then the ticker is invalid 
			//There is no way to throw an error for this because it will still spit out a number even if the ticker is invalid
			//also for whavtever reason "asdf" as a ticker throws an error different from the other types
			if(line.contains("currentPageName\":\"lookup\"")||SYM.equalsIgnoreCase("asdf")) {
				throw new InvalidSecurityException("Invalid ticker");
			}
			if(line.contains("regularMarketPrice\":{")) {
				int target = line.indexOf("regularMarketPrice\":{");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				price = line.substring(start + 2, deci + 3);
				if(price.contains(",")) {
					price = price.substring(0, price.length()-1);
				}
				double roundOff = Double.parseDouble(price);
				return roundOff;
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for market beta and return that value as a double
	public double getBeta() throws IOException{
		String beta = null;
		url = new URL("https://finance.yahoo.com/quote/" + SYM);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		String line = buff.readLine();
		while(line != null) {
			if(line.contains("beta\":{\"raw\":")) {
				int target = line.indexOf("beta\":{\"raw\":");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				beta = line.substring(start + 2, deci + 5);	
				if(beta.contains(",")) {
					beta = beta.substring(0, beta.length()-2);
				}
				double dnum = Double.parseDouble(beta);
				double roundOff = Math.round(dnum* 100.0) / 100.0;
				return roundOff;	
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for market PE and return that value as a double
	public double getPE() throws IOException{
		String PE = null;
		
		url = new URL("https://finance.yahoo.com/quote/" + SYM);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		String line = buff.readLine();
		while(line != null) {
			if(line.contains("trailingPE\":{\"raw\":")) {
				int target = line.indexOf("trailingPE\":{\"raw\":");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				PE = line.substring(start + 2, deci + 5);	
				if(PE.contains(",")) {
					PE = PE.substring(0, PE.length()-1);
				}
				double dnum = Double.parseDouble(PE);
				double roundOff = Math.round(dnum* 100.0) / 100.0;
				return roundOff;	
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for market NAV and return that value as a double
	public double getNAV() throws IOException{
		String NAV = null;
		url = new URL("https://finance.yahoo.com/quote/" + SYM);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);

		String line = buff.readLine();
		while(line != null) {
			if(line.contains("navPrice\":{\"raw\":")) {
				int target = line.indexOf("navPrice\":{\"raw\":");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				NAV = line.substring(start + 2, deci + 3);
				if(NAV.contains(",")) {
					NAV = NAV.substring(0, NAV.length()-2);
				}
				double roundOff = Double.parseDouble(NAV);
				return roundOff;
			}
			line = buff.readLine();
		}
		return -9999;
	}
	
	
	//parameter: none
	//output: double
	//purpose: creates a web reader URL connection to search for market yield and return that value as a double
	public double getYield() throws IOException{
		String yield = null;
		url = new URL("https://finance.yahoo.com/quote/" + SYM);
		urlConn = url.openConnection();
		inStream = new InputStreamReader(urlConn.getInputStream());
		buff = new BufferedReader(inStream);
		
		String line = buff.readLine();
		while(line != null) {
			if(line.contains("yield\":{\"raw\":")) {
				int target = line.indexOf("yield\":{\"raw\":");
				int deci = line.indexOf(".", target);
				int start = deci;
				while(line.charAt(start) != '\"') {
					start--;
				}
				yield = line.substring(start + 2, deci + 7);	
				if(yield.contains(",")) {
					yield = yield.substring(0, yield.length()-2);
				}
				double dnum = Double.parseDouble(yield);
				double roundOff = Math.round(dnum* 10000.0) / 10000.0;
				return roundOff;
			}
			line = buff.readLine();
		}
		return -9999;	
	}
	
	
//	public static void main(String[] args){
//		try {
//			
//			WebReader test = new WebReader("TSLA");
//			double price = test.getPrice();
//			double beta = test.getBeta();
//			double PE = test.getPE();
//			System.out.println(beta);
//			System.out.println(price);
//			System.out.println(PE);
//		}
//		catch (IOException e) {
//			System.out.println(e);
//		}
//	}
	
}
