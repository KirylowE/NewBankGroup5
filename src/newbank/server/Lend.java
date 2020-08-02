package newbank.server;

public class Lend {

  private String borrowerName;
  private double amountLent;
  private double interestRate;



  public Lend(String borrowerName, double amountLent, double interestRate) {
    this.borrowerName = borrowerName;
    this.amountLent = amountLent;
    this.interestRate = interestRate;
  }


  @Override
  public String toString() {
    return "Borrower name: " + borrowerName + ", amountLent: " + amountLent + ", interestRate:  " + interestRate;
  }

  public String getBorrowerName() {
    return borrowerName;
  }
  public void setBorrowerName(String borrewerName) {
    this.borrowerName = borrewerName;
  }
  public double getAmountLent() {
    return amountLent;
  }
  public void setAmountLent(double amountLent) {
    this.amountLent = amountLent;
  }
  public double getInterestRate() {
    return interestRate;
  }
  public void setInterestRate(double interestRate) {
    this.interestRate = interestRate;
  }

}
