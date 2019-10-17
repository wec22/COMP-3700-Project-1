public class TXTReceiptBuilder implements IReceiptBuilder {

    StringBuilder sb = new StringBuilder();

    @Override
    public void appendHeader(String header) {
        sb.append(header).append("\n");
    }

    @Override
    public void appendCustomer(CustomerModel customer) {
        sb.append("Customer ID: ").append(customer.customerID).append("\n");
        sb.append("Customer Name: ").append(customer.name).append("\n");
    }

    @Override
    public void appendProduct(ProductModel product) {
        sb.append("Product ID: ").append(product.productID).append("\n");
        sb.append("Product Name: ").append(product.name).append("\n");
    }

    @Override
    public void appendPurchase(PurchaseModel purchase) {
        ProductModel product = StoreManager.getDataAdapter().loadProduct(purchase.productID);

        Double cost = purchase.quantity * product.price;
        Double tax = cost * 0.09;
        Double total = cost + tax;

        sb.append("Quantity: ").append(purchase.quantity).append("\n");
        sb.append("Cost: ").append(String.format("%8.3f", cost)).append("\n");
        sb.append("Tax: ").append(String.format("%8.3f", tax)).append("\n");
        sb.append("Total: ").append(String.format("%8.3f", total)).append("\n");
    }

    @Override
    public void appendFooter(String footer) {
        sb.append(footer).append("\n");
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
