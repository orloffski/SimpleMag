package view.stockviews.invoices;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DBClass;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private TextField filter;
	
	@FXML
	private ImageView addBtn;
	
	@FXML
	private ImageView editBtn;
	
	@FXML
	private ImageView deleteBtn;

	@FXML
	private void initialize() {
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
		typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		lastcreatedColumn.setCellValueFactory(cellData -> cellData.getValue().lastcreatedProperty());
		counterpartyColumn.setCellValueFactory(cellData -> cellData.getValue().counterpartyProperty());
		countColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty());
		summColumn.setCellValueFactory(cellData -> cellData.getValue().fullSummProperty());
		
		dbClass = new DBClass();
	    try{
	    	connection = dbClass.getConnection();
	        buildData();	        	    
	    }
	    catch(ClassNotFoundException | SQLException ce){
	    	ce.printStackTrace();
	    }
	}
	
	@FXML
	private void addInvoice() {
		boolean okClicked = openAddEditItemDialog(null);
		if(okClicked) {
			data.clear();
			buildData();
		}
	}
	
	@FXML
	private void editInvoice() {
		InvoiceHeader invoice = invoicesTable.getSelectionModel().getSelectedItem();
		
		boolean okClicked = openAddEditItemDialog(invoice);
		if(okClicked) {
			data.clear();
			buildData();
		}
	}
	
	private boolean openAddEditItemDialog(InvoiceHeader invoice) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/invoices/AddEditInvoiceView.fxml"));
        try {
			BorderPane page = loader.load();
			Stage dialogStage = new Stage();
	        dialogStage.getIcons().add(new Image("file:resources/images/invoices.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(main.getPrimaryStage());
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        AddEditInvoiceViewController controller = loader.getController();
	        controller.setMain(main);
	        controller.setDialogStage(dialogStage);
	        controller.setInvoice(invoice);
	        
	        dialogStage.showAndWait();
	        
	        return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return false;
	}
	
	private void buildData(){
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
	            		rs.getString("lastcreated"),
	            		rs.getInt("recipient_id"),
	            		rs.getString("recipient_name"),
						rs.getDouble("full_summ"));
	            data.add(invoice);   	       
	        }
	        invoicesTable.setItems(data);
	        
	        addFilter();
	    }
	    catch(Exception e){
	          e.printStackTrace();           
	    }
	}
	
	private void addFilter() {
		FilteredList<InvoiceHeader> filteredData = new FilteredList<>(data, p -> true);
        
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(invoice -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (invoice.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<InvoiceHeader> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(invoicesTable.comparatorProperty());
        invoicesTable.setItems(sortedData);
	}
	
	public void setMain(Main main) {
        this.main = main;
    }
}
