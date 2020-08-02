package newbank.server;

import newbank.dbase.Dispatcher;
import newbank.dbase.EnvironmentVariable;

import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class NewBank {

  private static final NewBank bank = new NewBank();

  private final String className = NewBankClientHandler.class.getName();
  public HashMap<String, Customer> customers;
	private Dispatcher dispatcher;
	public Customer customer;
	public ArrayList<Lender> lenders;
	public HashMap <String, ArrayList<Lend>> loans;



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
      this.dispatcher = Dispatcher.getInstance();
      // get the customers from database, start an action through the dispatcher object.
      // this is server-side, the customers list can be safely enumerated
      customers = this.dispatcher.getCustomers();
    }
  }


  // temporary main method to test the functionality of this class locally,
  // without starting the resource-hungry server
  // TODO: remove the main method once this class is complete
  public static void main(String[] args) {

  }

	private void addTestData() {

  	// TODO: Add surnames or change to random names altogether

		Customer bhagy = new Customer("1", "Bhagy");
		bhagy.accounts.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer("2", "Christina");
		christina.accounts.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);

		Customer john = new Customer("3", "John");
		john.accounts.addAccount(new Account("Checking1", 250.0));
		john.accounts.addAccount(new Account("Checking2", 350.0));
		customers.put("John", john);

		Customer isabel = new Customer("4", "Isabel");
		isabel.accounts.addAccount(new Account(" Balance", 750.0));
		customers.put("Isabel", isabel);

		Customer anna = new Customer("5", "Anna");
		anna.accounts.addAccount(new Account("Checking", 1250.0));
		customers.put("Anna", anna);
	}

	public static NewBank getBank() {
		return bank;
	}

	/**
	 * @param userName
	 * @param password
	 * @return
	 */
	public synchronized Customer checkLogInDetails(String userName, String password) {
		// server logging
		Logger.getLogger(this.className).log(Level.INFO, userName + " user attempted to log in.");
		// get customer object
		Customer customer = this.dispatcher.getCustomerByUserName(userName.trim());
		if (customer != null) {
			this.customer = customer;
			return customer;
		}
		return null;
	}

	/**
	 * showMyAccounts allows each customer to display her/his accounts
	 * @return
	 */
	public String showMyAccounts() {
		return this.customer.accounts.printAccounts();
	}

	/**
	 * Method to add a new account for a customerID. Returns SUCCESS or FAIL
	 * @param name
	 * @return
	 */
	public String addNewAccount(String name) {
		String status = "FAIL";
		Boolean accountFound = false;
		Scanner openingBal = new Scanner(System.in);

		accountFound = this.customer.accounts.addNewCustomerAccount(name);
		if (!accountFound) {
			System.out.println("Please enter Opening Balance: ");
			Double openingBalance = openingBal.nextDouble();
			while(openingBalance < 0){
				System.out.println("Please enter a positive amount.");
				openingBalance = openingBal.nextDouble();
			}
			this.customer.accounts.addAccount(new Account(name, openingBalance));
			status = "SUCCESS";
		}
		else{
			System.out.println("Account already exists.");
		}
		return status;
	}

}

