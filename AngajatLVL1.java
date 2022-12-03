import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class AngajatLVL1 extends Thread {
    Scanner scanner;
    Scanner scanner2;
    HashMap<String, Integer> orders;
    ConcurrentHashMap<String, Integer> orders_count;
    ExecutorService tpe;

    public AngajatLVL1(Scanner scanner, ExecutorService tpe){
        this.scanner = scanner;
        this.orders = new HashMap<>();
        this.orders_count = new ConcurrentHashMap<>();
        this.tpe = tpe;
    }



    @Override
    public void run() {
        boolean urm = false;
        synchronized (Main.semaphore) {
            urm = scanner.hasNextLine();
        }
        while (urm)
        {
            synchronized (this) {
                try {
                    this.scanner2 = new Scanner(Main.file_products);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String str = null;
            try {
                Main.semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(scanner.hasNextLine()) {
                str = scanner.nextLine();
            }
            else
            {
                break;
            }
            Main.semaphore.release();
            String orderId;
            int numberOfProducts = 0;
            StringTokenizer tokenizer = new StringTokenizer(str, ", ");
            if(tokenizer.hasMoreTokens())
            {
                orderId = tokenizer.nextToken();
                if(tokenizer.hasMoreTokens())
                {
                    numberOfProducts = Integer.parseInt(tokenizer.nextToken());
                    orders.put(orderId, numberOfProducts);
                    orders_count.put(orderId, 0);
                    int i = 0;
                    while (i < numberOfProducts && scanner2.hasNextLine() )
                    {
                        str = scanner2.nextLine();
                        tokenizer = new StringTokenizer(str, ",");
                        String curOrderId = null;
                        if(tokenizer.hasMoreTokens())
                        {
                            curOrderId = tokenizer.nextToken();
                        }
                        if(curOrderId!=null && curOrderId.compareTo(orderId) == 0)
                        {
                            String curProductId = tokenizer.nextToken();
                            i++;
                            Main.inPool.incrementAndGet();
                            tpe.submit(new AngajatLVL2(this, tpe, orderId, curProductId));

                        }
                    }
                }
            }
            synchronized (Main.semaphore)
            {
                urm = scanner.hasNextLine();
            }
        }


        int left = Main.inPool.get();
        if(left == 0)
        {
            tpe.shutdown();
        }
        else
        {
            while (left != 0)
            {
                left = Main.inPool.get();
                if(left == 0)
                {
                    tpe.shutdown();
                }
            }
        }

        for(Map.Entry<String, Integer> order : orders_count.entrySet())
        {
            if(order.getValue() == orders.get(order.getKey()))
            {
                try {
                    Main.writingOrdersSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Main.printWriterOrders.printf("%s,%d,shipped\n", order.getKey(), order.getValue());
                Main.writingOrdersSemaphore.release();
            }
        }

    }
}
