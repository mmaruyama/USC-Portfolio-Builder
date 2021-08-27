import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class BrokerageFirm {
	
	private ITPHelper help = new ITPHelper();
	private ArrayList<Client> clients;
	//This data HashMap is only used to initially set data
	private HashMap<ArrayList<String>, ArrayList<ArrayList<String>>> data;
	//these will contain stocks and bonds even if they are no longer in any portfolios
	private Set<Stock> stocks;
	private Set<Bond> bonds;
	
	public BrokerageFirm() {
		clients = new ArrayList<>();
		data = new HashMap<>();
		stocks = new HashSet<>();
		bonds = new HashSet<>();
		help.print("Welcome to the our brokerage firm");
		help.print("We are reading in stock and bond information from the internet!");
		help.print("Please be patient with us, as the connection can take up to 2 minutes. We will be with you soon!");
		readInitialDataFromFile();
	}
	
	//parameter: string
	//output: none
	//purpose: takes in a line of information parsed in from a file to add the information on stocks and bonds on clients to our database
	private void parseDataLineToPortfolio(String lineOfFile) throws InvalidSecurityException{
		//Timestamp	Email Address	Your first name	Your last name	Which section are you in?	
		//Your number 1 favorite tv show	Which streaming service is it on?	Your number 2 favorite tv show	Which streaming service is it on?	Your number 3 favorite tv show	Which streaming service is it on?
		Scanner sc = new Scanner(lineOfFile);
		sc.useDelimiter(",");
		//Adding try and catch to first see if we can create a new TVWatcher and if the sc.next() has the necessary information to proceed
		try {
			//isStock/isIRA/portfolioName/portfolioCash/clientName/age/employed/ticker/stockLong/company/yearsToRetirement (formatted like the following)
			String isLong;
			String isIRA;
			String isStock;
			String name;
			String ticker;
			String retireYears;
			String client;
			String company;
			String cash;
			String employed;
			String age;
			while(sc.hasNext()) {
				isStock = sc.next();
				isIRA = sc.next();
				name = sc.next();
				cash = sc.next();
				client = sc.next();
				age = sc.next();
				employed = sc.next();
				ticker = sc.next().toUpperCase();
				isLong = "";
				retireYears= "";
				company = "";
				//the scanner will grab various information based on if the file reader line is stock or bond, IRA, etc
				if(Boolean.parseBoolean(isStock)) {
					isLong = sc.next();
				}
				if(Boolean.parseBoolean(isIRA)) {
					retireYears = sc.next();
				}
				else {
					company = sc.next();
				}
				
				//Sending on relevant inforamtion about client and portfolio as a key for stocks
				ArrayList<String> key = new ArrayList<>();
				key.add(isIRA);
				key.add(name);
				key.add(cash);
				key.add(client);
				key.add(age);
				key.add(employed);
				
				if(Boolean.parseBoolean(isIRA)) {
					key.add(retireYears);
				}
				else {
					key.add(company);
				}
				
				//Creates a value of stock and how many there are to immediately add after
				ArrayList<String> key1 = new ArrayList<>();
				key1.add(isStock);
				key1.add(ticker);
				if(Boolean.parseBoolean(isStock) == true) {
					key1.add(isLong);
				}
				
				if(data.containsKey(key)) {						
					data.get(key).add(key1);
				}
				else {
					ArrayList<ArrayList<String>> temp = new ArrayList<>();
					temp.add(key1);
					data.put(key, temp);
				}

				// if it doesn't have next data isn't added
			}
		}
		//If you cannot, I will throw a new exception that is an IOException
		catch (Exception e) {
			//Choosing not to print stacktrace errors and just want cleaner looking error message
			throw new InvalidSecurityException("Error reading in a line of data: " + lineOfFile + "\n");
		}
	}
	
	
	//parameter: none
	//output: none
	//purpose: takes a file in and goes through line by line using the parseDataLineToPortfolio to add information to database
	private void readInitialDataFromFile() {
		String file = "bin/ClientPortfolioData.csv";
		ArrayList<String> info = FileReader.readFile(file);
		
		//throwing this loop into a try where after we get the line, we parsing it and if
		//the error persists we are acknowledging it's there but moving on
		for(int i = 1; i < info.size(); i++) {
			String line = info.get(i);
			try {
				//parses line from file and adds it to the data hashmap
				parseDataLineToPortfolio(line);
			} 
			catch(InvalidSecurityException e) {
				System.out.println(e);
			}
		}
		//Each parseDataLineToPortfolio adds a portfolio 
		for(HashMap.Entry<ArrayList<String>, ArrayList<ArrayList<String>>> pair: data.entrySet()) {
			//breaks down data information to initalize data into our portfolio before the user can start manipualting it
			HashMap<String,Integer> blank = new HashMap<>();
			for(ArrayList i: pair.getValue()) {
				//adding info to stock and bond lists
				if(Boolean.parseBoolean((String) i.get(0)) == true){
					Position position;
					//determining if the security is stock or bond
					if(Boolean.parseBoolean((String) i.get(2)) == true){
						position = Position.LONG;
					}
					else {
						position = Position.SHORT;
					}
					Stock test = new Stock((String) i.get(1), position);
					stocks.add(test);
				}
				else {
					Bond test = new Bond((String) i.get(1));
					bonds.add(test);
				}
				//adding the stock and bond to each respective set
				
				//adding info to blank portfolio or seeing if there already exists a portfolio
				if(blank.containsKey(i.get(1))) {
					int currNumber = blank.get(i.get(1));
					currNumber = currNumber + 1;
					blank.replace((String) i.get(1), currNumber);
				}
				else {
					blank.put((String) i.get(1), 1);
				}
				//updating info where if the stock exists already, you are simply adding another to the inventory, if not you are creating a new key
				
			}

			Portfolio temp;
			if(Boolean.parseBoolean(pair.getKey().get(0)) == false){
				temp = new _401k(pair.getKey().get(1), blank, Double.parseDouble(pair.getKey().get(2)), pair.getKey().get(6));
			}
			else {
				temp = new IRA(pair.getKey().get(1), blank, Double.parseDouble(pair.getKey().get(2)), Integer.parseInt(pair.getKey().get(6)));
			}
			//similar to how you determine if its a stock or bond, we do the same for types of portfolios and create a temporary one to use and add to clients
			boolean employed = Boolean.parseBoolean(pair.getKey().get(5));
			int age = Integer.parseInt(pair.getKey().get(4));
			Client person = new Client(pair.getKey().get(3), employed, age);
			boolean found = false;
			for(int i=0;i<clients.size();i++) {
				if(clients.get(i).equals(person)) {
					clients.get(i).addPortfolio(temp);
					found = true;
				}
			}
			if(found == false) {
				clients.add(0, person);
				clients.get(0).addPortfolio(temp);
			}
			//adding clients and populating them with portfolios
		}
	}
	
	
	//parameter: none
	//output: none
	//purpose: runs the normal function of our brokerage firm that prints out the menu enum of options you can perform and executes respective methods
	public void run(){

		boolean keepGoing = true;
		while(keepGoing){
			Menu.printMenu();
			int num = help.inputInt(">", 1,  Menu.getNumOptions());
			Menu option = Menu.getOption(num);
			switch(option){
				case MARKET_INFO:
					currentMarketMovement();
					break;
				case SEE_ALL_CLIENTS: 
					seeClients();
					break;
				case  SEE_ALL_STOCKS: 
					seeAllStocks();
					break;
				case SEE_ALL_BONDS: 
					seeAllBonds();
					break;
				case VIEW_ALL_401K: 
					seeAll401k();
					break;
				case VIEW_ALL_IRA: 
					seeAllIRA();
					break;
				case ADD_CLIENT: 
					addClient();
					break;
				case DELETE_CLIENT:
					deleteClient();
					break;
				case ADD_SECURITY: 
					addSecurity();
					break;
				case SELL_SECURITY:
					sellSecurity();
					break;
				case ADD_PORTFOLIO: 
					addPortfolio();
					break;
				case DELETE_PORTFOLIO: 
					deletePortfolio();
					break;
				case CREATE_EXAMPLE_PORTFOLIO: 
					createExamplePortfolio();
					break;	
				case GET_NET_WORTH: 
					getNetWorth();
					break;
				case RETIREMENT_PLAN: 
					retirementPlan();
					break;
				case EXPORT_TAX_FORM: 
					exportTaxForm();
					break;
				case QUIT:
					keepGoing = false; 
					writeOutData();
					break;
				}
			//Simple menu program utilized in previous hws
		}
		help.print("Goodbye! Thanks for coming!");
	}
	
	
	//parameter: none
	//output: none
	//purpose: prints out all clients currently with information
	public void seeClients() {
		System.out.println("Here are all our current clients:");
		int counter = 1;
		for(Client i:clients) {
			System.out.println(counter + ") " + i.getName() + " has " + i.getHoldings().size() + " portfolio(s):");
			int counter2 = 1;
			for(Portfolio j:i.getHoldings()) {
				System.out.println("\t" + counter2 + ". " + j.getName());
				counter2++;
			}
			counter++;
		}
		//Just cycling through clients and printing out portfolio information
	}
	
	
	//parameter: none
	//output: none
	//purpose: going through stocks and bonds sets to print out all information
	public void seeAllStocks() {
		help.print("Here are all our stocks (past and present) in people's portfolios");
		int counter = 1;
		for(Stock i:stocks) {
			System.out.println(counter + ") " + i);
			counter++;
		}
	}
	
	//parameter: none
	//output: none
	//purpose: going through stocks and bonds sets to print out all information
	public void seeAllBonds() {
		help.print("Here are all our current bonds (past and present) in people's portfolios");
		int counter = 1;
		for(Bond i:bonds) {
			System.out.println(counter + ") " + i);
			counter++;
		}
	}
	
	
	//parameter: none
	//output: none
	//purpose: going through all the clients grabbing all respective portfolios and printing its contents
	public void seeAll401k() {
		help.print("Here are all our current 401k accounts");
		int counter = 1;
		_401k template = new _401k("", 0, "");
		for(Client i:clients) {
			for(Portfolio j:i.getHoldings()) {
				if(j.getClass() == template.getClass()) {
					System.out.println(counter + ") " + j);
					counter++;
				}
			}
		}
	}
	
	//parameter: none
	//output: none
	//purpose:prints out all portfolios that are IRAs
	public void seeAllIRA() {
		help.print("Here are all our current IRA accounts");
		int counter = 1;
		IRA template = new IRA("", 0, 0);
		for(Client i:clients) {
			for(Portfolio j:i.getHoldings()) {
				if(j.getClass() == template.getClass()) {
					System.out.println(counter + ") " + j);
					counter++;
				}
			}
		}
	}
	
	//parameter: none
	//output: none
	//purpose: ask the user for various information and add that information to make a client
	public void addClient() {
		System.out.println("You are going to be adding a client with no portfolios or holdings currently! But that's ok because you can add some later!");
		String name = help.inputLine("What is the client's name?");
		boolean working = help.inputYesNoAsBoolean("Is " + name + " currently employed?"); 
		//minimum age to own account is 18 and want realistic max ages
		int age = help.inputInt("What is " + name + "'s age?", 18, 130);
		Client temp = new Client(name, working, age);
		clients.add(temp);
		System.out.println("We successfully added " + name);
	}
	
	//parameter: none
	//output: none
	//purpose: deletes a client
	public void deleteClient() {
		help.print("Pick what client do you want to delete\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		clients.remove(index-1);
		help.print("\nWe successfully deleted the client");
	}
	
	//parameter: none
	//output: none
	//purpose: asks for a client and adds a security if they can to a given portfolio of that client
	public void addSecurity() {
		//Note I cannot add error checking for if you add a stock ticker but classify it as a bond (because it will just give the bond attributes NA values
		//So in that case it fails to capture all user errors
		help.print("Please pick which client you want to continue transacting for:\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		int counter = 1;
		help.print("\nNow pick the portfolio you want to add the stock to");
		if(clients.get(index-1).getHoldings().size()!=0) {
			for(Portfolio i: clients.get(index-1).getHoldings()) {
				System.out.println(counter + ". " + i);
				counter++;
			}
			int pIndex = help.inputInt("\nWhat is the portfolio number?", 1, clients.get(index-1).getHoldings().size());
			System.out.println("Thank you!");
			
			boolean choice = help.inputYesNoAsBoolean("Do you want to see a list of possible securities (y/n)?");
			if(choice == true) {
				seeAllStocks();
				help.print("\n");
				seeAllBonds();
			}
			//essentially have to navigate through this binary tree to distill the kind of security your adding
			boolean stock = help.inputYesNoAsBoolean("\nDo you want to add a stock? (yes for stock, no for bond)");
			String ticker = help.inputWord("\nPlease enter the ticker of the security you want to buy\nNote it doesn't have to be a security we listed").toUpperCase();
			Security item;
			if(stock==true) {
				boolean extra = help.inputYesNoAsBoolean("\nDo you want to go long? (yes for long, no for short)");
				if(extra==true) {
					//Note if the ticker is invalid, then it will throw two InvalidSecurityException Errors (1 for stock ticker and 1 for stock price)
					item = new Stock(ticker, Position.LONG);
					stocks.add((Stock) item);
				}
				else {
					item = new Stock(ticker, Position.SHORT);
					stocks.add((Stock) item);
				}
			}
			else {
				item = new Bond(ticker);
				bonds.add((Bond) item);
			}
			if (item.getPrice()!=0) {
				int num = help.inputInt("How many " + item.getTicker() + "'s would you like to buy?");
				clients.get(index-1).getHoldings().get(pIndex-1).addSecurity(ticker, num);
			}
		}
		else
			System.out.println("Sorry cannot add any securities to this client because this client has no portfolios!");
	}
	
	//parameter: none
	//output: none
	//purpose: sell a security if possible of a client
	public void sellSecurity() {
		help.print("Please pick which client you want to continue transacting for:\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		int counter = 1;
		help.print("\nNow pick the portfolio you want to sell the stock in");
		if(clients.get(index-1).getHoldings().size()!=0) {
			for(Portfolio i: clients.get(index-1).getHoldings()) {
				System.out.println(counter + ". " + i);
				counter++;
			}
			int pIndex = help.inputInt("\nWhat is the portfolio number?", 1, clients.get(index-1).getHoldings().size());
			System.out.println("\nThank you!");
			System.out.println("\nHere is a list of the portfolio's current holdings:");
			for(Entry<String, Integer> k:clients.get(index-1).getHoldings().get(pIndex-1).getSecurities().entrySet()) {
				System.out.println(k.getKey() + " : " + k.getValue());
			}
			//again go within a binary tree to determine what stock to get
			String ticker = help.inputWord("Which of these securities would you like to sell (enter the ticker)?").toUpperCase();
			if(clients.get(index-1).getHoldings().get(pIndex-1).getSecurities().containsKey(ticker)) {
				int num = help.inputInt("How many " + ticker + "'s would you like to sell?");
				clients.get(index-1).getHoldings().get(pIndex-1).sellSecurity(ticker, num);
			}
			else {
				System.out.println("Sorry! You entered a security not in this portfolio!");
			}
		}
		else
			System.out.println("Sorry cannot sell the security becasue the client doesn't have a portfolio!");
	}

	//parameter: none
	//output: none
	//purpose: adds an empty portfolio to a client
	public void addPortfolio() {
		help.print("Pick what client you want to add the portfolio to");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		String name = help.inputLine("What is the name of the portfolio?");
		double cash = help.inputDouble("How much cash are you putting into the portfolio?",0,99999999);
		boolean ira = help.inputYesNoAsBoolean("Do you want to open an IRA account? (yes for IRA, no for 401k)");
		Portfolio temp;
		//If you want to add stocks you have to go to add security tab (can't do it here)
		if(ira==true) {
			int years = help.inputInt("Approximately how many years until retirement",0,100);
			temp = new IRA(name, cash, years);
		}
		else {
			String company = help.inputLine("What is the company you work for?");
			temp = new _401k(name, cash, company);
		}
		clients.get(index-1).addPortfolio(temp);
		help.print("We successfully added the portfolio to " + clients.get(index-1).getName());
	}
	
	//parameter: none
	//output: none
	//purpose: deletes a portfolio from a client
	public void deletePortfolio() {
		help.print("Pick what client you want to delete the portfolio from\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		int counter = 1;
		for(Portfolio i:clients.get(index-1).getHoldings()) {
			System.out.println(counter + ") " + i);
			counter++;
		}
		int pIndex = help.inputInt("Please pick the portfolio index you want to delete", 1, clients.get(index-1).getHoldings().size());
		clients.get(index-1).getHoldings().remove(pIndex-1);
		help.print("\nWe successfully deleted the portfolio from " + clients.get(index-1).getName());
	}
	
	//parameter: none
	//output: none
	//purpose: creates a portfolio and can add securities. Then has the option of adding that porfolio to a client as a real portfolio
	public void createExamplePortfolio(){
		Portfolio temp;
		boolean ira = help.inputYesNoAsBoolean("Do you want to work with an IRA account? (yes for IRA, no for 401k)");
		double cash = help.inputDouble("How much cash do you want to play with?",0,9999999);
		//at this point, we can set the other parameters later if the user wants to keep the portfolio, otherwise we will put in placeholders now 
		//just to calculate price and play with stocks
		if(ira==true) {
			temp = new IRA("", cash, 0);
		}
		else {
			temp = new _401k("", cash, "");
		}
		boolean done = false;
		while(done == false) {
			//Can add as many securities as you want until you run out of cash
			System.out.println("Current price: " + temp.updatePrice());
			System.out.println("Current cash: " + temp.getCash());
			System.out.println("Current securities: ");
			for(Entry<String, Integer> pair:temp.getSecurities().entrySet()) {
				System.out.println("\t" + pair.getKey() + " : " + pair.getValue());
			}
			
			boolean addsell = help.inputYesNoAsBoolean("Would you like to add or sell securities? (yes for add, no for sell)");
			if(addsell == true) {
				boolean display = help.inputYesNoAsBoolean("Do you want to see some securities for inpsiration?");
				if(display == true) {
					seeAllStocks();
					help.print("\n");
					seeAllBonds();
				}
				boolean stock = help.inputYesNoAsBoolean("\nDo you want to add a stock? (yes for stock, no for bond)");
				String ticker = help.inputWord("\nPlease enter the ticker of the security you want to buy\nNote it doesn't have to be a security we listed").toUpperCase();
				Security item;
				if(stock==true) {
					//same process as add stock method, but not the same becasuse you aren't picking specific client to add the stock too
					boolean extra = help.inputYesNoAsBoolean("\nDo you want to go long? (yes for long, no for short)");
					if(extra==true) {
						//Note if the ticker is invalid, then it will throw two InvalidSecurityException Errors (1 for stock ticker and 1 for stock price)
						item = new Stock(ticker, Position.LONG);
						stocks.add((Stock) item);
					}
					else {
						item = new Stock(ticker, Position.SHORT);
						stocks.add((Stock) item);
					}
				}
				else {
					item = new Bond(ticker);
					bonds.add((Bond) item);
				}
				if (item.getPrice()!=0) {
					int num = help.inputInt("How many " + item.getTicker() + "'s would you like to buy?");
					temp.addSecurity(ticker, num);
				}
			}
			else {
				for(Entry<String, Integer> k:temp.getSecurities().entrySet()) {
					System.out.println(k.getKey() + " : " + k.getValue());
				}
				//in a lot of ways, this is a mini version of the entire program becuase you are creating a fake model
				String ticker = help.inputWord("Which of these securities would you like to sell (enter the ticker)?").toUpperCase();
				if(temp.getSecurities().containsKey(ticker)) {
					int num = help.inputInt("How many " + ticker + "'s would you like to sell?");
					temp.sellSecurity(ticker, num);
				}
				else {
					System.out.println("Sorry! You entered a security not in this portfolio!");
				}
			}
			done = help.inputYesNoAsBoolean("Are you done?");
		}
		boolean keep = help.inputYesNoAsBoolean("Do you want to keep this portfolio and add to a client?");
		if(keep == true) {
			String name = help.inputLine("What do you want to name this portfolio?");
			if(ira==true) {
				int years = help.inputInt("How many years until retirement?",0,100);
				((IRA) temp).setYearsToRetirement(years);
			}
			else {
				String comp = help.inputLine("What is the company name?");
				((_401k) temp).setCompany(comp);
			}
			temp.setName(name);
			help.print("Pick what client you want to add the portfolio to");
			seeClients();
			int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
			clients.get(index-1).addPortfolio(temp);
			help.print("We added the portfolio!");
		}
		//if the user likes the portfolio, they can add it to their clients
		help.print("Thank you!");
	}
	
	//parameter: none
	//output: none
	//purpose: pick a client and get the price of stocks and add to current cash to get net worth of all portfolios
	public void getNetWorth() {
		help.print("Please pick which client you want to get the net worth for:\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		double value = 0;
		for(Portfolio i:clients.get(index-1).getHoldings()) {
			i.updateData();
			value = value + i.getPrice();
		}
		System.out.println(clients.get(index-1).getName() + " has a net worth of $" + value);
	}
	
	//I am basically overloading this getNetWorth (but changed the method name) so it can take an int parameter specifically so I can use this function to call the clients
	//given that I already know which client I want to pick (used in retirementPlan	
	public double getNetWorthForRP(int picker) {
		double value = 0;
		for(Portfolio i:clients.get(picker-1).getHoldings()) {
			i.updateData();
			value = value + i.getPrice();
		}
		return value;
	}
	
	
	//parameter: none
	//output: none
	//purpose: asks for a target net worth and then tells how many years given various points it would take to reach that net worth
	public void retirementPlan() {
		//Basically will tell you how many years it will take to achieve your desired retirement net worth and approx how old you will be
		help.print("Please pick which client you want to make the retirement plan for:\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		double currentValue = getNetWorthForRP(index);
		double futureValue = help.inputDouble("What is your target net worth you want in order to retire");
		if(currentValue >= futureValue) {
			System.out.println("You already have enough assests to retire! Enjoy retirement!");
		}
		else {
			System.out.println("Your current net worth is: " + currentValue);
			System.out.println("Your desired future net worth is: " + futureValue + "\n");
			ArrayList<Double> returns = new ArrayList<>();
			returns.add(.01);
			returns.add(.05);
			returns.add(.10);
			returns.add(.15);
			//Based off simple compound interest function
			for(Double i:returns) {
				System.out.println("If average compounding return is: " + i);
				double years = Math.log(futureValue/currentValue)/Math.log(1+i);
				System.out.println(String.format("\tIt would take %.02f years to reach $" + futureValue + " to retire!", years));
				System.out.println("\tYou will be about " + Math.round(clients.get(index-1).getAge()+years) + " years old!");
			}
		}
	}
	
	//parameter: none
	//output: none
	//purpose: writes out a file of "taxes" for a specific client
	public void exportTaxForm() {
		// make the file connection
		help.print("Please pick which client you want the tax form for:\n");
		seeClients();
		int index = help.inputInt("\nPick the corresponding number of the client", 1, clients.size());
		//writing out basic tax info like net worth, stocks, and amount taxed
		try (FileOutputStream fout = new FileOutputStream("src/" + clients.get(index-1).getName() + ".txt");
				PrintWriter out = new PrintWriter(fout)){
			out.println("******************************");
			out.println(clients.get(index-1).getName() + " tax form 2022");
			out.println("******************************");
			for(Portfolio i: clients.get(index-1).getHoldings()) {
				out.println("\n" + i.getName() + ":");
				out.println("\tCash : " + i.getCash());
				for(Entry<String, Integer> j: i.getSecurities().entrySet()) {
					out.println("\t"+j.getKey() + " : " + j.getValue());
				}
			}
			//Not really how we calculate tax but for I'm doing this an easier way fake way
			double netWorth = getNetWorthForRP(index);
			out.println("\n\nNet Worth: " + netWorth);
			double taxRate = .35;
			out.println("Tax Rate: " + taxRate);
			double taxes = netWorth * taxRate;
			out.println(String.format("OWED TAXES ARE: %.02f", taxes));
			
			System.out.println("Wrote the data to file: src/" + clients.get(index-1).getName() + ".txt");
		} // everything does auto-close
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print(e);
		}
	}
	
	//parameter: none
	//output: none
	//purpose: prints out the current market index prices
	public void currentMarketMovement() {
		System.out.println("Here are the current market indices information:");
		HashMap<String,String> urls = new HashMap<>();
		urls.put("Dow Jones","https://finance.yahoo.com/quote/%5EDJI?p=^DJI&.tsrc=fin-srch");//DowJones
		urls.put("S&P 500","https://finance.yahoo.com/quote/%5EGSPC?p=^GSPC&.tsrc=fin-srch");//S&P
		urls.put("NASDAQ","https://finance.yahoo.com/quote/%5EIXIC?p=^IXIC&.tsrc=fin-srch");//NASDAQ
		//need to cycle through every index and print relevant info
		//Because you need ot establish the URLConnect it can take a while
		for(Entry<String,String> i:urls.entrySet()) {
			try {
				IndexReader getter = new IndexReader(i.getValue());
				double price = getter.getPrice();
				IndexReader getter1 = new IndexReader(i.getValue());
				double close = getter1.getClose();
				IndexReader getter2 = new IndexReader(i.getValue());
				double open = getter2.getOpen();
				double change = price - close;
				System.out.println(i.getKey() + " Information:");
				System.out.println("\tCurrent Price: " + price);
				System.out.println(String.format("\tChange: %.2f", change));
				System.out.println("\tPrevious Close: " + close);
				System.out.println("\tOpen: " + open + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Index Not Found");
			}
		}
	}
	
	//parameter: none
	//output: none
	//purpose: After you are done with the data, this method which is called right before we quit the program will overwrite all the data to the ClientPortfolioCSV so it will save all the changes made
	public void writeOutData() {
		try (FileOutputStream fout = new FileOutputStream("src/ClientPortfolioData.csv");
				PrintWriter out = new PrintWriter(fout)){
			for(Client i:clients) {
				for(Portfolio j: i.getHoldings()){
					
					for(Entry<String,Integer> pair:j.getSecurities().entrySet()) {
						String isStock = "";
						String position = "";
						String company = "";
						int yearsRetire = 0;
						boolean found = false;
						for(Stock m:stocks) {
							if(m.getTicker().equalsIgnoreCase(pair.getKey())) {
								found = true;
								isStock = "true";
								if(m.getPosition() == Position.LONG) {
									position = "true";
								}
								else {
									position = "false";
								}
							}
						}
						//determining what info to read out to files by trying to make it easy for me
						if(found == false) {
							isStock = "false";
						}
						String isIRA;
						if(j instanceof IRA) {
							isIRA = "true";
							yearsRetire = ((IRA) j).getYearsToRetirement();
						}
						else {
							isIRA = "false";
							company = ((_401k) j).getCompany();
						}
						String portfolioName = j.getName();
						double cash = j.getCash();
						String client = i.getName();
						int age = i.getAge();
						String employed;
						if(i.isEmployed() == true) {
							employed = "true";
						}
						else {
							employed = "false";
						}
						String ticker = pair.getKey();
						int count = 0;
						while(count < pair.getValue()) {
							if(isStock.equalsIgnoreCase("true")) {
								if(isIRA.equalsIgnoreCase("true")) {
									out.print("\n" + isStock + "," + isIRA + "," + portfolioName + "," + cash + "," + client + "," + age + "," + employed + "," + ticker + "," + position + "," + yearsRetire);
								}
								else {
									out.print("\n" + isStock + "," + isIRA + "," + portfolioName + "," + cash + "," + client + "," + age + "," + employed + "," + ticker + "," + position + "," + company);
								}
							}
							else {
								if(isIRA.equalsIgnoreCase("true")) {
									out.print("\n" + isStock + "," + isIRA + "," + portfolioName + "," + cash + "," + client + "," + age + "," + employed + "," + ticker + "," + yearsRetire);
								}
								else {
									out.print("\n" + isStock + "," + isIRA + "," + portfolioName + "," + cash + "," + client + "," + age + "," + employed + "," + ticker + "," + company);
								}
							}
							count++;
						}
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		System.out.print(e);
	}
			
				
//			out.println("******************************");
//			out.println(clients.get(index-1).getName() + " tax form 2022");
//			out.println("******************************");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrokerageFirm demo = new BrokerageFirm();
		demo.run();
		
	}

}
