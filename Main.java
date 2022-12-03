import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static Semaphore semaphore;
    public static Semaphore writingProductsSemaphore;
    public static Semaphore writingOrdersSemaphore;
    public static File file_products;
    public static AtomicInteger inPool = new AtomicInteger();
    public final static String outOrders = "orders_out.txt";
    public final static String outProducts = "order_products_out.txt";
    public static PrintWriter printWriterProducts;
    public static PrintWriter printWriterOrders;


    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        inPool.set(0);
        File file_order = new File(args[0] + "/orders.txt");
        file_products = new File(args[0] + "/order_products.txt");
        File fileOutProducts = new File(outProducts);
        printWriterProducts = new PrintWriter(fileOutProducts);
        printWriterOrders = new PrintWriter(outOrders);
        int numberOfThreads = Integer.parseInt(args[1]);
        Scanner scanner_order = new Scanner(file_order);
        Thread[] threads = new Thread[numberOfThreads];
        ExecutorService tpe = Executors.newFixedThreadPool(numberOfThreads);
        semaphore = new Semaphore(1);
        writingProductsSemaphore = new Semaphore(1);
        writingOrdersSemaphore = new Semaphore(1);
        for(int i = 0; i < numberOfThreads; i++)
        {
            threads[i] = new AngajatLVL1(scanner_order, tpe);
            threads[i].start();
        }

        for(int i = 0; i < numberOfThreads; i++)
        {
            try {
                threads[i].join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        printWriterOrders.close();
        printWriterProducts.close();
    }
}
