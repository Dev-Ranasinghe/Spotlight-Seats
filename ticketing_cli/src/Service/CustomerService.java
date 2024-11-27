package Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.locks.ReentrantLock;

public class CustomerService implements Runnable {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final HttpClient client = HttpClient.newHttpClient();
    private String task;
    private Integer customerId;
    private String customerJson;

    public CustomerService(String task, Integer customerId, String customerJson) {
        this.task = task;
        this.customerId = customerId;
        this.customerJson = customerJson;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            switch (task) {
                case "fetchAll":
                    fetchAllCustomers();
                    break;
                case "fetchById":
                    if (customerId != null) {
                        fetchCustomerById(customerId);
                    }
                    break;
                case "save":
                    if (customerJson != null) {
                        saveCustomer(customerJson);
                    }
                    break;
                case "delete":
                    if (customerId != null) {
                        deleteCustomer(customerId);
                    }
                    break;
                default:
                    System.out.println("Invalid task specified.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void fetchAllCustomers() {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/customer"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body().trim();

            if (jsonResponse.startsWith("[") && jsonResponse.endsWith("]")) {
                jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
            }

            String[] customers = jsonResponse.split("},\\s*\\{");
            System.out.println("\n----------------------------------------------------------------------------------------");
            System.out.printf("%-15s %-25s %-20s %-20s\n", "Customer ID", "Customer Name", "Customer Contact", "Customer Email");
            System.out.println("----------------------------------------------------------------------------------------");

            for (String customer : customers) {
                customer = "{" + customer.replaceAll("^[{\\[]|[}\\]]$", "") + "}";
                String[] fields = customer.split(",\\s*\"");

                String customerId = "", customerName = "", customerContact = "", customerEmail = "";
                for (String field : fields) {
                    String[] keyValue = field.split(":\\s*");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].replaceAll("[\"{}]", "").trim();
                        String value = keyValue[1].replaceAll("[\"{}]", "").trim();

                        // Match keys with customer details
                        switch (key) {
                            case "customerId":
                                customerId = value;
                                break;
                            case "customerName":
                                customerName = value;
                                break;
                            case "customerContact":
                                customerContact = value;
                                break;
                            case "customerEmail":
                                customerEmail = value;
                                break;
                        }
                    }
                }

                // Print customer details in a tabular format
                System.out.printf("%-15s %-25s %-20s %-20s\n", customerId, customerName, customerContact, customerEmail);
            }
            System.out.println("----------------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void fetchCustomerById(int customerId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/customer/" + customerId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Customer Data: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void saveCustomer(String customerJson) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/customer"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(customerJson))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Saved Customer: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void deleteCustomer(int customerId) {
        lock.lock();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/customer/" + customerId))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Customer deletion success.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


//package Service;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class CustomerService {
//
//    private static final ReentrantLock lock = new ReentrantLock();
//    private static final HttpClient client = HttpClient.newHttpClient();
//
//    public void fetchAllCustomers() {
//        lock.lock();
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:8080/api/customer"))
//                    .GET()
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            String jsonResponse = response.body().trim();
//
//            if (jsonResponse.startsWith("[") && jsonResponse.endsWith("]")) {
//                jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
//            }
//
//            String[] customers = jsonResponse.split("},\\s*\\{");
//            System.out.println("\n");
//            for (String customer : customers) {
//                customer = "{" + customer.replaceAll("^[{\\[]|[}\\]]$", "") + "}";
//                System.out.println("Customer Details =>");
//
//                String[] fields = customer.split(",\\s*\"");
//                for (String field : fields) {
//                    String[] keyValue = field.split(":\\s*");
//                    if (keyValue.length == 2) {
//                        String key = keyValue[0].replaceAll("[\"{}]", "").trim();
//                        String value = keyValue[1].replaceAll("[\"{}]", "").trim();
//                        System.out.println(key + ": " + value);
//                    }
//                }
//                System.out.println();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void fetchCustomerById(int customerId) {
//        lock.lock();
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:8080/api/customer/" + customerId))
//                    .GET()
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("Customer Data: " + response.body());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void saveCustomer(String customerJson) {
//        lock.lock();
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:8080/api/customer"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(customerJson))
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("Saved Customer: " + response.body());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void deleteCustomer(int customerId) {
//        lock.lock();
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("http://localhost:8080/api/customer/" + customerId))
//                    .DELETE()
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("Deleted Customer: " + response.body());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//    }
//}
