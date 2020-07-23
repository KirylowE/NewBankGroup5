package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}

	public String addingMoneyToBalance(String typeAccount,double amountToAdd) {
		for (Account a : accounts) {
			if(a.getAccountName().equals(typeAccount)){
				a.addMoneyToBalance(amountToAdd); 
				// System.out.println(a.getAccountName()); 
				return "REQUEST ACCEPTED"+ " - " + " DEPOSIT " + a.getAccountName(); 
			}
		}
		return "REQUEST DENIED" + " - " + " DEPOSIT " + typeAccount;
	}  
	 
	public String withdrawingMoneyToBalance(String typeAccount,double amountToSubtract) {
		//return addingMoneyToBalance(typeAccount,- amountToSubtract);
		//or 
		for (Account a : accounts) {
			if(a.getAccountName().equals(typeAccount)){
				if(amountToSubtract<a.getBalance() ) {
					a.subtractMoneyToBalance(amountToSubtract);
					// System.out.println(a.getAccountName()); 
					return "REQUEST ACCEPTED " + " - "  + " WITHDRAW "  + a.getAccountName(); 
				}
				else return "REQUEST DENIED " + " - " + " Not enough money for bank account "+ a.getAccountName()+ " withdrawal";
			}
		}
		return "REQUEST DENIED" + " - " + " WITHDRAW " + typeAccount;
		
	}

			//----
			// PAY
			//----

	public Boolean pay(String nameAccountSendsMoney, String ownerAccountNameReceivesMoney,String accountNameReceivesMoney, double amountToTransfer) {
		for (Account a : accounts) {
			if (a.getAccountName().equalsIgnoreCase(nameAccountSendsMoney)) {
				if (amountToTransfer <= a.getBalance()) {
					a.subtractMoneyToBalance(amountToTransfer);
					return true;
				}
			}
			return false;
		}
		return false; // if the flow does not enter in the for
	}

			//----
			// MOVE
			//----


	public String move(String accountFrom, String accountTo , double amountToMove) {

		for ( Account a: accounts) {
			if(a.getAccountName().equals(accountFrom)) {
				if(amountToMove<=a.getBalance()){
					a.subtractMoneyToBalance(amountToMove);
					for (Account b:accounts){
						if(b.getAccountName().equals(accountTo)){
							b.addMoneyToBalance(amountToMove);
							return "SUCCESS";
						}
					}
				}
			}
		}

		return "FAIL";
	}


}
