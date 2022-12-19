import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class AngajatLVL2 implements Runnable {

    private AngajatLVL1 superior;
    private ExecutorService tpe;
    private String idOrder;
    private ConcurrentHashMap<String, Integer> orders;
    private Scanner scanner;

    public AngajatLVL2(AngajatLVL1 superior , ExecutorService tpe, String idOrder, ConcurrentHashMap<String, Integer> orders) throws FileNotFoundException {
        this.superior = superior;
        this.tpe = tpe;
        this.idOrder = idOrder;
        this.orders = orders;
        scanner = new Scanner(Tema2.file_products);
    }

    public ExecutorService getTpe() {
        return tpe;
    }

    public void setTpe(ExecutorService tpe) {
        this.tpe = tpe;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public ConcurrentHashMap<String, Integer> getOrders() {
        return orders;
    }

    public void setOrders(ConcurrentHashMap<String, Integer> orders) {
        this.orders = orders;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void run() {
        boolean productFind = false;
        int nr = 0;
        boolean hasNextLine = true;
        while (!productFind && hasNextLine)
        {
            hasNextLine = scanner.hasNextLine();
            String str = null;
            if (hasNextLine)
            {
                str = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(str, ", ");
                String curOrderId = null;
                if(tokenizer.hasMoreTokens())
                {
                    curOrderId = tokenizer.nextToken();

                    if(curOrderId != null && curOrderId.compareTo(idOrder)  == 0)
                    {
                        String curProductId = tokenizer.nextToken();
                        //StringBuilder stringBuilder = new StringBuilder(curOrderId);
                        //stringBuilder.append(",");
                        //stringBuilder.append(curProductId);
                        try {
                            Tema2.writingProductsSemaphore.acquire();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                            int shippedNrProducts = superior.getOrders_count().get(idOrder) - superior.getOrders().get(idOrder);
                            if(nr >= shippedNrProducts)
                            {
                                Tema2.printWriterProducts.printf("%s,%s,shipped\n",idOrder, curProductId);
                                int val = orders.get(idOrder);
                                orders.put(idOrder, val - 1);
                                productFind = true;
                            }
                            else
                            {
                                nr++;
                            }
                            Tema2.writingProductsSemaphore.release();

                    }
                }

            }
        }
        Tema2.inPool.decrementAndGet();
    }
}
