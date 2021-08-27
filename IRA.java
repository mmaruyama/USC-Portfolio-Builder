import java.util.HashMap;

public class IRA extends Portfolio {
	
	private int yearsToRetirement;

	public IRA(String name, HashMap<String, Integer> securities, double price, double cash, int yearsToRetirement) {
		super(name, securities, price, cash);
		this.yearsToRetirement = yearsToRetirement;
	}

	public IRA(String name, HashMap<String, Integer> securities, double cash,int yearsToRetirement) {
		super(name, securities, cash);
		this.yearsToRetirement = yearsToRetirement;
	}
//adding info on yearsToRetire
	
	public IRA(String name, double cash,int yearsToRetirement) {
		super(name, cash);
		this.yearsToRetirement = yearsToRetirement;
	}

	/**
	 * @return the yearsToRetirement
	 */
	public int getYearsToRetirement() {
		return yearsToRetirement;
	}

	/**
	 * @param yearsToRetirement the yearsToRetirement to set
	 */
	public void setYearsToRetirement(int yearsToRetirement) {
		this.yearsToRetirement = yearsToRetirement;
	}
	
	@Override 
	public boolean equals(Object obj) {
		boolean temp = super.equals(obj);
		if(temp==false) {
			return temp;
		}
		return this.yearsToRetirement==(((IRA) obj).getYearsToRetirement());
	}
	
	public int compareTo(IRA other) {
		int num = super.compareTo(other);
		if(num==0) {
			num = this.yearsToRetirement - other.getYearsToRetirement();
			if(num>0) {
				return 1;
			}
			else if (num<0) {
				return -1;
			}
			else {
				return 0;
			}
		}
		return num;
	}
	
	@Override
	public String toString() {
		return "IRA " + super.toString();
	}
	
}
