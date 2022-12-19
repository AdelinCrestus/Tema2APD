import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Tema2 {
    public static AtomicInteger inPool = new AtomicInteger();
    public final static String outOrders = "orders_out.txt";
    public final static String outProducts = "order_products_out.txt";
    public static PrintWriter printWriterProducts;
    public static PrintWriter printWriterOrders;

    public static CyclicBarrier barrier;
    public static File file_products;

    public static Semaphore semaphore;

    public static Semaphore semaphoreLVL2;
    public static Semaphore writingProductsSemaphore;
    public static Semaphore writingOrdersSemaphore;

    public static AtomicBoolean closedPool = new AtomicBoolean(false);

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
        semaphoreLVL2 = new Semaphore(1);
        writingProductsSemaphore = new Semaphore(1);
        writingOrdersSemaphore = new Semaphore(1);
        barrier = new CyclicBarrier(numberOfThreads);;
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
        for(int i = 0; i < numberOfThreads; i++)
        {
            threads[i].join();
        }


        printWriterOrders.close();
        printWriterProducts.close();

    }
}