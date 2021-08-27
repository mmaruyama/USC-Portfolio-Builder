import java.util.HashMap;

public class _401k extends Portfolio {
	private String company;
	
	
	//Creating 3 different types of constructors based on the information available, in case people don't know all the information yet
	_401k(String name, HashMap<String, Integer> securities, double price, double cash, String company){
		super(name, securities, price, cash);
		this.company = company;
	}
	
	_401k(String name, HashMap<String, Integer> securities, double cash, String company){
		super(name, securities, cash);
		this.company = company;
	}
	
	_401k(String name, double cash, String company){
		super(name, cash);
		this.company = company;
	}
	
	
	//These are the simple imported getters and setters
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	
	//Takes the super comparer and just adds the company comparer
	public int compareTo(_401k other) {
		int num = super.compareTo(other);
		if(num==0) {
			num = this.company.compareTo(other.getCompany());
		}
		return num;
	}
	
	@Override 
	public boolean equals(Object obj) {
		boolean temp = super.equals(obj);
		if(temp==false) {
			return temp;
		}
		return this.company.equalsIgnoreCase(((_401k) obj).getCompany());
	}
	
	@Override
	//I don't really care about printing any additional information, but just wanted to add the fact that it is in fact a 401k 
	public String toString() {
//		String securityNames = "";
//		HashMap<Security, Integer> temp = super.getSecurities();
//		for(Security i:temp.keySet()) {
//			securityNames += " " + i.getTicker();
//		}
		return "401k " + super.toString();
	}

	
	
	
}
