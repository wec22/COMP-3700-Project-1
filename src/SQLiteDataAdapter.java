import java.sql.*;

public class SQLiteDataAdapter implements IDataAdapter {

	Connection conn = null;

	public int connect(String dbPath) {
		try {
			// db parameters
			String url = "jdbc:sqlite:" + dbPath;
			// create a connection to the database
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return CONNECTION_OPEN_FAILED;
		}
		return CONNECTION_OPEN_OK;
	}

	@Override
	public int disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return CONNECTION_CLOSE_FAILED;
		}
		return CONNECTION_CLOSE_OK;
	}

	public ProductModel loadProduct(int productID) {
		ProductModel product = new ProductModel();

		try {
			String sql = "SELECT ProductId, Name, Price, Quantity FROM Products WHERE ProductId = " + productID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			product.productID = rs.getInt("ProductId");
			product.name = rs.getString("Name");
			product.price = rs.getDouble("Price");
			product.quantity = rs.getDouble("Quantity");


		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return product;
	}

	public int saveProduct(ProductModel product) {
		try {
			String sql = "INSERT INTO Products(ProductId, Name, Price, Quantity) VALUES " + product;
			System.out.println(sql);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			String msg = e.getMessage();
			System.out.println(msg);
			if (msg.contains("UNIQUE constraint failed"))
				return PRODUCT_DUPLICATE_ERROR;
		}

		return PRODUCT_SAVED_OK;
	}

	public CustomerModel loadCustomer(int customerID){
		CustomerModel customer = new CustomerModel();

		try {
			String sql = "SELECT CustomerId, Name, Address, PaymentInfo FROM Customers WHERE CustomerId = " + customerID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			customer.customerID = rs.getInt("CustomerId");
			customer.name = rs.getString("Name");
			customer.address = rs.getString("Address");
			customer.paymentInfo = rs.getString("PaymentInfo");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return customer;
	}

	public int saveCustomer(CustomerModel customer) {
		try {
			String sql = "INSERT INTO Customers(CustomerId, Name, Address, PaymentInfo) VALUES" + customer;
			System.out.println(sql);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);

		} catch (Exception e) {
			String msg = e.getMessage();
			System.out.println(msg);
			if (msg.contains("UNIQUE constraint failed"))
				return CUSTOMER_DUPLICATE_ERROR;
		}

		return CUSTOMER_SAVED_OK;
	}

	@Override
	public PurchaseModel loadPurchase(int id) {
		PurchaseModel purchase = new PurchaseModel();

		try {
			String sql = "SELECT PurchaseID, CustomerID, ProductID, Quantity, Date FROM Purchases WHERE PurchaseId = " + id;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			purchase.purchaseID = rs.getInt("PurchaseId");
			purchase.customerID = rs.getInt("CustomerID");
			purchase.productID = rs.getInt("ProductID");
			purchase.quantity = rs.getInt("Quantity");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return purchase;
	}

	@Override
	public int savePurchase(PurchaseModel model) {
		try {
			String sql = "INSERT INTO Purchases(PurchaseID, CustomerID, ProductID, Quantity) VALUES" + model;

			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch(Exception e) {
			String msg = e.getMessage();
			System.out.println(msg);
			if (msg.contains("UNIQUE constraint failed"))
				return PURCHASE_DUPLICATE_ERROR;
		}
		return PURCHASE_SAVED_OK;
	}
}
