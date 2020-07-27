package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewBankClientHandler extends Thread{

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
		// keep getting requests from the client and processing them
		// add a limit count
		int limit = 3;

		try {
			while(limit > 0){
				// ask for user name
				out.println("\nEnter Username");
				String userName = in.readLine();
				// ask for password
				out.println("Enter Password");
				String password = in.readLine();
				out.println("Checking Details...");
				// authenticate user and get customer ID token from bank for use in subsequent requests
				CustomerID customer = bank.checkLogInDetails(userName, password);
				// if the user is authenticated then get requests from the user and process them
				if (customer != null) {
					Customer cs = bank.initLoggedCustomer(customer.getKey());
					// server logging
					Logger.getLogger(this.className).log(Level.INFO, customer.getKey() + " user logged in success.");
					// client messages
					out.println("\nLog In Successful.");
					out.println("Hello " + cs.getFirstName() + ". Please select option.\n");
					while (true) {
						mainMenu();
						String request = in.readLine();
						if (request.equals("EXIT")) {
							String responce = "Exiting";
							out.println(responce);
							break;
						}
						System.out.println("Request from " + cs.getFirstName() + " (" + customer.getKey() + ")");
						String responce = bank.processRequest(customer, request);
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
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	public void mainMenu(){
		out.println("       New Bank Menu");
		out.println("1.SHOWMYACCOUNTS");
		out.println("2.DEPOSIT");
		out.println("3.WITHDRAW");
		out.println("4.NEWACCOUNT");
		out.println("5.MOVE");
		out.println("6.PAY");
		out.println("7.MICROLOAN");
		out.println("8.EXIT");
		out.println("Please enter an option: (Example- SHOWMYACCOUNTS) ");
	}
}
