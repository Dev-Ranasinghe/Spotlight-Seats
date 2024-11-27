package Controller;

import java.util.Scanner;

public class AdminController {

    public boolean adminVerification() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Admin username: ");
            String adminUsername = scanner.nextLine();
            System.out.print("Admin password: ");
            String adminPassword = scanner.nextLine();
            if (!adminUsername.equals("l")) {
                throw new Exception("Invalid Username !!!");
            }
            if (!adminPassword.equals("l")) {
                throw new Exception("Invalid Password !!!");
            }
            System.out.println("Log-in Success as an Admin :)");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
