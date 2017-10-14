package view.stockviews;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
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
	            		rs.getString("vendor_country"));
	            data.add(item);   	       
	        }
	        itemsTable.setItems(data);
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
}
