package newbank.server;

import newbank.dbase.Dispatcher;
import newbank.dbase.EnvironmentVariable;

import java.util.HashMap;
import java.util.Scanner;

public class NewBank {

  private static final NewBank bank = new NewBank();

  private HashMap<String, Customer> customers;

  /**
   * Constructor uses a distinction between development and production mode.
   * This should not make a difference to object types in this class;
   * E.g. customers lists are retrieved from different sources and can be used interchangeably
   */
  private NewBank() {
    String envDev = EnvironmentVariable.getVariableValue("NB_DEV");
    boolean isDev = (envDev != null) && envDev.equals("true");
    if (isDev) {
      // Development mode that uses the hardcoded test data
      System.out.println("New Bank is running in Development mode, local test data is used");
      // get customers by calling the local method that adds few test records
      customers = new HashMap<>();
      addTestData();
    } else {
      // Production mode that uses the database
      System.out.println("New Bank is running in Production mode, database is used.");
      // Start the dispatcher before running database operations
      Dispatcher dispatcher = Dispatcher.getInstance();
      // get the customers from database, start an action through the dispatcher object.
      customers = dispatcher.getCustomers();
    }
  }


  // temporary main method to test the functionality of this class locally,
  // without starting the resource-hungry server
  // TODO: remove the main method once this class is complete
  public static void main(String[] args) {

  }

	
	private void addTestData() {

  	// TODO: Add surnames or change to random names altogether
		
		Customer bhagy = new Customer("1");
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer("2");
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
	
		Customer john = new Customer("3");
		john.addAccount(new Account("Checking1", 250.0));
		john.addAccount(new Account("Checking2", 350.0));
		customers.put("John", john);
		
		Customer isabel = new Customer("4");
		isabel.addAccount(new Account(" Balance", 750.0));
		customers.put("Isabel", isabel);

		Customer anna = new Customer("5");
		anna.addAccount(new Account("Checking", 1250.0));
		customers.put("Anna", anna);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())){
			switch(request){

			case "SHOWMYACCOUNTS" : return showMyAccounts(customer)+" ";
          
      case "DEPOSIT" :
        Scanner in= new Scanner(System.in);
        System.out.println("Please, select the account to deposit money: ");
        String type=in.next();
        System.out.println("Please, indicate the amount of money to deposit:  ");
        double amount=in.nextDouble();
        if(amount <= 0){
        	return "FAIL";
        }
        return  (customers.get(customer.getKey())).addingMoneyToBalance(type,amount);
          
      case "WITHDRAW":
        Scanner inToWithdraw= new Scanner(System.in);
        System.out.println("Please, select the account to withdraw money: ");
        String typeToWithdraw=inToWithdraw.next();
        System.out.println("Please, indicate the amount of money: ");
        double amountToWithdraw=inToWithdraw.nextDouble();
        if(amountToWithdraw <= 0){
        	return "FAIL";
        }
        return  (customers.get(customer.getKey())).withdrawingMoneyToBalance(typeToWithdraw,amountToWithdraw);

			case "NEWACCOUNT":
        Scanner newAccount = new Scanner(System.in);
        System.out.println("Please enter new Account Name: ");
        String accountName = newAccount.next();
        return addNewAccount(customer, accountName);
          
      case "MOVE":
        Scanner inToMove= new Scanner(System.in);
        System.out.println("Please, select the account FROM: ");
        String typeToMove1=inToMove.next();
        System.out.println("Please, select the account TO: ");
        String typeToMove2=inToMove.next();
        System.out.println("Please, indicate the amount of money: ");
        double amountToMove=inToMove.nextDouble();
        if (amountToMove <= 0){
        	return "FAIL";
        }
        else{
        	return (customers.get(customer.getKey())).move(typeToMove1,typeToMove2,amountToMove);
        }

      case "PAY":
        Scanner inToTransfer= new Scanner(System.in);
        System.out.println("Please, select the account from which you wish to pay: " );
        String typeToTransfer1=inToTransfer.next();
        System.out.println("Please, select the person/company: " );
        String typeToTransfer2=inToTransfer.next();
        System.out.println("Please, select the account of the person/company: ");
        String typeToTransfer3=inToTransfer.next();
        System.out.println("Please, indicate the amount of money you wish to pay: ");
        double amountToTransfer=inToTransfer.nextDouble();
        if(amountToTransfer <= 0 ) {
        	return "FAIL";
        }
        Boolean transferResult=(customers.get(customer.getKey())).pay(typeToTransfer1, typeToTransfer2, typeToTransfer3,amountToTransfer);
        System.out.println(transferResult);
        if(transferResult){
          customers.get(typeToTransfer2).addingMoneyToBalance(typeToTransfer3, amountToTransfer);
          return "SUCCESS";
        }
          
          
			case "EXIT": break;
          
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	/**
	 * Method to add a new account for a customer. Returns SUCCESS or FAIL
	 * @param customer
	 * @param name
	 * @return
	 */
	private String addNewAccount(CustomerID customer, String name) {
		String status = "FAIL";
		Boolean accountFound = false;
		Scanner openingBal = new Scanner(System.in);

		accountFound = customers.get(customer.getKey()).addNewCustomerAccount(name);
		if (!accountFound) {
			System.out.println("Please enter Opening Balance: ");
			Double openingBalance = openingBal.nextDouble();
			while(openingBalance < 0){
				System.out.println("Please enter a positive amount.");
				openingBalance = openingBal.nextDouble();
			}
			customers.get(customer.getKey()).addAccount(new Account(name, openingBalance));
			status = "SUCCESS";
		}
		else{
			System.out.println("Account already exists.");
		}
		return status;
	}
}

