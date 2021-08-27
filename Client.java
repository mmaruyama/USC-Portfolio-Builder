import java.util.*;

public class Client {
	private String name;
	private boolean employed;
	private int age;
	private ArrayList<Portfolio> holdings;
	
	public Client(String name, boolean employed, int age, ArrayList<Portfolio> holdings) {
		this.name = name;
		this.employed = employed;
		this.age = age;
		this.holdings = holdings;
	}

	public Client(String name, boolean employed, int age) {
		ArrayList<Portfolio> holdings = new ArrayList<>();
		this.name = name;
		this.employed = employed;
		this.age = age;
		this.holdings = holdings;
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
	 * @return the employed
	 */
	public boolean isEmployed() {
		return employed;
	}

	/**
	 * @param employed the employed to set
	 */
	public void setEmployed(boolean employed) {
		this.employed = employed;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the holdings
	 */
	public ArrayList<Portfolio> getHoldings() {
		return holdings;
	}
	
	//parameter: none
	//output: none
	//purpose: prints out the portfolios it has
	public void seePortfolio() {
		System.out.println("Here are your portfolios:");
		int count = 1;
		for(Portfolio i:holdings) {
			System.out.println(count + ". " + i.getName() + " " + i.getClass());
			i.seeSecurities();
			count++;
		}
	}
	
	//parameter: portfolio
	//output: none
	//purpose: adds portfolio to client
	public void addPortfolio(Portfolio p) {
		holdings.add(p);
	}
	
	//parameter: portfolio
	//output: none
	//purpose: deletes a portfolio from a client
	public void deletePortfolio(Portfolio p) {
		holdings.remove(p);
	}
	
	//chooses the portfolio and will add number of securities desires
	//parameter: string, int
	//output: none
	//purpose: checks to see if the the portfolio can add a security based on the number they want to buy
	public void addSecurity(String s, int numAdd) {
		System.out.println("What portfolio would you like to add the security too:");
		ITPHelper help = new ITPHelper();
		for(int i=0;i<holdings.size();i++) {
			System.out.println("\t" + (i+1) + "." + holdings.get(i).getName());
		}
		int index = help.inputInt("Please select the number of the portfolio", 1, holdings.size());
		holdings.get(index-1).addSecurity(s, numAdd);
	}
	
	//parameter: string ,int
	//output: none
	//purpose: checks to see if portfolio has ticker and sells the inputted number
	public void sellSecurity(String s, int numAdd) {
		System.out.println("What portfolio would you like to sell the security:");
		ITPHelper help = new ITPHelper();
		for(int i=0;i<holdings.size();i++) {
			System.out.println("\t" + (i+1) + "." + holdings.get(i).getName());
		}
		int index = help.inputInt("Please select the number of the portfolio", 1, holdings.size());
		holdings.get(index-1).sellSecurity(s, numAdd);
	}
	
	@Override
	public String toString() {
		String working = "";
		if(this.employed == true) {
			working = " is employed ";
		}
		else {
			working = " isn't working ";
		}
		String holds = "";
		for(Portfolio i:holdings) {
			holds = holds + " " + i.toString() + "\n";
		}
		return "Client " + this.name + " age " + this.age +  working + "and has:" + holds;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + (employed ? 1231 : 1237);
		result = prime * result + ((holdings == null) ? 0 : holdings.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (this.name.equalsIgnoreCase(other.name) == false) {
			return false;
		}
		if (employed != other.employed)
			return false;
		if (age != other.age)
			return false;
		//ignoring holdings (because they are subject to change too frequently
		return true;
	}
	
//	public static void main(String[] args) {
//		Stock tsla = new Stock("TSLA", Position.LONG);
//		HashMap<String,Integer> list = new HashMap<>();
//		list.put("tsla",2);
//		_401k p = new _401k("test", list, 10000, "company");
//		ArrayList<Portfolio> ex = new ArrayList<>();
//		ex.add(p);
//		Client program = new Client("test", true, 21, ex);
//		program.sellSecurity("tsla",1);
//		program.seePortfolio();
//
//	}
	
}
