import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userID;
    private LocalDate userDateOfReg;
    private double balance;

    public User(String userName, String userEmail, String userPassword) {
        setUserName(userName);
        setUserPassword(userPassword);
        setUserEmail(userEmail);
        this.userDateOfReg = LocalDate.now();
        this.userID = String.valueOf(Instant.now().getEpochSecond());
        this.balance = 0;
    }


    private void setUserName(String userName) {
        this.userName = userName;
    }

    private void setUserEmail(String userEmail) {
        String regex = "^[A-Za-z]+[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userEmail);
        if (matcher.find()) {
            this.userEmail = userEmail;
            //  System.out.println("Valid email address.");

        } else {

            System.out.println("Invalid email address.");
            Administration.registerUser();
            throw new IllegalArgumentException();
        }

    }

    private void setUserPassword(String userPassword) {
        String regex = "(?=.*\\d)(?=.*\\w)(?=.*[!@#&()–\\[{}\\]:;',?\\/*~$^+=<>]).{8,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userPassword);
        if (matcher.find()) {
            //   System.out.println("Password accepted!");
            this.userPassword = userPassword;
        } else {
            System.out.println("Invalid password");
            System.out.println("Your password should be at least 8 characters long and contains at least one digit, one word character, and one special character");
            Administration.registerUser();
            throw new IllegalArgumentException();

        }


    }


    public boolean checkPassword(String userPassword) {
        return this.userPassword.equals(userPassword);
    }

    public boolean checkEmail(String userEmail) {
        return this.userEmail.equals(userPassword);
    }

    public boolean makeDeposit() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter amount to deposit");
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount<=0){
                System.out.println("You can't deposit negative amount. Try again: ");
              return makeDeposit();
            }
            this.balance += amount;
            System.out.println("Deposit was successful. New balance: " + this.balance);
            updateBalance(this.balance);

            return true;
        } catch (Exception e) {

            System.out.println("Incorrect amount");

            return makeDeposit();
        }

    }

    private void updateBalance(double deposit) {

        try {
            String idFilePath = String.format("SA_01_03_2023_Project/users/usersDB/%s.ser", this.userID);
            FileInputStream fileInputStream = new FileInputStream(idFilePath);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            User foundUser = (User) in.readObject();
            foundUser.balance = deposit;

            FileOutputStream fileOutputStream = new FileOutputStream(idFilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(foundUser);
            out.close();
            fileOutputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserID() {
        return userID;
    }

    public LocalDate getUserDateOfReg() {
        return userDateOfReg;
    }

    public double getBalance() {
        return balance;
    }

    public void payToRentBook() {
        this.balance -= 2;
    }
}
