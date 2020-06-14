package com.dawson.highwaytohell.fehighwaytohell.LoanCalculator;

/**
 * Class which takes care of calculating the loan
 */
public class LoanCalculator {

    private double balance;
    private double rate;

    /**
     * Constructor for the loan calculator
     * @param balance the balance (amount asked for) from the bank
     * @param rate the monthly interest rate
     */
    public LoanCalculator(double balance, double rate) {
        if(balance < 0 || rate < 0 || rate > 100){
            throw new IllegalArgumentException();
        }
        this.balance=balance;
        this.rate=rate;
    }

    /**
     * With a monthly value to Payoff, returns number of months needed to pay off the loan
     * Throws exception if over 60 years
     * @param payedOff the value payed off per month
     * @return number of months needed to pay off
     */
    public int monthsToPayOff(double payedOff){
        double first = Math.log(1-(((rate/1200)*balance)/payedOff));
        double second = Math.log(1+(rate/1200));
        int result  = (int) Math.ceil(-(first/second));
        if (Double.isNaN(first) || Double.isNaN(second)) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    /**
     * Calculates the number of monhts and years needed to pay off the loan
     * @param payedOff the amount payed off each month
     * @return String containing number of years and months
     */
    public String yearsAndMonthToPayOff(double payedOff){
        int months = monthsToPayOff(payedOff);
        int years = months / 12;
        int newMonths = months%12;
        if(months == 0){
            return Integer.toString(years) + " years";
        }else{
            return Integer.toString(years) + " years " + Integer.toString(newMonths) + " months";
        }

    }

    /**
     * Calculate the amount the user has to pay on a monthly basis
     * @param payedOff the amount he pays off
     * @return the amount he has to pay on a monthly basis
     */
    public double getMonthlyPayment (double payedOff)
    {
        double monthlyPayment;
        double monthlyInterestRate;
        int numberOfPayments = monthsToPayOff(payedOff);
        if (numberOfPayments != 0 && rate != 0)
        {
            //calculate the monthly payment
            monthlyInterestRate = rate / 1200;

            monthlyPayment =
                    (balance * monthlyInterestRate) /
                            (1 - (1 / Math.pow ((1 + monthlyInterestRate), numberOfPayments)));

            monthlyPayment = Math.round (monthlyPayment * 100) / 100.0;
        }
        else
            monthlyPayment = 0;
        return monthlyPayment;

    }


    /**
     * Get total cost of the credit (loan + balance)
     * @param payedOff the amount payed off per month
     * @return the cost of the credit (loan + balance)
     */
    public double getTotalCostOfCredit(double payedOff)
    {
        return Math.round((getMonthlyPayment(payedOff) * monthsToPayOff(payedOff)) * 100.0) / 100.0;

    }


    /**
     * Get total number of interest the user has paid through his loan
     * @param payedOff the amount payed off per month
     * @return the amount paid as interest overall
     */
    public double getTotalInterest (double payedOff)
    {
        if(Math.round((getTotalCostOfCredit(payedOff) - balance) * 100.0) / 100.0 > 0){
            return Math.round((getTotalCostOfCredit(payedOff) - balance) * 100.0) / 100.0;
        }else
            return 0;
    }

    /**
     * Get the total number the user saved (unless it is worst case, where he didn't save anything)
     * @param payedOff the amount he pays off each month
     * @param worstCasePayedOff the worst case scenario paid per month betwen option A, B and C
     * @return
     */
    public String amountSaved(double payedOff, double worstCasePayedOff){
        double first = getTotalCostOfCredit(worstCasePayedOff);
        double second = getTotalCostOfCredit(payedOff);
        double amountSaved  = first-second;
        if(amountSaved > 0){
            return Double.toString(Math.round(amountSaved*100.0)/100.0);
        }
        else{
            return "-";
        }
    }

    /**
     * Get the total amount of time the user saved (unless it is worst case, where he didn't save anything)
     * @param payedOff the amount he pays off per month
     * @param worstCasePayedOff the worst case scenario paid per month between option A, B and C
     * @return
     */
    public String timeSaved(double payedOff, double worstCasePayedOff){
        double monthsSaved = monthsToPayOff(worstCasePayedOff) - monthsToPayOff(payedOff);
        if(monthsSaved > 0){
            return yearsAndMonthToPayOff(monthsSaved);
        }
        else{
            return "-";
        }
    }

    /**
     * Getter for balance
     * @return returns the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Setter for balance
     * @param balance sets the old balance with new one
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Getter for rate
     * @return returns the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * Setter for rate
     * @param rate sets the old rate with the new one
     */
    public void setRate(double rate) {
        this.rate = rate;
    }


}
