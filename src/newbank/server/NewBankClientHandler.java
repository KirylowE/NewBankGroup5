package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {
			while(true){
				// ask for user name
				out.println("Enter Username");
				String userName = in.readLine();
				// ask for password
				out.println("Enter Password");
				String password = in.readLine();
				out.println("Checking Details...");
				// authenticate user and get customer ID token from bank for use in subsequent requests
				CustomerID customer = bank.checkLogInDetails(userName, password);
				// if the user is authenticated then get requests from the user and process them
				if(customer != null) {
					out.println("Log In Successful. What do you want to do?");
					while(true) {
						mainMenu();
						String request = in.readLine();
						if (request.equals("EXIT")) {
							String responce = "Exiting";
							out.println(responce);
							break;
						}
						out.println("Request from " + customer.getKey());
						String responce = bank.processRequest(customer, request);
						out.println(responce);
					}
				}
				else {
					out.println("Log In Failed");
				}
			}
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
