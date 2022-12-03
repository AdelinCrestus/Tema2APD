public class Product {
    private String idOrder;
    private String idProduct;

    public Product() {
    }

    public Product(String idOrder, String idProduct) {
        this.idOrder = idOrder;
        this.idProduct = idProduct;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
}
