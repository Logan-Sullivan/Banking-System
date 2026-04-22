package Utils;
import Account_Classes.*;
import User_Classes.Customer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

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
    public static void fetchCustsAndAccountsFromCSV(ArrayListManager<Customer> CustomerList) {
        // CS: points to the file to instantiate the customers from.
        File file = new File("src/data.csv"); // changed to use data.csv instead of customers.csv

        try (Scanner fileReader = new Scanner(file)) {
            while (fileReader.hasNextLine()) {
                String text = fileReader.nextLine();
                String[] formattedText = text.split(",");

                Customer customer = (new Customer(formattedText[0], formattedText[1], formattedText[2], formattedText[3], formattedText[4], formattedText[5], formattedText[6]));

                for (int i = 7; i < formattedText.length; i++) {
                    if (formattedText[i].isEmpty()) continue;

                    String[] accountVars = formattedText[i].split("\\|");
                    String accountType = accountVars[0];

                    switch (accountType) {
                        case "SavingsAccount" -> {
                            double rate = Double.parseDouble(accountVars[1]);
                            String freq = accountVars[2];
                            boolean overdraft = accountVars[3].equals(("1"));
                            double balance = Double.parseDouble(accountVars[4]);

                            SavingsAccount savings = new SavingsAccount(rate, freq, overdraft, balance);
                            customer.accountList.add(savings);
                        }
                        case "TMBAccount" -> {
                            double balance = Double.parseDouble(accountVars[1]);
                            int overdraftIdentifier = Integer.parseInt(accountVars[2]);
                            TMBAccount tmb = new TMBAccount(null, balance);
                            tmb.overdraftIdentifier = overdraftIdentifier;
                            customer.accountList.add(tmb);
                        }
                        case "GDAccount" -> {
                            double balance = Double.parseDouble(accountVars[1]);
                            boolean flexible = Boolean.parseBoolean(accountVars[2]);

                            GDAccount gd = new GDAccount(null, balance, flexible);
                            customer.accountList.add(gd);
                        }
                        case "CDAccount" -> {
                            double balance = Double.parseDouble(accountVars[1]);
                            double rate = Double.parseDouble(accountVars[2]);
                            String date = accountVars[3];
                            double penalty = Double.parseDouble(accountVars[4]);

                            CDAccount cd = new CDAccount(balance, rate, null, penalty);
                            customer.accountList.add(cd);
                        }
                    } // End of account switch
                } // end of forloop

                CustomerList.addInOrder(customer);
            }//end of while loop
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }//end of try-catch 1
    }//end of fetchCustsFromCSV

    // CS: gets all TMB accounts in the list and links them to the proper savingsaccount if applicable. I'll make it work for all checkings soon
    public static void handleOverdrafts(ArrayListManager<Customer> CustomerList){
        for (int i = 0; i < CustomerList.getMcount(); i++){
            Customer customer = CustomerList.getValue(i);

            for (Account account : customer.accountList){
                if (account instanceof TMBAccount tmb){
                    int identifier = tmb.overdraftIdentifier;
                    if (identifier >= 0 && identifier < customer.accountList.size()){
                        Account overdraftAccount = customer.accountList.get(identifier);

                        if (overdraftAccount instanceof SavingsAccount saving){
                            tmb.setOverdraftProtAccount(saving);
                        }
                    }
                }
            } // end of for account loop
        } // end of for things in customerlist
    } // end of handleOverdrafts

    // CS: I hate this.
    public static void writeCustomersToCsv(ArrayListManager<Customer> CustomerList) {
        try {
            // CS: writes the updated accounts and otherwise into the CSV file
            FileWriter Writer = new FileWriter("src/data.csv"); // changed to use data.csv instead of customers.csv

            for (int i = 0; i < CustomerList.getMcount(); i++) {
                Customer customer = CustomerList.getValue(i);

                StringBuilder customerBuilder = new StringBuilder();
                customerBuilder.append(customer.customerId).append(",").append(customer.address).append(",").append(customer.city)
                        .append(",").append(customer.state).append(",").append(customer.zipcode).append(",").append(customer.firstName)
                        .append(",").append(customer.lastName);

                for (Account account : customer.accountList){
                    customerBuilder.append(",");

                    if (account instanceof SavingsAccount saving){
                        // Ike: write full savings data so reload matches the parser format
                        customerBuilder.append(saving.getClass().getSimpleName()).append("|")
                                .append(saving.getInterestRate()).append("|")
                                .append(saving.getCompoundFreq()).append("|")
                                .append(saving.isOverdraftBackup() ? "1" : "0").append("|")
                                .append(saving.getBalance());
                    }
                    else if (account instanceof TMBAccount tmb){
                        customerBuilder.append(tmb.getClass().getSimpleName()).append("|")
                                .append(tmb.getBalance()).append("|")
                                .append(tmb.overdraftIdentifier);
                    }
                    else if (account instanceof GDAccount gd){
                        customerBuilder.append(gd.getClass().getSimpleName()).append("|")
                                .append(gd.getBalance()).append("|")
                                .append(gd.dailyRateFlexible);
                    }
                    else if (account instanceof CDAccount cd){
                        customerBuilder.append(cd.getClass().getSimpleName()).append("|")
                                .append(cd.getBalance()).append("|")
                                .append(cd.fixedRate).append("|")
                                .append(cd.maturityDate).append("|")
                                .append(cd.earlyPenalty);
                    }
                } // end of the for loop

                Writer.write(customerBuilder.toString());
                Writer.write("\n");
            }

            Writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end of try-catch
    }//end of writeCustomersToCsv
}
