package newbank.server;

public class Account {

	private String index;
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
	public Account(String index, String primaryKey, String accountName, double openingBalance){
		this.index = index;
		this.primaryKey = primaryKey;
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\nNo");
		sb.append(" ".repeat(3));
		sb.append(String.format("%6s", "Name"));
		sb.append(String.format("%28s", "Balance"));
		sb.append("\n-------------------------------------------\n");
			int index = Integer.parseInt(this.getIndex());
			String name = this.getAccountName();
			sb.append(index);
			sb.append(" ".repeat(7 - this.getIndex().length())).append(this.getAccountName());
			sb.append(" ".repeat(25 - name.length())).append(this.getBalance());
			sb.append("\n");
		sb.append("-------------------------------------------\n");
		return sb.toString();
	}

	public double addMoneyToBalance(double amountForBalance) {
		this.openingBalance = this.openingBalance + amountForBalance;
		return this.openingBalance;
	}

	public String getIndex() {
		return this.index;
	}

	public String getAccountName() {
		 return this.accountName;
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
		System.out.println(new Account("1", "1", "2", 1000));
	}
	
}
