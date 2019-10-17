public class PurchaseModel {
	public int purchaseID;
	public int customerID;
	public int productID;
	public int quantity;
	public String date;


	public String toString() {
		return "(" + purchaseID + "," +
				customerID + "," +
				productID + "," +
				quantity + ')';
	}
}
