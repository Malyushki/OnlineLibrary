import java.io.*;
import java.util.ArrayDeque;
import java.util.Scanner;

public class Administration {

    public static boolean logAdmin() {
        Scanner scannerInput = new Scanner(System.in);
        System.out.println("Enter admin name");
        String adminName = scannerInput.nextLine();
        System.out.println("Enter admin password");
        String adminPass = scannerInput.nextLine();

        String pathToAdminFile = "SA_01_03_2023_Project/users/admins.txt";
        try {
            File adminsFile = new File(pathToAdminFile);
            Scanner scanner = new Scanner(adminsFile);
            while (scanner.hasNextLine()) {
                String[] readAdminInfo = scanner.nextLine().split(",");
                if (readAdminInfo[0].equals(adminName) && readAdminInfo[1].equals(adminPass)) {
                    System.out.println("Successful log in");
                    System.out.printf("Welcome %s.%n",adminName);
                    return true;

                }
            }
            System.out.println("Unsuccessful log in");

            return false;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerUser() {
        try {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter your username");
        String userName = userInput.nextLine();
        System.out.println("Enter your email");
        String userMail = userInput.nextLine();
        System.out.println("Enter your password");
        String userPass = userInput.nextLine();

        User user = new User(userName, userMail, userPass);


        String pathToUsers = "SA_01_03_2023_Project/users/usersDB/";
        String fileName = String.format("%s.ser", user.getUserID());

            FileOutputStream fileOutputStream = new FileOutputStream(pathToUsers + fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(user);
            out.close();
            fileOutputStream.close();
            // System.out.printf("Serialized data is saved in " + fileName + System.lineSeparator());


            File file = new File("SA_01_03_2023_Project/users/users.txt");
            try (FileWriter fileWriter = new FileWriter(file, true);
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getUserID()).append(",").append(user.getUserName()).append(System.lineSeparator());
                writer.write(String.valueOf(sb));
                writer.flush(); // flush the buffer to ensure data is written to disk
                file.exists();
            }

            System.out.println("Your registration was successful");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static User logUser() {
        try {
            Scanner userInput = new Scanner(System.in);

            System.out.println("Enter your username");
            String userName = userInput.nextLine();

            System.out.println("Enter your password");
            String userPass = userInput.nextLine();

            String pathToUserFile = "SA_01_03_2023_Project/users/users.txt";
            File userFile = new File(pathToUserFile);
            Scanner scanner = new Scanner(userFile);
            String userID = null;
            while (scanner.hasNextLine()) {
                String[] readUsersInfo = scanner.nextLine().split(",");
                if (readUsersInfo[1].equals(userName)) {
                    userID = readUsersInfo[0];

                }
            }
            if (userID == null) {
                System.out.println("Unsuccessful log in. Username doesn't exist");

                return logUser();
            } else {
                System.out.println(userID);
                String idFilePath = String.format("SA_01_03_2023_Project/users/usersDB/%s.ser", userID);
                FileInputStream fileInputStream = new FileInputStream(idFilePath);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
               // System.out.println(in);
                User currentUser = (User) in.readObject();


                if (currentUser.checkPassword(userPass)) {
                    System.out.println("Successful login");
                    return currentUser;
                } else {

                    System.out.println("Wrong password");
                    fileInputStream.close();
                    in.close();

                    return  logUser();
                }
            }


        } catch (IOException | ClassNotFoundException e) {

            throw new RuntimeException(e);
        }


    }

    public static void regBook() {

        Scanner bookInput = new Scanner(System.in);
        System.out.println("Enter book ISBN");
        String isbnBook = bookInput.nextLine();
        System.out.println("Enter book title");
        String titleBook = bookInput.nextLine();
        System.out.println("Enter book author");
        String authorBook = bookInput.nextLine();
        System.out.println("Enter book content");
        String contentBook = bookInput.nextLine();
        Book book = new Book(isbnBook, titleBook, authorBook, contentBook);


        String pathToBooks = "SA_01_03_2023_Project/catalogue/all_books/";
        String fileName = String.format("%s.ser", book.getIdISBN());
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(pathToBooks + fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(book);



            // System.out.printf("Serialized data is saved in " + fileName + System.lineSeparator());


            File file = new File("SA_01_03_2023_Project/catalogue/catalogue.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            StringBuilder sb = new StringBuilder();
            sb.append(book.getBookName()).append(",").append(book.getIdISBN()).append(System.lineSeparator());

            writer.write(sb.toString());
            writer.close();
            out.close();
            fileOutputStream.close();

            System.out.println("New book registration was successful");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBook() {
        try {
            Scanner adminInput = new Scanner(System.in);

            System.out.println("Enter book ISBN");
            String isbnBook = adminInput.nextLine();
            String idFilePath = String.format("SA_01_03_2023_Project/catalogue/all_books/%s.ser", isbnBook);
            File fileToDelete = new File(idFilePath);
            if (fileToDelete.exists()) {
                fileToDelete.deleteOnExit();
            } else {
                System.out.println("There isn't such book in the catalogue");
                deleteBook();
            }

            File catalogue = new File("SA_01_03_2023_Project/catalogue/catalogue.txt");
            Scanner fileReader = new Scanner(catalogue);
            String lineToDelete = null;
            ArrayDeque<String> catalogueInfoQueue = new ArrayDeque<>();
            while (fileReader.hasNextLine()) {
                String readCataloguesLines = fileReader.nextLine();
                String isbnFromCatalogue = readCataloguesLines.split(",")[1];
                if (!isbnFromCatalogue.equals(isbnBook)) {
                    catalogueInfoQueue.offer(readCataloguesLines);
                }
            }

            PrintWriter writer = new PrintWriter(catalogue);

            fileReader.close();
            while (!catalogueInfoQueue.isEmpty()){
                writer.write(catalogueInfoQueue.poll()+System.lineSeparator());
            }
            writer.close();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteUser() {
        try {
            Scanner adminInput = new Scanner(System.in);

            System.out.println("Enter username");
            String userName = adminInput.nextLine();

            //Find object

            String pathToUserFile = "SA_01_03_2023_Project/users/users.txt";
            File userFile = new File(pathToUserFile);
            Scanner scanner = new Scanner(userFile);
            String userID = null;
            while (scanner.hasNextLine()) {
                String[] readUsersInfo = scanner.nextLine().split(",");
                if (readUsersInfo[1].equals(userName)) {
                    userID = readUsersInfo[0];

                }
            }
            if (userID == null) {
                System.out.println("Username doesn't exist");
                return deleteUser();
            } else {

                String userToDeletePath = String.format("SA_01_03_2023_Project/users/usersDB/%s.ser", userID);
                File fileToDelete = new File(userToDeletePath);

                if (fileToDelete.exists()) {
                    fileToDelete.deleteOnExit();
                } else {
                    System.out.println("There isn't such user in the database");
                    return deleteUser();
                }

                File database = new File("SA_01_03_2023_Project/users/users.txt");
                Scanner fileReader = new Scanner(database);
                String lineToDelete = null;
                ArrayDeque<String> userDBQueue = new ArrayDeque<>();
                while (fileReader.hasNextLine()) {
                    String readDatabaseLines = fileReader.nextLine();
                    String idUserFromDatabase = readDatabaseLines.split(",")[0];
                    if (!idUserFromDatabase.equals(userID)) {
                        userDBQueue.offer(readDatabaseLines);

                    }
                }
                fileReader.close();
                PrintWriter writer = new PrintWriter(database);
                while (!userDBQueue.isEmpty()){
                    writer.write(userDBQueue.poll()+System.lineSeparator());
                }


                writer.close();

                System.out.printf("Username %s deleted successful.",userName);
                System.out.println("-----------------");

                return true;


            }
        } catch (IOException e) {
            deleteUser();
            throw new RuntimeException(e);
        }
    }

    public static void showAllUsers(){

        try {
            String pathToUserFile = "SA_01_03_2023_Project/users/users.txt";
            File userFile = new File(pathToUserFile);
            Scanner scanner = new Scanner(userFile);
            int countUsers = 1;
            while (scanner.hasNextLine()){
                System.out.printf("%d %s%n",countUsers,scanner.nextLine().split(",")[1]);
                countUsers++;
            }
            System.out.println("-----------");
            scanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Book findBook() throws FileNotFoundException {
        try {
            Scanner userInput = new Scanner(System.in);

            System.out.println("Enter book title");
            String bookTitle = userInput.nextLine();

            String pathToCatalogue = "SA_01_03_2023_Project/catalogue/catalogue.txt";
            File catalogue = new File(pathToCatalogue);
            Scanner scanner = new Scanner(catalogue);
            String bookID = null;

            while (scanner.hasNextLine()) {
                String[] readUsersInfo = scanner.nextLine().split(",");
                if (readUsersInfo[0].equals(bookTitle)) {
                    bookID = readUsersInfo[1];

                }
            }
            if (bookID == null) {
                System.out.println("Book doesn't exist");

                return findBook();
            } else {
                String idFilePath = String.format("SA_01_03_2023_Project/catalogue/all_books/%s.ser", bookID);
                FileInputStream fileInputStream = new FileInputStream(idFilePath);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
                Book foundBook = (Book) in.readObject();
                fileInputStream.close();
                in.close();

                System.out.println("Success");
                return foundBook;
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }


}

