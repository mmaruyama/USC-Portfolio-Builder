import java.io.IOException;

public class Bond extends Security {
	private double NAV;
	private double yield;
	
	Bond(String ticker, double price, double NAV, double yield) {
		super(ticker, price);
		// TODO Auto-generated constructor stub
		this.NAV = NAV;
		this.yield = yield;
	}
	
	//know just ticker, numBought, and position
	Bond(String ticker){
		super(ticker);
		//I need to throw a try and catch loop because the webReader is trying to webscrape information
		//it fills in information from the web
		//Note sometime this information will throw random errors because of the information on the web is being serviced or constanlty changing 
		//Also will fail if the information isn't presented normally
		try {
			WebReader getter = new WebReader(ticker);
			super.updateData();
			this.NAV = getter.getNAV();
			this.yield = getter.getYield();
		}
		catch (IOException e) {
			System.out.print(e);
		}
	}

	/**
	 * @return the nAV
	 */
	public double getNAV() {
		return NAV;
	}

	/**
	 * @return the yield
	 */
	public double getYield() {
		return yield;
	}
	
	//This tests if fields return -9999.0 and will input NA because the information doesn't exist
	public String toString() {
		String NAVString = "";
		String yieldString = "";
		if(Math.abs(NAV- -9999.0)<0.01) {
			NAVString = "NA";
		}
		else NAVString = String.valueOf(NAV);
		if(Math.abs(yield- -9999.0)<0.01) {
			yieldString = "NA";
		}
		else yieldString = String.valueOf(yield);
		return "Bond " + super.getTicker() + "[price=" + super.getPrice() + ", NAV=" + NAVString + ", yield=" + yieldString + ", dayBought=" + super.getDayBought() + "]"; 
	}
	
	//Basically just calls the most information through the webscraper again and updates the data
	@Override
	//parameter: none
	//output: none
	//purpose: calls the web reader to get the most up to date information of the bond
	public void updateData() {
		try {
			WebReader getter = new WebReader(super.getTicker());
			super.updateData();
			this.NAV = getter.getNAV();
			this.yield = getter.getYield();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}
