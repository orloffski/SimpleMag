package view.stockviews;

import java.sql.SQLException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Items;

public class ItemsViewController {
	
	private ObservableList<Items> data;
	private DBClass dbClass;
	private Connection connection;
	
	@FXML
	private TableView<Items> itemsTable;
	
	@FXML
    private TableColumn<Items, String> vendorCodeColumn;
	
    @FXML
    private TableColumn<Items, String> nameColumn;
    
    @FXML
    private TableColumn<Items, String> vendorCountryColumn;
    
    @FXML
	private ImageView add;
    
    @FXML
	private ImageView edit;
    
    @FXML
	private ImageView delete;
	
	@FXML
	private void initialize() {
		vendorCodeColumn.setCellValueFactory(cellData -> cellData.getValue().vendorCodeProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		vendorCountryColumn.setCellValueFactory(cellData -> cellData.getValue().vendorCountryProperty());
		
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();
	        buildData();
	    }
	    catch(ClassNotFoundException ce){
	    	ce.printStackTrace();
	    }
	    catch(SQLException ce){
	    	ce.printStackTrace();
	    }
	}
	
	@FXML
	private void addItem() {
		Items item = new Items(0, null, null, null, 0);
		
		boolean okClicked = openAddEditItemDialog(item);
		if(okClicked) {
			data.clear();
			buildData();
		}
	}
	
	@FXML
	private void editItem() {
		Items item = itemsTable.getSelectionModel().getSelectedItem();
		
		if(item != null) {
			boolean okClicked = openAddEditItemDialog(item);
			if(okClicked) {
				data.clear();
				buildData();
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Не выбран товар для изменения");
	        alert.setContentText("Для изменения товара выберите товар из списка");

	        alert.showAndWait();
		}
		
	}
	
	@FXML
	private void deleteItem() {	
		int indexToDelete = itemsTable.getSelectionModel().getSelectedIndex();
		Items itemToDelete = itemsTable.getSelectionModel().getSelectedItem();
		
		if(itemToDelete != null) {		
		    try {
		    	PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ?");
				statement.setInt(1, itemToDelete.getId());
				statement.executeUpdate();
				
				data.remove(indexToDelete);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Не выбран товар для удаления");
	        alert.setContentText("Для удаления товара из системы выберите товар из списка");

	        alert.showAndWait();
		}
	}
	
	public void buildData(){
		data = FXCollections.observableArrayList();
	    try{      
	        String SQL = "SELECT * FROM items ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	            Items item = new Items(rs.getInt("id"), 
	            		rs.getString("vendor_code"), 
	            		rs.getString("name"),
	            		rs.getString("vendor_country"),
	            		rs.getInt("unit_id"));
	            data.add(item);   	       
	        }
	        itemsTable.setItems(data);
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	private boolean openAddEditItemDialog(Items item) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/ItemCard.fxml"));
        try {
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.setTitle("Добавление товара");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        ItemCardController cardController = loader.getController();
	        cardController.setDialogStage(dialogStage);
	        cardController.setItem(item);
	        
	        dialogStage.showAndWait();
	        
	        return cardController.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return false;
	}
}
