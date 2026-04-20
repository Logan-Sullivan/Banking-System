package Utils;

import java.time.LocalDate;


public class Transaction {
    double amount;
    int transactionId;
    LocalDate TransactionDate;
    String description;

    public Transaction(double amount, int transactionId, String description) {
        this.amount = amount;
        this.transactionId = transactionId;
        this.TransactionDate = LocalDate.now();
        this.description = description;
    }


    @Override
    public String toString() {
        return transactionId + ","+description+","+amount;
    }

}
