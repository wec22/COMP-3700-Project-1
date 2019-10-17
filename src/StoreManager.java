import javax.swing.*;

public class StoreManager {
	private static IDataAdapter adapter = null;

	private StoreManager(String db, String dbPath) {
		ProductModel product = adapter.loadProduct(3);

		System.out.println("Loaded product: " + product);
	}

	public static IDataAdapter getDataAdapter() {

		return adapter;
	}

	public static void setDataAdapter(String db, String dbPath) {
		if (db.equals("Oracle"))
			adapter = new OracleDataAdapter();
		else if (db.equals("SQLite"))
			adapter = new SQLiteDataAdapter();
		adapter.connect(dbPath);
	}

	public static void main(String[] args) {
		String dbPath = "store.db";
		JFileChooser fc = new JFileChooser();
		if (adapter == null) {
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				dbPath = fc.getSelectedFile().getAbsolutePath();
			System.out.println(dbPath);
			setDataAdapter("SQLite", dbPath);
		}

		MainUI ui = new MainUI();
		ui.view.setVisible(true);

	}

}
