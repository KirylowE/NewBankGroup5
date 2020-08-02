package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
		out.println("-----------------------------");
		out.println("New Bank Terminal is running.");
		out.println("-----------------------------");
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
				Optional<Account> account = this.bank.customer.accounts.getAccounts().stream().findFirst().filter(c -> c.getIndex().equals(input));
				if (account.isPresent()) {
					return account.get().getAccountName();
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
        // String userName = in.readLine();
        String userName = "vip";
        // ask for password
        out.println("Enter Password");
        // String password = in.readLine();
        String password = " ";
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
          while (true) {
            mainMenu();
            String request = in.readLine();
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
				// SHOWMYACCOUNTS
				return this.bank.showMyAccounts() + " ";
			}

			case "2": {
				// DEPOSIT
				try {
					String operation = "to deposit money";
					String acc = this.selectAccount(operation);
					if (acc.equals("Q") || acc.equals("M")) {
						System.out.println("Menu command " + acc);
						break;
					}
					Double amount = this.selectAmount(operation);
					if (amount == null) {
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
					String operation = "to withdraw money";
					String acc = this.selectAccount(operation);
					if (acc.equals("Q") || acc.equals("M")) {
						System.out.println("Menu command " + acc);
						break;
					}
					Double amount = this.selectAmount(operation);
					if (amount == null) {
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
            break;
          }
						Boolean transferResult = this.bank.customer.pay(typeToTransfer1, typeToTransfer2, typeToTransfer3, amount);
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

			case "8": {
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
				return "FAIL";
			}

		}
		return "";
	}

	public void mainMenu() {
		out.println("New Bank Menu");
		out.println("1. SHOW MY ACCOUNTS");
		out.println("2. DEPOSIT MONEY");
		out.println("3. WITHDRAW MONEY");
		out.println("4. NEW ACCOUNT");
		out.println("5. MOVE MONEY TO ANOTHER ACCOUNT");
		out.println("6. PAY SOMEONE");
		out.println("7. GET MICROLOAN");
		out.println("8. LOG OUT");
		out.println("Please enter an option (1 - 8):");
	}
}
