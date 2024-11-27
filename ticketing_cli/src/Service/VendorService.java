package Service;

import SystemParameters.ConfigParameters;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.locks.ReentrantLock;

public class VendorService implements Runnable {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final HttpClient client = HttpClient.newHttpClient();
    private final ConfigParameters configParameters = new ConfigParameters();
    private String task;
    private Integer vendorId;
    private String vendorJson;
    private String vendorEmail;

    public VendorService(String task, Integer vendorId, String vendorJson, String vendorEmail) {
        this.task = task;
        this.vendorId = vendorId;
        this.vendorJson = vendorJson;
        this.vendorEmail = vendorEmail;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            switch (task) {
                case "fetchAll":
                    fetchAllVendors();
                    break;
                case "fetchById":
                    if (vendorId != null) {
                        fetchVendorById(vendorId);
                    }
                    break;
                case "fetchByEmail":
                    if (vendorEmail != null) {
                        fetchVendorIdByEmail(vendorEmail);
                    }
                    break;
                case "save":
                    if (vendorJson != null) {
                        saveVendor(vendorJson);
                    }
                    break;
                case "delete":
                    if (vendorId != null) {
                        deleteVendor(vendorId);
                    }
                    break;
                case "fetchActive":
                    fetchActiveEvents();
                    break;
                default:
                    System.out.println("Invalid task specified.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void fetchActiveEvents() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/event/active"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Active Events:");
                System.out.println(response.body());
            } else {
                System.out.println("Failed to fetch active events. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fetchAllVendors() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/vendor"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body().trim();

            if (jsonResponse.startsWith("[") && jsonResponse.endsWith("]")) {
                jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
            }

            String[] vendors = jsonResponse.split("},\\s*\\{");
            System.out.println("\n----------------------------------------------------------------------------------------");
            System.out.printf("%-15s %-25s %-20s %-20s\n", "Vendor ID", "Vendor Name", "Vendor Contact", "Vendor Email");
            System.out.println("----------------------------------------------------------------------------------------");

            for (String vendor : vendors) {
                vendor = "{" + vendor.replaceAll("^[{\\[]|[}\\]]$", "") + "}";
                String[] fields = vendor.split(",\\s*\"");

                String vendorId = "", vendorName = "", vendorContact = "", vendorEmail = "";
                for (String field : fields) {
                    String[] keyValue = field.split(":\\s*");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].replaceAll("[\"{}]", "").trim();
                        String value = keyValue[1].replaceAll("[\"{}]", "").trim();

                        // Match keys with vendor details
                        switch (key) {
                            case "vendorId":
                                vendorId = value;
                                break;
                            case "vendorName":
                                vendorName = value;
                                break;
                            case "vendorContact":
                                vendorContact = value;
                                break;
                            case "vendorEmail":
                                vendorEmail = value;
                                break;
                        }
                    }
                }

                // Print vendor details in a tabular format
                System.out.printf("%-15s %-25s %-20s %-20s\n", vendorId, vendorName, vendorContact, vendorEmail);
            }
            System.out.println("----------------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchVendorById(int vendorId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/vendor/" + vendorId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Vendor Data by ID: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fetchVendorIdByEmail(String vendorEmail) {
        try {
            // Build and send the HTTP request to get the vendor ID by email
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/vendor/email-id/" + vendorEmail))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Assuming the response body contains only the vendor ID, as an integer (e.g., "1")
            return response.body().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or any error handling logic
        }
    }

    public void saveVendor(String vendorJson) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/vendor"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(vendorJson))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Saved Vendor: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            lock.unlock();
        }
    }

    public Integer getTotalTicketsByVendor(Integer vendorId) {
        try {
            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/vendor/" + vendorId + "/totalTickets"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the response body to an Integer
            if (response.statusCode() == 200) {
                return Integer.parseInt(response.body().trim());
            } else {
                System.err.println("Failed to fetch total tickets. HTTP Status: " + response.statusCode());
                return 0; // Default to 0 if there's an issue
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching total tickets: " + e.getMessage());
            return 0; // Default to 0 if an exception occurs
        }
    }

    public void deleteVendor(int vendorId) {
        lock.lock();
        try {
            Integer totalTickets = getTotalTicketsByVendor(vendorId);
            if (totalTickets == null) {
                totalTickets = 0; // Default to 0 if no tickets are found
            }

            int updatedTotalTickets = totalTickets - Integer.parseInt(configParameters.getTotalTickets());
            configParameters.updateProperty("totalTickets", String.valueOf(updatedTotalTickets));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/vendor/" + vendorId))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Vendor deletion successful.");
            } else {
                System.out.println("Failed to delete vendor. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
