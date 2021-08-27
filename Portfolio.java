import java.io.IOException;
import java.util.*;

public abstract class Portfolio implements Comparable<Portfolio>, Updateable{
	private String name;
	private double price;
	//why do i need "Integer" wrapper
	private HashMap<String, Integer> securities;
	private double cash;
	
	Portfolio(String name, HashMap<String, Integer> securities, double price, double cash){
		this.name = name;
		this.securities = securities;
		this.price = price;
		this.cash = cash;
	}
	
	Portfolio(String name, HashMap<String, Integer> securities, double cash){
		this.name = name;
		this.securities = securities;
		this.cash = cash;
		this.price = updatePrice();
	}
	
	//this is for starting a brand new portfolio
	Portfolio(String name, double cash){
		this.name = name;
		this.cash = cash;
		this.securities = new HashMap<>();
		this.price = 0.0;
	}

	/**
	 * @return the cash
	 */
	public double getCash() {
		return cash;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the securities
	 */
	public HashMap<String, Integer> getSecurities() {
		return securities;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		//makes sure i get the most up to date price
		updatePrice();
		return price;
	}

	/**
	 * @param cash the cash to set
	 */
	public void setCash(double cash) {
		this.cash = cash;
	}
	
	//parameter: string
	//output: boolean
	//purpose: checks if a ticker is in the portfolio
	public boolean inPortfolio(String o) {
		return securities.containsKey(o);
	}
	
	//parameter: none
	//output: double
	//purpose: goes through each stock in portfolio and updates the price
	public double updatePrice() {
		double total =cash;
		for(String i:securities.keySet()) {
			Security ex = new Security(i);
			total += ex.getPrice()*securities.get(i);
		}
		return Math.round(total*100.0)/100.0;
	}
	
	//parameter: string, int
	//output: none
	//purpose: gets the price of stock from string and calculates total cost of numbers wanted and checks to see if there is enough cash
	public void addSecurity(String o, int num) {
		Security ex = new Security(o);
		if(cash < ex.getPrice()*num) {
			//tests to see if you have enough money to add to the portfolio
			System.out.println("Sorry! You have insufficient funds to buy the desired number of " + o);
		}
		else {
			cash = cash - num*ex.getPrice();
			setCash(cash);
			if(inPortfolio(o)) {
				int currNumber = securities.get(o);
				currNumber = currNumber + num;
				securities.replace(o, currNumber);
			}
			else {
				securities.put(o, num);
			}
			System.out.println("We have successfully added " + o + " to the portfolio!");
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(cash);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((securities == null) ? 0 : securities.hashCode());
		return result;
	}

	@Override
	//This is enough to test to see if portfolios are equal becuase every single aspect must be the same
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Portfolio other = (Portfolio) obj;
		if (Double.doubleToLongBits(cash) != Double.doubleToLongBits(other.cash))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (securities == null) {
			if (other.securities != null)
				return false;
		} else if (!securities.equals(other.securities))
			return false;
		return true;
	}

	@Override
	public void updateData() {
		this.price = updatePrice();
	}
	
	public int compareTo(Portfolio other) {
		if(this.equals(other)) return 0;
		
		int num = this.name.compareTo(other.name);
		if (num==0) {
			double diff = this.price - other.getPrice();
			if(diff > 0) {
				return 1;
			}
			else if(diff<0) {
				return -1;
			}
			else {
				diff = this.cash - other.getCash();
				if(diff > 0) {
					return 1;
				}
				else if(diff<0) {
					return -1;
				}
				else {
					//don't really care about individual securities but rather just the size of the portfolio
					num = this.securities.size()-other.getSecurities().size();
					if(num>0) {
						return 1;
					}
					else if (num < 0) {
						return -1;
					}
					else {
						return 0;
					}
				}
			}
		}
		return num;
	}
	
	//parameter: none
	//output: none
	//purpose: prints all securities in portolio
	public void seeSecurities() {
		int count = 1;
		for (Map.Entry<String,Integer> pair : securities.entrySet()) {
            System.out.println("\t" + count + ") You have " + pair.getValue() + " of " + pair.getKey());
            count++;
    	}
	}
	
	//parameter: string, int
	//output: none
	//purpose: calculates price of securities and make sure you have enough of the security in portfolio and deletes them but adds the price to the cash
	public void sellSecurity(String o, int num) {
		Security ex = new Security(o);
		if(inPortfolio(o)) {
			if(num > securities.get(o)) {
				System.out.println("Sorry! Not enough securities to sell " + num);
				//makes sure you have enough securities to sell
			}
			else {
				double value = num*ex.getPrice();
				cash = cash + value;
				setCash(cash);
				int currNumber = securities.get(o);
				currNumber = currNumber - num;
				if(currNumber <= 0) {
					securities.remove(o);
				}
				else {
					securities.replace(o,  currNumber);
				}
				System.out.println("We have successfully sold " + o + "!");
			}
		}	
	}

	@Override
	public String toString() {
		String securityNames = "";
		for(HashMap.Entry<String, Integer> pair: securities.entrySet()) {
			securityNames += " " + pair.getKey() + "(" + pair.getValue() + ")";
		}
		return "Portfolio [name=" + name + ", price=" + price + ", securities=" + securityNames + ", cash=" + cash + "]";
	}
	
	
}
