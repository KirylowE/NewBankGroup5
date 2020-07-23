package newbank.server;

import java.util.HashMap;
import java.util.Scanner;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
	
		Customer john = new Customer();
		john.addAccount(new Account("Checking1", 250.0));
		john.addAccount(new Account("Checking2", 350.0));
		customers.put("John", john);
		
		Customer isabel = new Customer();
		isabel.addAccount(new Account(" Balance", 750.0));
		customers.put("Isabel", isabel);

		Customer anna = new Customer();
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
					//-----
					case "DEPOSIT" :
						Scanner in= new Scanner(System.in);
						System.out.println("Please, select the account you wish deposit money to");
						String type=in.next();
						System.out.println("Please, indicate the amount of money you wish to deposit  ");
						double amount=in.nextDouble();
						return  (customers.get(customer.getKey())).addingMoneyToBalance(type,amount);
					// -------
					case "WITHDRAW":
						Scanner inToWithdraw= new Scanner(System.in);
						System.out.println("Please, select the account you wish to withdraw money from ");
						String typeToWithdraw=inToWithdraw.next();
						System.out.println("Please, indicate the amount of money you wish to withdraw ");
						double amountToWithdraw=inToWithdraw.nextDouble();
						return  (customers.get(customer.getKey())).withdrawingMoneyToBalance(typeToWithdraw,amountToWithdraw);
					// ----------
					// ----------
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
						Boolean transferResult=(customers.get(customer.getKey())).transferringMoney(typeToTransfer1, typeToTransfer2, typeToTransfer3,amountToTransfer);
						System.out.println(transferResult);
						if(transferResult){
							customers.get(typeToTransfer2).addingMoneyToBalance(typeToTransfer3, amountToTransfer);
							return "SUCCESS";
						}
			default : return "FAIL"; 
			}
		}
		return "FAIL";
	}
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	} 
}
