package view.stockviews;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Items;
import model.Units;

public class ItemCardController {
	
	private Connection connection;
	private Items item;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean newItem = true;
	private ObservableList<String> units;
	
	@FXML
	private ComboBox<String> unitComboBox;
	
	@FXML
	private TextField vendorCode; 
	
	@FXML
	private TextField name; 
	
	@FXML
	private TextField vendorCountry; 

	@FXML
	private void initialize() {
		try {
			connection = new DBClass().getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		init();
	}
	
	@FXML
    private void handleCancel() {
        dialogStage.close();
    }
	
	@FXML
    private void handleOK() {
		if(checkItem()) {
			String SQL = "";
			try {				
				connection = new DBClass().getConnection();
				if(newItem) {
					SQL = "INSERT INTO items "
							+ "SET vendor_code = '"
							+ vendorCode.getText().toString() + "', "
							+ "name = '" 
							+ name.getText().toString() + "', "
							+ "vendor_country = '"
							+ vendorCountry.getText().toString() + "', "
							+ "unit_id = "
							+ "(SELECT id FROM units WHERE unit = '" + unitComboBox.getValue()
							+ "');";
				}else {
					SQL = "UPDATE items "
							+ "SET vendor_code = '"
							+ vendorCode.getText().toString() + "', "
							+ "name = '" 
							+ name.getText().toString() + "', "
							+ "vendor_country = '"
							+ (vendorCountry.getText() != null ? vendorCountry.getText().toString() : "") + "', "
							+ "unit_id = "
							+ "(SELECT id FROM units WHERE unit = '" + unitComboBox.getValue() + "') "
							+ "WHERE id = "
							+ this.item.getId() + ";";
				}
				
		        connection.createStatement().executeUpdate(SQL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println(SQL);
				e.printStackTrace();
			}			
			
			okClicked = true;
			dialogStage.close();
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("������������ ����������");
	        alert.setContentText("�� ��������� ������������ ����! ���� ������� � ������������ �����������!");

	        alert.showAndWait();
		}
    }
	
	private void init() {
		units = FXCollections.observableArrayList();
		
		try{      
	        String SQL = "SELECT * FROM units";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	            Units unit = new Units(rs.getInt("id"), 
	            		rs.getString("unit"));
	            //unitsData.add(unit);
	            units.add(unit.getUnit());
	        }
	    }catch(Exception e){
	    	System.out.println("SQL Error");
	          e.printStackTrace();           
	    }
		
		unitComboBox.setItems(units);
	}
	
	public boolean isOkClicked() {
        return okClicked;
    }
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	public void setItem(Items item) {
		this.item = item;
		
		if(this.item.getId() > 0) {
			newItem = false;
			dialogStage.setTitle("��������� ������");
			
			vendorCode.setText(item.getVendorCode());
			name.setText(item.getName());
			vendorCountry.setText(item.getVendorCountry());
		}
	}
	
	private boolean checkItem() {
		if(vendorCode.getText().length() != 0 && name.getText().length() != 0)
			return true;
		
		return false;
	}
}
