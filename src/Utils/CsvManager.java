package Utils;
import Account_Classes.*;
import Loan_Classes.*;
import User_Classes.Customer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import Account_Classes.*;

public class CsvManager {
    //i think this best belongs in the manger class once thats made as i believe they will be the ones with permissions to close an account
    public static void removeCustFromArrBySSN(ArrayListManager<Customer> CustomerList, String SSN) {
        try {
            for (int i = 0; i < CustomerList.getMcount(); i++) {
                if (CustomerList.getValue(i).customerId.equals(SSN)) {
                    System.out.println("Removing " + CustomerList.getValue(i).firstName + " " + CustomerList.getValue(i).lastName);
                    CustomerList.removeM(i);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
        System.out.println("cust not found");
    }

    // CS: fetches the customer information then the account information from the CSV and loads it to create all accounts and customers.
    public static void fetchCustsAndAccountsFromCSV(ArrayListManager<Customer> CustomerList, Timeline timeline) {
        fetchCustsAndAccountsFromCSV(CustomerList, "src/data.csv", timeline);
    }

    // Same as fetchCustsAndAccountsFromCSV(CustomerList) but allows the UI to choose a file.
    public static void fetchCustsAndAccountsFromCSV(ArrayListManager<Customer> CustomerList, String path, Timeline timeline) {
        File file = new File(path);

        try (Scanner fileReader = new Scanner(file)) {
            Customer customer = null;
            while (fileReader.hasNextLine()) {
                String text = fileReader.nextLine().trim();
                if (text.isEmpty()) continue;
                if (text.startsWith("DATE,")){ // Checks the file for Date, parses it into timeline
                    String dateString = text.split(",")[1];
                    LocalDate savedDate = LocalDate.parse(dateString);
                    timeline.setDate(savedDate);
                    continue;
                }
                String[] formattedText = text.split(",", -1);
                // must be >= 8 because we access index 7
                if (formattedText.length >= 8) {
                    customer = (new Customer(formattedText[0], formattedText[1], formattedText[2],
                            formattedText[3], formattedText[4], formattedText[5], formattedText[6],
                            Integer.parseInt(formattedText[7])));
                    CustomerList.addInOrder(customer);
                    timeline.addServices(customer.atm);

                    continue;
                }
                if (customer == null) continue;
                String[] vars = text.split("\\|");
                String itemType = vars[0];

                switch (itemType) {
                    case "SavingsAccount" -> {
                        String id = vars[1];
                        double rate = Double.parseDouble(vars[2]);
                        String freq = vars[3];
                        boolean overdraft = vars[4].equals(("1"));
                        double balance = Double.parseDouble(vars[5]);

                        SavingsAccount savings = new SavingsAccount(id, rate, freq, overdraft, balance);
                        customer.accountList.add(savings);
                        // add savings account to timeline
                        AppState.timeline.addServices(savings);
                    }
                    case "TMBAccount" -> {
                        String id = vars[1];
                        double balance = Double.parseDouble(vars[2]);
                        TMBAccount tmb = new TMBAccount(id, null, balance);
                        String overdraftAccount = vars.length > 3 ? vars[3] : "";
                        if (!overdraftAccount.isEmpty()){
                            for (Account saveAccount : customer.accountList){
                                if (saveAccount instanceof SavingsAccount savings && savings.accountNumber.equals(overdraftAccount)){
                                    tmb.setOverdraftProtAccount(savings);
                                }
                            }
                        }
                        customer.accountList.add(tmb);
                        // add TMB account to timeline
                        AppState.timeline.addServices(tmb);
                    }
                    case "GDAccount" -> {
                        String id = vars[1];
                        double balance = Double.parseDouble(vars[2]);
                        boolean flexible = Boolean.parseBoolean(vars[3]);

                        GDAccount gd = new GDAccount(id, null, balance, flexible);
                        String interestAccount = vars.length > 4 ? vars[4] : "";
                        if (!interestAccount.isEmpty()){
                            for (Account saveAccount : customer.accountList){
                                if (saveAccount instanceof SavingsAccount savings && savings.accountNumber.equals(interestAccount)){
                                    gd.setInterestAccount(savings);
                                }
                            }
                        }
                        customer.accountList.add(gd);
                        // add GD account to timeline
                        AppState.timeline.addServices(gd);
                    }
                    case "CDAccount" -> {
                        String id = vars[1];
                        double balance = Double.parseDouble(vars[2]);
                        double rate = Double.parseDouble(vars[3]);
                        String date = vars[4];
                        double penalty = Double.parseDouble(vars[5]);

                        LocalDate maturityDate = null;
                        if (date != null && !date.isBlank() && !date.equals("null")) {
                            try {
                                maturityDate = LocalDate.parse(date);
                            } catch (Exception ignored) {
                                maturityDate = null;
                            }
                        }

                        CDAccount cd = new CDAccount(id, balance, rate, maturityDate, penalty);
                        customer.accountList.add(cd);
                        // add CD account to timeline
                        AppState.timeline.addServices(cd);
                    }
                    case "MortgageLoan" ->{
                        String id = vars[1];
                        int term = Integer.parseInt(vars[2]);
                        double rate = Double.parseDouble(vars[3]);
                        double principle = Double.parseDouble(vars[4]);
                        MortgageLoan mgl = new MortgageLoan(id, term, rate, principle, java.time.Period.ofMonths(1), LocalDate.now());
                        customer.payoffList.add(mgl);
                        // add mortgage loan to timeline
                        AppState.timeline.addServices(mgl);
                    }
                    case "ShortTermLoan" -> {
                        String id = vars[1];
                        double rate = Double.parseDouble(vars[2]);
                        double principle = Double.parseDouble(vars[3]);

                        ShortTermLoan stl = new ShortTermLoan(id, rate, principle, java.time.Period.ofMonths(1), LocalDate.now());
                        customer.payoffList.add(stl);
                        // add short term loan to timeline
                        AppState.timeline.addServices(stl);
                    }
                    case "CreditCard" -> {
                        String id = vars[1];
                        double duePayment = Double.parseDouble(vars[2]);
                        double rate = Double.parseDouble(vars[3]);
                        String status = vars[4];
                        boolean problem = Boolean.parseBoolean(vars[5]);
                        // reload credit card limit from CSV
                        double limit = Double.parseDouble(vars[6]);
                        CreditCard card = new CreditCard(id, duePayment, rate, status, problem, limit, LocalDate.now());
                        customer.payoffList.add(card);
                        // add credit card to timeline
                        AppState.timeline.addServices(card);
                    }
                    case "Transaction" -> {
                        String cardId = vars[1];
                        for (Loan loan : customer.payoffList){
                            if (loan instanceof CreditCard card && card.id.equals(cardId)){
                                int id = Integer.parseInt(vars[2]);
                                double amount = Double.parseDouble(vars[3]);
                                String desc = vars[4];
                                Transaction transaction = new Transaction(amount, id,  desc);
                                card.transactions.add(transaction);
                            }
                        }
                    }
                } // End of account switch
            } // end of forloop
        }//end of while loop
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }//end of try-catch 1
        // Parses accounts into timeline and loans
        for (int i = 0; i < CustomerList.getMcount(); i++) {
            Customer customer = CustomerList.getValue(i);

            for (Account account : customer.accountList) {
                if (account instanceof TimeService time) {
                    timeline.addServices(time);
                }
            }

            for (Loan loan : customer.payoffList) {
                if (loan instanceof TimeService time) {
                    timeline.addServices(time);
                }
            }
        }
        // convert checking account types after loading data
        updateCheckingAccountTypes(CustomerList, timeline);
        // Checks previous date and now, so when it loads it automatically applies as needed
        LocalDate today = LocalDate.now();
        LocalDate lastDate = timeline.getLastUpdatedDate();
        if(lastDate.isBefore(today)){
            long days = java.time.temporal.ChronoUnit.DAYS.between(lastDate, today);
            timeline.advanceTime((int) days);
        }
    }
    //end of fetchCustsFromCSV
    // converts TMB/GD accounts on 5000 balance rule
    public static void updateCheckingAccountTypes(ArrayListManager<Customer> CustomerList, Timeline timeline) {
        if (CustomerList == null) {
            return;
        }

        for (int i = 0; i < CustomerList.getMcount(); i++) {
            Customer customer = CustomerList.getValue(i);

            if (customer == null || customer.accountList == null) {
                continue;
            }

            for (int j = 0; j < customer.accountList.size(); j++) {
                Account account = customer.accountList.get(j);

                if (account instanceof GDAccount gd && gd.getBalance() < 5000.0) {
                    TMBAccount tmb = new TMBAccount(gd.accountNumber, null, gd.getBalance());

                    if (gd.getOverdraftProtAccount() != null) {
                        tmb.setOverdraftProtAccount(gd.getOverdraftProtAccount());
                    }

                    customer.accountList.set(j, tmb);

                    if (timeline != null) {
                        timeline.addServices(tmb);
                    }
                }

                else if (account instanceof TMBAccount tmb && tmb.getBalance() > 5000.0) {
                    GDAccount gd = new GDAccount(tmb.accountNumber, null, tmb.getBalance(), true);

                    if (tmb.getOverdraftProtAccount() != null) {
                        gd.setInterestAccount(tmb.getOverdraftProtAccount());
                    }

                    customer.accountList.set(j, gd);

                    if (timeline != null) {
                        timeline.addServices(gd);
                    }
                }
            }
        }
    }
    // CS: I hate this.
    public static void writeCustomersToCsv(ArrayListManager<Customer> CustomerList, Timeline timeline) {
        try {

            // CS: writes the updated accounts and otherwise into the CSV file
            FileWriter Writer = new FileWriter("src/data.csv"); // changed to use data.csv instead of customers.csv
            Writer.write("DATE," + timeline.getLastUpdatedDate() + "\n");
            for (int i = 0; i < CustomerList.getMcount(); i++) {
                Customer customer = CustomerList.getValue(i);

                StringBuilder customerBuilder = new StringBuilder();
                customerBuilder.append(customer.customerId).append(",").append(customer.address).append(",").append(customer.city)
                        .append(",").append(customer.state).append(",").append(customer.zipcode).append(",").append(customer.firstName)
                        .append(",").append(customer.lastName).append(",").append(customer.atm.getWithdraws()).append("\n");

                for (Account account : customer.accountList){
                    if (account instanceof SavingsAccount saving){
                        // Ike: write full savings data so reload matches the parser format
                        customerBuilder.append(saving.getClass().getSimpleName()).append("|")
                                .append(saving.accountNumber).append("|")
                                .append(saving.getInterestRate()).append("|")
                                .append(saving.getCompoundFreq()).append("|")
                                .append(saving.isOverdraftBackup() ? "1" : "0").append("|")
                                .append(saving.getBalance()).append("\n");
                    }
                    else if (account instanceof TMBAccount tmb){
                        String overdraft;
                        if (tmb.getOverdraftProtAccount() != null) overdraft = tmb.getOverdraftProtAccount().accountNumber;
                        else overdraft = "";
                        customerBuilder.append(tmb.getClass().getSimpleName()).append("|")
                                .append(tmb.accountNumber).append("|")
                                .append(tmb.getBalance()).append("|")
                                .append(overdraft).append("\n");
                    }
                    else if (account instanceof GDAccount gd){
                        String overdraft;
                        if (gd.getOverdraftProtAccount() != null) overdraft = gd.getOverdraftProtAccount().accountNumber;
                        else overdraft = "";
                        customerBuilder.append(gd.getClass().getSimpleName()).append("|")
                                .append(gd.accountNumber).append("|")
                                .append(gd.getBalance()).append("|")
                                .append(gd.dailyRateFlexible).append("|")
                                .append(overdraft).append("\n");
                    }
                    else if (account instanceof CDAccount cd){
                        customerBuilder.append(cd.getClass().getSimpleName()).append("|")
                                .append(cd.accountNumber).append("|")
                                .append(cd.getBalance()).append("|")
                                .append(cd.fixedRate).append("|")
                                .append(cd.maturityDate).append("|")
                                .append(cd.earlyPenalty).append("\n");
                    }
                } // end of the for loop
                for (Loan loan : customer.payoffList){
                    if (loan instanceof MortgageLoan mgl){
                        customerBuilder.append(mgl.getClass().getSimpleName()).append("|")
                                .append(mgl.id).append("|")
                                .append(mgl.term).append("|")
                                .append(mgl.interest_rate).append("|")
                                .append(mgl.principal).append("\n");
                    }
                    else if (loan instanceof ShortTermLoan stl){
                        customerBuilder.append(stl.getClass().getSimpleName()).append("|")
                                .append(stl.id).append("|")
                                .append(stl.interest_rate).append("|")
                                .append(stl.principal).append("\n");
                    }
                    else if (loan instanceof CreditCard card){
                        customerBuilder.append(card.getClass().getSimpleName()).append("|")
                                .append(card.id).append("|")
                                .append(card.getBalance()).append("|")
                                .append(card.interest_rate).append("|")
                                .append("Current").append("|")
                                .append(card.getIsProblemAccount()).append("|")
                                .append(card.creditLimit).append("\n");
                        for (Transaction transaction : card.transactions){
                            customerBuilder.append("    Transaction|")
                                    .append(card.id).append("|")
                                    .append(transaction.transactionId).append("|")
                                    .append(transaction.amount).append("|")
                                    .append(transaction.description).append("\n");
                        }
                    }
                }
                Writer.write(customerBuilder.toString());
                Writer.write("\n");
            }

            Writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end of try-catch
    }//end of writeCustomersToCsv

    public static void fetchChecksFromCSV(List<Check> Checklist,ArrayListManager<Customer> CustomerList){
        String ID,SenderID,receiverName,status;
        Double amount;
        Customer cust;
        Account acc;
        HashMap<String,Account> accountMap = new HashMap<>();
        HashMap<String,Customer> CustomerMap = new HashMap<>();
        //for each customer
        for (int i = 0; i < CustomerList.getMcount(); i++) {
            cust = CustomerList.getValue(i);
            CustomerMap.put(cust.firstName +" "+cust.lastName,cust);
            //for each customer's owned accounts
            for (int j = 0; j < CustomerList.getValue(i).accountList.size(); j++) {
                acc = CustomerList.getValue(i).accountList.get(j);
                accountMap.put(acc.accountNumber, acc);
            }
        }
        File file = new File("src/checks.csv");
        try (Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNextLine()) {
                String text = fileReader.nextLine();

                String[] formattedText = text.split(",");
                if (formattedText.length ==5) {
                    SenderID = formattedText[0];
                    receiverName = formattedText[1] + " " + formattedText[2];
                    amount = Double.parseDouble(formattedText[3]);
                    status = formattedText[4];

                    Checklist.add(new Check(
                            amount,
                            (accountMap.get(SenderID)),
                            CustomerMap.get(receiverName),
                            status)
                    );
                }


            }
        }catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
    }

    public static void writeChecksToCSV(List<Check> Checklist){
        try{
            FileWriter Writer = new FileWriter("src/checks.csv");
            for (int i = 0; i < Checklist.size(); i++) {
                Writer.write(Checklist.get(i).CheckCSVString());
            }
                Writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
// I really wish I used hashmaps for this, but at this point I'm limit testing myself by getting it to work
