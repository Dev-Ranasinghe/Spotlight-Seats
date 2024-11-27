package Controller;

import java.util.Scanner;

public class MenuController {

    static Scanner scanner = new Scanner(System.in);

    public static boolean adminDeleteManagement(String userType) {
        System.out.print("Do you want to delete any " + userType + "? (y/n): ");
        String deleteConfirmation = scanner.nextLine().trim().toLowerCase();

        // Check if input is 'y' or 'n'
        if ("y".equals(deleteConfirmation)) {
            return true; // Return true if the user confirms to delete
        } else if ("n".equals(deleteConfirmation)) {
            return false; // Return false if the user declines
        } else {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            return adminDeleteManagement(userType); // Recursively prompt the user until valid input is received
        }
    }

    public int getNumberInRange(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int number;

        // Loop until the input is within the specified range
        while (true) {
            System.out.print("Enter your choice (valid choices " + min + " - " + max + " ): ");
            String input = scanner.nextLine(); // Get the input as a string

            try {
                // Try to parse the input to an integer
                number = Integer.parseInt(input);

                // Check if the number is within the specified range
                if (number >= min && number <= max) {
                    break; // Exit the loop if the number is within range
                } else {
                    System.out.println("Invalid input. The number must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                // Handle invalid number format (e.g., user entered a non-numeric value)
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        return number;
    }

    public String notNullChecker(String promptMessage, String variable){
        Scanner scanner = new Scanner(System.in);
        String input;
        while(true){
            System.out.print(promptMessage); // Display the custom prompt message
            input = scanner.nextLine();
            if(!input.isEmpty()){
                return input;
            }
            else {
                System.out.println("Empty input rejected for " + variable + ".");
            }
        }
    }

    public String getValidInput(String promptMessage, String validationType) {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Define regex patterns for different validation types
        String stringPattern = "^[a-zA-Z]+$";
        String numberPattern = "^[0-9]+$";
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        while (true) {
            System.out.print(promptMessage); // Display the custom prompt message
            input = scanner.nextLine();

            // Check input based on validation type
            if (validationType.equalsIgnoreCase("string") && input.matches(stringPattern)) {
                return input; // Return if input is a valid string
            } else if (validationType.equalsIgnoreCase("number") && input.matches(numberPattern)) {
                return input; // Return if input is a valid number
            } else if (validationType.equalsIgnoreCase("email") && input.matches(emailPattern)) {
                return input; // Return if input is a valid email
            } else {
                System.out.println("Invalid input. Please enter a valid " + validationType + ".");
            }
        }
    }

    public String getYesOrNo(String message) {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print(message); // Display custom message
            input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return input; // Return true for "y"
            } else if (input.equals("n")) {
                return input; // Return false for "n"
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

}