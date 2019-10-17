import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddCustomerUI {
	public JFrame view;
	public JButton btnAdd = new JButton("Add");
	public JButton btnCancel = new JButton("Cancel");

	public JTextField txtCustomerID = new JTextField(20);
	public JTextField txtName = new JTextField(20);
	public JTextField txtAddress = new JTextField(20);
	public JTextField txtPaymentInfo = new JTextField(20);

	public AddCustomerUI() {
		this.view = new JFrame();

		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		view.setTitle("Add Customer");
		view.setSize(600,400);
		view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

		JPanel line1 = new JPanel(new FlowLayout());
		line1.add(new JLabel("Customer id"));
		line1.add(txtCustomerID);
		view.getContentPane().add(line1);

		JPanel line2 = new JPanel(new FlowLayout());
		line2.add(new JLabel("Name"));
		line2.add(txtName);
		view.getContentPane().add(line2);

		JPanel line3 = new JPanel(new FlowLayout());
		line3.add(new JLabel("Address"));
		line3.add(txtAddress);
		view.getContentPane().add(line3);

		JPanel line4 = new JPanel(new FlowLayout());
		line4.add(new JLabel("Payment Info"));
		line4.add(txtPaymentInfo);
		view.getContentPane().add(line4);

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(btnAdd);
		buttons.add(btnCancel);
		view.getContentPane().add(buttons);

		btnAdd.addActionListener(new AddButtonListener());

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				view.dispose();
			}
		});
	}

	public void run() {
		view.setVisible(true);
	}

	class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			CustomerModel customer = new CustomerModel();

			//region verify and add data
			try {
				customer.customerID = Integer.parseInt(txtCustomerID.getText());
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid value for Customer ID.");
				return;
			}

			String name = txtName.getText();
			if (name.length() == 0) {
				JOptionPane.showMessageDialog(null, "Customer name cannot be empty.");
				return;
			}
			customer.name = name;

			String address = txtAddress.getText();
			if (address.length() == 0) {
				JOptionPane.showMessageDialog(null, "Address cannot be empty.");
				return;
			}
			customer.address = address;

			String payment = txtPaymentInfo.getText();
			if (payment.length() == 0) {
				JOptionPane.showMessageDialog(null, "Payment info cannot be empty.");
				return;
			}
			customer.paymentInfo = payment;
			//endregion

			switch (StoreManager.getDataAdapter().saveCustomer(customer)) {
				case IDataAdapter.CUSTOMER_DUPLICATE_ERROR:
					JOptionPane.showMessageDialog(null, "Customer NOT added successfully! Duplicate customer ID!");
				default:
					JOptionPane.showMessageDialog(null, "customer added successfully!" + customer);
			}

		}
	}
}
