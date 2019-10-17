public class CustomerModel {
	public int customerID;
	public String name, address, paymentInfo;

	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(customerID).append(",");
		sb.append("\"").append(name).append("\",");
		sb.append("\"").append(address).append("\",");
		sb.append("\"").append(paymentInfo).append("\")");
		return sb.toString();
	}
}
