package view.stockviews;

import java.sql.SQLException;

import java.sql.Connection;
import java.sql.ResultSet;

import application.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private void initialize() {
		vendorCodeColumn.setCellValueFactory(cellData -> cellData.getValue().vendorCodeProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();
	        buildData();
	    }
	    catch(ClassNotFoundException ce){
	    	ce.printStackTrace();
	        System.out.println("ClassNotFoundException");
	    }
	    catch(SQLException ce){
	    	ce.printStackTrace();
	        System.out.println("SQLException");
	    }
	}
	
	public void buildData(){
		data = FXCollections.observableArrayList();
	    try{      
	        String SQL = "SELECT * FROM items ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	            Items item = new Items(rs.getInt("id"), rs.getString("vendor_code"), rs.getString("name"));
	            data.add(item);   	       
	        }
	        System.out.println(""+data.size());
	        itemsTable.setItems(data);
	    }
	    catch(Exception e){
	          e.printStackTrace();
	          System.out.println("Error on Building Data");            
	    }
	}
}
