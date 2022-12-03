public class Order {
    private String id;
    private int nrProduse;
    private int nrProduseFinalizate;

    public Order() {
    }

    public Order(String id, int nrProduse) {
        this.id = id;
        this.nrProduse = nrProduse;
        nrProduseFinalizate = 0;
    }

    public int getNrProduseFinalizate() {
        return nrProduseFinalizate;
    }

    public void setNrProduseFinalizate(int nrProduseFinalizate) {
        this.nrProduseFinalizate = nrProduseFinalizate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNrProduse() {
        return nrProduse;
    }

    public void setNrProduse(int nrProduse) {
        this.nrProduse = nrProduse;
    }
}
