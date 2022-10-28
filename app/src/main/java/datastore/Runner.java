package datastore;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.org.somak.datastore.client.LSMClient;
import com.org.somak.datastore.client.dto.Customer;
import com.org.somak.datastore.client.dto.CustomerKey;
import com.org.somak.datastore.client.dto.Product;
import com.org.somak.datastore.client.dto.ProductKey;
import com.org.somak.datastore.configuration.journal.JournalMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

public class Runner {


    private final String customerData = "C:\\somak\\distributed-data-processing\\datastore\\app\\src\\main\\resources\\datastore\\customer.csv";
    private final String productData = "C:\\somak\\distributed-data-processing\\datastore\\app\\src\\main\\resources\\datastore\\product.csv";
    private final ExecutorService executor = Executors.newFixedThreadPool(20);
    private List<CompletableFuture<String>> cfList = new ArrayList<>();
    private final MetricRegistry metrics = new MetricRegistry();

    private final Timer overAllTimer = metrics.timer(name(Runner.class, "overAll"));
    public void execute() throws IOException {

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(5, TimeUnit.MILLISECONDS);
        reporter.report();

        preCleanUp();
        executeCustomerData();
        executeProductData();

        for (CompletableFuture f : cfList)
            f.join();
        executor.shutdown();
//        context.stop();

    }

    private void executeProductData() {

        Map<ProductKey, Product> cMap = new HashMap<>();
        try (BufferedReader bf = Files.newBufferedReader(Path.of(productData))) {
            int n = 0;
            String line = "";
            System.out.println("reading prod data");
            while ((line = bf.readLine()) != null) {
                if (n != 0 && n % 100 == 0) {
                    Map<ProductKey, Product> map = new HashMap<>();
                    map.putAll(cMap);
                    cMap.clear();
                    System.out.println("Executing product data at n " + n);
                    cfList.add(CompletableFuture.supplyAsync(() -> {

                        for (Map.Entry<ProductKey, Product> entry : map.entrySet()) {
                            Timer.Context context = overAllTimer.time();
                            LSMClient<ProductKey, Product> client = new LSMClient();
                            client.saveData("T_PRODUCT", entry.getKey(), entry.getValue());
                            context.stop();
                        }
                        return "DONE";
                    }, executor));
                }
                String[] array = line.trim().split(",");
                ProductKey key = ProductKey
                        .builder()
                        .productName(array[0])
                        .build();
                Product product = Product
                        .builder()
                        .productDetails(array[1])
                        .price(Double.parseDouble(array[2]))
                        .units(Integer.parseInt(array[3]))
                        .build();

                cMap.put(key, product);
                n++;
            }
            //execute remaining ones
            CompletableFuture.runAsync(() -> {
                for (Map.Entry<ProductKey, Product> entry : cMap.entrySet()) {
                    Timer.Context context = overAllTimer.time();
                    LSMClient<ProductKey, Product> client = new LSMClient();
                    client.saveData("T_PRODUCT", entry.getKey(), entry.getValue());
                    context.stop();
                }
            }, executor);

        } catch (IOException exp) {

        }
    }

    private void executeCustomerData() {

        Map<CustomerKey, Customer> cMap = new HashMap<>();
        try (BufferedReader bf = Files.newBufferedReader(Path.of(customerData))) {
            int n = 0;
            String line = "";
            while ((line = bf.readLine()) != null) {
                if (n != 0 && n % 100 == 0) {
                    Map<CustomerKey, Customer> map = new HashMap<>();
                    map.putAll(cMap);
                    cMap.clear();
                    System.out.println("Executing customer data at n " + n);
                    CompletableFuture.runAsync(() -> {
                        for (Map.Entry<CustomerKey, Customer> entry : map.entrySet()) {
                            LSMClient<CustomerKey, Customer> client = new LSMClient();
                            client.saveData("T_CUSTOMER", entry.getKey(), entry.getValue());
                        }
                    }, executor);
                }
                String[] array = line.trim().split(",");
                CustomerKey key = CustomerKey
                        .builder()
                        .customerId(array[0])
                        .build();
                Customer customer = Customer
                        .builder()
                        .customerBirthDate(array[3])
                        .customerName(array[1])
                        .gender(array[2])
                        .build();

                cMap.put(key, customer);
                n++;
            }
            //execute remaining ones
            CompletableFuture.runAsync(() -> {
                for (Map.Entry<CustomerKey, Customer> entry : cMap.entrySet()) {
                    LSMClient<CustomerKey, Customer> client = new LSMClient();
                    client.saveData("T_CUSTOMER", entry.getKey(), entry.getValue());
                }
            }, executor);

        } catch (IOException exp) {

        }
    }

    public void preCleanUp() throws IOException {

        String customerData = JournalMetadata.getEngineMetaData("JOURNAL_DIR") + "\\T_CUSTOMER";
        String productData = JournalMetadata.getEngineMetaData("JOURNAL_DIR") + "\\T_PRODUCT";
        if (Path.of(customerData).toFile().exists())
            deleteDirectoryTree(customerData);

        if (Path.of(productData).toFile().exists())
            deleteDirectoryTree(productData);

    }

    private void deleteDirectoryTree(String customerData) throws IOException {
        Files.walkFileTree(Path.of(customerData), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                Files.delete(path);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException {
                Files.delete(directory);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
