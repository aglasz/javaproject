import java.util.Scanner;

public class oldDate {
    String date;
    public String setDate() {

        Scanner addYear = new Scanner(System.in);
        System.out.println("YYYY: ");
        int year = addYear.nextInt();

        if (year >= 1000 && year <= 2023) {
            Scanner addMonth = new Scanner(System.in);
            System.out.println("MM: ");
            int month = addMonth.nextInt();

            if (month >= 1 && month <= 12) {

                Scanner addDay = new Scanner(System.in);
                System.out.println("DD: ");
                int day = addDay.nextInt();

                if (day >= 1 && day <= 31) {
                    this.date = "" + year  + month + day + "";


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
}

