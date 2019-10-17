public class ProductModel {
	public int productID;
	public String name, vendor, description;
	public double price, quantity;

	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(productID).append(",");
		sb.append("\"").append(name).append("\"").append(",");
		sb.append(price).append(",");
		sb.append(quantity).append(")");
		return sb.toString();
	}
}
