import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

public class AddPurchaseUI {
	public JFrame view;

	public JButton btnAdd = new JButton("Add");
	public JButton btnCancel = new JButton("Cancel");

	public JTextField txtProductID = new JTextField(20);
	public JTextField txtCustomerID = new JTextField(20);
	public JTextField txtPurchaseID = new JTextField(20);
	public JTextField txtQuantity = new JTextField(20);

	public JLabel lblPrice = new JLabel("Product Price: ");
	public JLabel lblDate = new JLabel("Date: ");//lblDate

	public JLabel lblCustomerName = new JLabel("Customer Name: ");//lblCustomerName
	public JLabel lblProductName = new JLabel("Product Name: ");//lblProductName

	public JLabel lblTax = new JLabel("Tax: ");//lblTax
	public JLabel lblCost = new JLabel("Cost: ");//lblCost
	public JLabel lblTotalCost = new JLabel("Total Cost: ");//lblTotalCost

	private PurchaseModel purchase;
	private CustomerModel customer;
	private ProductModel product;

	public AddPurchaseUI() {
		this.view = new JFrame();

		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		view.setTitle("Make Purchase");
		view.setSize(600, 400);
		view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

		JPanel line = new JPanel(new FlowLayout());
		line.add(new JLabel("PurchaseID"));
		line.add(txtPurchaseID);
		line.add(lblDate);
		view.getContentPane().add(line);
//customerID
		line = new JPanel(new FlowLayout());
		line.add(new JLabel("CustomerID"));
		line.add(txtCustomerID);
		line.add(lblCustomerName);
		view.getContentPane().add(line);
		//ProductID
		line = new JPanel(new FlowLayout());
		line.add(new JLabel("ProductID"));
		line.add(txtProductID);
		line.add(lblProductName);
		view.getContentPane().add(line);

		line = new JPanel(new FlowLayout());
		line.add(new JLabel("Quantity"));
		line.add(txtQuantity);
		line.add(lblPrice);
		view.getContentPane().add(line);

		line = new JPanel(new FlowLayout());
		line.add(lblCost);
		line.add(lblTax);
		line.add(lblTotalCost);
		view.getContentPane().add(line);

		line = new JPanel(new FlowLayout());
		line.add(btnAdd);
		line.add(btnCancel);
		view.getContentPane().add(line);

		txtProductID.addFocusListener(new ProductIDFocusListener());
		txtCustomerID.addFocusListener(new CustomerIDFocusListener());
		txtQuantity.getDocument().addDocumentListener(new QuantityChangeListener());

		btnAdd.addActionListener(new AddButtonListener());
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.dispose();
			}
		});
	}

	public void run() {
		purchase = new PurchaseModel();
		purchase.date = Calendar.getInstance().getTime().toString();
		lblDate.setText("Date of Purchase: " + purchase.date);
		view.setVisible(true);
	}

	private class ProductIDFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent focusEvent) {

		}

		@Override
		public void focusLost(FocusEvent focusEvent) {
			String text = txtProductID.getText();

			if (text == null || text.length() == 0) {
				lblProductName.setText("Product Name: ");
				return;
			}

			try {
				purchase.productID = Integer.parseInt(text);
				product = StoreManager.getDataAdapter().loadProduct(purchase.productID);
				lblProductName.setText("Product Name: " + product.name);
			} catch(NumberFormatException | NullPointerException e) {
				lblProductName.setText("Product Name: ");
				JOptionPane.showMessageDialog(null, "Error: Invalid ProductID", "Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			lblPrice.setText("Product Price: " + product.price);
		}
	}

	private class CustomerIDFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

		}

		@Override
		public void focusLost(FocusEvent focusEvent) {
			String text = txtCustomerID.getText();
			if (text == null || text.length() == 0) {
				lblCustomerName.setText("Customer Name: ");
				return;
			}
			try {
				purchase.customerID = Integer.parseInt(text);
				customer = StoreManager.getDataAdapter().loadCustomer(purchase.customerID);
				lblCustomerName.setText("Customer Name: " + customer.name);
			} catch (NumberFormatException | NullPointerException e) {
				lblCustomerName.setText("Customer Name: ");
				JOptionPane.showMessageDialog(null, "Error: Invalid CustomerID", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class QuantityChangeListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			process();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			process();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			process();
		}

		private void process() {
			String s = txtQuantity.getText();

			if (s == null || s.length() == 0)
				return;

			try {
				purchase.quantity = Integer.parseInt(s);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Error: invalid quantity", "Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (purchase.quantity <=0) {
				JOptionPane.showMessageDialog(null, "Error: invalid quantity", "Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (purchase.quantity > product.quantity) {
				JOptionPane.showMessageDialog(null, "Not enough available stock", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			Double cost = purchase.quantity * product.price;
			Double tax = cost * 0.09;
			Double total = cost + tax;

			lblCost.setText("Cost: $" + String.format("%8.2f", cost).trim());
			lblTax.setText("Tax: $" + String.format("%8.2f", tax).trim());
			lblTotalCost.setText("Total: $" + String.format("%8.2f", total).trim());
		}
	}

	class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String id = txtPurchaseID.getText();

			if (id == null || id.length() == 0) {
				JOptionPane.showMessageDialog(null, "PurchaseID cannot be null");
				return;
			}

			try {
				purchase.purchaseID = Integer.parseInt(id);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "PurchaseID is invalid");
				return;
			}

			switch (StoreManager.getDataAdapter().savePurchase(purchase)) {
				case IDataAdapter.PURCHASE_DUPLICATE_ERROR:
					JOptionPane.showMessageDialog(null, "Duplicate purchaseID, purchase unsuccessful");
					break;
				default:
					JOptionPane.showMessageDialog(null, "Purchase added successfully");
			}

			//region print receipt
			IReceiptBuilder receipt = new TXTReceiptBuilder();

			receipt.appendCustomer(customer);
			receipt.appendProduct(product);
			receipt.appendPurchase(purchase);

			System.out.println(receipt.toString());
			//endregion
		}
	}

}
