import java.util.Scanner;
import java.time.*;
import java.sql.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;





public class Main {
    static String dataURL = "jdbc:postgresql://localhost:5432/BookData";

    static Boolean running = true;


    public static String setDate() {
        String date = "";
        Scanner addYear = new Scanner(System.in);
        System.out.println("YYYY: ");
        int year = addYear.nextInt();

        if (year >= 1900 && year <= 2023) {
            Scanner addMonth = new Scanner(System.in);
            System.out.println("MM: ");
            int month = addMonth.nextInt();

            if (month >= 1 && month <= 12) {

                Scanner addDay = new Scanner(System.in);
                System.out.println("DD: ");
                int day = addDay.nextInt();

                if (day >= 1 && day <= 31) {
                    date = "" + year  + month + day + "";



                } else {
                    System.out.println("Invalid Day");
                }
            } else {
                System.out.println("Invalid Month");
            }
        } else {
            System.out.println("Invalid Year");
        }
    return date;
    }
    public static void addBook(){
        Scanner addBook = new Scanner(System.in);

        System.out.println("Book Title: ");
        String book = addBook.nextLine();

        if (book != "") {

            try {
                Date date = Date.valueOf(LocalDate.now());

                Connection table = DriverManager.getConnection(dataURL);

                String checkIfBookExistsSql = "SELECT * FROM book_log WHERE book_title = ?";
                PreparedStatement checkIfBookExistsPstmt = table.prepareStatement(checkIfBookExistsSql);
                checkIfBookExistsPstmt.setString(1,book);

                var results = checkIfBookExistsPstmt.executeQuery();
                if (results.isBeforeFirst()) {
                    System.out.println("Book has already been marked as for today!");
                    return;
                }

                String sql = "INSERT INTO book_log (book_title, date_finished) VALUES((SELECT title FROM book WHERE title = ?), ?)";
                PreparedStatement pstmt = table.prepareStatement(sql);

                pstmt.setString(1,book);
                pstmt.setDate(2,date);
                pstmt.executeUpdate();
            }catch (SQLException error){
                System.out.println("That title is not on your bookshelf.");
                System.out.println("");
            };


        } else {
            System.out.println("Title is empty.");
        }
    }

    public static void deleteBook(){
        Scanner addBook = new Scanner(System.in);
        System.out.println("Book Title to be deleted: ");
        System.out.println("** Please note: titles must match exactly to be deleted. **");
        String book = addBook.nextLine();
        if (book != ""){
            try {
                Connection table = DriverManager.getConnection(dataURL);
                String sql = "DELETE FROM book_log WHERE book_title = ?";
                PreparedStatement pstmt = table.prepareStatement(sql);
                pstmt.setString(1, book);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println(book + " was successfully deleted.");
                } else { System.out.println(book + " does not exist.");};

            }catch (SQLException error){
                System.out.println(error.getMessage());
            };
        } else {
            System.out.println("Title is empty.");
        }
    }
    public static void addOldBook(){
        Scanner addBook = new Scanner(System.in);
        System.out.println("** If you enter a title you have already read, the date will update in the system. **");
        System.out.println("Book Title: ");
        String book = addBook.nextLine();

        if (book != "") {

            try {
                String date = setDate();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate convert = LocalDate.parse(date, dtf);
                Date date2 = Date.valueOf(convert);


                if (date != null) {
                    try {
                        Connection table = DriverManager.getConnection(dataURL);
                        String sql = "INSERT INTO book_log (book_title, date_finished) VALUES((SELECT title FROM book WHERE title = ?), ?)";
                        PreparedStatement pstmt = table.prepareStatement(sql);

                        pstmt.setString(1, book);
                        pstmt.setDate(2, date2);
                        pstmt.executeUpdate();
                    } catch (SQLException error) {
                        System.out.println("That book isn't on your bookshelf.");
                        System.out.println("");
                    }
                }

            } catch (Exception e) {
                System.out.println("Invalid date format.");

            }
        } else {
            System.out.println("Title is empty.");
        }
    }

    public static void library(){
        try {
            Connection table = DriverManager.getConnection(dataURL);
            String SQL = "SELECT title, book.author_id,authors.author_id, author_name, genre FROM book INNER JOIN authors ON book.author_id = authors.author_id";
            PreparedStatement pstmt = table.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("");
            System.out.println("Your Library:");
            if (!rs.isBeforeFirst() ) {
                System.out.println("is empty.");
                System.out.println("");
                return;

            }

            while (rs.next()) {
                System.out.println(rs.getString("title")+" by "+rs.getString("author_name")+ ": " + rs.getString("genre")+"." );

            }
            System.out.println("");
        }catch (SQLException error){
            System.out.println(error.getMessage());
        }
    }

    public static void addLibrary(){
        Scanner addTitle = new Scanner(System.in);
        System.out.println("Book Title: ");
        String book = addTitle.nextLine();

        if (book != "") {
            Scanner addAuthor = new Scanner(System.in);
            System.out.println("Author: ");
            String author = addAuthor.nextLine();
            if (author != "") {
                Scanner addGenre = new Scanner(System.in);
                System.out.println("Genre: ");
                String genre = addGenre.nextLine();
                if (genre != "") {
                    try {
                        Connection table = DriverManager.getConnection(dataURL);
                        String SQL = "INSERT INTO authors (author_name) VALUES (?) ON CONFLICT (author_name) DO UPDATE SET author_name = ?";
                        PreparedStatement pstmt = table.prepareStatement(SQL);
                        pstmt.setString(1, author);
                        pstmt.setString(2, author);
                        pstmt.executeUpdate();

                        String SQL2 = "INSERT INTO book (title, author_id, genre) VALUES (?, (SELECT author_id FROM authors WHERE author_name = ?), ?)";
                        PreparedStatement pstmt2 = table.prepareStatement(SQL2);
                        pstmt2.setString(1, book);
                        pstmt2.setString(2, author);
                        pstmt2.setString(3, genre);
                        pstmt2.executeUpdate();

                    }catch (SQLException error){
                        System.out.println(error.getMessage());
                    }

                } else {
                    System.out.println("Genre is empty.");
                }
            } else {
                System.out.println("Author is empty.");
            }

        } else {
            System.out.println("Title is empty.");
        }
    }
    public static void deleteLibrary(){
        Scanner addBook = new Scanner(System.in);
        System.out.println("Book Title to be deleted: ");
        System.out.println("** Please note: titles must match exactly to be deleted. **");
        String book = addBook.nextLine();
        if (book != ""){
            try {
                Connection table = DriverManager.getConnection(dataURL);
                String sql = "DELETE FROM book WHERE title = ?";
                PreparedStatement pstmt = table.prepareStatement(sql);
                pstmt.setString(1, book);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println(book + " was successfully deleted.");
                } else { System.out.println(book + " does not exist.");};

            }catch (SQLException error){
                System.out.println(error.getMessage());
            };
        } else {
            System.out.println("Title is empty.");
        }
    }
    public static void print(){
        try {
            Connection table = DriverManager.getConnection(dataURL);
            String SQL = "SELECT * FROM book_log";
            PreparedStatement pstmt = table.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst() ) {
                System.out.println("You haven't read any books yet.");
                System.out.println("");
                return;
            }
            while (rs.next()) {
                System.out.println("You finished " + rs.getString("book_title") + " on " + rs.getDate("date_finished") + ".");

                }
            System.out.println("");

        }catch (SQLException error){
            System.out.println(error.getMessage());
        }

    }

    public static void viewIncomplete(){
        try {
            Connection table = DriverManager.getConnection(dataURL);
            String SQL = "SELECT * FROM book WHERE book.title NOT IN (SELECT book_log.book_title FROM book_log)";
            PreparedStatement pstmt = table.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst() ) {
                System.out.println("You've read all your books!!.");
                System.out.println("");
                return;

            }
            while (rs.next()) {
                System.out.println(rs.getString("title"));

            }
            System.out.println("");

        }catch (SQLException error){
            System.out.println(error.getMessage());
        }

    }



    public static void main(String[] args) {


        System.out.println("Welcome to Book Diary");
        System.out.println("Your personal book logging system.");
        System.out.println("");


    while(running) {
        Scanner menu = new Scanner(System.in);
        System.out.println("[1] View your bookshelf."
                + "\n[2] Add a book to your bookshelf."
                + "\n[3] Log a book completed today."
                + "\n[4] Log a previously completed book."
                + "\n[5] View all completed books."
                + "\n[6] View all incomplete books."
                + "\n[7] Delete a book from log."
                + "\n[8] Delete a book from your bookshelf."
                + "\n[9] Quit");
        String choice = menu.nextLine();

        if (choice.equals("3")){
            addBook();
        } else if (choice.equals("9")) {
            System.out.println("Thank you for using Book Diary.");
            System.out.println("Happy Reading");
            running = false;
        } else if (choice.equals("4")) {
            addOldBook();
        } else if (choice.equals("5")) {
           print();
        } else if (choice.equals("7")) {
            deleteBook();
        }
        else if (choice.equals("6")) {
            viewIncomplete();
        }else if (choice.equals("1")) {
            library();
        } else if (choice.equals("2")) {
            addLibrary();
        }
        else if (choice.equals("8")) {
            deleteLibrary();
        }else {
            System.out.println("Invalid Menu Choice");
        }
    }

        }
    }



