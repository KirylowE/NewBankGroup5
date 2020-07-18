package newbank.server;

public class Account {
	
	private String accountName;
	private double openingBalance;
	private double amountForBalance;

	public Account(String accountName, double openingBalance){ //openingBalance= currentBalance
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}
	

	
	public String toString() {
		return (accountName + " : " + openingBalance); 
		}
 
	
	 public double addMoneyToBalance(double amountForBalance) {
		 openingBalance=openingBalance + amountForBalance;
		 return openingBalance;
	 }
	 
	 public String getAccountName() {
		 return accountName; 
	 } 
	 
	 public double getBalance() {
		 return openingBalance; 
	 }
	 
	 
	 public double subtractMoneyToBalance(double amountForBalance) {
		  //return addMoneyToBalance(-amountForBalance);
		 //or
		  openingBalance=openingBalance - amountForBalance;
			 return openingBalance;
	 }
	  
	  
	
	
	
}
