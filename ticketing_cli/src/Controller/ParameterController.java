package Controller;

import SystemParameters.ConfigParameters;

import java.io.IOException;

public class ParameterController {
    public static void displaySystemParameters(ConfigParameters configParameters) throws IOException, InterruptedException {
        int columnWidth1 = 30; // Width for the "Parameter" column
        int columnWidth2 = 20; // Width for the "Value" column
        String lineSeparator = "=".repeat(columnWidth1 + columnWidth2 + 3); // Adjust for column widths and separators
        String columnSeparator = "|";

        System.out.println("\n" + lineSeparator);
        System.out.println(columnSeparator + centerAlign("System Parameters", columnWidth1 + columnWidth2 + 1) + columnSeparator);
        System.out.println(lineSeparator);
        System.out.println(columnSeparator + String.format(" %-32s %-20s ", "Parameter", "Value") + columnSeparator);
        System.out.println(lineSeparator);

        // Table rows with consistent padding
        System.out.println(columnSeparator + String.format(" %-32s %-20s ", " Total Tickets (Non updatable)", configParameters.getTotalTickets()) + columnSeparator);
        System.out.println(lineSeparator);
        System.out.println(columnSeparator + String.format(" %-32s %-20s ", "1) Max Tickets", configParameters.getMaxTickets()) + columnSeparator);
        System.out.println(columnSeparator + String.format(" %-32s %-20s ", "2) Ticket Release Rate", configParameters.getTicketReleaseRate()) + columnSeparator);
        System.out.println(columnSeparator + String.format(" %-32s %-20s ", "3) Customer Retrieval Rate", configParameters.getCustomerRetrievalRate()) + columnSeparator);
        System.out.println(lineSeparator);

        System.out.println();
    }

    private static String centerAlign(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, width - text.length() - padding));
    }
}
