import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class AngajatLVL2 extends Thread{
    private AngajatLVL1 superior;
    private ExecutorService tpe;
    private String idOrder;
    private String idProduct;

    public AngajatLVL2(AngajatLVL1 superior, ExecutorService tpe, String idOrder, String idProduct) {
        this.superior = superior;
        this.tpe = tpe;
        this.idOrder = idOrder;
        this.idProduct = idProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public AngajatLVL1 getSuperior() {
        return superior;
    }

    public void setSuperior(AngajatLVL1 superior) {
        this.superior = superior;
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


    @Override
    public void run() {
        try {
            Main.writingProductsSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Main.printWriterProducts.printf("%s,%s,shipped\n",idOrder, idProduct );
        Main.writingProductsSemaphore.release();
        int k = superior.getOrders_count().get(idOrder);
        superior.getOrders_count().put(idOrder, k + 1);
        Main.inPool.decrementAndGet();
        synchronized (superior.getScanner())
        {
            superior.getScanner().notifyAll();
        }
    }
}
