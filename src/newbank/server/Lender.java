package newbank.server;

import java.util.ArrayList;

public class Lender {

  private double amountToLend;
  private double amountLent;
  private double interestRate;
  private String accountName;
  private String customerID;


  /**
   * @param key
   * @param accountName
   * @param amountToLend
   * @param interestRate
   */
  public Lender(String key, String accountName, double amountToLend, double interestRate) {
    this.accountName=accountName;
    this.amountToLend = amountToLend;
    this.interestRate= interestRate;
    this.customerID= key;
    this.amountLent=0;
  }

  public String getAccountName() {
    return accountName;
  }

  public String toString(){
    return "Lender: " + customerID + " Amount available to lend : " + amountToLend + " Interest rate : " + interestRate + " Amount lent : " + amountLent;
  }

  public String getCustomerID(){
    return customerID;
  }

  public double getAmountLent() {
    return amountLent;
  }

  public double getAmountToLend() {
    return amountToLend;
  }

  public double getInterestRate() {
    return interestRate;
  }

  public double getTotal() {
    return amountToLend + (interestRate*amountToLend);
  }

  public double ZeroMoneyMicroLoan(String AccountName, double amountToLend ) {
    amountToLend=0.0;
    return amountToLend;
  }

  public void setAmountLent(double amountLent){
    this.amountLent+= amountLent;
  }




}