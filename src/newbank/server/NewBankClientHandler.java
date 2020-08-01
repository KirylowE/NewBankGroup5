package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

	public void run() {
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
        // ask for password
        out.println("Enter Password");
        String password = in.readLine();
        out.println("Checking Details...");
        // authenticate user and get customer object from bank for use in subsequent requests
        Customer customer = bank.checkLogInDetails(userName, password);
        // if the user is authenticated then get requests from the user and process them
        if (customer != null) {
          customer.setAccounts();
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
					out.println("Please, select the account to deposit money: ");
					String type = in.readLine();
					out.println("Please, indicate the amount of money to deposit:  ");
					double amount = Double.parseDouble(in.readLine());
					if (amount <= 0) {
						return "FAIL";
					}
					return this.bank.customer.addingMoneyToBalance(type, amount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "3": {
				// WITHDRAW
				try {
					out.println("Please, select the account to withdraw money: ");
					String typeToWithdraw = in.readLine();
					out.println("Please, indicate the amount of money: ");
					double amountToWithdraw = Double.parseDouble(in.readLine());
					if (amountToWithdraw <= 0) {
						return "FAIL";
					}
					return this.bank.customer.withdrawingMoneyToBalance(typeToWithdraw, amountToWithdraw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "4": {
				// NEWACCOUNT
				try {
					String status = "FAIL";
					Boolean accountFound = false;
					out.println("Please enter new Account Name: ");
					String accountName = in.readLine();
					accountFound = this.bank.customer.addNewCustomerAccount(accountName);
					if (!accountFound) {
						out.println("Please enter Opening Balance: ");
						Double openingBalance = Double.parseDouble(in.readLine());
						while(openingBalance < 0){
							out.println("Please enter a positive amount.");
							openingBalance = Double.parseDouble(in.readLine());
						}
						this.bank.customer.addAccount(new Account(accountName, openingBalance));
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
					out.println("Please, select the account FROM: ");
					String typeToMove1 = in.readLine();
					out.println("Please, select the account TO: ");
					String typeToMove2 = in.readLine();
					out.println("Please, indicate the amount of money: ");
					double amountToMove = Double.parseDouble(in.readLine());
					if (amountToMove <= 0) {
						return "FAIL";
					} else {
						return this.bank.customer.move(typeToMove1, typeToMove2, amountToMove);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			case "6": {
				// PAY
				try {
					out.println("Please, select the account from which you wish to pay: ");
					String typeToTransfer1 = in.readLine();
					out.println("Please, select the person/company: ");
					String typeToTransfer2 = in.readLine();
					out.println("Please, select the account of the person/company: ");
					String typeToTransfer3 = in.readLine();
					out.println("Please, indicate the amount of money you wish to pay: ");
					double amountToTransfer = Double.parseDouble(in.readLine());
					if (amountToTransfer <= 0) {
						return "FAIL";
					}
					Boolean transferResult = this.bank.customer.pay(typeToTransfer1, typeToTransfer2, typeToTransfer3, amountToTransfer);
					out.println(transferResult);
					if (transferResult) {
						this.bank.customers.get(typeToTransfer2).addingMoneyToBalance(typeToTransfer3, amountToTransfer);
						return "SUCCESS";
					}
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
		out.println("1. SHOWMYACCOUNTS");
		out.println("2. DEPOSIT");
		out.println("3. WITHDRAW");
		out.println("4. NEWACCOUNT");
		out.println("5. MOVE");
		out.println("6. PAY");
		out.println("7. MICROLOAN");
		out.println("8. LOG OUT");
		out.println("Please enter an option (1 - 8):");
	}
}
