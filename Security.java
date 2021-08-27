import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Security implements Comparable<Security>, Updateable{
	private String ticker;
	private double price;
	private LocalDate dayBought;
	private WebReader getter;
	
	Security(String ticker, double price){
		this.ticker = ticker;
		this.price = price;
		this.dayBought = LocalDate.now();
	}
	
	Security(String ticker){
		try {
			//reading in price available now (real time)
			getter = new WebReader(ticker);
			double price = getter.getPrice();
			this.ticker = ticker;
			this.price = price;
			this.dayBought = LocalDate.now();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * @return the ticker
	 */
	public String getTicker() {
		return ticker;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the dayBought
	 */
	//Will return the latest time bought
	public LocalDate getDayBought() {
		return dayBought;
	}
	
	//parameter: none
	//output: none
	//purpose: updates price of said security
	@Override
	public void updateData() {
		try {
			this.price = getter.getPrice();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	@Override
	public int compareTo(Security other) {
		if(this.equals(other)) return 0;
		
		//just going to compare ticker (we don't care about the number bought)
		int num = this.ticker.compareTo(other.ticker);
		if (num==0) {
			double diff = this.price - other.getPrice();
			if(diff > 0) {
				return 1;
			}
			else if(diff<0) {
				return -1;
			}
			else {
				num =  this.dayBought.compareTo(other.dayBought);
			}
		}
		return num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Security " + ticker + " [price=" + price + ", dayBought=" + dayBought + "]";
	}

	@Override
	//just comparing if the tickers are the same because the prices could be different on when they were bought
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ((obj instanceof Security)==true) {
			Security other = (Security) obj;
			if(other.getTicker().length() > 1) {
				if(this.ticker.equals(other.getTicker())) return true;
				else return false;
			}
		}
		return false;
	}
	
//	public static void main(String[] args){
		
//		ArrayList<String> test = new ArrayList<>();
//		ArrayList<Security> test2 = new ArrayList<>();
//		test.add("TSLA");
//		test.add("GME");
//		for(String i:test) {
//			Security ge = new Security(i);
//			test2.add(ge);
//			System.out.println(ge);
//		}
//		System.out.println(test2);
//			Security test = new Security("TSLA");
//			System.out.println(test);
//			Security test1 = new Security("GME");
//			System.out.println(test1);
		
		
//	}
//	
}
