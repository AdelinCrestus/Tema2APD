import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class AngajatLVL1 extends Thread{
    private Scanner scanner;

    private ConcurrentHashMap<String, Integer> orders; // comanda - nr produse nelivrate
    private ConcurrentHashMap<String, Integer> orders_count;

    private ExecutorService tpe;


    public AngajatLVL1(Scanner scanner, ExecutorService tpe) {
        this.scanner = scanner;
        this.orders = new ConcurrentHashMap<>();
        this.orders_count = new ConcurrentHashMap<>();
        this.tpe = tpe;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public ConcurrentHashMap<String, Integer> getOrders_count() {
        return orders_count;
    }

    public void setOrders_count(ConcurrentHashMap<String, Integer> orders_count) {
        this.orders_count = orders_count;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ConcurrentHashMap<String, Integer> getOrders() {
        return orders;
    }

    public void setOrders(ConcurrentHashMap<String, Integer> orders) {
        this.orders = orders;
    }

    public ExecutorService getTpe() {
        return tpe;
    }

    public void setTpe(ExecutorService tpe) {
        this.tpe = tpe;
    }

    @Override
    public void run() {
        boolean urm = false;
        try {
            Tema2.semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        urm = scanner.hasNextLine();
        Tema2.semaphore.release();

        while (urm)
        {
            String str = null;
            try {
                Tema2.semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(scanner.hasNextLine())
            {
                str = scanner.nextLine();
                Tema2.semaphore.release();
                String orderId;
                int numberofProducts = 0;
                StringTokenizer tokenizer = new StringTokenizer(str, ", ");
                if(tokenizer.hasMoreTokens())
                {
                    orderId = tokenizer.nextToken();
                    if(tokenizer.hasMoreTokens())
                    {
                        numberofProducts = Integer.parseInt(tokenizer.nextToken());
                        orders.put(orderId,numberofProducts);
                        orders_count.put(orderId, numberofProducts);
                        for(int i = 0; i < numberofProducts; i++) {
                            Tema2.inPool.incrementAndGet();
                            try {
                                tpe.submit(new AngajatLVL2(this, tpe, orderId, orders));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            else {
                Tema2.semaphore.release();
                urm = false;
            }

        }

        try {
            Tema2.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        boolean closedPool = false;
        while (!Tema2.closedPool.get()) {

            int inPool = Tema2.inPool.get();
            if(inPool == 0)
            {
                closedPool = Tema2.closedPool.compareAndSet(false, true);
            }
            if (closedPool) {
                tpe.shutdown();
            }
            closedPool = false;
        }


        for(Map.Entry<String, Integer> order: orders.entrySet())
        {
            if(order.getValue() == 0 && orders_count.get(order.getKey()) != 0)
            {
                try {
                    Tema2.writingOrdersSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Tema2.printWriterOrders.printf("%s,%d,shipped\n", order.getKey(), orders_count.get(order.getKey()));
                Tema2.writingOrdersSemaphore.release();
            }
        }
    }
}
