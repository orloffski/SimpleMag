package view.stockviews.items;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Items;
import model.Prices;

public class PricesViewController {
	
	private ObservableList<Prices> data;
	private Connection connection;
	private Stage dialogStage;
	private static int itemId;
	
	@FXML
	private Label itemLabel;
	
	@FXML
	private Label priceLabel;
	
	@FXML
	private Button changePriceBtn;
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private TextField priceChangeField;

	@FXML
	private void initialize() {
	}
	
	public void buildData(int id){
		data = FXCollections.observableArrayList();
		
		try{
	    	connection = new DBClass().getConnection();
	    	String SQL = "SELECT * " + 
	    			"FROM prices " + 
	    			"WHERE item_id = '" + id + "' " +
	    			"ORDER BY lastcreated " + 
	    			"DESC " + 
	    			"LIMIT 1;";
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	            Prices price = new Prices(rs.getInt("id"), 
	            		rs.getString("price"),
	            		rs.getInt("item_id"));
	            data.add(price);   
	            System.out.println(data.size());
	        }
	    }
	    catch(ClassNotFoundException ce){
	    	ce.printStackTrace();
	    }
	    catch(SQLException ce){
	    	ce.printStackTrace();
	    }
	}
	
	void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	void setItem(Items item) {
		itemLabel.setText(item.getVendorCode().toString() + " " + item.getName().toString());
		itemId = item.getId();
		buildData(itemId);
		
		priceLabel.setText(data.isEmpty() ? "0" : data.get(0).getPrice());
	}
	
	@FXML
	private void close() {
		dialogStage.close();
	}
	
	@FXML
	private void addNewPrice() {
		try {
			connection = new DBClass().getConnection();
			String SQL = "INSERT INTO prices SET price = '" 
					+ priceChangeField.getText().toString() + "', "
					+ "reason = 'manual', "
					+ "item_id = '"
					+ itemId + "';";
			connection.createStatement().executeUpdate(SQL);
			data.clear();
			buildData(itemId);
			priceLabel.setText(data.isEmpty() ? "0" : data.get(0).getPrice());
			priceChangeField.setText("");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
