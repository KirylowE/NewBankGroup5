package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private final String className = NewBankClientHandler.class.getName();

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	/*
	 */

	private void startScreen() {
		out.println("............................................");
		out.println("New Bank Terminal is running. Please log in.");
		out.println("............................................");

	}

	/**
	 * Account selection screen
	 *
	 * @param operation String to be included in prompt (e.g. "to deposit money")
	 * @return response String with account name or menu command
	 */
	private String selectAccount(String operation) {
		while (true) {
			try {
				out.println(this.bank.customer.accounts.printAccounts());
				out.println("Please select the account " + operation + " by typing the Account No.");
				out.println("Type Q or M to return to Main Menu.");
				String input = in.readLine();
				if (input.equals("M") || input.equals("Q")) {
					return input;
				}
				for (Account account : this.bank.customer.accounts.getAccounts()) {
					if (account.getIndex().equals(input)) {
						return account.getAccountName();
					}
				}
				out.println("Incorrect account.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Account selection screen
	 *
	 * @param operation String to be included in prompt (e.g. "to deposit money")
	 * @return amount of money selected or null if menu command
	 */
	private Double selectAmount(String operation) {
		while (true) {
			try {
				out.println("Please select the amount " + operation);
				out.println("Type Q or M to return to Main Menu.");
				String input = in.readLine();
				if (input.equals("M") || input.equals("Q")) {
					return null;
				}
				if (ClientConsole.matchAmount(input)) {
					double amount = Double.parseDouble(input);
					if (amount > 0) {
						return amount;
					}
				}
				out.println("Incorrect amount. The selected amount must be greater than 0.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		startScreen();
		this.launch();
	}

	/**
	 * starts the login sequence. displays main menu on successful login.
	 */
  public void launch() {
    // keep getting requests from the client and processing them
    // add a limit count
    int limit = 3;

    try {
      while (limit >= 0) {
        // ask for user name
        out.println("\nEnter Username");
        String userName = in.readLine();
        // String userName = "vip";
        // ask for password
        out.println("Enter Password");
        String password = in.readLine();
        // String password = " ";
        out.println("Checking Details...");
        // authenticate user and get customer object from bank for use in subsequent requests
        Customer customer = bank.checkLogInDetails(userName, password);
        // if the user is authenticated then get requests from the user and process them
				if (customer != null) {
					// server logging
					Logger.getLogger(this.className).log(Level.INFO, customer.getUserName() + " user logged in success.");
					// client messages
					out.println("\nLog In Successful.");
					out.println("Hello " + customer.getFirstName() + ". Please select option.\n");
					this.mainMenu();
					while (true) {
						out.println("Main Menu. Please type an option (1 - 8) and hit the Enter key.");
						out.println("Type P or M to list all options.");
						String request = in.readLine();
						if (request.equals("M") || request.equals("P")) {
						this.mainMenu();
						}
							if (request.equals("EXIT")) {
							String responce = "Exiting";
							out.println(responce);
							break;
						}
						System.out.println("Request from " + customer.getFirstName() + " (" + customer.getUserName() + ")");
						String responce = this.processRequest(request);
						out.println(responce);
					}
				} else {
					String one = limit == 1 ? "" : "s";
					// client messages
					out.println("Incorrect username or password.");
					out.println(limit + " more attempt" + one + " before account is locked.");
					limit--;
				}
			}
      out.println("Maximum login attempts reached. Please contact customer service.");

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        in.close();
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }
    }
  }

	/**
	 * @param request
	 * @return
	 */
	// commands from the customer are processed in this method
	public synchronized String processRequest(String request) {
		switch (request) {
			case "1": {
				// SHOW MY ACCOUNTS
				out.println("YOUR ACCOUNTS:");
				return this.bank.showMyAccounts() + " ";
			}

			case "2": {
				// DEPOSIT MONEY
				try {
					out.println("\nCURRENT ACTION: DEPOSIT MONEY");
					String operation = "to deposit money";
					String acc = this.selectAccount(operation);
					if (acc.equals("Q") || acc.equals("M")) {
						this.mainMenu();
						break;
					}
					Double amount = this.selectAmount(operation);
					if (amount == null) {
						this.mainMenu();
						break;
					}
					return this.bank.customer.accounts.addingMoneyToBalance(acc, amount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "3": {
				// WITHDRAW
				try {
					out.println("\nCURRENT ACTION: WITHDRAW MONEY");
					String operation = "to withdraw money";
					String acc = this.selectAccount(operation);
					if (acc.equals("Q") || acc.equals("M")) {
						this.mainMenu();
						break;
					}
					Double amount = this.selectAmount(operation);
					if (amount == null) {
						this.mainMenu();
						break;
					}
						return this.bank.customer.accounts.withdrawingMoneyToBalance(acc, amount);
        } catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "4": {
				// NEWACCOUNT
				try {
					out.println("\nCURRENT ACTION: NEW ACCOUNT");
					String status = "FAIL";
					Boolean accountFound;
					out.println(this.bank.customer.accounts.printAccounts());
					out.println("Please enter new Account Name: ");
					String accountName = in.readLine();
					accountFound = this.bank.customer.accounts.addNewCustomerAccount(accountName);
					if (!accountFound) {
						out.println("Please enter Opening Balance: ");
						double openingBalance = Double.parseDouble(in.readLine());
						while(openingBalance < 0){
							out.println("Please enter a positive amount.");
							openingBalance = Double.parseDouble(in.readLine());
						}
						this.bank.customer.accounts.addAccount(new Account(accountName, openingBalance));
						status = "SUCCESS";
					}
					else{
						out.println("Account already exists.");
					}
					return status;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "5": {
				// MOVE
				try {
					out.println("\nCURRENT ACTION: MOVE MONEY TO ANOTHER ACCOUNT");
					out.println(this.bank.customer.accounts.printAccounts());
					String accFrom = this.selectAccount("you wish to move money FROM");
					if (accFrom == null) {
						return null;
					}
					String accTo = this.selectAccount("you wish to move money TO");
					if (accTo == null) {
						return null;
					}
					Double amount = this.selectAmount("to move");
					if (amount == null) {
						this.mainMenu();
						break;
					}
						return this.bank.customer.move(accFrom, accTo, amount);
        } catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "6": {
				// PAY
				try {
					out.println("\nCURRENT ACTION: PAY SOMEONE");
					String typeToTransfer1 = this.selectAccount("you wish to pay from");
					if (typeToTransfer1 == null) {
						return null;
					}
					out.println("Please, select the person/company: ");
					String typeToTransfer2 = in.readLine();
					if (typeToTransfer2 == null) {
						return null;
					}
					out.println("Please, select the account of the person/company: ");
					String typeToTransfer3 = in.readLine();
            if (typeToTransfer3 == null) {
						return null;
					}
					Double amount = this.selectAmount("to transfer");
          if (amount == null) {
						this.mainMenu();
						break;
          }
						Boolean transferResult = this.bank.customer.pay(typeToTransfer1, amount);
						out.println(transferResult);
						if (transferResult) {
							this.bank.customers.get(typeToTransfer2).accounts.addingMoneyToBalance(typeToTransfer3, amount);
							return "SUCCESS";
						}
          break;
        } catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "7":{
				// BECOME A LENDER
	try{
				//Scanner inToBecomeLender= new Scanner(System.in);
				out.println("Please, select the account: ");
				String accountName=in.readLine();
				out.println("Please, select the amount to lend: ");
				double amountToLend= Double.parseDouble(in.readLine());
				out.println("Please, indicate the interest rate: ");
				double interestRate= Double.parseDouble(in.readLine());
				if(this.bank.customer.validationDataLender(accountName, amountToLend , interestRate)) {
					Lender lender = new Lender(this.bank.customer.getPrimaryKey(),accountName,amountToLend,interestRate);
					this.bank.lenders.add(lender);
					return "SUCCESS";
				}
				return "FALSE";
		}

	catch (Exception e) {
		e.printStackTrace();
	}

			}

			case "8":{
				// SHOW LENDERS
				if (this.bank.lenders.size()==0){
					return "No Lenders available or error occurred";
				}

					for(Lender l : this.bank.lenders) {
						System.out.println(l.toString());
					}
					return "SUCCESS";


			}

			case "9":{

				// MICROLOAN

try{
				System.out.println("Please, select the lender: ");
				String lenderName=in.readLine();
				System.out.println("Please, select the account where to receive money: ");
				String accountNameBorrower=in.readLine();
				System.out.println("Please, indicate the amount to receive: ");
				double amountToReceive=Double.parseDouble(in.readLine());
				//System.out.print(lenders);
				//return "true";
				for(Lender l : this.bank.lenders){
					if(l.getCustomerID().equalsIgnoreCase(lenderName)){
						if (l.getAmountLent() <= l.getAmountToLend() - amountToReceive) {
							boolean transferResultToMicroLoan=this.bank.customer.pay(l.getAccountName(), amountToReceive);
							System.out.println(transferResultToMicroLoan); // false
							if(transferResultToMicroLoan){
								this.bank.customer.addingMoneyToBalance(accountNameBorrower, amountToReceive);
								l.setAmountLent(amountToReceive);
								if (this.bank.loans.containsKey(lenderName) == false)
									this.bank.loans.put(lenderName, new ArrayList<Lend>());
								ArrayList<Lend> aL = this.bank.loans.get(lenderName);
								aL.add(new Lend(this.bank.customer.getPrimaryKey(),amountToReceive,l.getInterestRate()));
								this.bank.loans.replace(lenderName,aL);
								return "SUCCESS";
							}
						}
					}
				}
				return "FAIL";
		}
				catch (Exception e) {
								e.printStackTrace();
				}



			}
			case "10":{
				// SHOW LOANS

				if (this.bank.loans.isEmpty()) {
					return "No loans available";
				}
				return this.bank.loans.toString() ;

			}
			case "11": {
				// LOG OUT
				// Client message
				out.println(this.bank.customer.getUserName() + " logged out.");
				// Server message
				Logger.getLogger(this.className).log(Level.INFO, this.bank.customer.getUserName() + " user logged out.");
				// call the action
				this.launch();
				break;
			}

			default: {
				return "";
			}

		}
		return "";
	}

	public void mainMenu() {
    
    out.println("New Bank Menu");
		out.println("1. SHOWMYACCOUNTS");
		out.println("2. DEPOSIT");
		out.println("3. WITHDRAW");
		out.println("4. NEWACCOUNT");
		out.println("5. MOVE");
		out.println("6. PAY");
		out.println("7. BECOME A LENDER");
		out.println("8. SHOW LENDERS");
		out.println("9. MICROLOAN");
		out.println("10. SHOW LOANS");
		out.println("11. LOG OUT");
		out.println("Please enter an option (1 - 11):");

	}
}
