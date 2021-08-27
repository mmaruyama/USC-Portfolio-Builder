import java.io.IOException;

public class Stock extends Security {
	private double beta;
	private double PE;
	private Position position;
	
	//know all information
	Stock(String ticker, double price, double beta, double PE, Position position) {
		super(ticker, price);
		// TODO Auto-generated constructor stub
		this.beta = beta;
		this.PE = PE;
		this.position = position;
	}
	
	//know just ticker and position
	Stock(String ticker, Position position){
		//grabs information from the web
		super(ticker);
		this.position = position;
		try {
			WebReader getter = new WebReader(ticker);
			super.updateData();
			this.beta = getter.getBeta();
			this.PE = getter.getPE();
		}
		catch (IOException e) {
			System.out.print(e);
		}
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return the beta
	 */
	public double getBeta() {
		return beta;
	}

	/**
	 * @return the pE
	 */
	public double getPE() {
		return PE;
	}
	
	public String toString() {
		String betaString = "";
		String PEString = "";
		if(Math.abs(beta- -9999.0) <0.01) {
			betaString = "NA";
		}
		else betaString = String.valueOf(beta);
		if(Math.abs(PE- -9999.0)<0.01) {
			PEString = "NA";
		}
		else PEString = String.valueOf(PE);
		return "Stock " + super.getTicker() + "[price=" + super.getPrice() + ", beta=" + betaString + ", PE=" + PEString + ", position=" + position +  ", dayBought=" + super.getDayBought() + "]"; 
	}
	
	@Override
	public void updateData() {
		try {
			WebReader getter = new WebReader(super.getTicker());
			super.updateData();
			this.beta = getter.getBeta();
			this.PE = getter.getPE();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}
