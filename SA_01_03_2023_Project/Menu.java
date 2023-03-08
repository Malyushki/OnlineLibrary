import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {

    public static void mainMenu() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our Virtual Liberty AI");
        System.out.println("For admin log in enter [a]");
        System.out.println("For user login enter [u]");
        System.out.println("For new registration enter [n]");
        System.out.println("To exit enter [e]");
        String command = scanner.nextLine();
        switch (command.toLowerCase()) {
            case "a":
                if (Administration.logAdmin()) {
                    adminMenu();
                }else {
                    mainMenu();
                }
                break;
            case "u":
                userMenu(Administration.logUser());
                break;
            case "n":
                Administration.registerUser();
                userMenu(Administration.logUser());
                break;
            case "e":
                System.out.println("Thank you for using Virtual Liberty AI");
                System.exit(0);

        }

    }

    public static void adminMenu() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Register new book [r]");
        System.out.println("Delete book [b]");
        System.out.println("Show all users [u]");
        System.out.println("Delete user [d]");
        System.out.println("Return to Main menu [m]");

        String command = scanner.nextLine();
        switch (command.toLowerCase()) {
            case "r":
                Administration.regBook();
                adminMenu();
                break;
            case "b":
                Administration.deleteBook();
                adminMenu();
                break;
            case "u":
                Administration.showAllUsers();
                adminMenu();
                break;
            case "d":
                Administration.deleteUser();
                adminMenu();
                break;
            case "m":
                Menu.mainMenu();
                break;
        }
    }

    public static void userMenu(User user) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome " + user.getUserName() + "!");
        System.out.println("To deposit money enter [d]");
        System.out.println("To search for a book enter [s]");
        System.out.println("Return to Main menu [m]");
        String command = scanner.nextLine();
        switch (command.toLowerCase()) {
            case "d":
                user.makeDeposit();
                userMenu(user);
                break;
            case "s":
                bookMenu(Administration.findBook(), user);
                break;
            case "m":
                Menu.mainMenu();
                break;
        }
    }

    public static void bookMenu(Book book, User user) throws FileNotFoundException {
        System.out.println(book);
        System.out.println("To rent this book enter [r]");
        System.out.println("To search for a new book enter [s]");
        System.out.println("Return to Main menu [m]");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        switch (command.toLowerCase()) {
            case "r":
                if (user.getBalance() >= 2) {
                    user.payToRentBook();
                    System.out.printf("-----%s-----%n",book.getBookName());
                    System.out.println(book.getContent());
                    System.out.println("----------");
                    System.out.println("To go back enter [b]");
                     command = scanner.nextLine();
                    if (command.toLowerCase().equals("b")){
                        userMenu(user);
                    }else {
                        System.exit(0);
                    }
                } else {
                    System.out.println("Insufficient  balance");
                    userMenu(user);
                }
                break;
            case "s":
                bookMenu(Administration.findBook(), user);
                break;
            case "m":
                Menu.mainMenu();
                break;
        }


    }
}
