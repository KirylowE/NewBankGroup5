package newbank.server;

public class Account {

	private String primaryKey;

	private String accountName;
	private double openingBalance;
	private double amountForBalance;

	public Account(String accountName, double openingBalance){ //openingBalance= currentBalance
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	// temporary second constructor to create account with a primary key without disturbing existing references
	public Account(String primaryKey, String accountName, double openingBalance){
		this.primaryKey = primaryKey;
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n");
		sb.append(String.format("%1s", "Id"));
		sb.append(String.format("%6s", "Name"));
		sb.append(String.format("%20s", "Balance"));
		sb.append("\n-----------------------------------\n");
		sb.append(String.format("%1s", this.getPrimaryKey()));
		sb.append(String.format("%6s", this.getAccountName()));
		sb.append(String.format("%20s", this.getBalance()));
		sb.append("\n-----------------------------------\n");
		return sb.toString();
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

	public static void main(String[] args) {
		System.out.println(new Account("1", "2", 1000));
	}
	
}
