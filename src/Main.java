import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;


public class Main {
    static LocalDate now = LocalDate.now();
    static HashMap<String, String> library = new HashMap<String, String>();
    static Boolean running = true;
    public static void addBook(){
        Scanner addBook = new Scanner(System.in);
        System.out.println("** If you enter a title you have already read, the date will update in the system. **");
        System.out.println("Book Title: ");
        String book = addBook.nextLine();

        if (book != "") {
            String date = "" +now +"";
            library.put(book, date);
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
            library.remove(book);
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
                Date oldDate = new Date();
                String validDate = oldDate.setDate();
                if (validDate != null){
                    library.put(book, validDate);
                }

            } catch (Exception e) {
                System.out.println("Invalid date format.");
            }
        } else {
            System.out.println("Title is empty.");
        }
    }
    public static void print(){
        if (library.size() == 0) {
            System.out.println("You haven't read any books yet.");
        } else {
            for (String i : library.keySet()) {
                System.out.println("Title: " + i);
                System.out.println("Completed Date: " + library.get(i));
                System.out.println();
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Book Diary");
        System.out.println("Your personal book logging system.");

    while(running) {
        Scanner menu = new Scanner(System.in);
        System.out.println("[C]omplete a book today, [L]og a past completed book, [V]iew all completed books, [D]elete a book, [Q]uit");
        String choice = menu.nextLine().toUpperCase();

        if (choice.equals("C")){
            addBook();
        } else if (choice.equals("Q")) {
            System.out.println("Thank you for using Book Diary.");
            System.out.println("Happy Reading");
            running = false;
        } else if (choice.equals("L")) {
            addOldBook();
        } else if (choice.equals("V")) {
           print();
        } else if (choice.equals("D")) {
            deleteBook();
        }else {
            System.out.println("Invalid Menu Choice");
        }
    }

        }
    }
