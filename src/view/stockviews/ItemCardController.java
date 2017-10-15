package view.stockviews;

import java.sql.Connection;
import java.sql.SQLException;

import application.DBClass;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Items;

public class ItemCardController {
	
	private Items item;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean newItem = true;
	
	@FXML
	private TextField vendorCode; 
	
	@FXML
	private TextField name; 
	
	@FXML
	private TextField vendorCountry; 

	@FXML
	private void initialize() {
		
	}
	
	@FXML
    private void handleCancel() {
        dialogStage.close();
    }
	
	@FXML
    private void handleOK() {
		if(checkItem()) {						
			Connection connection;
			String SQL;
			try {				
				connection = new DBClass().getConnection();
				if(newItem) {
					SQL = "INSERT INTO items "
							+ "SET vendor_code = '"
							+ vendorCode.getText().toString() + "', "
							+ "name = '" 
							+ name.getText().toString() + "', "
							+ "vendor_country = '"
							+ vendorCountry.getText().toString() + "';";
				}else {
					SQL = "UPDATE items "
							+ "SET vendor_code = '"
							+ vendorCode.getText().toString() + "', "
							+ "name = '" 
							+ name.getText().toString() + "', "
							+ "vendor_country = '"
							+ (vendorCountry.getText() != null ? vendorCountry.getText().toString() : "") + "' "
							+ "WHERE id = "
							+ this.item.getId() + ";";
				}
				
		        connection.createStatement().executeUpdate(SQL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			
			okClicked = true;
			dialogStage.close();
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Некорректное заполнение");
	        alert.setContentText("Не заполнены обязательные поля! Поля Артикул и Наименование обязательны!");

	        alert.showAndWait();
		}
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
			dialogStage.setTitle("Изменение товара");
			
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
