
public enum Menu {
	MARKET_INFO("See the major market indices"),
	SEE_ALL_CLIENTS("Print all clients in database"),
	SEE_ALL_STOCKS("Print all stocks found in all portfolios"),
	SEE_ALL_BONDS("Print all bonds found in all portfolios"),
	VIEW_ALL_401K("Print all 401k portfolios"),
	VIEW_ALL_IRA("Print all IRA portfolios"),
	ADD_CLIENT("Add a client to our firm"),
	DELETE_CLIENT("Delete a client from our firm"),
	ADD_SECURITY("Add a security to a portfolio"),
	SELL_SECURITY("Sell a security in a portfolio"),
	ADD_PORTFOLIO("Add a brokerage account (portfolio) to a client"),
	DELETE_PORTFOLIO("Delete a brokerage account (portfolio) from a client"),
	CREATE_EXAMPLE_PORTFOLIO("Construct a hypothetical portfolio with option to add to client's account"),
	GET_NET_WORTH("Get net worth of a client"),
	RETIREMENT_PLAN("Get a retirement plan information from accounts"),
	EXPORT_TAX_FORM("Create file of client's account for taxes"),
	QUIT("Quit");
	
	private String description;
	    
	//simple menu information to print out and get the correct command
	
	private Menu(String d){
		this.description = d;   
	}
	    
	//parameter: int
	//output: none
	//purpose: gets the corresponding option to menu pick
    public static Menu getOption(int num){
       return  Menu.values()[num-1];
       
    }
    
	//parameter: none
	//output: double
	//purpose: prints out all menu options
    public static void printMenu(){
    	System.out.println();
         for(int i = 1 ; i <= Menu.values().length; i++){
             System.out.println(i + ": " + Menu.values()[i-1].description);
            }
    }
    
	//parameter: none
	//output: double
	//purpose: gets length of options
    public static int getNumOptions(){
        return Menu.values().length;
    }
}
