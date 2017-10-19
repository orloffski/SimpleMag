package view.stockviews.invoices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.InvoiceHeader;

public class InvoicesViewController {
	
	private Main main;
	
	private ObservableList<InvoiceHeader> data;
	private DBClass dbClass;
	private Connection connection;
	
	@FXML
	private TableView<InvoiceHeader> invoicesTable;
	
	@FXML
    private TableColumn<InvoiceHeader, String> numberColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> typeColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> statusColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> lastcreatedColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, String> counterpartyColumn;
	
	@FXML
    private TableColumn<InvoiceHeader, Number> countColumn;

	@FXML
    private TableColumn<InvoiceHeader, Number> summColumn;

	@FXML
	private void initialize() {
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
		typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		lastcreatedColumn.setCellValueFactory(cellData -> cellData.getValue().lastcreatedProperty());
		counterpartyColumn.setCellValueFactory(cellData -> cellData.getValue().counterpartyProperty());
		countColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty());
		summColumn.setCellValueFactory(cellData -> cellData.getValue().summProperty());
		
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
	
	public void buildData(){
		data = FXCollections.observableArrayList();
	    try{      
	        String SQL = "SELECT * FROM invoices_headers ORDER BY id";            
	        ResultSet rs = connection.createStatement().executeQuery(SQL);  
	        while(rs.next()){
	        	InvoiceHeader invoice = new InvoiceHeader(
	        			rs.getInt("id"), 
	            		rs.getString("number"), 
	            		rs.getString("type"),
	            		rs.getString("status"),
	            		rs.getString("counterparty"),
	            		rs.getInt("count"),
	            		rs.getDouble("summ"),
	            		rs.getInt("counterparty_id"),
	            		rs.getString("lastcreated"));
	            data.add(invoice);   	       
	        }
	        invoicesTable.setItems(data);
	        
	        //addFilter();
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
