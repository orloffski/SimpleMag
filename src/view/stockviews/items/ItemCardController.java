package view.stockviews.items;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Items;
import model.Units;
import view.AbstractController;

public class ItemCardController extends AbstractController{
	
	private Main main;
	
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
	private Button barcodeBtn;
	
	@FXML
	private Button saveBtn;

	@FXML
	private void initialize() {
		try {
			connection = new DBClass().getConnection();
		} catch (ClassNotFoundException | SQLException e) {
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

					int inserted = connection.createStatement().executeUpdate(SQL);

					if(inserted == 1){
						SQL = "SELECT * FROM items ORDER BY id DESC LIMIT 1";
						ResultSet rs = connection.createStatement().executeQuery(SQL);

						if(rs.next()) {
							this.item = new Items(rs.getInt("id"),
									rs.getString("vendor_code"),
									rs.getString("name"),
									rs.getString("vendor_country"),
									rs.getInt("unit_id"));
						}
					}
				}else {
					SQL = "UPDATE items "
							+ "SET vendor_code = '" + vendorCode.getText().toString() + "', "
							+ "name = '" + name.getText().toString() + "', "
							+ "vendor_country = '"
							+ (vendorCountry.getText() != null ? vendorCountry.getText().toString() : "") + "', "
							+ "unit_id = " + "(SELECT id FROM units WHERE unit = '" + unitComboBox.getValue() + "') "
							+ "WHERE id = " + this.item.getId() + ";";

					connection.createStatement().executeUpdate(SQL);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println(SQL);
				e.printStackTrace();
			}			
			
			okClicked = true;
			//dialogStage.close();
			saveBtn.setDisable(true);
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Ошибка сохранения");
	        alert.setContentText("Для сохранения карточки товара заполните все поля");

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
	          e.printStackTrace();           
	    }
		
		unitComboBox.setItems(units);
	}
	
	boolean isOkClicked() {
        return okClicked;
    }

	void setItem(Items item) {
		this.item = item;
		
		if(this.item.getId() > 0) {
			newItem = false;
			dialogStage.setTitle("Изменение карточки товара");
			
			vendorCode.setText(item.getVendorCode());
			name.setText(item.getName());
			vendorCountry.setText(item.getVendorCountry());
			unitComboBox.setValue(units.get(item.getUnitId() - 1));
		}else{
			dialogStage.setTitle("Создание карточки товара");
		}
	}
	
	private boolean checkItem() {
		return vendorCode.getText().length() != 0 && name.getText().length() != 0;

	}
	
	@FXML
	private void openBarcodeTable() {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/items/BarcodesView.fxml"));
        try {
        	BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Штрихкоды");
	        dialogStage.getIcons().add(new Image("file:resources/images/barcode.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        BarcodesViewController cardController = loader.getController();
	        cardController.setDialogStage(dialogStage);
	        cardController.setItem(item);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void openPriceView() {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/items/PricesView.fxml"));
        try {
        	BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Цены");
	        dialogStage.getIcons().add(new Image("file:resources/images/price.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        PricesViewController cardController = loader.getController();
	        cardController.setDialogStage(dialogStage);
	        cardController.setItem(item);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setMain(Main main) {
        this.main = main;
    }

	@Override
	protected void clearForm() {

	}
}
